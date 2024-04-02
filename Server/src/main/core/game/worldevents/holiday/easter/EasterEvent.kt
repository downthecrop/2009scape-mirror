package core.game.worldevents.holiday.easter

import core.ServerConstants
import core.api.*
import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import core.game.event.EventHook
import core.game.event.XPGainEvent
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.emote.Emotes
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.world.GameWorld
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.path.Pathfinder
import core.game.worldevents.WorldEvent
import core.tools.RandomFunction
import core.tools.StringUtils
import core.tools.colorize
import org.rs09.consts.Items
import java.util.*

class EasterEvent : WorldEvent("easter"), TickListener, InteractionListener, LoginListener, Commands {
    private val spawnedItems = ArrayList<GroundItem>()
    private var timeUntilNextEggSpawn = 0
    private var currentLoc = ""

    override fun tick() {
        if (timeUntilNextEggSpawn-- > 0) return
        timeUntilNextEggSpawn = EGG_SPAWN_TIME

        for (egg in spawnedItems)
        {
            GroundItemManager.destroy(egg)
        }
        spawnedItems.clear()

        val (locName, locData) = getRandomLocations()
        currentLoc = locName

        for (loc in locData)
        {
            val gi = GroundItemManager.create(GroundItem(
                Item(eggs.random()),
                loc,
                EGG_SPAWN_TIME,
                null
            ))
            gi.forceVisible = true
            spawnedItems.add(gi)
        }

        sendNews("Eggs have appeared in $locName!")
    }

    override fun login(player: Player) {
        if (spawnedItems.isNotEmpty())
            sendMessage(player, colorize("%GEggs were last spotted in $currentLoc!"))
        player.hook(Event.XpGained, xpEventHook)
    }

    override fun defineCommands() {
        define("eggspawntest") {player, _ ->
            notify(player, "Spawning 10 eggs nearby...")
            repeat(10) { spawnEggFor(player) }
        }
    }

    object xpEventHook : EventHook<XPGainEvent>
    {
        override fun process(entity: Entity, event: XPGainEvent) {
            if (entity !is Player) return

            val lastRoll = getAttribute(entity, LAST_EGG_ROLL, 0)
            if (getWorldTicks() - lastRoll < 10) return
            setAttribute(entity, LAST_EGG_ROLL, getWorldTicks())

            val activeEggRate = if (GameWorld.settings!!.isDevMode) EGG_RATE_DEV else EGG_RATE

            if (RandomFunction.roll(activeEggRate))
                spawnEggFor(entity)
        }
    }


    fun getRandomLocations() : Pair<String, List<Location>>
    {
        val toReturn = ArrayList<Location>()
        val name = locNames.random()
        val locs = locationsForName(name)
        for (loc in locs)
            if (RandomFunction.nextBool()) toReturn.add(loc)
        return Pair(name, toReturn)
    }

    override fun checkActive(cal: Calendar): Boolean {
        return cal.get(Calendar.MONTH) == Calendar.APRIL || ServerConstants.FORCE_EASTER_EVENTS
    }

    private fun onEggBroken (player: Player)
    {
        val eggsBroken = getAttribute(player, EGGS_BROKEN, 0) + 1

        if (eggsBroken == 10)
        {
            player.emoteManager.unlock(Emotes.BUNNY_HOP)
            Emotes.BUNNY_HOP.play(player)
            sendMessage(player, colorize("%RYou have unlocked the 'Bunny Hop' emote!"))
        }
        else if (eggsBroken == 15)
        {
            addItemOrDrop(player, BUN_EARS)
            sendMessage(player, colorize("%RYou have been given bunny ears!"))
        }
        else if (eggsBroken % 5 == 0)
        {
            var trackUnlocked = false;
            for (track in tracks)
            {
                if (player.musicPlayer.hasUnlocked(track))
                    continue
                player.musicPlayer.unlock(track)
                trackUnlocked = true
                break
            }
            if (!trackUnlocked)
                giveRandomLoot(player)
        }
        else giveRandomLoot(player)

        setAttribute(player, EGGS_BROKEN, eggsBroken)
    }

    private fun giveRandomLoot (player: Player)
    {
        //Give some loot
        val loot = lootTable.roll(player)[0]
        addItemOrDrop(player, loot.id, loot.amount)
        val term = if (loot.amount == 1)
            if (StringUtils.isPlusN(loot.name)) "an"
            else "a"
        else "some"

        val name = if (loot.amount == 1) loot.name else StringUtils.plusS(loot.name)
        sendMessage(player, "Inside the egg you find $term ${name.lowercase()}.")
    }

