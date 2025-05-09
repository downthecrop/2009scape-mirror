package content.region.kandarin.quest.tree

import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.tools.END_DIALOGUE

class LocalGnomeDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> playerl("Hello little man.").also { stage++ }
            1 -> npcl(FacialExpression.OLD_LAUGH1, "Little man stronger than big man. Hee hee, lardi dee, lardi da.").also { stage = END_DIALOGUE }
        }
    }
}