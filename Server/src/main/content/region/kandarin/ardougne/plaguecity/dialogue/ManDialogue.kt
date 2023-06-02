package content.region.kandarin.ardougne.plaguecity.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.plugin.Initializable
import core.tools.END_DIALOGUE

@Initializable
class ManDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> npcl(FacialExpression.HALF_GUILTY, "We don't have good days here anymore. Curse King Tyras.").also { stage++ }
            1 -> options("Oh okay, bad day then.", "Why, what has he done?", "I'm looking for a woman called Elena.").also { stage++ }
            2 -> when (buttonID) {
                1 -> playerl(FacialExpression.FRIENDLY, "Oh okay, bad day then.").also { stage = END_DIALOGUE }
                2 -> playerl(FacialExpression.FRIENDLY, "Why, what has he done?").also { stage = 3 }
                3 -> playerl(FacialExpression.FRIENDLY, "I'm looking for a woman called Elena.").also { stage = 4 }
            }
            3 -> npcl(FacialExpression.FRIENDLY, "His army curses our city with this plague then wanders off again, leaving us to clear up the pieces.").also { stage = END_DIALOGUE }
            4 -> npcl(FacialExpression.THINKING, "Not heard of her.").also { stage = END_DIALOGUE }
        }
    }
}