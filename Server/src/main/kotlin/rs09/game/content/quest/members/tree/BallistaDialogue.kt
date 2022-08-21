package rs09.game.content.quest.members.tree

import api.*
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE

class BallistaDialogue : DialogueFile(){
    override fun handle(componentID: Int, buttonID: Int) {
        val questStage = questStage(player!!, TreeGnomeVillage.questName)
        if (questStage > 30) {
            when (stage) {
                0 -> sendDialogue(player!!, "The Khazard stronghold has already been breached.").also { stage = END_DIALOGUE }
            }
        } else if (questStage != 30) return

        val tracker1 = getAttribute(player!!, "/save:treegnome:tracker1", false)
        val tracker2 = getAttribute(player!!, "/save:treegnome:tracker2", false)
        val tracker3 = getAttribute(player!!, "/save:treegnome:tracker3", false)

        if(tracker1 && tracker2 && tracker3) {
            when(stage){
                0 -> playerl("That tracker gnome was a bit vague about the x coordinate! What could it be?").also { stage++ }
                1 -> interpreter!!.sendOptions("Enter the x-coordinate of the stronghold",
                        "0001","0002","0003","0004").also { stage++ }
                2 -> {
                    sendDialogue(player!!, "You entered the height and y coordinates you got from the tracker gnomes.")
                    val answer = getAttribute(player!!,"treegnome:xcoord",1)
                    when (buttonID) {
                        answer -> {
                            sendDialogue(player!!, "The huge spear flies through the air and screams down directly into the Khazard stronghold. A deafening crash echoes over the battlefield as the front entrance is reduced to rubble.")
                            setQuestStage(player!!, TreeGnomeVillage.questName, 31)
                        }
                        else -> sendDialogue(player!!, "The huge spear completely misses the Khazard stronghold!")
                    }
                    stage = END_DIALOGUE
                }
            }
        } else {
            when(stage){
                0 -> sendDialogue(player!!,"You enter some random coordinates.").also { stage++ }
                1 -> sendDialogue(player!!,"The huge spear completely misses the Khazard stronghold!").also { stage++ }
                2 -> playerl("I've got no hope of hitting the stronghold without knowing any of the coordinates. Maybe I should ask Montai's tracker gnomes for more coordinates.").also { stage = END_DIALOGUE }
            }
        }
    }
}