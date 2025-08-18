package content.region.kandarin.quest.murdermystery

import core.api.*
import core.game.dialogue.DialogueFile
import core.tools.END_DIALOGUE
import org.rs09.consts.Items

class FlypaperDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> sendDialogue(player!!, "There's some flypaper in there. Should I take it?").also { stage++ }
            1 -> options("Yes, it might be useful.", "No, I don't see any need for it.").also { stage++ }
            2 -> when(buttonID) {
                1 -> if (addItem(player!!, Items.FLYPAPER_1811)){
                        sendDialogue(player!!, "You take a piece of fly paper. There is still plenty of fly paper left.")
                        .also { stage = END_DIALOGUE }
                    }
                    else {
                        sendDialogue(player!!, "You don't have enough space in your inventory.")
                        .also { stage = END_DIALOGUE }
                    }
                2 -> sendDialogue(player!!, "You leave the paper in the sack.").also { stage = END_DIALOGUE }
            }
        }
    }
}