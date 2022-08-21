package rs09.game.content.quest.members.tree

import api.questStage
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE

class KhazardWarlordDialogue : DialogueFile(){
    override fun handle(componentID: Int, buttonID: Int) {
        val questStage = questStage(player!!,TreeGnomeVillage.questName)
        if(questStage == 31){
            when(stage) {
                0 -> playerl("Hello there.").also { stage++ }
                1 -> npcl("You think you're so clever. You know nothing!").also { stage++ }
                2 -> playerl("What?").also { stage++ }
                3 -> npcl("I'll crush you and those pesky little green men!").also { stage = END_DIALOGUE }
            }
        } else if (questStage == 40) {
            when(stage) {
                0 -> playerl("You there, stop!").also { stage++ }
                1 -> npcl("Go back to your pesky little green friends.").also { stage++ }
                2 -> playerl("I've come for the orbs.").also { stage++ }
                3 -> npcl("You're out of your depth traveller. These orbs are part of a much larger picture.").also { stage++ }
                4 -> playerl("They're stolen goods, now give them here!").also { stage++ }
                5 -> npcl("Ha, you really think you stand a chance? I'll crush you.").also {
                    npc!!.attack(player!!)
                    stage = END_DIALOGUE
                }
            }
        }
    }
}