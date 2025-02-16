package content.region.asgarnia.burthorpe.quest.trollstronghold

import content.data.Quests
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.Items

class DunstanDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (getQuestStage(player!!, Quests.TROLL_STRONGHOLD)) {
            in 1..10 -> {
                when (stage) {
                    START_DIALOGUE -> npcl(FacialExpression.FRIENDLY, "Have you managed to rescue Godric yet?").also { stage++ }
                    1 -> playerl(FacialExpression.FRIENDLY, "Not yet.").also { stage++ }
                    2 -> npcl(FacialExpression.FRIENDLY, "Please hurry! Who knows what they will do to him? Is there anything I can do in the meantime?").also { stage++ }
                    3 -> showTopics(
                            Topic(FacialExpression.THINKING, "Can you put some spikes on my Climbing boots?", 30),
                            Topic(FacialExpression.THINKING, "Is it OK if I use your anvil?", 10),
                            Topic(FacialExpression.NEUTRAL, "Nothing, thanks.", 20),
                    )
                    10 -> npcl(FacialExpression.FRIENDLY, "So you're a smithy are you?").also { stage++ }
                    11 -> playerl(FacialExpression.FRIENDLY, "I dabble.").also { stage++ }
                    12 -> npcl(FacialExpression.FRIENDLY, "A fellow smith is welcome to use my anvil!").also { stage++ }
                    13 -> playerl(FacialExpression.FRIENDLY, "Thanks!").also { stage++ }
                    14 -> npcl(FacialExpression.FRIENDLY, "Anything else before I get on with my work?").also { stage = 3 }
                    20 -> npcl(FacialExpression.NEUTRAL, "All right. Speak to you later then.").also { stage = END_DIALOGUE }
                    30 -> playerl("Can you put some spikes on my Climbing boots?").also { stage++ }
                    31 -> npcl("For you, no problem.").also { stage++ }
                    32 -> npc("Do you realise that you can only use the Climbing", "boots right now? The Spiked boots can only be used in", "the Icelands but no ones been able to get there for", "years!").also { stage++ }
                    33 -> showTopics(
                            Topic(FacialExpression.NEUTRAL, "Yes, but I still want them.", 40, true),
                            Topic(FacialExpression.NEUTRAL, "Oh OK, I'll leave them thanks.", 43),
                    )
                    40 -> {
                        if (inInventory(player!!, Items.CLIMBING_BOOTS_3105)  && inInventory(player!!, Items.IRON_BAR_2351)) {
                            sendDoubleItemDialogue(player!!, Items.IRON_BAR_2351, Items.CLIMBING_BOOTS_3105, "You give Dunstan an Iron bar and the climbing boots.")
                            sendMessage(player!!, "You give Dunstan an Iron bar and the climbing boots.")
                            if (removeItem(player!!, Item(Items.CLIMBING_BOOTS_3105)) && removeItem(player!!, Item(Items.IRON_BAR_2351))) {
                                addItemOrDrop(player!!, Items.SPIKED_BOOTS_3107)
                                stage++
                            } else {
                                stage = END_DIALOGUE
                            }
                        } else if (inInventory(player!!, Items.CLIMBING_BOOTS_3105)){
                            npcl("Sorry, I'll need an iron bar to make the spikes.")
                            stage = 3
                        } else {
                            playerl("I don't have them on me.")
                            stage = 3
                        }
                    }
                    41 -> sendItemDialogue(player!!, Items.SPIKED_BOOTS_3107, "Dunstan has given you the spiked boots.").also { stage++
                        sendMessage(player!!, "Dunstan has given you the spiked boots.")
                    }
                    43 -> npcl(FacialExpression.FRIENDLY, "Anything else before I get on with my work?").also {
                        stage = 3
                    }
                }
            }
            11 -> {
                when (stage) {
                    START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "Has Godric returned home?").also { stage++ }
                    1 -> npcl(FacialExpression.FRIENDLY, "He is safe and sound, thanks to you my friend!").also { stage++ }
                    2 -> playerl(FacialExpression.FRIENDLY, "I'm glad to hear it.").also { stage++ }
                    3 -> npcl(FacialExpression.FRIENDLY, "I have very little to offer you by way of thanks, but perhaps you will accept these family heirlooms. They were found by my great-great-grandfather, but we still don't have any idea what they do.").also { stage++ }
                    4 -> {
                        stage = END_DIALOGUE
                        finishQuest(player!!, Quests.TROLL_STRONGHOLD)
                    }
                }
            }
        }
    }
}