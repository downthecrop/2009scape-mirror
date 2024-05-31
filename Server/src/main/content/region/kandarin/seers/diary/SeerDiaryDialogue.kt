package content.region.kandarin.seers.diary

import core.api.sendItemDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.link.diary.AchievementDiary
import core.game.node.entity.player.link.diary.DiaryType
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE

class SeerDiaryDialogue : DialogueFile() {

    companion object {

        const val LOST_HEAD_BAND = 20
        const val HEAD_BAND_HELP = 30
        const val CLAIM_HEAD_BAND = 40
        const val ASK_FOR_HELP = 50
    }

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            START_DIALOGUE ->{
                        if (AchievementDiary.canReplaceReward(player, DiaryType.SEERS_VILLAGE, 0)) {
                            playerl(FacialExpression.SAD, "I seem to have lost my seers' headband...").also {
                                stage = LOST_HEAD_BAND
                            }
                        } else if (AchievementDiary.hasClaimedLevelRewards(player, DiaryType.SEERS_VILLAGE, 0)) {
                            playerl(FacialExpression.ASKING, "Can you remind me what my headband does?").also {
                                stage = HEAD_BAND_HELP
                            }
                        } else if (AchievementDiary.canClaimLevelRewards(player, DiaryType.SEERS_VILLAGE, 0)) {
                            playerl(
                                FacialExpression.HAPPY,
                                "Hi. I've completed the Easy tasks in my Achievement Diary."
                            ).also {
                                stage = CLAIM_HEAD_BAND
                            }
                        } else {
                            playerl(FacialExpression.ASKING, "Do you have an Achievement Diary for me?").also {
                                stage = ASK_FOR_HELP
                            }
                        }
                    }


        LOST_HEAD_BAND -> {
            if (AchievementDiary.grantReplacement(player, DiaryType.SEERS_VILLAGE, 0))
                npcl(FacialExpression.ANNOYED, "Here's your replacement. Please be more careful.").also {
                    stage = END_DIALOGUE
                }
            else
            // This line is just guessed
                npcl(FacialExpression.HALF_GUILTY, "It seems your inventory is full").also { stage = END_DIALOGUE }
        }
        HEAD_BAND_HELP -> npcl(FacialExpression.NEUTRAL, "Your headband marks you as an honourary seer. Geoffrey - who works in the field to the south - will give you free flax every day.").also {
            stage = END_DIALOGUE
        }

        // This has to be npc otherwise wordwrap goes wrong and extends to 5 lines
        ASK_FOR_HELP -> npc(FacialExpression.HAPPY, "I certainly do - we have a set of tasks spanning Seers'", "Village, Catherby, Hemenster and the Sinclair Mansion.",
        "Just complete the tasks listed in the Achievement Diary", "and they will be ticked off automatically.").also { stage++ }
        ASK_FOR_HELP + 1 -> playerl(FacialExpression.ASKING, "Can you help me out with the Achievement Diary tasks?").also { stage++ }
        ASK_FOR_HELP + 2 -> npcl(FacialExpression.SAD,
        "I'm afraid not. It is important that adventurers complete the tasks unaided. That way, only the truly worthy collect the spoils.").also {
            stage = END_DIALOGUE
        }

        CLAIM_HEAD_BAND -> npcl(FacialExpression.HAPPY, "Well done, adventurer. You are clearly a "+(if (player!!.isMale) "man" else "woman")+" of great wisdom. I have a gift for you.").also { stage++ }
        CLAIM_HEAD_BAND + 1 -> {
            if (!AchievementDiary.flagRewarded(player, DiaryType.SEERS_VILLAGE, 0)) {
                npcl(FacialExpression.NEUTRAL, "Come back when you have two free inventory slots.").also {
                    stage = END_DIALOGUE
                }
            } else {
                sendItemDialogue(player!!, AchievementDiary.getRewards(DiaryType.SEERS_VILLAGE, 0)[0],
                    "The seer hands you a strange-looking headband and a rusty lamp.").also { stage++ }
            }
        }
        CLAIM_HEAD_BAND + 2 -> npcl(FacialExpression.HAPPY, "You are now an honourary seer and Geoffrey - who works in the field to the south - will give you free flax every day. Don't call him 'Geoffrey' though: he prefers to be known as 'Flax'.").also { stage++ }
        CLAIM_HEAD_BAND + 3 -> playerl(FacialExpression.ASKING, "Flax? What sort of name is that for a person?").also { stage++ }
        CLAIM_HEAD_BAND + 4 -> npcl(FacialExpression.NEUTRAL, "I know, I know. The poor boy is a simple soul - he just really loves picking flax. A little too much, I fear.").also {
            stage = END_DIALOGUE
        }
    }
    }
}