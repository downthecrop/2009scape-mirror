package content.region.kandarin.dialogue

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
class ShopKeeperDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun newInstance(player: Player?): DialoguePlugin {
        return ShopKeeperDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(
            FacialExpression.HAPPY, "Can I help you at all?"
        ).also { stage = START_DIALOGUE }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options(
                "Yes, please. I'd like to see your stock.", "No thanks, I must be going now."
            ).also { stage++ }

            1 -> when (buttonId) {
                1 -> playerl(
                    FacialExpression.FRIENDLY, "Yes, please. I'd like to see your stock."
                ).also { stage++ }

                2 -> playerl(
                    FacialExpression.FRIENDLY, "No thanks, I must be going now."
                ).also { stage = END_DIALOGUE }
            }

            2 -> {
                openNpcShop(player, NPCs.SHOPKEEPER_555)
                end()
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SHOPKEEPER_555)
    }
}