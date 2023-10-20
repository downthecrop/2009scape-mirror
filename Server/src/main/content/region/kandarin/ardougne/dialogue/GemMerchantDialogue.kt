package content.region.kandarin.ardougne.dialogue

import core.api.openNpcShop
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class GemMerchantDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun newInstance(player: Player?): DialoguePlugin {
        return GemMerchantDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(
            FacialExpression.HAPPY, "Here, look at my lovely gems."
        ).also { stage = START_DIALOGUE }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                openNpcShop(player, NPCs.GEM_MERCHANT_570)
                end()
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GEM_MERCHANT_570)
    }
}