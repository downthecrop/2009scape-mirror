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
 * Handles the RimmingtonShopKeeper dialogue.
 */
@Initializable
class RimmingtonShopKeeperDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FacialExpression.HAPPY, "Can I help you at all?").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("Yes, please. What are you selling?", "How should I use your shop?", "No, thanks.").also { stage++ }
            1 -> when (buttonId) {
                1 -> end().also { openNpcShop(player, npc.id) }
                2 -> npcl(FacialExpression.HAPPY, "I'm glad you ask! You can buy as many of the items stocked as you wish. You can also sell most items to the shop.").also { stage = END_DIALOGUE }
                3 -> end()
            }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return RimmingtonShopKeeperDialogue(player)
    }

    override fun getIds(): IntArray = intArrayOf(
            NPCs.SHOPKEEPER_530,
            NPCs.SHOP_ASSISTANT_531
    )
}