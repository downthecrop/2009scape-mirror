import api.*
import core.game.component.Component
import core.game.node.entity.Entity
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills;
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs09.consts.Items
import rs09.game.content.dialogue.DialogueFile
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.InterfaceListener
import rs09.game.world.GameWorld;

val AVACH_NIMPORTO_LOC = Location.create(1637, 4709)
val PORTAL = 29534
val SIGNS = intArrayOf(29461, 29462, 29463, 29464)
val HOLES = intArrayOf(29476, 29477, 29478)
val NUMBERS = intArrayOf(29447, 29448, 29449, 29450, 29451, 29452, 29453, 29454, 29455)
val DEAD_PLANT_OBJ = 29456
val FLAG_OBJ = 29457

val TUTORIAL = 685

val INSTRUCTION_SIGNS = hashMapOf(
    29463 to 684,
    29464 to 687,
    29462 to 688,
    29461 to 690
    )

val RABBIT = 7125
val FARMERS = intArrayOf(7128, 7129, 7130)
val FARMER_BLINKIN = 7131
val MRS_WINKIN = 7132

val MAX_SEEDS = 300
val FARMER_CLEAR_RADIUS = 3
val VINESWEEPER_BORDERS = ZoneBorders(1600,4672,1663,4735)

fun sendUpdatedPoints(player: Player) {
    val points = player.getAttribute("vinesweeper:points", 0);
    player.varpManager.get(1195).setVarbit(6, points).send(player)
}

data class SeedDestination(val player: Player, val loc: Location, val alive: Boolean) {
    override fun equals(other: Any?): Boolean {
        if(other is SeedDestination) {
            return loc.equals(other.loc)
        } else {
            return false
        }
    }
}
var SEED_LOCS: HashSet<Location> = HashSet()

fun isSeed(loc: Location): Boolean {
    val scenery = getScenery(loc)
    return scenery != null && SEED_LOCS.contains(scenery.location)
}

fun populateSeeds() {
    while(SEED_LOCS.size < MAX_SEEDS) {
        val loc = VINESWEEPER_BORDERS.getRandomLoc()
        val scenery = getScenery(loc)
        if(scenery != null && HOLES.contains(scenery.id)) {
            SEED_LOCS.add(loc)
        }
    }
}

fun isHole(loc: Location): Boolean {
    val scenery = getScenery(loc)
    return scenery != null && HOLES.contains(scenery.id)
}

fun scheduleNPCs(player: Player, loc: Location, alive: Boolean, rabbit: Boolean) {
    val dest = SeedDestination(player, loc, alive)
    val ids = if(rabbit) { intArrayOf(RABBIT, *FARMERS) } else { FARMERS }
    for(npc in findLocalNPCs(player, ids, 30)) {
        if(npc is VinesweeperNPC) {
            npc.seedDestinations.add(dest)
            npc.resetWalk()
        }
    }
}

object VinesweeperTeleport {
    @JvmStatic
    fun teleport(npc: NPC, player: Player) {
        npc.animate(Animation(437))
        npc.faceTemporary(player, 1)
        npc.graphics(Graphics(108))
        player.lock()
        player.audioManager.send(125)
        Projectile.create(npc, player, 109).send()
        npc.sendChat("Avach nimporto!")
        GameWorld.Pulser.submit(object : Pulse(1) {
            var counter = 0
            override fun pulse(): Boolean {
                when (counter++) {
                    2 -> {
                        player.savedData.globalData.essenceTeleporter = npc.id
                        player.setAttribute("/save:vinesweeper:return-tele:x", npc.location.x)
                        player.setAttribute("/save:vinesweeper:return-tele:y", npc.location.y)
                        player.properties.teleportLocation = AVACH_NIMPORTO_LOC
                    }
                    3 -> {
                        player.graphics(Graphics(110))
                        player.unlock()
                        return true
                    }
                }
                return false
            }
        })
    }
}

