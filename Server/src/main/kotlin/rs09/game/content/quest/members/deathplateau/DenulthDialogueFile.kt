package rs09.game.content.quest.members.deathplateau

import api.questStage
import api.setQuestStage
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE

/**
 * @author qmqz
 */

class DenulthDialogueFile() : DialogueFile() {

    override fun handle(interfaceId: Int, buttonId: Int) {
        npc = NPC(NPCs.DENULTH_1060)

        when(questStage(player!!, "Death Plateau")) {
            0 ->  when (stage) {
                0 -> player(FacialExpression.FRIENDLY, "Hello!").also { stage++ }
                1 -> npc(FacialExpression.FRIENDLY, "Hello citizen, how can I help you?").also { stage++ }
                2 -> options("Do you have any quests for me?", "What is this place?", "You can't, thanks.").also { stage++ }
                3 -> when (buttonId) {
                    1 -> player(FacialExpression.FRIENDLY, "Do you have any quests for me?").also { stage = 10 }
                    3 -> player(FacialExpression.FRIENDLY, "You can't, thanks.").also { stage = END_DIALOGUE }
                }

                10 -> npc(FacialExpression.FRIENDLY, "I don't know if you can help us!").also { stage++ }
                11 -> npcl(FacialExpression.FRIENDLY, "The trolls have taken up camp on the Death Plateau! " +
                        "They are using it to launch raids at night on the village." +
                        "We have tried to attack the camp, but the main path is heavily guarded!").also { stage++ }
                12 -> player(FacialExpression.ASKING, "Perhaps there is a way you can sneak up at night?").also { stage++ }
                13 -> npc(FacialExpression.FRIENDLY, "If there is another way, I do not know of it.").also { stage++ }
                14 -> npc(FacialExpression.FRIENDLY, "Do you know of such a path?").also { stage++ }
                15 -> options("No, but perhaps I could try and find one?", "No, sorry.").also { stage++ }
                16 -> when (buttonId) {
                    1 -> player(FacialExpression.FRIENDLY, "No, but perhaps I could try and find one?").also { stage = 20 }
                }

                20 -> npc(FacialExpression.FRIENDLY, "Citizen you would be well rewarded!").also { stage++ }
                21 -> npcl(FacialExpression.FRIENDLY, "If you go up to Death Plateau, be very careful as the trolls will attack you on sight!").also { stage++ }
                22 -> player(FacialExpression.FRIENDLY, "I'll be careful.").also { stage++ }
                23 -> npc(FacialExpression.FRIENDLY, "One other thing.").also { stage++ }
                24 -> player(FacialExpression.FRIENDLY, "What's that?").also { stage++ }
                25 -> npc(FacialExpression.FRIENDLY, "All of our equipment is kept in the castle on the hill.").also { stage++ }
                26 -> npcl(FacialExpression.FRIENDLY, "The stupid guard that was on duty last night lost the combination to the lock!" +
                        "I told the Prince that the Imperial Guard should've been in charge of security!").also { stage++ }
                27 -> player(FacialExpression.ASKING, "No problem, what does the combination look like?").also { stage++ }
                28 -> npcl(FacialExpression.FRIENDLY, "The equipment room is unlocked when the stone balls are placed in the correct order on the stone mechanism outside it." +
                        "The right order is written on a piece of paper the guard had.").also { stage++ }
                29 -> player(FacialExpression.FRIENDLY, "A stone what...?!").also { stage++ }
                30 -> npcl(FacialExpression.FRIENDLY, "Well citizen, the Prince is fond of puzzles. Why we couldn't just have a key is beyond me!").also { stage++ }
                31 -> player(FacialExpression.SUSPICIOUS, "I'll get on it right away!").also { stage++ }
                32 -> {
                    setQuestStage(player!!, "Clock Tower", 1)
                    END_DIALOGUE
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