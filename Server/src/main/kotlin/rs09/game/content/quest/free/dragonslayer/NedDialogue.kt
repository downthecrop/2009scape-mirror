package rs09.game.content.quest.free.dragonslayer

import core.game.content.dialogue.DialoguePlugin
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.AchievementDiary
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import rs09.game.content.quest.free.princealirescue.NedPARDialogue
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
        val dSlayerStage = player.questRepository.getStage("Dragon Slayer")
        val parStage = player.questRepository.getStage("Prince Ali Rescue")

        if (dSlayerStage == 20 || dSlayerStage == 30) {
            addOption("Dragon Slayer", NedDSDialogue(dSlayerStage))
        }
        if (parStage == 20 || parStage == 30 || parStage == 40 || parStage == 50) {
            addOption("Prince Ali Rescue", NedPARDialogue(parStage))
        }

        if (!sendChoices()) {
            npc(
                "Why, hello there, lad. Me friends call me Ned. I was a",
                "man of the sea, but it's past me now. Could I be",
                "making or selling you some rope?"
            )
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {

            DIALOGUE_INITIAL_OPTIONS_HANDLE -> {
                npc(
                    "Why, hello there, lad. Me friends call me Ned. I was a",
                    "man of the sea, but it's past me now. Could I be",
                    "making or selling you some rope?"
                )
                loadFile(optionFiles[buttonId - 1])
            }

            0 -> {
                options(
                    "Yes, I would like some rope.",
                    "No thanks Ned, I don't need any.",
                    "Ask about Achievement Diaries"
                )
                stage = 1
            }
            1 -> when (buttonId) {
                1 -> {
                    player("Yes, I would like some rope.")
                    stage = 10
                }
                2 -> {
                    player("No thanks Ned, I don't need any.")
                    stage = END_DIALOGUE
                }
                3 -> {
                    player("I'd like to talk about Achievement Diaries.")
                    stage = 900
                }
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
            900 -> {
                if (diary == null) {
                    diary = player.achievementDiaryManager.getDiary(DiaryType.LUMBRIDGE)
                }
                if (diary!!.isComplete(level, true) && !diary!!.isLevelRewarded(level)) {
                    player("I've done all the medium tasks in my Lumbridge", "Achievement Diary.")
                    stage = 950
                }
                else if (diary!!.isLevelRewarded(level) && diary!!.isComplete(level, true) && !player.hasItem(diary!!.type.getRewards(level)[0])) {
                    player("I've seemed to have lost my explorer's ring...")
                    stage = 960
                }
                else {
                    options(
                        "What is the Achievement Diary?",
                        "What are the rewards?",
                        "How do I claim the rewards?",
                        "See you later."
                    )
                    stage = 901
                }
            }
            901 -> when (buttonId) {
                1 -> {
                    player("What is the Achievement Diary?")
                    stage = 910
                }
                2 -> {
                    player("What are the rewards?")
                    stage = 920
                }
                3 -> {
                    player("How do I claim the rewards?")
                    stage = 930
                }
                4 -> {
                    player("See you later!")
                    stage = 940
                }
            }
            910 -> {
                npc(
                    "Ah, well it's a diary that helps you keep track of",
                    "particular achievements you've made in the world of",
                    "2009Scape. In Lumbridge and Draynor i can help you",
                    "discover some very useful things indeed."
                )
                stage++
            }
            911 -> {
                npc("Eventually with enough exploration you will be", "rewarded for your explorative efforts.")
                stage++
            }
            912 -> {
                npc(
                    "You can access your Achievement Diary by going to",
                    "the Quest Journal. When you've opened the Quest",
                    "Journal click on the green star icon on the top right",
                    "hand corner. This will open the diary."
                )
                stage = 900
            }
            920 -> {
                npc(
                    "Ah, well there are different rewards for each",
                    "Achievement Diary. For completing the Lumbridge and",
                    "Draynor diary you are presented with an explorer's",
                    "ring."
                )
                stage++
            }
            921 -> {
                npc("This ring will become increasingly useful with each", "section of the diary that you complete.")
                stage = 900
            }
            930 -> {
                npc(
                    "You need to complete the task so that they're all ticked",
                    "off then you can claim your reward. Most of them are",
                    "straightforward although you might find some required",
                    "quests to be started, if not finished."
                )
                stage++
            }
            931 -> {
                npc(
                    "To claim the explorer's ring speak to Explorer Jack in ",
                    "Lumbridge, Bob in Bob's Axes in Lumbridge, or myself."
                )
                stage = 900
            }
            940 -> end()
            950 -> {
                npc("Yes I see that, you'll be wanting your", "reward then I assume?")
                stage++
            }
            951 -> {
                player("Yes please.")
                stage++
            }
            952 -> {
                diary!!.setLevelRewarded(level)
                for (i in diary!!.type.getRewards(level)) {
                    player.inventory.add(i, player)
                }
                npc("This ring is a representation of the adventures you", "went on to complete your tasks.")
                stage++
            }
            953 -> {
                player("Wow, thanks!")
                stage = 900
            }
            960 -> {
                player.inventory.add(diary!!.type.getRewards(level)[0], player)
                npc("You better be more careful this time.")
                stage = 900
            }
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