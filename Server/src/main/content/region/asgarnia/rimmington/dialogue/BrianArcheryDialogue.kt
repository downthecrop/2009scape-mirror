package content.region.asgarnia.rimmington.dialogue

import core.api.openNpcShop
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.entity.npc.NPC
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

/**
 * Handles the BrianArchery's dialogue.
 */
@Initializable
class BrianArcheryDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FacialExpression.HAPPY, "Would you like to buy some archery equipment?").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("Let's see what you've got, then.", "No thanks, I've got all the archery equipment I need.").also { stage++ }
            1 -> when (buttonId) {
                1 -> end().also { openNpcShop(player, npc.id) }
                2 -> playerl(FacialExpression.HALF_GUILTY, "No thanks, I've got all the archery equipment I need.").also { stage = 10 }
            }

            10 -> {
                npcl(FacialExpression.FRIENDLY, "Okay. Fare well on your travels.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return BrianArcheryDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BRIAN_1860)
    }
}
