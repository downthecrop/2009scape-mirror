package rs09.game.content.dialogue.region.zanaris

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * @author qmqz
 */

@Initializable
class FairyShopAssistantDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FacialExpression.OLD_NORMAL,"Can I help you at all?").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> options("Yes please. What are you selling?", "No thanks.").also { stage++ }

            1 -> when (buttonId) {
                1 -> end().also { npc.openShop(player) }
                2 -> player(FacialExpression.NEUTRAL, "No thanks.").also { stage = 99 }
            }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return FairyShopAssistantDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FAIRY_SHOP_ASSISTANT_535)
    }
}
