package content.global.ame.events.mime

import content.global.ame.events.mime.MimeActivity.MimeEmote
import core.ServerConstants
import core.api.*
import core.game.activity.ActivityPlugin
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.player.link.emote.Emotes
import core.game.node.scenery.Scenery
import core.game.system.command.Privilege
import core.game.system.command.sets.CommandSet
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import core.tools.ticksToCycles
import org.rs09.consts.Animations
import org.rs09.consts.Components
import org.rs09.consts.Items
import org.rs09.consts.Music.ARTISTRY_247
import org.rs09.consts.NPCs

private var waitingPlayerList: ArrayList<Player> = ArrayList()
private var activePlayerList : ArrayList<Player> = ArrayList()
private var lastEmote: Int = 0

val MIME_SUCCESS = "ame:mime_success"
val MIME_SUCCESS_COUNT = "ame:mime_success_count"

val mimeEmotes = listOf(Emotes.GLASS_BOX, Emotes.CLIMB_ROPE, Emotes.LEAN, Emotes.GLASS_WALL)
val mimeItems = listOf(Items.MIME_MASK_3057, Items.MIME_TOP_3058, Items.MIME_LEGS_3059, Items.MIME_GLOVES_3060, Items.MIME_BOOTS_3061)

@Initializable
class MimeActivity: ActivityPlugin("mimeEvent", false, true, true), MapArea {
    val MIME_EVENT_REGION = 8010

    lateinit var mimeNpc: NPC
    lateinit var watcher1Npc: NPC
    lateinit var watcher2Npc: NPC
    lateinit var watcher3Npc: NPC

    val copyMimePerformanceFirst = "You need to copy the mime's performance, then you'll be returned to where you were."
    val activeLocation =  Location(2008, 4762, 0)
    val spawnInLocation =  Location(2008, 4764, 0)

    var mimeSpotlight: Scenery = getScenery(2010, 4761, 0)!!
    var playerSpotLight: Scenery = getScenery(2007, 4761, 0)!!
    val spotlightOn = 1136
    val spotlightOff = 1135

    enum class MimeEmote(val buttonId: Int, val anim: Int) {
        THINK(2, Animations.HUMAN_THINK_857),
        CRY(3, Animations.HUMAN_CRY_860),
        LAUGH(4, Animations.HUMAN_LAUGH_861),
        DANCE(5, Animations.HUMAN_DANCE_866),
        CLIMB_ROPE(6, Animations.HUMAN_CLIMB_ROPE_1130),
        LEAN(7, Animations.HUMAN_LEAN_1129),
        GLASS_BOX(8, Animations.HUMAN_GLASS_BOX_1131),
        GLASS_WALL(9, Animations.HUMAN_GLASS_WALL_1128);

        companion object {
            private val VALUES = values()
            fun roll(): Int = VALUES[RandomFunction.random(VALUES.size)].anim
        }
    }

    override fun configure() {
        RegionManager.forId(MIME_EVENT_REGION).music = ARTISTRY_247

        mimeNpc = createNpc(NPCs.MIME_1056, Location(2011, 4762, 0))
        watcher1Npc = createNpc(NPCs.STRANGE_WATCHER_1057, Location(2015, 4756, 0))
        watcher2Npc = createNpc(NPCs.STRANGE_WATCHER_1058, Location(2012, 4754, 0))
        watcher3Npc = createNpc(NPCs.STRANGE_WATCHER_1059, Location(2006, 4755, 0))

        queueScript(mimeNpc, 1, QueueStrength.SOFT, persist = true) { stage ->
            when (stage % 5) {
                    0 ->  {
                        addWaitingPlayers()
                        spotlightMime()
                        face(mimeNpc,mimeNpc.location.transform(Direction.SOUTH))
                        return@queueScript delayScript(mimeNpc, 2)
                    }
                    1 ->  {
                        mimeNpc.animate(getNextMimeEmote())
                        return@queueScript delayScript(mimeNpc, 6)
                    }
                    2 ->  {
                        spotlightPlayer()
                        face(mimeNpc,mimeNpc.location.transform(Direction.WEST))
                        mimeNpc.animate(Animation(Animations.HUMAN_BOW_858))
                        return@queueScript delayScript(mimeNpc, 2)
                    }
                    3 ->  {
                        sendMimeEmoteIface()
                        addWaitingPlayers()
                        return@queueScript delayScript(mimeNpc, 11)
                    }
                    4 ->  { checkSuccess()
                        addWaitingPlayers()
                        return@queueScript delayScript(mimeNpc, 2)
                    }
                }
            return@queueScript keepRunning(mimeNpc)
        }

    }

    private fun createNpc(npcId: Int, location: Location ): NPC {
        val thisNpc = NPC.create( npcId, location, Direction.SOUTH )
        thisNpc.init()
        return thisNpc
    }
    private fun getNextMimeEmote(): Animation {
        val currentEmote = MimeEmote.roll()
        lastEmote = currentEmote
        return Animation(currentEmote)
    }

    fun addWaitingPlayers() {
        if (waitingPlayerList.isEmpty()) return
        val iterator = waitingPlayerList.iterator()

        while (iterator.hasNext()) {
            val player = iterator.next()
            activePlayerList.add(player)
            forceMove(player, player.location, activeLocation, 0, ticksToCycles(2), dir = Direction.SOUTH, 819)
            iterator.remove()
        }
    }

    private fun sendMimeEmoteIface() {
        if (activePlayerList.isEmpty()) return
        resetMimeRound()
        for (player in activePlayerList) {
            openInterface(player, Components.MACRO_MIME_EMOTES_188)
        }
    }

    fun resetMimeRound() {
        for (player in activePlayerList) {
            getAttribute(player, MIME_SUCCESS, false)
        }
    }

