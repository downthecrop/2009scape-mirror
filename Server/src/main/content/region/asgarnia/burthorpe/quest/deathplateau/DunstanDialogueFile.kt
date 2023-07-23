package content.region.asgarnia.burthorpe.quest.deathplateau

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class DunstanDialogueFile : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (getQuestStage(player!!, DeathPlateau.questName)) {
            21 -> {
                when (stage) {
                    START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "Hi!").also { stage++ }
                    1 -> npcl(FacialExpression.FRIENDLY, "Hi! How can I help?").also { stage++ }
                    2 -> playerl(FacialExpression.FRIENDLY, "Tenzing has asked me to bring you his climbing boots, he needs to have spikes put on them.").also { stage++ }
                    3 -> npcl(FacialExpression.FRIENDLY, "He does, does he? Well I won't do it till he pays for the last set I made for him!").also { stage++ }
                    4 -> playerl(FacialExpression.FRIENDLY, "This is really important!").also { stage++ }
                    5 -> npcl(FacialExpression.FRIENDLY, "How so?").also { stage++ }
                    6 -> playerl(FacialExpression.FRIENDLY, "Well, I need the Sherpa to show me a secret way up Death Plateau so that the Imperial Guard can destroy the troll camp! He won't help me till I've got the spikes!").also { stage++ }
                    7 -> npcl(FacialExpression.FRIENDLY, "Hmm. That's different!").also { stage++ }
                    8 -> npcl(FacialExpression.FRIENDLY, "Tell you what, I'll make them for you on one condition.").also { stage++ }
                    9 -> playerl(FacialExpression.FRIENDLY, "*sigh* What's the condition?").also { stage++ }
                    10 -> npcl(FacialExpression.FRIENDLY, "My son has just turned 16 and I'd very much like him to join the Imperial Guard. The Prince's elite forces are invite only so it's very unlikely he'll get in. If you can arrange that you have a deal!").also { stage++ }
                    11 -> playerl(FacialExpression.FRIENDLY, "That won't be a problem as I'm helping out the Imperial Guard!").also { stage++ }
                    12 -> npcl(FacialExpression.FRIENDLY, "Excellent! You'll need to bring an Iron bar for the spikes!").also {
                        setQuestStage(player!!, "Death Plateau", 22)
                        stage = END_DIALOGUE
                    }
                }
            }
            22 -> {
                when (stage) {
                    START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "Hi!").also { stage++ }
                    1 -> npcl(FacialExpression.FRIENDLY, "Have you managed to get my son signed up for the Imperial Guard?").also { stage++ }
                    2 -> playerl(FacialExpression.FRIENDLY, "Not yet! I just need to speak to Denulth!").also { stage = END_DIALOGUE }
                }
            }
            23 -> {
                when (stage) {
                    START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "Hi!").also { stage++ }
                    1 -> npcl(FacialExpression.FRIENDLY, "Have you managed to get my son signed up for the Imperial Guard?").also {
                        stage++
                    }

                    2 -> {
                        if (!inInventory(player!!, Items.CERTIFICATE_3114)) {
                            playerl(FacialExpression.FRIENDLY, "I have but I don't have the entrance certificate on me.").also { stage++ }
                        } else {
                            sendDialogue(player!!, "You give Dunstan the certificate.").also { stage = 5 }
                        }
                    }

                    3 -> npcl(FacialExpression.FRIENDLY, "Good but I need to have the certificate.").also { stage = END_DIALOGUE }

                    5 -> npcl(FacialExpression.FRIENDLY, "Thank you!").also {
                        // Jumps to the next stage immediately in one continuous dialogue (questStage 24, stage 2).
                        setQuestStage(player!!, "Death Plateau", 24)
                        stage = 2
                    }
                }
            }
            24 -> {
                when (stage) {
                    START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "Hi!").also { stage++ }
                    1 -> npcl(FacialExpression.FRIENDLY, "Hi!").also { stage++ }
                    2 -> npcl(FacialExpression.FRIENDLY, "Now to keep my end of the bargain. Give me the boots and an iron bar and I'll put on the spikes.").also {
                        if (inInventory(player!!, Items.CLIMBING_BOOTS_3105)  && inInventory(player!!, Items.IRON_BAR_2351)) {
                            stage = 7
                        } else if (inInventory(player!!, Items.CLIMBING_BOOTS_3105)){
                            stage = 3
                        } else if (inInventory(player!!, Items.IRON_BAR_2351)){
                            stage = 4
                        } else {
                            stage = 5
                        }
                    }
                    3 -> playerl(FacialExpression.FRIENDLY, "I don't have the iron bar.").also { stage = END_DIALOGUE }
                    4 -> playerl(FacialExpression.FRIENDLY, "I don't have the climbing boots.").also { stage = END_DIALOGUE }
                    5 -> playerl(FacialExpression.FRIENDLY, "I don't have the iron bar or the climbing boots.").also { stage = END_DIALOGUE }

                    7 -> sendDoubleItemDialogue(player!!, Items.IRON_BAR_2351, Items.CLIMBING_BOOTS_3105, "You give Dunstan an iron bar and the climbing boots.").also {
                        if (removeItem(player!!, Item(Items.CLIMBING_BOOTS_3105)) && removeItem(player!!, Item(Items.IRON_BAR_2351))) {
                            stage++
                        } else {
                            stage = END_DIALOGUE
                        }
                    }
                    8 -> sendItemDialogue(player!!, Items.SPIKED_BOOTS_3107, "Dunstan has given you the spiked boots.").also { stage++
                        addItemOrDrop(player!!, Items.SPIKED_BOOTS_3107)
                    }
                    9 -> playerl(FacialExpression.FRIENDLY, "Thank you!").also { stage++ }
                    10 -> npcl(FacialExpression.FRIENDLY, "No problem.").also {
                        stage = END_DIALOGUE
                    }
                }
            }
        }
    }
}