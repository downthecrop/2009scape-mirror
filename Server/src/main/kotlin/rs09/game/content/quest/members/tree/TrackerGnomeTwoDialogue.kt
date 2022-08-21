package rs09.game.content.quest.members.tree

import api.*
import org.rs09.consts.Items
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE

class TrackerGnomeTwoDialogue : DialogueFile(){
    override fun handle(componentID: Int, buttonID: Int) {
        val questStage = questStage(player!!, TreeGnomeVillage.questName)
        when {
            questStage == 30 -> {
                when (stage) {
                    0 -> playerl("Are you OK?").also { stage++ }
                    1 -> npcl("They caught me spying on the stronghold. They beat and tortured me.").also { stage++ }
                    2 -> npcl("But I didn't crack. I told them nothing. They can't break me!").also { stage++ }
                    3 -> playerl("I'm sorry little man.").also { stage++ }
                    4 -> npcl("Don't be. I have the position of the stronghold!").also { stage++ }
                    5 -> sendDialogue(player!!, "The gnome tells you the y coordinate.").also {
                        setAttribute(player!!, "/save:treegnome:tracker2", true)
                        stage++
                    }
                    6 -> playerl("Well done.").also { stage++ }
                    7 -> npcl("Now leave before they find you and all is lost.").also { stage++ }
                    8 -> playerl("Hang in there.").also { stage++ }
                    9 -> npcl("Go!").also { stage = END_DIALOGUE }
                }
            }
            questStage >= 40 -> {
                when(stage) {
                    0 -> playerl("Hello").also { stage++ }
                    1 -> npcl("When will this battle end? I feel like I've been locked up my whole life.").also { stage = END_DIALOGUE }
                }
            }
            questStage > 30 -> {
                if(inInventory(player!!,Items.ORB_OF_PROTECTION_587)){
                    when(stage) {
                        0 -> playerl("How are you tracker?").also { stage++ }
                        1 -> npcl("Now we have the orb I'm much better. Soon my comrades will come and free me.").also { stage = END_DIALOGUE }
                    }
                } else {
                    when(stage) {
                        0 -> playerl("Hello again.").also { stage++ }
                        1 -> npcl("Well done, you've broken down their defences. This battle must be ours.").also { stage = END_DIALOGUE }
                    }
                }
            }
        }
    }
}