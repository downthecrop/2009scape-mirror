package content.region.fremennik.jatizso.dialogue

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
class HringHringDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun newInstance(player: Player?): DialoguePlugin {
        return HringHringDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(
            FacialExpression.FRIENDLY,
            " Oh, hello again. Want some ore?"
        ).also { stage = START_DIALOGUE }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("I'll have a look.", "Not right now.").also { stage++ }
            1 -> when (buttonId) {
                1 -> playerl(
                    FacialExpression.FRIENDLY,
                    "I'll have a look."
                ).also { stage++ }

                2 -> playerl(
                    FacialExpression.FRIENDLY,
                    "Not right now."
                ).also { stage = END_DIALOGUE }
            }

            2 -> {
                openNpcShop(player, NPCs.HRING_HRING_5483)
                end()
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HRING_HRING_5483)
    }
}