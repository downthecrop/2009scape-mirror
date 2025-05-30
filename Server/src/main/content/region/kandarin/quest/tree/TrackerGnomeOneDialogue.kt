package content.region.kandarin.quest.tree

import content.data.Quests
import core.api.*
import org.rs09.consts.Items
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.tools.END_DIALOGUE

class TrackerGnomeOneDialogue : DialogueFile(){
    override fun handle(componentID: Int, buttonID: Int) {
        val questStage = getQuestStage(player!!, Quests.TREE_GNOME_VILLAGE)
        when {
            questStage >= 40 -> {
                when (stage) {
                    0 -> playerl("Hello").also { stage++ }
                    1 -> npcl(FacialExpression.OLD_BOWS_HEAD_SAD, "When will this battle end? I feel like I've been fighting forever.").also { stage = END_DIALOGUE }
                }
            }
            questStage > 30 -> {
                if(inInventory(player!!, Items.ORB_OF_PROTECTION_587)){
                    when(stage) {
                        0 -> playerl("How are you tracker?").also { stage++ }
                        1 -> npcl(FacialExpression.OLD_NORMAL, "Now we have the orb I'm much better. They won't stand a chance without it.").also { stage = END_DIALOGUE }
                    }
                } else {
                    when(stage) {
                        0 -> playerl("Hello again.").also { stage++ }
                        1 -> npcl(FacialExpression.OLD_NORMAL, "Well done, you've broken down their defences. This battle must be ours.").also { stage = END_DIALOGUE }
                    }
                }
            }
            questStage == 30 -> {
                when (stage) {
                    0 -> playerl("Do you know the coordinates of the Khazard stronghold?").also { stage++ }
                    1 -> npcl(FacialExpression.OLD_NORMAL, "I managed to get one, although it wasn't easy.").also { stage++ }
                    2 -> sendDialogue(player!!, "The gnome tells you the height coordinate.").also {
                        setAttribute(player!!, "/save:treegnome:tracker1", true)
                        stage++
                    }
                    3 -> playerl("Well done.").also { stage++ }
                    4 -> npcl(FacialExpression.OLD_NORMAL, "The other two tracker gnomes should have the other coordinates if they're still alive.").also { stage++ }
                    5 -> playerl("OK, take care.").also { stage = END_DIALOGUE }
                }
            }
        }
    }
}