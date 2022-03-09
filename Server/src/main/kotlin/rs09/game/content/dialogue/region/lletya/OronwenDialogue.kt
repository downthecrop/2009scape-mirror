package rs09.game.content.dialogue.region.lletya

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
class OronwenDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FacialExpression.FRIENDLY,"Hello, can I help?").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> options("Yes please. What are you selling?", "No thanks.").also { stage++ }

            1 -> when(buttonId) {
                1 -> player(FacialExpression.FRIENDLY, "Yes please. What are you selling?").also { stage = 10 }
                2 -> player(FacialExpression.FRIENDLY, "No thanks.").also { stage = 99 }
            }

            10 -> end().also { npc.openShop(player) }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return OronwenDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ORONWEN_2353)
    }
}
