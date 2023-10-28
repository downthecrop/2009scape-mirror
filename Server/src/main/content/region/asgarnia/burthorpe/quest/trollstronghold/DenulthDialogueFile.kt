package content.region.asgarnia.burthorpe.quest.trollstronghold

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.Items

/**
 * Denulth sub dialogue file for troll stronghold.
 * Called by DenulthDialogue
 * @author ovenbread
 */
class DenulthDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (getQuestStage(player!!, TrollStronghold.questName)) {
            in 1..7 -> {
                when (stage) {
                    START_DIALOGUE -> npcl(FacialExpression.FRIENDLY, "How are you getting on with rescuing Godric?").also { stage++ }
                    1 -> {
                        if (hasAnItem(player!!, Items.CLIMBING_BOOTS_3105).exists()) {
                            playerl(FacialExpression.FRIENDLY, " I've got some climbing boots.").also { stage = 2 }
                        } else {
                            playerl(FacialExpression.FRIENDLY, "I haven't found a way to climb up yet.").also { stage = 3 }
                        }
                    }
                    2 -> npcl(FacialExpression.FRIENDLY, "Then hurry, friend! What are you still doing here?").also { stage = END_DIALOGUE }
                    3 -> npcl(FacialExpression.FRIENDLY, "Hurry, friend! Who knows what they'll do with Godric?").also { stage = END_DIALOGUE }
                }
            }
            in 8 .. 10 -> {
                when (stage) {
                    START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "Hello!").also { stage++ }
                    1 -> npcl(FacialExpression.FRIENDLY, "Welcome back friend!").also { stage++ }
                    2 -> playerl(FacialExpression.FRIENDLY, "I've found my way into the prison.").also { stage++ }
                    3 -> npcl(FacialExpression.FRIENDLY, "...and?").also { stage++ }
                    4 -> playerl(FacialExpression.FRIENDLY, "That's all.").also { stage++ }
                    5 -> npcl(FacialExpression.FRIENDLY, "Hurry, friend. Find a way to free Godric!").also {
                        stage = END_DIALOGUE
                    }
                }
            }
            11 -> {
                when (stage) {
                    START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "Hello!").also { stage++ }
                    1 -> npcl(FacialExpression.FRIENDLY, "Welcome back friend!").also { stage++ }
                    2 -> playerl(FacialExpression.FRIENDLY, "I have freed Godric!").also { stage++ }
                    3 -> npcl(FacialExpression.FRIENDLY, "Oh, what great news! You should hurry to tell Dunstan, he will be overjoyed!").also {
                        stage = END_DIALOGUE
                    }
                }
            }
        }
    }
}