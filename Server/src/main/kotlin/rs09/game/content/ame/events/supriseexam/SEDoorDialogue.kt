package rs09.game.content.ame.events.supriseexam

import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE

class SEDoorDialogue(val preExam: Boolean) : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        stage = if(preExam){
            dialogue("I should probably speak with Mr. Mordaut first.")
            END_DIALOGUE
        } else {
            dialogue("The door won't budge. Perhaps I should","ask for directions.")
            END_DIALOGUE
        }
    }
}