package content.region.desert.nardah.dialogue

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
class Nkuku(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(core.game.dialogue.FacialExpression.FRIENDLY,"Good day to you.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> {
                npc(core.game.dialogue.FacialExpression.FRIENDLY, "May Saradomin be with you.").also { stage = 99 }
            }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return Nkuku(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.NKUKU_3032)
    }
}