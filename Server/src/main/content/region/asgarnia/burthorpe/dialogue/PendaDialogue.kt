package content.region.asgarnia.burthorpe.dialogue

import core.api.isQuestComplete
import core.api.toIntArray
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

/**
 * Penda Dialogue
 * @author 'Vexia
 * @author ovenbread
 */
@Initializable
class PendaDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int) : Boolean {
        if(isQuestComplete(player!!, "Death Plateau")) {
            when (stage) {
                START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "Hi!").also { stage = (1..5).toIntArray().random() }
                1 -> npcl(FacialExpression.HAPPY, "I heard about what you did, thank you!").also { stage = END_DIALOGUE }
                2 -> npcl(FacialExpression.WORRIED, "The White Knights are still going to take over.").also { stage = END_DIALOGUE }
                3 -> npcl(FacialExpression.PANICKED, "I hear the Imperial Guard are preparing an attack!").also { stage = END_DIALOGUE }
                4 -> npcl(FacialExpression.HAPPY, "Thank you so much!").also { stage = END_DIALOGUE }
                5 -> npcl(FacialExpression.HAPPY, "Surely we are safe now!").also { stage = END_DIALOGUE }
            }
        } else {
            when (stage) {
                START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "Hi!").also { stage = (1..12).toIntArray().random() }
                1 -> npcl(FacialExpression.ANGRY, "Let me at 'em!").also { stage = END_DIALOGUE }
                2 -> npcl(FacialExpression.ANGRY, "Trolls? Schmolls!").also { stage = END_DIALOGUE }
                3 -> npcl(FacialExpression.PANICKED, "The trolls are coming!").also { stage = END_DIALOGUE }
                4 -> npcl(FacialExpression.PANICKED, "The trolls took my baby son!").also { stage = END_DIALOGUE }
                5 -> npcl(FacialExpression.PANICKED, "Trolls!").also { stage = END_DIALOGUE }
                6 -> npcl(FacialExpression.FRIENDLY, "Hello stranger.").also { stage = END_DIALOGUE }
                7 -> npcl(FacialExpression.ANGRY, "Go away!").also { stage = END_DIALOGUE }
                8 -> npcl(FacialExpression.PANICKED, "Run!").also { stage = END_DIALOGUE }
                9 -> npcl(FacialExpression.FRIENDLY, "Hi!").also { stage = END_DIALOGUE }
                10 -> npcl(FacialExpression.SAD, "The Imperial Guard can no longer protect us!").also { stage = END_DIALOGUE }
                11 -> npcl(FacialExpression.WORRIED, "The White Knights will soon have control!").also { stage = END_DIALOGUE }
                12 -> npcl(FacialExpression.FRIENDLY, "Welcome to Burthorpe!").also { stage++ }
                13 -> playerl(FacialExpression.FRIENDLY, "Thanks!").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return PendaDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PENDA_1087)
    }
}
