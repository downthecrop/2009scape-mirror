package rs09.game.content.quest.members.tree

import api.inInventory
import api.questStage
import org.rs09.consts.Items
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE

class RemsaiDialogue : DialogueFile(){
    override fun handle(componentID: Int, buttonID: Int) {
        val questStage = questStage(player!!, TreeGnomeVillage.questName)
        when {
            inInventory(player!!,Items.ORBS_OF_PROTECTION_588) -> {
                when(stage) {
                    0 -> playerl("I've returned.").also { stage++ }
                    1 -> npcl("You're back, well done brave adventurer. Now the orbs are safe we can perform the ritual for the spirit tree. We can live in peace once again.").also { stage = END_DIALOGUE }
                }
            }
            inInventory(player!!, Items.ORB_OF_PROTECTION_587) -> {
                when(stage) {
                    0 -> playerl("Hello Remsai.").also { stage++ }
                    1 -> npcl("Hello, did you find the orb?").also { stage++ }
                    2 -> playerl("I have it here.").also { stage++ }
                    3 -> npcl("You're our saviour.").also { stage = END_DIALOGUE }
                }
            }
            questStage < 40 -> {
                when(stage) {
                    0 -> playerl("Hello Remsai.").also { stage++ }
                    1 -> npcl("Hello, did you find the orb?").also { stage++ }
                    2 -> playerl("No, I'm afraid not.").also { stage++ }
                    3 -> npcl("Please, we must have the orb if we are to survive.").also { stage = END_DIALOGUE }
                }
            }
            questStage == 40 -> {
                when(stage) {
                    0 -> playerl("Are you ok?").also { stage++ }
                    1 -> npcl("Khazard's men came. Without the orb we were defenceless. They killed many and then took our last hope, the other orbs. Now surely we're all doomed. Without them the spirit tree is useless.").also { stage = END_DIALOGUE }
                }
            }
            questStage > 40 -> {
                when(stage) {
                    0 -> playerl("I've returned.").also { stage++ }
                    1 -> npcl("You're back, well done brave adventurer. Now the orbs are safe we can perform the ritual for the spirit tree. We can live in peace once again.").also { stage = END_DIALOGUE }
                }
            }
        }
    }
}