class VinesweeperListener : InteractionListener() {
    fun dig(player: Player, loc: Location) {
        if(isSeed(loc)) {
            val oldPoints = player.getAttribute("vinesweeper:points", 0)
            player.setAttribute("/save:vinesweeper:points", Math.max(oldPoints-10, 0))
            sendUpdatedPoints(player)
            player.sendMessage("Oh dear! It looks like you dug up a potato seed by mistake.");
            scheduleNPCs(player, loc, false, false)
            val scenery = getScenery(loc)
            if(scenery != null) {
                SceneryBuilder.replace(scenery, scenery.transform(DEAD_PLANT_OBJ))
            }
        } else {
            player.incrementAttribute("/save:vinesweeper:points", 1)
            sendUpdatedPoints(player)
            var count = 0
            for(dx in -1..1) {
                for(dy in -1..1) {
                    if(isSeed(loc.transform(dx, dy, 0))) {
                        count += 1
                    }
                }
            }
            val scenery = getScenery(loc)
            if(scenery != null) {
                SceneryBuilder.replace(scenery, scenery.transform(NUMBERS[count]))
            }
            if(count == 0) {
                for(dx in -1..1) {
                    for(dy in -1..1) {
                        val newLoc = loc.transform(dx, dy, 0)
                        if(isHole(newLoc))
                        dig(player, newLoc)
                    }
                }
            }
        }
    }
    override fun defineListeners() {
        ZoneBuilder.configure(VinesweeperZone())
        populateSeeds()
        on(PORTAL, SCENERY, "enter") { player, _ ->
            val x = player.getAttribute("vinesweeper:return-tele:x", 3052)
            val y = player.getAttribute("vinesweeper:return-tele:y", 3304)
            teleport(player, Location(x, y))
            return@on true
        }
        on(SIGNS, SCENERY, "read") { player, node ->
            player.interfaceManager.open(Component(INSTRUCTION_SIGNS[node.id]!!))
            return@on true
        }
        on(HOLES, SCENERY, "dig") { player, node ->
            if(!player.inventory.contains(Items.SPADE_952, 1)) {
                // TODO (crash): authenticity
                player.sendMessage("You need a spade to dig here.")
            } else {
                player.visualize(Animation(8709), Graphics(1543))
                dig(player, node.location)
            }
            return@on true
        }
        on(HOLES, SCENERY, "flag") { player, node ->
            if(player.inventory.remove(Item(Items.FLAG_12625, 1))) {
                player.sendMessage("You add a flag to the patch.")
                val scenery = node as Scenery
                SceneryBuilder.replace(scenery, scenery.transform(FLAG_OBJ))
                scheduleNPCs(player, scenery.location, true, true)
            } else {
                // TODO (crash): authenticity
                player.sendMessage("You do not have a flag to place in the patch.")
            }
            return@on true
        }
        on(HOLES, SCENERY, "inspect") { player, node ->
            player.animate(Animation(8710))
            player.lock(5)
            GameWorld.Pulser.submit(object : Pulse(5) {
                override fun pulse(): Boolean {
                    val msg = when(RandomFunction.random(0, 7)) {
                        0 -> "You don't see anything interesting. You can't be sure if there's a seed there or not."
                        1 -> "You get some mud in your eye and it stings! You have no idea what is in the hole."
                        2 -> "The mud seems to be too thick to see what is there."
                        3 -> "A slimy worm wriggles out of the mud, making you jump and lose concentration. You're not sure if there is a seed here or not."
                        else -> if(isSeed(node.location)) {
                            "You notice a seed hidden in the dirt."
                        } else {
                            "You are certain there is no seed planted here."
                        }
                    }
                    sendDialogue(player, msg)
                    return true
                }
            })
            return@on true
        }
        on(MRS_WINKIN, NPC, "talk-to") { player, npc ->
			openDialogue(player, WinkinDialogue(), npc)
            return@on true
        }
        on(MRS_WINKIN, NPC, "trade") { player, _ ->
            player.interfaceManager.open(Component(686))
            return@on true
        }
        on(MRS_WINKIN, NPC, "buy flags") { player, npc ->
            val dialogue = WinkinDialogue()
            dialogue.stage = 20
            openDialogue(player, dialogue, npc)
            return@on true
        }
        on(RABBIT, NPC, "feed") { player, node ->
            if(player.inventory.remove(Item(Items.OGLEROOT_12624, 1))) {
                val npc = node as NPC
                // TODO: find burrow animation id
                //npc.animate(9603)
                npc.sendChat("Squeak!")
                npc.lock(3)
                player.skills.addExperience(Skills.HUNTER, 30.0)
                GameWorld.Pulser.submit(object : Pulse(3) {
                    override fun pulse(): Boolean {
                        //npc.setInvisible(true)
                        npc.respawnTick = GameWorld.ticks + 50
                        npc.location = npc.properties.spawnLocation
                        return true
                    }
                })
            } else {
                // TODO (crash): authenticity
                player.sendMessage("You don't have any ogleroots to feed the rabbit.")
            }
            return@on true
        }
        on(FARMER_BLINKIN, NPC, "talk-to") { player, npc ->
            //player.interfaceManager.open(Component(TUTORIAL))
			openDialogue(player, BlinkinDialogue(), npc)
            return@on true
        }
        on(FARMER_BLINKIN, NPC, "buy-flags") { player, npc ->
            val dialogue = BlinkinDialogue()
            dialogue.stage = 21
            openDialogue(player, dialogue, npc)
            return@on true
        }
        on(FARMER_BLINKIN, NPC, "buy-roots") { player, npc ->
            val dialogue = BlinkinDialogue()
            dialogue.stage = 40
            openDialogue(player, dialogue, npc)
            return@on true
        }
    }
}

