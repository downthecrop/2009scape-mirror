package rs09.game.content.jobs

import GatheringJobs
import SlayingJob
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE

class CancelJobDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage){
            0 -> options("Check Job Progress","Cancel Job").also { stage++ }
            1 -> when(buttonID){
                1 -> player("How am I doing on my job?").also { stage++ }
                2 -> player("I'd like to cancel my job.").also { stage = 10 }
            }
            2 -> {
                val amt = player!!.getAttribute("jobs:original_amount",0)
                val type = player!!.getAttribute("jobs:type",0)
                val jobId = player!!.getAttribute("jobs:id",0)
                val needed = player!!.getAttribute("jobs:amount",0)
                if(type == 0){
                    player!!.dialogueInterpreter.sendItemMessage(GatheringJobs.values()[jobId].itemId,"You still need to gather ${needed} more.")
                } else {
                    player!!.dialogueInterpreter.sendDialogue("You still need to kill $needed more ${NPC(SlayingJob.values()[jobId].ids[0]).name.toLowerCase()}.")
                }
                stage = END_DIALOGUE
            }

            10 -> npc("It will cost 500 coins.").also { stage++ }
            11 -> options("Yes, please.","No, thanks.").also { stage++ }
            12 -> when(buttonID){
                1 -> player("Yes, please.").also{ stage = 20 }
                2 -> player("No, thanks.").also { stage = 30 }
            }
            20 -> npc("Alright then, hand over the money.").also { stage++ }
            21 -> if(player!!.inventory.contains(995,500)){
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

            22 -> npc("Ah, that sucks! Get to work.").also { stage = END_DIALOGUE }
            30 -> npc("Alright then, get to work!").also { stage = END_DIALOGUE }
        }
    }
}