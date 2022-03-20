package rs09.game.content.quest.members.deathplateau

import api.questStage
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE

/**
 * @author qmqz
 */

class EohricDialogueFile() : DialogueFile() {

    override fun handle(interfaceId: Int, buttonId: Int) {
        npc = NPC(NPCs.EOHRIC_1080)

        when(questStage(player!!, "Death Plateau")) {
            0 -> when (stage) {
                0 -> npc(FacialExpression.ASKING, "Hello. Can I help?").also { stage++ }
                1 -> options("What is this place?", "That's quite an outfit.", "Goodbye.").also { stage++ }
                2 -> when (buttonId) {
                    1 -> npcl(FacialExpression.FRIENDLY, "This is Burthorpe Castle, home to His Royal Highness Prince Anlaf, heir to the throne of Asgarnia.").also { stage = 10 }
                    2 -> npcl(FacialExpression.HAPPY, "Why, thank you. I designed it myself. I've always found purple such a cheerful colour!").also { stage = 1 }
                    3 -> player(FacialExpression.FRIENDLY, "Goodbye.").also { stage = END_DIALOGUE }
                }
                10 -> npc(FacialExpression.FRIENDLY, "No doubt you're impressed.").also { stage++ }
                11 -> options("Where is the prince?", "Goodbye.").also { stage++ }
                12 -> when (buttonId) {
                    1 -> npcl(FacialExpression.SUSPICIOUS, "I cannot disclose the prince's exact whereabouts for fear of compromising his personal safety.").also { stage = 20 }
                    2 -> player(FacialExpression.FRIENDLY, "Goodbye.").also { stage = END_DIALOGUE }
                }
                20 -> npcl(FacialExpression.FRIENDLY, "But rest assured that he is working tirelessly to maintain the safety and wellbeing of Burthorpe's people.").also { stage = 1 }
            }

            1 -> when (stage) {
                0 -> player(FacialExpression.FRIENDLY, "Hi!").also { stage++ }
                1 -> npc(FacialExpression.FRIENDLY, "Hi, can I help?").also { stage++ }
                2 -> options("I'm looking for the guard that was on the last night.",
                    "Do you know of another way up Death Plateau?",
                    "No, I'm just looking around.").also { stage++ }
                3 -> when (buttonId) {
                    1 -> player(FacialExpression.THINKING, "I'm looking for the guard that was on the last night.").also { stage = 10 }
                    3 -> player(FacialExpression.FRIENDLY, "No, I'm just looking around.").also { stage = END_DIALOGUE }

                    10 -> npcl(FacialExpression.FRIENDLY, "There was only one guard on last night. Harold. He's a nice lad, if a little dim.").also { stage++ }
                    11 -> player(FacialExpression.FRIENDLY, "Do you know where he is staying?").also { stage++ }
                    12 -> npc(FacialExpression.FRIENDLY, "Harold is staying at the Toad and Chicken.").also { stage++ }
                    13 -> player(FacialExpression.FRIENDLY, "Thanks!").also { stage = END_DIALOGUE }
                }

            }
            /*
             npc(FacialExpression.FRIENDLY, "").also { stage++ }
             player(FacialExpression.FRIENDLY, "").also { stage++ }

             npcl(FacialExpression.FRIENDLY, "").also { stage++ }
             playerl(FacialExpression.FRIENDLY, "").also { stage++ }
             */
        }
    }
}