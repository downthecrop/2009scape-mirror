package rs09.game.content.quest.members.tree

import api.*
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE

class TrackerGnomeThreeDialogue : DialogueFile(){

    private val xcoordMap = mapOf(
            1 to "Less than my hands.",
            2 to "More than my head, less than my fingers.",
            3 to "More than we but less than our feet.",
            4 to  "My legs and your legs.")

    override fun handle(componentID: Int, buttonID: Int) {
        val questStage = questStage(player!!, TreeGnomeVillage.questName)
        when {
            questStage == 30 -> {
                when(stage) {
                    0 -> playerl("Are you OK?").also { stage++ }
                    1 -> npcl("OK? Who's OK? Not me! Hee hee!").also { stage++ }
                    2 -> playerl("What's wrong?").also { stage++ }
                    3 -> npcl("You can't see me, no one can. Monsters, demons, they're all around me!").also { stage++ }
                    4 -> playerl("What do you mean?").also { stage++ }
                    5 -> npcl("They're dancing, all of them, hee hee.").also { stage++ }
                    6 -> sendDialogue(player!!,"He's clearly lost the plot.").also { stage++ }
                    7 -> playerl("Do you have the coordinate for the Khazard stronghold?").also { stage++ }
                    8 -> npcl("Who holds the stronghold?").also { stage++ }
                    9 -> playerl("What?").also { stage++ }
                    10 -> {
                        // Generate the x coordinate answer
                        if(getAttribute(player!!,"treegnome:xcoord",0) == 0){
                            val answer = (1..4).random()
                            npcl(xcoordMap[answer])
                            setAttribute(player!!,"/save:treegnome:xcoord",answer)
                        } else {
                            npcl(xcoordMap[getAttribute(player!!,"treegnome:xcoord",1)])
                        }
                        stage++
                    }
                    11 -> playerl("You're mad").also { stage++ }
                    12 -> npcl("Dance with me, and Khazard's men are beat.").also { stage++ }
                    13 -> sendDialogue(player!!,"The toll of war has affected his mind.").also { stage++ }
                    14 -> playerl("I'll pray for you little man.").also { stage++ }
                    15 -> npcl("All day we pray in the hay, hee hee.").also {
                        setAttribute(player!!, "/save:treegnome:tracker3", true)
                        stage = END_DIALOGUE
                    }
                }
            }
            questStage == 31 -> {
                when(stage) {
                    0 -> playerl("Hello again.").also { stage++ }
                    1 -> npcl("Don't talk to me, you can't see me. No one can, just the demons.").also { stage = END_DIALOGUE }
                }
            }
            questStage > 31 -> {
                when(stage) {
                    0 -> playerl("Hello").also { stage++ }
                    1 -> npcl("I feel dizzy, where am I? Oh dear, oh dear I need some rest.").also { stage++ }
                    2 -> playerl("I think you do.").also { stage = END_DIALOGUE }
                }
            }
        }
    }

}