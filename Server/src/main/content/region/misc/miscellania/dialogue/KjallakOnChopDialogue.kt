package content.region.misc.miscellania.dialogue

import core.tools.END_DIALOGUE
import core.game.dialogue.DialogueFile

class KjallakOnChopDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage){
            0 -> npc("Hey! You're not allowed to chop those!").also { stage++ }
            1 -> player("Oh, ok...").also { stage = END_DIALOGUE }
        }
    }
}