@Initializable
class VinesweeperNPC : AbstractNPC {
    fun compareDistance(a: SeedDestination, b: SeedDestination): Int {
        val da = a.loc.getDistance(location).toInt()
        val db = b.loc.getDistance(location).toInt()
        return db - da
    }
    var seedDestinations: ArrayList<SeedDestination> = ArrayList();
    constructor() : super(RABBIT, null, true) {}
    private constructor(id: Int, location: Location) : super(id, location) {}
    override fun construct(id: Int, location: Location, vararg objects: Any?): AbstractNPC {
        return VinesweeperNPC(id, location)
    }

    init {
        walkRadius = 22
    }

    override fun getIds(): IntArray {
        return intArrayOf(RABBIT, *FARMERS)
    }

    override fun handleTickActions() {
        val dest = seedDestinations.find { sd -> sd.loc == location }
        if(dest != null) {
            for(npc in RegionManager.getRegionPlane(location).npcs) {
                if(npc is VinesweeperNPC) {
                    npc.seedDestinations.remove(dest)
                    npc.resetWalk()
                }
            }
            val scenery = getScenery(dest.loc)
            if(scenery != null) {
                if(id == RABBIT) {
                    val replacement = if(SEED_LOCS.contains(dest.loc)) { DEAD_PLANT_OBJ } else { HOLES[0] }
                    SceneryBuilder.replace(scenery, scenery.transform(replacement))
                    scheduleNPCs(dest.player, dest.loc, false, false)
                } else {
                    if(dest.alive) {
                        handleFarmerFlag(scenery, dest)
                    } else {
                        sendChat("Hmm. Looks like there's a plant here.")
                        lock(3)
                        GameWorld.Pulser.submit(object : Pulse(3) {
                            override fun pulse(): Boolean {
                                sendChat("Gracious me! This one's dead")
                                SceneryBuilder.replace(scenery, scenery.transform(HOLES[0]))
                                farmerClear(dest)
                                return true
                            }
                        })
                    }
                }
            }
            seedDestinations.remove(dest)
        }
        super.handleTickActions()
    }

