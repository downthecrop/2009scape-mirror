package content.region.kandarin.witchhaven.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * @author qmqz
 * Does not take quest into consideration
 */

@Initializable
class MayorHobbDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(core.game.dialogue.FacialExpression.FRIENDLY,"Well hello there; welcome to our little village. Pray, stay awhile.").also { stage = 99 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return MayorHobbDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MAYOR_HOBB_4874)
    }
}
