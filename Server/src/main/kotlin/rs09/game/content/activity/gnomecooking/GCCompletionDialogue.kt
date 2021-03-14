package rs09.game.content.activity.gnomecooking

import core.game.content.dialogue.FacialExpression
import core.game.node.item.Item
import rs09.game.content.activity.gnomecooking.GnomeTipper.getTip
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE
import rs09.tools.stringtools.colorize

class GCCompletionDialogue(val job: GnomeCookingJob) : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage){
            0 -> {
                val neededItem = player!!.getAttribute<Item>("$GC_BASE_ATTRIBUTE:$GC_NEEDED_ITEM", null)
                if (neededItem != null && player!!.inventory.containsItem(neededItem)) {
                    player!!.dialogueInterpreter.sendDialogues(job.npc_id, FacialExpression.OLD_HAPPY, "Thank you!")
                    player!!.inventory.remove(neededItem)
                    player!!.inventory.add(getTip(job.level))
                    player!!.removeAttribute("$GC_BASE_ATTRIBUTE:$GC_JOB_ORDINAL")
                    player!!.removeAttribute("$GC_BASE_ATTRIBUTE:$GC_NEEDED_ITEM")
                    var curPoints = player!!.getAttribute("$GC_BASE_ATTRIBUTE:$GC_POINTS", 0)
                    curPoints += 3
                    if (curPoints == 12) {
                        player!!.inventory.add(Item(9474))
                        player!!.sendMessage(colorize("%RYou have been granted a food delivery token. Use it to have food delivered."))
                    } else if (curPoints % 12 == 0) {
                        var curRedeems = player!!.getAttribute("$GC_BASE_ATTRIBUTE:$GC_REDEEMABLE_FOOD", 0)
                        player!!.setAttribute(
                            "/save:$GC_BASE_ATTRIBUTE:$GC_REDEEMABLE_FOOD",
                            if (curRedeems != 10) ++curRedeems else curRedeems
                        )
                        player!!.sendMessage(colorize("%RYou have been granted a single food delivery charge."))
                    }
                    player!!.setAttribute("/save:$GC_BASE_ATTRIBUTE:$GC_POINTS", curPoints)
                } else {
                    player!!.dialogueInterpreter.sendDialogues(job.npc_id, FacialExpression.ANGRY, "Where's my food?!")
                }
                stage = END_DIALOGUE
            }
        }
    }
}