package content.region.karamja.brimhaven.dialogue

import core.api.openInterface
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.diary.AchievementDiary
import core.game.node.entity.player.link.diary.DiaryType
import core.tools.END_DIALOGUE
import org.rs09.consts.Components
import org.rs09.consts.NPCs

class PirateJackieDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> playerl(FacialExpression.NEUTRAL, "Ahoy there!").also { stage++ }
            1 -> npcl(FacialExpression.NEUTRAL, "Ahoy!").also { stage++ }
            2 -> options(
                "What is this place?",
                "What do you do?",
                "I'd like to trade in my tickets, please.",
                "I have a question about my Achievement Diary.",
                "See you later."
            ).also { stage++ }

            3 -> when (buttonID) {
                1 -> playerl(FacialExpression.NEUTRAL, "What is this place?").also { stage = 10 }
                2 -> playerl(FacialExpression.NEUTRAL, "What do you do?").also { stage = 20 }
                3 -> playerl(FacialExpression.NEUTRAL, "I'd like to trade in my tickets, please.").also { stage = 30 }
                4 -> playerl(FacialExpression.NEUTRAL, "I have a question about my Achievement Diary.").also {
                    stage = 40
                }

                5 -> playerl(FacialExpression.NEUTRAL, "See you later.").also { stage = END_DIALOGUE }
            }

            10 -> npcl(FacialExpression.NEUTRAL, "Welcome to the Brimhaven Agility Arena!").also { stage++ }
            11 -> npcl(
                FacialExpression.NEUTRAL,
                "If ye want to know more talk to Cap'n Izzy, he found it!"
            ).also { stage = END_DIALOGUE }

            20 -> npcl(
                FacialExpression.NEUTRAL,
                "I be the Jack o' tickets. I exchange the tickets ye collect in the Agility Arena for " +
                        "more stuff. Ye can obtain more agility experience or some items ye won't find anywhere else!"
            ).also { stage++ }

            21 -> playerl(FacialExpression.NEUTRAL, "Sounds good!").also { stage = END_DIALOGUE }
            30 -> {
                npcl(FacialExpression.NEUTRAL, "Aye, ye be on the right track.").also { stage = END_DIALOGUE }
                end()
                openInterface(player!!, Components.AGILITYARENA_TRADE_6)
            }

            40 -> when {
                AchievementDiary.canClaimLevelRewards(player!!, DiaryType.KARAMJA, 0) ->
                    playerl(FacialExpression.NEUTRAL, "I've done all the easy tasks in my Karamja Achievement Diary.").also { stage = 410 }

                AchievementDiary.canReplaceReward(player, DiaryType.KARAMJA, 0) ->
                    playerl(FacialExpression.NEUTRAL, "I've seemed to have lost my gloves..").also { stage = 420 }

                else -> options(
                    "What is the Achievement Diary?",
                    "What are the rewards?",
                    "How do I claim the rewards?",
                    "See you later."
                ).also { stage++ }
            }

            41 -> when (buttonID) {
                1 -> playerl(FacialExpression.NEUTRAL, "What is the Achievement Diary?").also { stage = 430 }
                2 -> playerl(FacialExpression.NEUTRAL, "What are the rewards?").also { stage = 440 }
                3 -> playerl(FacialExpression.NEUTRAL, "How do I claim the rewards?").also { stage = 450 }
                4 -> playerl(FacialExpression.NEUTRAL, "See you later.").also { stage = END_DIALOGUE }
            }

            410 -> npcl(FacialExpression.NEUTRAL, "Arr, ye have that, I see yer list. I s'pose ye'll be wanting yer reward then!").also { stage++ }

            411 -> playerl(FacialExpression.NEUTRAL, "Yes please.").also { stage++ }
            412 -> {
                AchievementDiary.flagRewarded(player, DiaryType.KARAMJA, 0)
                npcl(FacialExpression.NEUTRAL,
                    "These 'ere Karamja gloves be a symbol of yer explorin' on the island. All the merchants will recognise" +
                            " 'em when yer wear 'em and mabe give ye a little discount. I'll ave a word with some of the seafarin' folk who ").also { stage++ }
            }

            413 -> npcl(FacialExpression.NEUTRAL, "sail to Port Sarim and Ardougne, so they'll take ye on board half price if year wearin' them. Arrr, take this" +
                        " lamp I found washed ashore too.").also { stage++ }

            414 -> playerl(FacialExpression.NEUTRAL, "Wow, thanks!").also { stage = 40 }
            420 -> {
                AchievementDiary.grantReplacement(player, DiaryType.KARAMJA, 0)
                npcl(FacialExpression.NEUTRAL, "Arr matey, have another pair. Ye better be more careful this time.").also { stage = 40 }
            }

            430 -> npcl(
                FacialExpression.NEUTRAL,
                "It's a diary that helps you keep track of particular achievements. Here on Karamja it can help " +
                        "you discover some quite useful things. Eventually, with enough exploration, the people of Karamja will reward you.").also { stage++ }

            431 -> npcl(FacialExpression.NEUTRAL, "You can see what tasks you have listed by clicking on the green button in the Quest List.").also { stage = 40 }
            // The below text was too long when using npcl's automatic line splitting.
            440 -> sendNormalDialogue(
                NPC(NPCs.PIRATE_JACKIE_THE_FRUIT_1055),
                FacialExpression.NEUTRAL,
                "Well, there's three different pairs of Karamja gloves,",
                "which match up with the three levels of difficulty. Each",
                "has the same rewards as the previous level, and an",
                "additional one too... but I won't spoil your surprise."
            ).also { stage++ }

            441 -> npcl(FacialExpression.NEUTRAL,
                "Rest assured, the people of Karamja are happy to see you visiting the island.").also { stage = 40 }

            450 -> npcl(FacialExpression.NEUTRAL, "To claim the different Karamja gloves, speak to Kaleb Paramaya in Shilo Village, one of the jungle " +
                        "foresters near the Kharazi Jungle, or me.").also { stage = 40 }
        }
    }
}