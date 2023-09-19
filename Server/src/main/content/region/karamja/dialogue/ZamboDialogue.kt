package content.region.karamja.dialogue

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
class ZamboDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun newInstance(player: Player?): DialoguePlugin {
        return ZamboDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(
            FacialExpression.ASKING,
            " Hey, are you wanting to try some of my fine wines and spirits? All brewed locally on Karamja."
        ).also { stage = START_DIALOGUE }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("Yes, please.", "No, thank you.").also { stage++ }

            1 -> when (buttonId) {
                1 -> playerl(
                    FacialExpression.FRIENDLY, "Yes, please."
                ).also { stage++ }

                2 -> playerl(
                    FacialExpression.FRIENDLY, "No, thank you."
                ).also { stage = END_DIALOGUE }
            }

            2 -> {
                openNpcShop(player, NPCs.ZAMBO_568)
                end()
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ZAMBO_568)
    }
}