    override fun getMovementDestination(): Location? {
        if(seedDestinations.size > 0) {
            seedDestinations.sortBy { a -> a.loc.getDistance(location).toInt() }
            return seedDestinations.first().loc
        } else {
            return super.getMovementDestination()
        }
    }

    fun handleFarmerFlag(scenery: Scenery, dest: SeedDestination) {
        sendChat("Ah, another flag to clear. Let's see what's there.")
        lock(3)
        animate(Animation(451))
        if(SEED_LOCS.contains(dest.loc)) {
            val npc = this
            GameWorld.Pulser.submit(object : Pulse(3) {
                override fun pulse(): Boolean {
                    sendChat("Ah! A seed. Points for everyone near me!")
                    val level = dest.player.skills.getStaticLevel(Skills.FARMING)
                    val points = RandomFunction.random(level, 4 * level)
                    dest.player.incrementAttribute("/save:vinesweeper:points", points)
                    dest.player.inventory.add(Item(Items.FLAG_12625, 1))
                    sendUpdatedPoints(dest.player)
                    for(neighbor in RegionManager.getLocalPlayers(npc)) {
                        if(neighbor != dest.player) {
                            neighbor.incrementAttribute("/save:vinesweeper:points", points / 2)
                            sendUpdatedPoints(neighbor)
                        }
                    }
                    SceneryBuilder.replace(scenery, scenery.transform(HOLES[0]))
                    farmerClear(dest)
                    return true
                }
            })
        } else {
            SceneryBuilder.replace(scenery, scenery.transform(HOLES[0]))
            var i = 0
            val lines = arrayOf("Hmm, no seeds planted here, I'm afraid.", "I'll have to keep this 'ere flag. Sorry.")
            GameWorld.Pulser.submit(object : Pulse(3) {
                override fun pulse(): Boolean {
                    sendChat(lines[i++])
                    return i >= lines.size
                }
            })
        }
    }
    fun farmerClear(dest: SeedDestination) {
        for(dx in -FARMER_CLEAR_RADIUS..FARMER_CLEAR_RADIUS) {
            for(dy in -FARMER_CLEAR_RADIUS..FARMER_CLEAR_RADIUS) {
                val toClear = getScenery(dest.loc.transform(dx, dy, 0))
                if(toClear != null && intArrayOf(DEAD_PLANT_OBJ, *NUMBERS).contains(toClear.id)) {
                    SceneryBuilder.replace(toClear, toClear.transform(HOLES[0]))
                }
            }
        }
        SEED_LOCS.remove(dest.loc)
        populateSeeds()
    }
}

class VinesweeperZone : MapZone("Vinesweeper", true) {
    override fun enter(e: Entity): Boolean {
        if(e is Player) {
            e.interfaceManager.openOverlay(Component(689))
            sendUpdatedPoints(e)
        }

        return super.enter(e)
    }

    override fun leave(e: Entity, logout: Boolean): Boolean {
        if(e is Player) {
            e.interfaceManager.closeOverlay()
            if(!logout) {
                e.sendMessage("Winkin's Farm thanks you for your visit.")
                e.sendMessage("Leftover ogleroots and flags have been returned to the establishment.")
                e.sendMessage("You have been reimbursed at a rate of 10gp per ogleroot and the flags have been collected.")
                val flags = e.inventory.getAmount(Item(Items.FLAG_12625))
                if(flags > 0) {
                    e.setAttribute("/save:vinesweeper:stored-flags", flags)
                    e.inventory.remove(Item(Items.FLAG_12625, flags))
                }
                val roots = e.inventory.getAmount(Item(Items.OGLEROOT_12624))
                if(roots > 0) {
                    e.inventory.remove(Item(Items.OGLEROOT_12624, roots))
                    e.inventory.add(Item(Items.COINS_995, roots * 10))
                }
            }
        }

        return super.leave(e, logout)
    }

