package content.region.misc.etceteria.dialogue

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
class FishmongerEtcDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(core.game.dialogue.FacialExpression.FRIENDLY,"Welcome, ${player.getAttribute("fremennikname","fremmyname")}. My fish is fresher than any in Miscellania.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> end().also { npc.openShop(player) }
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return FishmongerEtcDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FISHMONGER_1369)
    }
}