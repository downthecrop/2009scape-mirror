package content.region.kandarin.ardougne.dialogue

import core.api.openNpcShop
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class FurTraderDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun newInstance(player: Player?): DialoguePlugin {
        return FurTraderDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(
            FacialExpression.ASKING, "Would you like to trade in fur?"
        ).also { stage = START_DIALOGUE }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("Yes.", "No.").also { stage++ }
            1 -> when (buttonId) {
                1 -> {
                   openNpcShop(player, NPCs.FUR_TRADER_573)
                    end()
                }

                2 -> playerl(
                    FacialExpression.HALF_GUILTY, "No, thanks."
                ).also { stage = END_DIALOGUE }

            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FUR_TRADER_573)
    }

}