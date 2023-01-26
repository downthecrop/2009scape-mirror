package content.region.tirranwn.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * @author qmqz
 */

@Initializable
class OronwenDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(core.game.dialogue.FacialExpression.FRIENDLY,"Hello, can I help?").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> options("Yes please. What are you selling?", "No thanks.").also { stage++ }

            1 -> when(buttonId) {
                1 -> player(core.game.dialogue.FacialExpression.FRIENDLY, "Yes please. What are you selling?").also { stage = 10 }
                2 -> player(core.game.dialogue.FacialExpression.FRIENDLY, "No thanks.").also { stage = 99 }
            }

            10 -> end().also { npc.openShop(player) }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return OronwenDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ORONWEN_2353)
    }
}