    fun setSpotlight(state: Boolean) {
        if (state) {
            animateScenery(mimeSpotlight, spotlightOn)
            animateScenery(playerSpotLight, spotlightOff)
        } else {
            animateScenery(mimeSpotlight, spotlightOff)
            animateScenery(playerSpotLight, spotlightOn)
        }
    }

    fun spotlightMime() = setSpotlight(true)

    fun spotlightPlayer() {
        face(mimeNpc,mimeNpc.location.transform(Direction.SOUTH))
        setSpotlight(false)
    }

    fun checkSuccess() {
        if (activePlayerList.isEmpty()) return

        val iterator = activePlayerList.iterator()
        while (iterator.hasNext()) {
            val player = iterator.next()
            closeInterface(player)
            var currentSuccessCount = getAttribute(player, MIME_SUCCESS_COUNT, 0)
            if (getAttribute(player,MIME_SUCCESS, false)) {
                animate(player,Animation(Animations.HUMAN_CHEER_862))
                currentSuccessCount++
            } else {
                animate(player,Animation(Animations.HUMAN_CRY_860))
            }

            setAttribute(player, MIME_SUCCESS_COUNT, currentSuccessCount)
            player.debug("[MIME_DEBUG] success count $currentSuccessCount")

            if (currentSuccessCount >= 3) {
                iterator.remove()
                removeAttribute(player, MIME_SUCCESS)
                removeAttribute(player, MIME_SUCCESS_COUNT)
                returnToWorld(player)
            }
        }
    }

    //thank you Ceikry
    fun giveMimeEmote(player: Player): Boolean {
        for (emote in mimeEmotes.shuffled()) {
            if (player.emoteManager.isUnlocked(emote)) continue
            player.emoteManager.unlock(emote)
            sendMessage(player, "You have unlocked a new Mime emote!")
            return true
        }
        return false
    }

    fun giveMimeItem(player: Player): Boolean {
        for (mimeItem in mimeItems.shuffled()) {
            if (hasAnItem(player, mimeItem, checkPOH = true).container != null) continue
            addItemOrDrop(player, mimeItem)
            return true
        }
        return false
    }

    fun handleReward(player: Player) {
        val rewardPool = listOf(::giveMimeEmote,::giveMimeItem)
        val rewardGiven = rewardPool.shuffled().any { it(player) }

        if (!rewardGiven) {
            addItemOrDrop(player, Items.COINS_995, 500)
        }

        restoreTabs(player)
    }

    fun returnToWorld(player: Player) {
        queueScript(player, 2, QueueStrength.SOFT) {
            player.locks.unlockTeleport()
            val destination = getAttribute(player, "/save:original-loc", ServerConstants.HOME_LOCATION)
            teleport(player, destination!!, TeleportManager.TeleportType.INSTANT)
            return@queueScript stopExecuting(player)
        }

        queueScript(player, 4, QueueStrength.SOFT) {
            setComponentVisibility(player, 548, 69, false)
            setComponentVisibility(player, 746, 12, false)
            unlock(player)
            removeAttributes(player, "/save:original-loc", "kidnapped-by-random")
            handleReward(player)
            return@queueScript stopExecuting(player)
        }

    }

    override fun getRestrictions(): Array<ZoneRestriction> = arrayOf(ZoneRestriction.RANDOM_EVENTS, ZoneRestriction.CANNON, ZoneRestriction.FOLLOWERS, ZoneRestriction.OFF_MAP)

    override fun newInstance(p: Player?): ActivityPlugin? = null

    override fun getSpawnLocation(): Location = spawnInLocation

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(ZoneBorders(2007, 4761, 2010, 4766))

    override fun areaEnter(entity: Entity) {
        if (!entity.isPlayer) return
        entity as Player
        removeTabs(entity, GAMETAB.AME_DEFAULT)
        entity.location = spawnInLocation
        sendMessage(entity,copyMimePerformanceFirst)
        queueScript(entity, 1, QueueStrength.SOFT) {
            waitingPlayerList.add(entity)
            return@queueScript stopExecuting(entity)
        }

        entity.locks.lockTeleport(1000000)
        super.areaEnter(entity)
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity.isPlayer) {
            waitingPlayerList.remove(entity)
            activePlayerList.remove(entity)
        }
    }
}

@Initializable
class MimeInterface() : ComponentPlugin() {
    override fun open(player: Player?, component: Component?) { super.open(player, component); player ?: return }

    override fun handle(player: Player, component: Component?, opcode: Int, button: Int, slot: Int, itemId: Int): Boolean {
        val emote = MimeEmote.values().firstOrNull { it.buttonId == button }
        if (emote == null) return true
        val entry = activePlayerList.firstOrNull { it == player } ?: return true

        animate(entry, Animation(emote.anim))
        if (emote.anim == lastEmote) {
            setAttribute(player, MIME_SUCCESS,true)
        }
        closeInterface(player)
        return true
    }
    override fun newInstance(arg: Any?): Plugin<in Any> {
        ComponentDefinition.forId(Components.MACRO_MIME_EMOTES_188).plugin = this
        return this
    }
}

@Initializable
class MimeTestCommands : CommandSet(Privilege.ADMIN) {
    override fun defineCommands() {
        define("mimeroll", Privilege.ADMIN, "::mimeroll", "Rolls mime rewards.") { player, _ ->
            MimeActivity().handleReward(player)
        }

        define("mimereset", Privilege.ADMIN, "::resetmime", "Resets mime Emotes.") { player, _ ->
            mimeEmotes.forEach { player.emoteManager.lock(it) }
            sendMessage(player,"All Mime emotes have been locked.")
        }
    }
}