    override fun configure() {
        super.registerRegion(6473)
    }
}

class VinesweeperRewards : InterfaceListener() {
    val IFACE = 686
    val TRADE_FOR_XP_BUTTON = 53
    val XP_CONFIRM = 72
    val XP_DENY = 73

    enum class Opcode(val value: Int) {
        VALUE(155),
        BUY1(196),
        BUY5(124),
        BUY10(199),
        BUYX(234),
    }

    data class Reward(val itemID: Int, val points: Int) {}

    val REWARDS = hashMapOf(
        18 to Reward(Items.TOMATO_SEED_5322, 10),
        19 to Reward(Items.SWEETCORN_SEED_5320, 150),
        20 to Reward(Items.STRAWBERRY_SEED_5323, 165),
        21 to Reward(Items.WATERMELON_SEED_5321, 680),
        22 to Reward(Items.GUAM_SEED_5291, 10),
        23 to Reward(Items.MARRENTILL_SEED_5292, 10),
        24 to Reward(Items.RANARR_SEED_5295, 4000),
        25 to Reward(Items.KWUARM_SEED_5299, 1000),
        26 to Reward(Items.TARROMIN_SEED_5293, 10),
        27 to Reward(Items.NASTURTIUM_SEED_5098, 10),

        28 to Reward(Items.WOAD_SEED_5099, 30),
        29 to Reward(Items.LIMPWURT_SEED_5100, 70),
        30 to Reward(Items.ASGARNIAN_SEED_5308, 5),
        31 to Reward(Items.KRANDORIAN_SEED_5310, 20),
        32 to Reward(Items.REDBERRY_SEED_5101, 5),
        33 to Reward(Items.CADAVABERRY_SEED_5102, 5),
        34 to Reward(Items.DWELLBERRY_SEED_5103, 5),
        35 to Reward(Items.JANGERBERRY_SEED_5104, 10),
        36 to Reward(Items.WHITEBERRY_SEED_5105, 25),

        37 to Reward(Items.POISON_IVY_SEED_5106, 30),
        38 to Reward(Items.ACORN_5312, 100),
        39 to Reward(Items.WILLOW_SEED_5313, 1800),
        40 to Reward(Items.MAPLE_SEED_5314, 12000),
        41 to Reward(Items.PINEAPPLE_SEED_5287, 10000),
        42 to Reward(Items.YEW_SEED_5315, 29000),
        43 to Reward(Items.PALM_TREE_SEED_5289, 35000),
        44 to Reward(Items.SPIRIT_SEED_5317, 55000),
        45 to Reward(Items.COMPOST_POTION4_6470, 5000),
        46 to Reward(Items.FLAG_12625, 50),
    )

    fun buy(player: Player, buttonID: Int, amount: Int) {
        val reward = REWARDS[buttonID] ?: return
        val cost = amount * reward.points
        val points = player.getAttribute("vinesweeper:points", 0)
        if(cost in 1 until points) {
            val item = Item(reward.itemID, amount)
            if(!player.inventory.add(item)) {
                GroundItemManager.create(item, player)
            }
            player.incrementAttribute("/save:vinesweeper:points", -cost)
            sendUpdatedPoints(player)
        } else {
            // TODO (crash): authenticity
            player.sendMessage("You don't have enough points for that.")
        }
    }

