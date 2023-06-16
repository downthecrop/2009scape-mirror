package content.region.asgarnia.burthorpe.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

/**
 * Represents the Wistan dialogue plugin.
 * @author 'Vexia
 * @author ovenbread
 */
@Initializable
class WistanDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "Hi!").also { stage++ }
            1 -> npcl(FacialExpression.FRIENDLY, "Welcome to Burthorpe Supplies. Your last shop before heading north into the mountains!").also { stage++ }
            2 -> npcl(FacialExpression.FRIENDLY, "Would you like to buy something?").also { stage++ }
            3 -> options("Yes, please.", "No, thanks.").also { stage++ }
            4 -> when (buttonId) {
                1 -> playerl(FacialExpression.FRIENDLY, "Yes, please.").also { stage = 5 }
                2 -> playerl(FacialExpression.FRIENDLY, "No, thanks.").also { stage = END_DIALOGUE }
            }
            5 -> npc.openShop(player).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): DialoguePlugin {
        return WistanDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.WISTAN_1083)
    }
}
