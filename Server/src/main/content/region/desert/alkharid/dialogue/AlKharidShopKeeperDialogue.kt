package content.region.desert.alkharid.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

/**
 * @author bushtail
 * "Henceforth, you shall be called... Craig."
 */

@Initializable
class AlKharidShopKeeperDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return AlKharidShopKeeperDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FacialExpression.HALF_ASKING, "Can I help you at all?")
        stage = 100
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage) {
            100 -> options("Yes, please. What are you selling?", "How should I use your shop?", "No, thanks.").also { stage++ }
            101 -> when(buttonId) {
                1 -> {
                    stage = END_DIALOGUE
                    npc.openShop(player)
                }
                2 -> playerl(FacialExpression.ASKING, "How should I use your shop?").also { stage = 201 }
                3 -> playerl(FacialExpression.NEUTRAL, "No, thanks.").also { stage = END_DIALOGUE }
            }
            201 -> npcl(FacialExpression.FRIENDLY, "I'm glad you ask! You can buy as many of the items stocked as you wish. You can also sell most items to the shop.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SHOPKEEPER_524, NPCs.SHOP_ASSISTANT_525)
    }
}