    override fun defineListeners() {
        on (eggs, IntType.GROUNDITEM, "take") {player, node ->
            if (node !is GroundItem) return@on true
            queueScript(player, strength = QueueStrength.NORMAL) { stage ->
                when (stage)
                {
                    0 -> {
                        animate(player, STOMP_ANIM)
                        return@queueScript delayScript(player, 1)
                    }
                    1 -> {
                        val item = GroundItemManager.get(node.id, node.location, null)
                        if (item == null || !item.isActive) return@queueScript stopExecuting(player)
                        sendGraphics(gfxForEgg(node.id), node.location)
                        GroundItemManager.destroy(node)
                        spawnedItems.remove(node)
                        onEggBroken(player)
                        return@queueScript stopExecuting(player)
                    }
                }
                return@queueScript keepRunning(player)
            }
            return@on true
        }
        on (eggs, IntType.ITEM, "release") {player, node ->
            if (node !is Item) return@on true
            queueScript(player, strength = QueueStrength.NORMAL) { stage ->
                when (stage)
                {
                    0 -> {
                        animate(player, STOMP_ANIM)
                        return@queueScript delayScript(player, 1)
                    }
                    1 -> {
                        if (!removeItem(player, node)) return@queueScript stopExecuting(player)
                        sendGraphics(gfxForEgg(node.id), player.location)
                        onEggBroken(player)
                        return@queueScript stopExecuting(player)
                    }
                }
                return@queueScript keepRunning(player)
            }
            return@on true
        }
    }

