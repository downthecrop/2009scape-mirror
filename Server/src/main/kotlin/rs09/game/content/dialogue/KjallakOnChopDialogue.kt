package rs09.game.content.dialogue

import rs09.tools.END_DIALOGUE

class KjallakOnChopDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage){
            0 -> npc("Hey! You're not allowed to chop those!").also { stage++ }
            1 -> player("Oh, ok...").also { stage = END_DIALOGUE }
        }
    }
}