package rs09.game.content.quest.free.dragonslayer

import core.game.content.dialogue.DialoguePlugin
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.AchievementDiary
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import rs09.game.content.dialogue.IfTopic
import rs09.game.content.dialogue.Topic
import rs09.game.content.quest.free.princealirescue.NedPARDialogue
import rs09.game.node.entity.player.link.diary.dialogues.NedDiaryDialogue
import rs09.game.world.GameWorld.settings
import rs09.tools.DIALOGUE_INITIAL_OPTIONS_HANDLE
import rs09.tools.END_DIALOGUE


/**
 * Core dialogue plugin for Ned.
 * @author Ceikry
 */
class NedDialogue(player: Player? = null) : DialoguePlugin(player) {
    /**
     * The achievement diary.
     */
    private var diary: AchievementDiary? = null
    private val level = 2

    override fun newInstance(player: Player): DialoguePlugin {
        return NedDialogue(player)
    }

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(
            "Why, hello there, lad. Me friends call me Ned. I was a",
            "man of the sea, but it's past me now. Could I be",
            "making or selling you some rope?"
        )
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                val dSlayerStage = player.questRepository.getStage("Dragon Slayer")
                val parStage = player.questRepository.getStage("Prince Ali Rescue")
                showTopics(
                    IfTopic("I'd like to talk about Dragon Slayer.", NedDSDialogue(dSlayerStage), dSlayerStage == 20 || dSlayerStage == 30),
                    IfTopic("I'd like to talk about Prince Ali Rescue.", NedPARDialogue(parStage), parStage == 20 || parStage == 30 || parStage == 40 || parStage == 50),
                    Topic("Yes, I would like some rope.", 10),
                    Topic("No thanks Ned, I don't need any.", END_DIALOGUE),
                    Topic("I'd like to ask about my Achievement Diary.", NedDiaryDialogue())
                )
            }

            10 -> {
                npc(
                    "Well, I can sell you some rope for 15 coins. Or I can",
                    "be making you some if you gets me 4 balls of wool. I",
                    "strands them together I does, makes em strong."
                )
                stage = 11
            }
            11 -> {
                player("You make rope from wool?")
                stage = 12
            }
            12 -> {
                npc("Of course you can!")
                stage = 13
            }
            13 -> {
                player("I thought you needed hemp or jute.")
                stage = 14
            }
            14 -> {
                npc("Do you want some rope or not?")
                stage = 15
            }
            15 -> stage = if (!player.inventory.containsItem(WOOL)) {
                options(
                    "Okay, please sell me some rope.",
                    "That's a little more than I want to pay.",
                    "I will go and get some wool."
                )
                16
            } else {
                options(
                    "Okay, please sell me some rope.",
                    "I have some balls of wool. Could you make me some rope?",
                    "That's a little more than I want to pay."
                )
                17
            }
            17 -> when (buttonId) {
                1 -> {
                    player("Okay, please sell me some rope.")
                    stage = 100
                }
                2 -> {
                    player("I have some balls of wool.", "Could you make me some rope?")
                    stage = 120
                }
                3 -> {
                    player("That's a little more than I want to pay.")
                    stage = 200
                }
            }
            16 -> when (buttonId) {
                1 -> {
                    player("Okay, please sell me some rope.")
                    stage = 100
                }
                2 -> {
                    player("That's a little more than I want to pay.")
                    stage = 200
                }
                3 -> {
                    player("I will go and get some wool.")
                    stage = 300
                }
            }
            40 -> when (buttonId) {
                1 -> {
                    player("Ned could you make other things from wool?")
                    stage = 41
                }
                2 -> {
                    player("Yes, I would like some rope.")
                    stage = 10
                }
                3 -> {
                    player("No thanks Ned, I don't need any.")
                    stage = 20
                }
            }
            100 -> {
                npc("There you go, finest rope in " + settings!!.name + ".")
                stage = 101
            }
            101 -> {
                interpreter.sendDialogue("You hand Ned 15 coins. Ned gives you a coil of rope.")
                stage = 102
            }
            102 -> {
                if (player.inventory.remove(COINS)) {
                    if (!player.inventory.add(ROPE)) {
                        GroundItemManager.create(ROPE, player)
                    }
                } else {
                    player.packetDispatch.sendMessage("You don't have enough coins to pay for rope.")
                }
                end()
            }
            120 -> {
                interpreter.sendDialogues(npc, null, "Sure I can.")
                stage = 121
            }
            121 -> {
                interpreter.sendDialogue("You hand over 4 balls of wool. Ned gives you a coil of rope.")
                stage = 122
            }
            122 -> {
                if (player.inventory.remove(WOOL)) {
                    player.inventory.add(ROPE)
                }
                end()
            }
            200 -> {
                npc(
                    "Well, if you ever need rope that's the price. Sorry.",
                    "An old sailor needs money for a little drop o' rum."
                )
                stage = 201
            }
            201 -> end()
            300 -> {
                npc("Aye, you do that. Remember, it takes 4 balls of wool to", "make strong rope.")
                stage = 301
            }
            301 -> end()
            20 -> {
                npc(
                    "Well, old Neddy is always here if you do. Tell your",
                    "friends. I can always be using the business."
                )
                stage = 21
            }
            21 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(743)
    }

    companion object {
        /**
         * Represents the coins item to remove.
         */
        private val COINS = Item(995, 15)

        /**
         * Represents the rope item to add.
         */
        private val ROPE = Item(954)

        /**
         * Represents the ball of wool item.
         */
        private val WOOL = Item(1759, 4)
    }
}