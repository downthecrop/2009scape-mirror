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
class MusaPointShopDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun newInstance(player: Player?): DialoguePlugin {
        return MusaPointShopDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(
            FacialExpression.FRIENDLY, "Can I help you at all?"
        ).also { stage = START_DIALOGUE }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("Yes please. What are you selling?", "No thanks.").also { stage++ }

            1 -> when (buttonId) {
                1 -> {
                    openNpcShop(player, NPCs.SHOPKEEPER_532)
                    end()
                }

                2 -> playerl(
                    FacialExpression.FRIENDLY, "No thanks."
                ).also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SHOPKEEPER_532, NPCs.SHOP_ASSISTANT_533)
    }
}