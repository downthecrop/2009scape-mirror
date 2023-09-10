package content.region.asgarnia.burthorpe.quest.trollstronghold

import core.api.finishQuest
import core.api.getQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE

class DunstanDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (getQuestStage(player!!, TrollStronghold.questName)) {
            in 1..10 -> {
                when (stage) {
                    START_DIALOGUE -> npcl(FacialExpression.FRIENDLY, "Have you managed to rescue Godric yet?").also { stage++ }
                    1 -> playerl(FacialExpression.FRIENDLY, "Not yet.").also { stage++ }
                    2 -> npcl(FacialExpression.FRIENDLY, "Please hurry! Who knows what they will do to him? Is there anything I can do in the meantime?").also { stage++ }
                    3 -> showTopics(
                            Topic(FacialExpression.THINKING, "Is it OK if I use your anvil?", 10),
                            Topic(FacialExpression.FRIENDLY, "Nothing, thanks.", END_DIALOGUE),
                    )
                    10 -> npcl(FacialExpression.FRIENDLY, "So you're a smithy are you?").also { stage++ }
                    11 -> playerl(FacialExpression.FRIENDLY, "I dabble.").also { stage++ }
                    12 -> npcl(FacialExpression.FRIENDLY, "A fellow smith is welcome to use my anvil!").also { stage++ }
                    13 -> playerl(FacialExpression.FRIENDLY, "Thanks!").also { stage++ }
                    14 -> npcl(FacialExpression.FRIENDLY, "Anything else before I get on with my work?").also { stage = 3 }
                }
            }
            11 -> {
                when (stage) {
                    START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "Has Godric returned home?").also { stage++ }
                    1 -> npcl(FacialExpression.FRIENDLY, "He is safe and sound, thanks to you my friend!").also { stage++ }
                    2 -> playerl(FacialExpression.FRIENDLY, "I'm glad to hear it.").also { stage++ }
                    3 -> npcl(FacialExpression.FRIENDLY, "I have very little to offer you by way of thanks, but perhaps you will accept these family heirlooms. They were found by my great-great-grandfather, but we still don't have any idea what they do.").also { stage++ }
                    4 -> {
                        stage = END_DIALOGUE
                        finishQuest(player!!, TrollStronghold.questName)
                    }
                }
            }
        }
    }
}