package rs09.game.content.dialogue.region.falador

import core.game.content.dialogue.DialoguePlugin
import core.game.content.global.BirdNest
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items
import rs09.game.node.entity.player.link.diary.DiaryLevel

/**
 * Represents the Wyson the gardener dialogue.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
class WysonTheGardenerDialogue : DialoguePlugin {
    /**
     * If its a bird nest reward.
     */
    private var birdNest = false

    /**
     * Constructs a new `WysonTheGardenerDialogue` `Object`.
     */
    constructor() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new `WysonTheGardenerDialogue` `Object`.
     * @param player the player.
     */
    constructor(player: Player?) : super(player) {}

    override fun newInstance(player: Player): DialoguePlugin {
        return WysonTheGardenerDialogue(player)
    }

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        birdNest = player.inventory.containsItem(MOLE_CLAW) || player.inventory.containsItem(MOLE_SKIN)
        if (birdNest) {
            npc("If I'm not mistaken, you've got some mole bits there!", "I'll trade 'em for bird nest if ye likes.")
            stage = 100
            return true
        }
        npc("I'm the head gardener around here.", "If you're looking for woad leaves, or if you need help", "with owt, I'm yer man.")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                options("Yes please, I need woad leaves.", "Sorry, but I'm not interested.")
                stage = 1
            }
            1 -> when (buttonId) {
                1 -> {
                    player("Yes please, I need woad leaves.")
                    stage = 10
                }
                2 -> {
                    player("Sorry, but I'm not interested.")
                    stage = 200
                }
            }
            10 -> {
                npc("How much are you willing to pay?")
                stage = 11
            }
            11 -> {
                options("How about 5 coins?", "How about 10 coins?", "How about 15 coins?", "How about 20 coins?")
                stage = 12
            }
            12 -> when (buttonId) {
                1 -> {
                    player("How about 5 coins?")
                    stage = 110
                }
                2 -> {
                    player("How about 10 coins?")
                    stage = 110
                }
                3 -> {
                    player("How about 15 coins?")
                    stage = 130
                }
                4 -> {
                    player("How about 20 coins?")
                    stage = 140
                }
            }
            110 -> {
                npc("No no, that's far too little. Woad leaves are hard to get. I", "used to have plenty but someone kept stealing them off", "me.")
                stage = 111
            }
            111 -> end()
            130 -> {
                npc("Mmmm... ok, that sounds fair.")
                stage = 131
            }
            131 -> if (player.inventory.contains(995, 15)) {
                player.inventory.remove(COINS[0])
                player.inventory.add(WOAD_LEAF)
                player("Thanks.")
                player.packetDispatch.sendMessage("You buy a woad leaf from Wyson.")
                stage = 132
            } else {
                end()
                player.packetDispatch.sendMessage("You need 15 cold coins to buy a woad leaf.")
            }
            132 -> {
                npc("I'll be around if you have any more gardening needs.")
                stage = 133
            }
            133 -> end()
            140 -> {
                npc("Thanks for being generous", "here's an extra woad leave.")
                stage = 141
            }
            141 -> if (player.inventory.contains(995, 20)) {
                player.inventory.remove(COINS[1])
                var i = 0
                while (i < 2) {
                    player.inventory.add(WOAD_LEAF, player)
                    i++
                }
                player("Thanks.")
                player.packetDispatch.sendMessage("You buy two woad leaves from Wyson.")
                stage = 132
            } else {
                end()
                player.packetDispatch.sendMessage("You need 15 cold coins to buy a woad leaf.")
            }
            200 -> {
                npc("Fair enough.")
                stage = 201
            }
            201 -> end()
            100 -> {
                options("Yes, I will trade the mole claws.", "Okay, I will trade the mole skin.", "I'd like to trade both.", "No, thanks.")
                stage = 900
            }
            900 -> when (buttonId) {
                1 -> {
                    player("Yes, I will trade the mole claws.")
                    stage = 910
                }
                2 -> {
                    player("Okay, I will trade the mole skin.")
                    stage = 920
                }
                3 -> {
                    player("I'd like to trade both.")
                    stage = 930
                }
                4 -> {
                    player("No, thanks.")
                    stage = 999
                }
            }
            910 -> {
                if (!player.inventory.containsItem(MOLE_CLAW)) {
                    player("Sorry, I don't have any mole claws.")
                    stage = 999
                }
                end()
                addRewards()
            }
            920 -> {
                if (!player.inventory.containsItem(MOLE_SKIN)) {
                    player("Sorry, I don't have any mole skins.")
                    stage = 999
                }
                end()
                addRewards()
            }
            930 -> {
                if (!player.inventory.containsItem(MOLE_CLAW) && !player.inventory.containsItem(MOLE_SKIN)) {
                    player("Sorry, I don't have any.")
                    stage = 999
                }
                addRewards()
                end()
            }
            999 -> end()
        }
        return true
    }

    /**
     * Adds nests.
     * @param nestAmount the amount.
     */
    private fun addRewards() {
        val moleClaws = player.inventory.getAmount(Items.MOLE_CLAW_7416)
        val moleSkin = player.inventory.getAmount(Items.MOLE_SKIN_7418)
        val nestAmount = moleClaws + moleSkin

        //Remove claws and skins
        player.inventory.remove(Item(Items.MOLE_CLAW_7416,moleClaws))
        player.inventory.remove(Item(Items.MOLE_SKIN_7418, moleSkin))

        //Add white lily seeds if the player has the hard diary done
        if(moleSkin > 0 && player.achievementDiaryManager.getDiary(DiaryType.FALADOR).checkComplete(DiaryLevel.HARD)) {
            player.inventory.add(Item(14589, moleSkin), player)
        }

        //Add nests
        for (i in 0 until nestAmount) {
            if(!player.inventory.add(Item(BirdNest.getRandomNest(true).nest.id, 1), player)){
                GroundItemManager.create(Item(BirdNest.getRandomNest(true).nest.id,1),player.location,player)
            }
        }
    }

    override fun getIds(): IntArray {
        return intArrayOf(36)
    }

    companion object {
        /**
         * Represents the coins item that can be used.
         */
        private val COINS = arrayOf(Item(995, 15), Item(995, 20))

        /**
         * Represents the woad leaf item.
         */
        private val WOAD_LEAF = Item(1793, 1)

        /**
         * The mole claw item.
         */
        private val MOLE_CLAW = Item(7416)

        /**
         * The mole skin.
         */
        private val MOLE_SKIN = Item(7418)
    }
}