package content.region.misthalin.varrock.dialogue

import core.api.lock
import core.api.queueScript
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

class KnockAtBankDoor : DialogueFile() {
    private val femaleBankerNPC = NPC(NPCs.BANKER_45)
    private val maleBankerNPC = NPC(NPCs.BANKER_44)
    private val femaleBankerDoorLoc = Location(3182, 3434, 0)

    override fun handle(componentID: Int, buttonID: Int) {
        npc = if (player!!.location == femaleBankerDoorLoc) femaleBankerNPC else maleBankerNPC

        when (stage) {
            START_DIALOGUE -> {
                player!!.dialogueInterpreter.sendPlainMessage(
                    true, "<col=08088A>Knock knock..."
                ).also {
                    lock(player!!, 3)
                    queueScript(player!!, 3) {
                        npcl(FacialExpression.NEUTRAL, "Who's there?")
                        stage++
                        return@queueScript true
                    }
                }
            }

            1 -> showTopics(
                Topic("I'm ${player!!.username}. Please let me in.", 10),
                Topic("Boo.", 20),
                Topic("Kanga.", 30),
                Topic("Thank.", 40),
                Topic("Doctor.", 50)
            )
            10 -> npcl("No. Staff only beyond this point. You can't come in here.").also { stage = END_DIALOGUE }
            20 -> npcl("Boo who?").also { stage++ }
            21 -> playerl("There's no need to cry!").also { stage++ }
            22 -> npcl(FacialExpression.FURIOUS, "What? I'm not... oh, just go away!").also { stage = END_DIALOGUE }
            30 -> npcl("Kanga who?").also { stage++ }
            31 -> playerl("No, 'kangaroo'.").also { stage++ }
            32 -> npcl(FacialExpression.FURIOUS, "Stop messing about and go away!").also { stage = END_DIALOGUE }
            40 -> npcl("Thank who?").also { stage++ }
            41 -> playerl("You're welcome!").also { stage++ }
            42 -> npcl(FacialExpression.FURIOUS, "Stop it!").also { stage = END_DIALOGUE }
            50 -> npcl(
                FacialExpression.FURIOUS,
                "Doctor. wh.. hang on, I'm not falling for that one again! Go away."
            ).also { stage = END_DIALOGUE }
        }
    }
}