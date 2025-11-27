package content.region.asgarnia.falador.dialogue

import content.data.tables.BirdNest
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items
import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.node.entity.player.link.diary.*

/**
 * Represents the Wyson the gardener dialogue.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
class  WysonTheGardenerDialogue(player: Player? = null) : DialoguePlugin(player) {

    /**
     * Constructs a new `WysonTheGardenerDialogue` `Object`.
     * Mole part dialogue source: https://www.youtube.com/watch?v=Dw-P9T7EhZk and https://www.youtube.com/watch?v=krZiIRupKbs
     * @param player the player.
     */
    //constructor(player: Player?) : super(player) {}

    override fun newInstance(player: Player): DialoguePlugin {
        return WysonTheGardenerDialogue(player)
    }

    /**
     * Choose greeting. Either you have mole parts or just the normal greeting.
     */
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (inInventory(player, Items.MOLE_CLAW_7416, 1) && inInventory(player, Items.MOLE_SKIN_7418, 1)) {
            npc("If I'm not mistaken, you've got some claws and skin", " from a big mole there! I'll trade 'em for bird nests if ye", "likes. Or was ye wantin' some woad leaves instead?")
            stage = 102
            return true
        }
        if (inInventory(player, Items.MOLE_SKIN_7418, 1)) {
            npc("If I'm not mistaken, you've got some skin from a big", "mole there! I'll trade it for bird nests if ye likes. Or", "was ye wantin' some woad leaves instead?")
            stage = 100
            return true
        }
        if (inInventory(player, Items.MOLE_CLAW_7416, 1)) {
            npc("If I'm not mistaken, you've got some claws from a big", "mole there! I'll trade it for bird nests if ye likes. Or", "was ye wantin' some woad leaves instead?")
            stage = 101
            return true
        }
        npc("I'm the head gardener around here.", "If you're looking for woad leaves, or if you need help", "with owt, I'm yer man.")
        stage = 0
        return true
    }

    /**
     * Dialogue.
     */
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {

            /**
             * Dialogue options: woad leaves.
             */
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
            131 -> if (removeItem(player,Item(Items.COINS_995, 15) ,Container.INVENTORY)) {
                addItemOrDrop(player, Items.WOAD_LEAF_1793, 1)

                player("Thanks.")
                sendMessage(player, "You buy a woad leaf from Wyson.")
                stage = 132
            } else {
                end()
                sendMessage(player, "You need 15 gold coins to buy a woad leaf.")
            }
            132 -> {
                npc("I'll be around if you have any more gardening needs.")
                stage = 133
            }
            133 -> end()
            140 -> {
                npc("Thanks for being generous", "here's an extra woad leaf.")
                stage = 141
            }
            141 -> if (removeItem(player,Item(Items.COINS_995, 20) ,Container.INVENTORY)) {
                addItemOrDrop(player, Items.WOAD_LEAF_1793, 2)
                player("Thanks.")
                sendMessage(player, "You buy two woad leaves from Wyson.")
                stage = 132
            } else {
                end()
                sendMessage(player, "You need 20 gold coins to buy a woad leaf.")
            }
            200 -> {
                npc("Fair enough.")
                stage = 201
            }
            201 -> end()

            /**
             * Dialogue options: mole parts.
             */
            100 -> {
                options("Ok, I will trade the mole skin.", "Yes please, I need woad leaves.", "Sorry, but I'm not interested.")
                stage = 900
            }
            101 -> {
                options("Yeah, I will trade the mole claws.", "Yes please, I need woad leaves.", "Sorry, but I'm not interested.")
                stage = 901
            }
            102 -> {
                options("Yeah, I will trade the mole claws.", "Okay, I will trade the mole skin.", "Alright, I'll trade the claws and skin.", "Yes please, I need woad leaves.", "Sorry, but I'm not interested.")
                stage = 902
            }
            900 -> when (buttonId) {
                1 -> {
                    player("Ok, I will trade the mole skin.")
                    stage = 920
                }
                2 -> {
                    player("Yes please, I need woad leaves.")
                    stage = 10
                }
                3 -> {
                    player("Sorry, but I'm not interested.")
                    stage = 200
                }
            }
            901 -> when (buttonId) {
                1 -> {
                    player("Yeah, I will trade the mole claws.")
                    stage = 910
                }
                2 -> {
                    player("Yes please, I need woad leaves.")
                    stage = 10
                }
                3 -> {
                    player("Sorry, but I'm not interested.")
                    stage = 200
                }
            }
            902 -> when (buttonId) {
                1 -> {
                    player("Yeah, I will trade the mole claws.")
                    stage = 910
                }
                2 -> {
                    player("Okay, I will trade the mole skin.")
                    stage = 920
                }
                3 -> {
                    player("Alright, I'll trade the claws and skin.")
                    stage = 930
                }
                4 -> {
                    player("Yes please, I need woad leaves.")
                    stage = 10
                }
                5 -> {
                    player("Sorry, but I'm not interested.")
                    stage = 200
                }
            }
            910 -> {
                if (!inInventory(player, Items.MOLE_CLAW_7416, 1)) {
                    player("Sorry, I don't have any mole claws.")
                    stage = 999
                } else {
                addClawRewards()
                npc("Pleasure doing business with ya.")
                stage = 999
                }
            }
            920 -> {
                if (!inInventory(player, Items.MOLE_SKIN_7418, 1)) {
                    player("Sorry, I don't have any mole skins.")
                    stage = 999
                } else {
                addSkinRewards()
                npc("Pleasure doing business with ya.")
                stage = 999
                }
            }
            930 -> {
                if (!inInventory(player, Items.MOLE_CLAW_7416, 1) || !inInventory(player, Items.MOLE_SKIN_7418, 1)) {
                    player("Sorry, I don't have any.")
                    stage = 999
                } else {
                addClawRewards()
                addSkinRewards()
                npc("Pleasure doing business with ya.")
                stage = 999
                }
            }
            999 -> end()
        }
        return true
    }

    /**
     * Adds nests.
     */
    private fun addClawRewards() {
        // count the number of claws
        val nestAmount = amountInInventory(player, Items.MOLE_CLAW_7416)
        // remove the counted number of skins
        if(removeItem(player, Item(Items.MOLE_CLAW_7416, nestAmount), Container.INVENTORY)){
            // add the counted number of nests. one by one so they each have random contents
            for (i in 0 until nestAmount) {
                addItemOrDrop(player, BirdNest.getRandomNest(true).nest.id, 1)
            }
        }
    }

    private fun addSkinRewards() {
        // count the number of skins
        val nestAmount = amountInInventory(player, Items.MOLE_SKIN_7418)
        // remove the counted number of skins
        if(removeItem(player, Item(Items.MOLE_SKIN_7418, nestAmount), Container.INVENTORY)) {
            // add the counted number of nests. one by one so they each have random contents
            // if Falador Hard diary is complete, add a white lilly seed
            for (i in 0 until nestAmount) {
                addItemOrDrop(player, BirdNest.getRandomNest(true).nest.id, 1)
                if (player.achievementDiaryManager.getDiary(DiaryType.FALADOR).isComplete(2)) {
                    addItemOrDrop(player, Items.WHITE_LILY_SEED_14589, 1)
                }
            }
        }
    }

    override fun getIds(): IntArray {
        return intArrayOf(36)
    }

}