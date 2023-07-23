package content.region.kandarin.quest.grandtree

import core.api.addItemOrDrop
import core.api.getQuestStage
import core.api.sendDialogue
import core.game.dialogue.DialogueFile
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs09.consts.Items

class AnitaDialogue : DialogueFile(){
    override fun handle(componentID: Int, buttonID: Int) {
        when(getQuestStage(player!!, TheGrandTree.questName)){
            60 -> {
                if(player!!.hasItem(Item(Items.GLOUGHS_KEY_788)) && stage < 12){
                    when(stage){
                        0 -> npcl("Have you taken that key to Glough yet?").also { stage++ }
                        1 -> playerl("No, I'm still carrying it around.").also { stage++ }
                        2 -> npcl("Oh. Please take it to Glough!").also { stage = END_DIALOGUE }
                    }
                } else {
                    when(stage){
                        0 -> playerl("Hello there.").also { stage++ }
                        1 -> npcl("Oh hello, I've seen you with the King.").also { stage++ }
                        2 -> playerl("Yes, I'm helping him with a problem.").also { stage++ }
                        3 -> npcl("You must know my boyfriend Glough then?").also { stage++ }
                        4 -> playerl("Indeed!").also { stage++ }
                        5 -> npcl("Could you do me a favour?").also { stage++ }
                        6 -> options("I suppose so.","No, I'm busy.").also { stage++ }
                        7 -> when(buttonID){
                            1 -> playerl("I suppose so.").also { stage = 10 }
                            2 -> playerl("No, I'm busy.").also { stage = END_DIALOGUE }
                        }
                        10 -> playerl("I suppose so.").also { stage++ }
                        11 -> npcl("Please give this key to Glough, he left it here last night.").also { stage++ }
                        12 -> sendDialogue(player!!, "Anita gives you a key.").also {
                            addItemOrDrop(player!!, Items.GLOUGHS_KEY_788)
                            stage++
                        }
                        13 -> npcl("Thanks a lot.").also { stage++ }
                        14 -> playerl("No... thank you!").also { stage = END_DIALOGUE }
                    }
                }
            }
        }
    }

}