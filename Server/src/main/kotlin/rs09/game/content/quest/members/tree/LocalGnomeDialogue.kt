package rs09.game.content.quest.members.tree

import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE

class LocalGnomeDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> playerl("Hello little man.").also { stage++ }
            1 -> npcl("Little man stronger than big man. Hee hee, lardi dee, lardi da.").also { stage = END_DIALOGUE }
        }
    }
}