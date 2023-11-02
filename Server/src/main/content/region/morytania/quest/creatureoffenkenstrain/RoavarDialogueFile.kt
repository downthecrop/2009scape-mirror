package content.region.morytania.quest.creatureoffenkenstrain

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.Items

class RoavarDialogueFile(private val dialogueNum: Int = 0) : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            START_DIALOGUE -> {
                if (dialogueNum == 1) {
                    npcl("If you've got the money, I've got a real treat for you.")
                    stage = 1
                } else if (dialogueNum == 2) {
                    npcl("You're interested in our speciality, I see. Would you like to buy some?")
                    stage = 2
                } else if (dialogueNum == 3) {
                    npcl("You can leave that alone, my friend. I've already sold you one of your own - eat that. I can't afford to give away freebies in this business!")
                    stage = END_DIALOGUE
                } else {
                    npcl("Hey! Don't touch that.")
                    stage = END_DIALOGUE
                }
            }
            1 -> playerl("What have you got?").also { stage = 3 }
            2 -> playerl("What exactly is in the jar?").also { stage = 3 }
            3 -> npcl("Pickled brain, my friend. Only 50 gold to you.").also { stage++ }
            4 -> playerl("Err... pickled brain from what animal?").also { stage++ }
            5 -> npcl("Animal? Don't be disgusting, man! No, this a human brain - only the best for my customers.").also { stage++ }
            6 -> showTopics(
                    Topic(FacialExpression.HALF_GUILTY, "I'll buy one.", 10, true),
                    Topic(FacialExpression.HALF_GUILTY, "I'm not hungry.", 20, true),
            )
            10 -> playerl("I'll buy one, please.").also { stage++ }
            11 -> {
                if (amountInInventory(player!!, Items.COINS_995) >= 50) {
                    if(removeItem(player!!, Item(Items.COINS_995, 50))) {
                        addItemOrDrop(player!!, Items.PICKLED_BRAIN_4199, 1)
                        npcl("A very wise choice. Don't eat it all at once, savour every morsel - that's my advice to you.")
                    }
                } else {
                    sendDialogue(player!!, "You do not have enough coins.")
                }
                stage = END_DIALOGUE
            }
           20 -> playerl("I'm afraid I'm not really hungry at the moment.").also { stage = END_DIALOGUE }
        }
    }
}