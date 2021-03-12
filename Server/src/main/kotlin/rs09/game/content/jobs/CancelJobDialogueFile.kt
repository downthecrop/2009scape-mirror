package rs09.game.content.jobs

import core.game.node.item.Item
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE

class CancelJobDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage){
            0 -> npc("Would you like to cancel your job?","It will cost 500 coins.").also { stage++ }
            1 -> options("Yes, please.","No, thanks.").also { stage++ }
            2 -> when(buttonID){
                1 -> player("Yes, please.").also{ stage = 10 }
                2 -> player("No, thanks.").also { stage = 20 }
            }
            10 -> npc("Alright then, hand over the money.").also { stage++ }
            11 -> if(player!!.inventory.contains(995,500)){
                player("Here you go.")
                player!!.inventory.remove(Item(995,500))
                player!!.removeAttribute("jobs:id")
                player!!.removeAttribute("jobs:amount")
                player!!.removeAttribute("jobs:original_amount")
                player!!.removeAttribute("jobs:type")
                stage = END_DIALOGUE
            } else {
                player("Oh, I don't seem to have the money...")
                stage++
            }

            12 -> npc("Ah, that sucks! Get to work.").also { stage = END_DIALOGUE }
            20 -> npc("Alright then, get to work!").also { stage = END_DIALOGUE }
        }
    }
}