    override fun defineListeners() {
        onOpen(IFACE) { _, _ ->
            /*for((buttonID, reward) in REWARDS) {
                sendItemOnInterface(player, IFACE, buttonID, reward.itemID, 5)
            }*/
            //player.packetDispatch.sendRunScript(2003, "")
            return@onOpen true
        }
        on(IFACE) { player, _, opcode, buttonID, _, _ ->
            when(opcode) {
                Opcode.VALUE.value -> {
                    when(buttonID) {
                        TRADE_FOR_XP_BUTTON -> {
                            player.packetDispatch.sendInterfaceConfig(686, 60, false)
                        }
                        XP_CONFIRM -> {
                            player.packetDispatch.sendInterfaceConfig(686, 60, true)
                            val level = player.skills.getStaticLevel(Skills.FARMING)
                            // TODO: more precise formula
                            val points_per_xp = if (level < 40) { 2.0*(40.0 - level.toDouble())/10.0 } else { 1.0 }
                            val points = player.getAttribute("vinesweeper:points", 0)
                            val xp = points / points_per_xp;
                            player.skills.addExperience(Skills.FARMING, xp)
                            player.setAttribute("/save:vinesweeper:points", 0)
                            sendUpdatedPoints(player)

                        }
                        XP_DENY -> {
                            player.packetDispatch.sendInterfaceConfig(686, 60, true)
                        }
                        else -> {
                            val reward = REWARDS[buttonID] ?: return@on true
                            player.sendMessage("${Item(reward.itemID).name}: ${reward.points} vinesweeper points")
                        }
                    }
                }
                Opcode.BUY1.value -> {
                    buy(player, buttonID, 1)
                    return@on true
                }
                Opcode.BUY5.value -> {
                    buy(player, buttonID, 5)
                    return@on true
                }
                Opcode.BUY10.value -> {
                    buy(player, buttonID, 10)
                    return@on true
                }
                Opcode.BUYX.value -> {
                    player.setAttribute("runscript") { amount: Int ->
                        buy(player, buttonID, amount)
                    }
                    player.dialogueInterpreter.sendInput(false, "Enter the amount:")
                    return@on true
                }
                else -> {}
            }
            return@on true
        }
    }
}



/*
https://www.youtube.com/watch?v=WkCVAOOR7Sw
buy-flags
Player: "Have you got any flags?"
NPC: "Let me check for you."
NPC: "Alright, you can have a total of 10 flags. To obtain a\nfull set of flags will cost you 5000 coins. Would you\nlike to buy these flags?"
"Yes, please." "No thanks."
NPC: "Here you are then, dear."

talk-to
NPC: "Oh hello there, dear. How can I help you?"
"Where are we?" "Have you got any flags?" "Do you have a spare spade?" "Do you have anything for trade?" "Nothing. I'm fine, thanks."

Player: "Do you have a spare spade?"
NPC: "Why, of course. I can sell you one for 5 gold pieces."
"Okay, thanks." "Actually, I've changed my mind."
NPC: "Here you are, then."

inspect:
chat log: "You examine the hole to see what might be in it."
chat dialog: "You notice a seed hidden in the dirt."

Farmer overhead:
"Ah, another flag to clear. Let's see what's there."
"Ah! A seed. Points for everyone near me!"

https://www.youtube.com/watch?v=UjxfJdHkJnM
inspect:
"Oh dear! It looks like you dug up a potato seed by mistake."
Farmer overhead:
"Hmm. Looks like there's a plant here."
"Gracious me! This one's dead"

flag:
"You add a flag to the patch."

digging ogleroot:
"You uncover a rather odd-looking root vegetable."

https://www.youtube.com/watch?v=fKyy0sgrBYM
Rabbit overhead:
"Squeak!"

https://www.youtube.com/watch?v=RnhNrwbUjjQ
Player: "Have you got any flags?"
NPC: "Let me check for you."
NPC: "Ah! First things first. One of the farmers dropped off\nsome flags for you. You can have them back. Here you\ngo."

NPC: "It looks like you have all the flags you need. You don't\nneed to buy any more."

inspect:
"You notice a seed hidden in the dirt."
"A slimy worm wriggles out of the mud, making you jump and lose\nconcentration. You're not sure if there is a seed here or not."
"You are certain there is no seed planted here."

farmer overhead:
"Hmm, no seeds planted here, I'm afraid."
"I'll have to keep this 'ere flag. Sorry."

*/