    companion object
    {
        const val LOC_LUM = "Lumbridge"
        const val LOC_DRAYNOR = "Draynor Village"
        const val LOC_FALADOR = "Falador"
        const val LOC_EDGE = "Edgeville"
        const val LOC_TGS = "Tree Gnome Stronghold"
        const val EGG_SPAWN_TIME = 5_000
        const val EGG_A = 11027
        const val EGG_B = 11028
        const val EGG_C = 11029
        const val EGG_D = 11030
        const val GFX_A = 1040
        const val GFX_B = 1045
        const val GFX_C = 1042
        const val GFX_D = 1043
        const val STOMP_ANIM = 10017
        const val BUN_EARS = 14658
        const val TRACK_BSB = 502
        const val TRACK_EJ = 273
        const val TRACK_FB = 603
        const val EGG_RATE = 64
        const val EGG_RATE_DEV = 3

        val EGGS_BROKEN = "/save:easter${ServerConstants.STARTUP_MOMENT.get(Calendar.YEAR)}:eggs"
        val LAST_EGG_ROLL = "easter:lasteggroll"
        val tracks = arrayOf(TRACK_BSB, TRACK_EJ, TRACK_FB)
        val eggs = intArrayOf(EGG_A, EGG_B, EGG_C, EGG_D)
        val locNames = arrayOf(LOC_LUM, LOC_DRAYNOR, LOC_FALADOR, LOC_EDGE, LOC_TGS)
        val LUMBRIDGE_SPOTS = arrayOf(Location.create(3190, 3240, 0),
            Location.create(3219, 3204, 0),Location.create(3212, 3201, 0),Location.create(3205, 3209, 0),
            Location.create(3205, 3217, 0),Location.create(3211, 3213, 0),Location.create(3206, 3229, 0),
            Location.create(3212, 3226, 0),Location.create(3212, 3244, 0),Location.create(3202, 3252, 0),
            Location.create(3197, 3220, 0),Location.create(3189, 3207, 0),Location.create(3229, 3200, 0),
            Location.create(3228, 3205, 0),Location.create(3251, 3212, 0),Location.create(3232, 3237, 0))

        val DRAYNOR_SPOTS = arrayOf(Location.create(3085, 3242, 0),
            Location.create(3083, 3236, 0),Location.create(3093, 3224, 0),Location.create(3099, 3241, 0),
            Location.create(3095, 3255, 0),Location.create(3089, 3264, 0),Location.create(3089, 3265, 0),
            Location.create(3088, 3268, 0),Location.create(3090, 3274, 0),Location.create(3100, 3281, 0),
            Location.create(3116, 3265, 0),Location.create(3123, 3272, 0),Location.create(3079, 3261, 0),
            Location.create(3127, 3280, 0),Location.create(3118, 3287, 0),Location.create(3111, 3270, 0),
            Location.create(3119, 3277, 0),Location.create(3113, 3283, 0))

        val FALADOR_SPOTS = arrayOf(
            Location.create(2961, 3332, 0),Location.create(2967, 3336, 0),Location.create(2974, 3329, 0),
            Location.create(2979, 3346, 0),Location.create(2970, 3348, 0),Location.create(2955, 3375, 0),
            Location.create(2942, 3386, 0),Location.create(2937, 3385, 0),Location.create(3005, 3370, 0),
            Location.create(3006, 3383, 0),Location.create(3007, 3387, 0),Location.create(2985, 3391, 0),
            Location.create(2984, 3381, 0),Location.create(2980, 3384, 0),Location.create(3025, 3345, 0),
            Location.create(3060, 3389, 0),Location.create(3052, 3385, 0))

        val EDGEVILLE_SPOTS = arrayOf(Location.create(3077, 3487, 0),Location.create(3082, 3487, 0),
            Location.create(3089, 3481, 0),Location.create(3084, 3479, 0),Location.create(3108, 3499, 0),
            Location.create(3110, 3517, 0),Location.create(3091, 3512, 0),Location.create(3092, 3507, 0),
            Location.create(3081, 3513, 0),Location.create(3079, 3513, 1),Location.create(3080, 3508, 1),
            Location.create(3093, 3467, 0))

        val TREE_GNOME_STRONGHOLD_SPOTS = arrayOf(
            Location.create(2480, 3507, 0),Location.create(2486, 3513, 0),Location.create(2453, 3512, 0),
            Location.create(2442, 3484, 0),Location.create(2438, 3486, 0),Location.create(2441, 3506, 0),
            Location.create(2471, 3482, 0),Location.create(2482, 3478, 0),Location.create(2480, 3469, 0),
            Location.create(2489, 3440, 0),Location.create(2470, 3417, 0),Location.create(2478, 3402, 0),
            Location.create(2486, 3407, 0),Location.create(2492, 3404, 0),Location.create(2493, 3413, 0),
            Location.create(2446, 3395, 0),Location.create(2422, 3398, 0),Location.create(2421, 3402, 0),
            Location.create(2418, 3398, 0),Location.create(2401, 3415, 0),Location.create(2397, 3436, 0),
            Location.create(2409, 3448, 0),Location.create(2482, 3391, 0))

        fun locationsForName (name: String) : Array<Location>
        {
            return when (name)
            {
                LOC_LUM     -> LUMBRIDGE_SPOTS
                LOC_DRAYNOR -> DRAYNOR_SPOTS
                LOC_FALADOR -> FALADOR_SPOTS
                LOC_TGS     -> TREE_GNOME_STRONGHOLD_SPOTS
                LOC_EDGE    -> EDGEVILLE_SPOTS
                else        -> LUMBRIDGE_SPOTS
            }
        }

        fun gfxForEgg (egg: Int) : Int
        {
            return when (egg)
            {
                EGG_A -> GFX_A
                EGG_B -> GFX_B
                EGG_C -> GFX_C
                EGG_D -> GFX_D
                else  -> GFX_A
            }
        }

        fun spawnEggFor (player: Player)
        {
            val dirs = Direction.values()
            val dir = dirs[RandomFunction.random(dirs.size)]
            var loc = player.location.transform(dir, 3)
            val path = Pathfinder.find(player, loc)
            loc = Location.create(path.points.last.x, path.points.last.y, loc.z)
            GroundItemManager.create(Item(eggs.random()), loc, player)
            sendMessage(player, colorize("%RAn egg has appeared nearby."))
        }

        val lootTable = WeightBasedTable.create(
            WeightedItem(Items.EASTER_EGG_1962, 1, 15, 0.10),
            WeightedItem(Items.PURPLE_SWEETS_10476, 5, 15, 0.10),
            WeightedItem(Items.COINS_995, 500, 2500, 0.15),
            WeightedItem(Items.ESS_IMPLING_JAR_11246, 1, 1, 0.05),
            WeightedItem(Items.BABY_IMPLING_JAR_11238, 1, 1, 0.05),
            WeightedItem(Items.EARTH_IMPLING_JAR_11244, 1, 1, 0.05),
            WeightedItem(Items.GOURM_IMPLING_JAR_11242, 1, 1, 0.05),
            WeightedItem(Items.MAGPIE_IMPLING_JAR_11252, 1, 1, 0.025),
            WeightedItem(Items.ECLECTIC_IMPLING_JAR_11248, 1, 1, 0.025),
            WeightedItem(Items.ESS_IMPLING_JAR_11246, 1, 1, 0.025),
            WeightedItem(Items.NATURE_IMPLING_JAR_11250, 1, 1, 0.015),
            WeightedItem(Items.NINJA_IMPLING_JAR_11254, 1, 1, 0.015),
            WeightedItem(Items.DRAGON_IMPLING_JAR_11256, 1, 1, 0.005)
        )
    }
}