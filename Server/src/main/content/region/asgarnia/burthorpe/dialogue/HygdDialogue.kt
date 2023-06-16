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
 * Hygd Dialogue
 * @author 'Vexia
 * @author ovenbread
 */
@Initializable
class HygdDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int) : Boolean {
        if(isQuestComplete(player!!, "Death Plateau")) {
            when (stage) {
                START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "Hi!").also { stage = (1..3).toIntArray().random() }
                1 -> npcl(FacialExpression.HAPPY, "I heard about what you did, thank you!").also { stage = END_DIALOGUE }
                2 -> npcl(FacialExpression.HAPPY, "Thank you so much!").also { stage = END_DIALOGUE }
                3 -> npcl(FacialExpression.HAPPY, "Surely we are safe now!").also { stage = END_DIALOGUE }
            }
        } else {
            when (stage) {
                START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "Hi!").also { stage = (1..3).toIntArray().random() }
                1 -> npcl(FacialExpression.FRIENDLY, "Hello stranger.").also { stage = END_DIALOGUE }
                2 -> npcl(FacialExpression.FRIENDLY, "Hi!").also { stage = END_DIALOGUE }
                3 -> npcl(FacialExpression.FRIENDLY, "Welcome to Burthorpe!").also { stage++ }
                4 -> playerl(FacialExpression.FRIENDLY, "Thanks!").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return HygdDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HYGD_1088)
    }
}

