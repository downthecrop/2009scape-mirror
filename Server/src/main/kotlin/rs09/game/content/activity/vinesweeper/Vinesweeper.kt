import api.ContentAPI
import core.game.component.Component
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Location
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import org.rs09.consts.Items
import rs09.game.content.dialogue.DialogueFile
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.InterfaceListener

val AVACH_NIMPORTO_LOC = Location.create(1637, 4709)
val PORTAL = 29534
val SIGNS = intArrayOf(29461, 29462, 29463, 29464)
val HOLES = intArrayOf(29476, 29477, 29478)
val NUMBERS = intArrayOf(29447, 29448, 29449, 29450, 29451, 29452, 29453, 29454, 29455)
var i = 0

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
val VINESWEEPER_BORDERS = ZoneBorders(1600,4672,1663,4735)

class VinesweeperListener : InteractionListener() {
    var SEED_LOCS: HashSet<Location> = HashSet()
    fun populateSeeds() {
        while(SEED_LOCS.size < MAX_SEEDS) {
            val loc = VINESWEEPER_BORDERS.getRandomLoc()
            val scenery = ContentAPI.getScenery(loc)
            if(scenery != null && HOLES.contains(scenery.id)) {
                SEED_LOCS.add(loc)
            }
        }
        System.out.println("seed_locs: ${SEED_LOCS}")
    }
    fun isSeed(loc: Location): Boolean {
        val scenery = ContentAPI.getScenery(loc)
        return scenery != null && HOLES.contains(scenery.id) && SEED_LOCS.contains(scenery.location)
    }
    fun isHole(loc: Location): Boolean {
        val scenery = ContentAPI.getScenery(loc)
        return scenery != null && HOLES.contains(scenery.id)
    }
    fun dig(player: Player, loc: Location) {
        if(isSeed(loc)) {
            player.sendMessage("TODO: dead plant and decrement points and schedule farmer")
        } else {
            var count = 0
            for(dx in -1..1) {
                for(dy in -1..1) {
                    if(isSeed(loc.transform(dx, dy, 0))) {
                        count += 1
                    }
                }
            }
            val scenery = ContentAPI.getScenery(loc)
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
            val loc = player.getAttribute("vinesweeper:return-tele", Location.create(3052, 3304))
            ContentAPI.teleport(player, loc)
            return@on true
        }
        on(SIGNS, SCENERY, "read") { player, node ->
            player.interfaceManager.open(Component(INSTRUCTION_SIGNS[node.id]!!))
            return@on true
        }
        on(HOLES, SCENERY, "dig") { player, node ->
            dig(player, node.location)
            //SceneryBuilder.replace(node.asScenery(), node.asScenery().transform(NUMBERS[i++ % NUMBERS.size]))
            return@on true
        }
        on(MRS_WINKIN, NPC, "trade") { player, node ->
            player.interfaceManager.open(Component(686))
            return@on true
        }
        on(MRS_WINKIN, NPC, "buy-flags") { player, node ->
            return@on true
        }
        on(FARMER_BLINKIN, NPC, "talk-to") { player, npc ->
            //player.interfaceManager.open(Component(TUTORIAL))
			ContentAPI.openDialogue(player, BlinkinDialogue(), npc)
            return@on true
        }
        on(FARMER_BLINKIN, NPC, "buy-flags") { player, npc ->
            val dialogue = BlinkinDialogue()
            dialogue.stage = 21
            ContentAPI.openDialogue(player, dialogue, npc)
            return@on true
        }
        on(FARMER_BLINKIN, NPC, "buy-roots") { player, npc ->
            val dialogue = BlinkinDialogue()
            dialogue.stage = 40
            ContentAPI.openDialogue(player, dialogue, npc)
            return@on true
        }
    }
}

class VinesweeperZone : MapZone("Vinesweeper", true) {
    override fun enter(e: Entity): Boolean {
        if(e is Player) {
            e.interfaceManager.openOverlay(Component(689))
            e.configManager.set(1195, 31337 shl 6);
        }

        return super.enter(e)
    }

    override fun leave(e: Entity, logout: Boolean): Boolean {
        if(e is Player) {
            e.interfaceManager.closeOverlay()
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

        // Magic number from dumps/scripts/2003.cs2
        TRADE_FOR_XP_BUTTON to Reward(11209, 0)
    )


    override fun defineListeners() {
        onOpen(IFACE) { player, _ ->
            /*for((buttonID, reward) in REWARDS) {
                ContentAPI.sendItemOnInterface(player, IFACE, buttonID, reward.itemID, 5)
            }*/
            //player.packetDispatch.sendRunScript(2003, "")
            return@onOpen true
        }
        on(IFACE) { player, component, opcode, buttonID, slot, itemID ->
            player.sendMessage("vinesweeper ${opcode}, ${buttonID}, ${slot}, ${itemID}")
            when(opcode) {
                Opcode.VALUE.value -> {
                    when(buttonID) {
                        TRADE_FOR_XP_BUTTON -> {
                            player.packetDispatch.sendInterfaceConfig(686, 60, false)
                        }
                        XP_CONFIRM -> {
                            player.sendMessage("TODO: award xp")
                            player.packetDispatch.sendInterfaceConfig(686, 60, true)
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
