package content.region.kandarin.quest.murdermystery

import core.api.*
import core.game.dialogue.DialogueFile
import core.tools.END_DIALOGUE

class FountainDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> sendDialogue(player!!, "The fountain is swarming with mosquitos. There's a nest of them underneath the fountain.").also { stage++ }
            1 -> playerl("I hate mosquitos, they're so annoying!").also { stage++ }
            2 -> sendDialogue(player!!, "It's certainly clear nobody's used poison here.").also { stage = END_DIALOGUE }
        }
    }
}