package rs09.game.content.dialogue.region.etceteria

import api.isQuestComplete
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
class FishmongerEtcDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FacialExpression.FRIENDLY,"Welcome, ${player.getAttribute("fremennikname","fremmyname")}. My fish is fresher than any in Miscellania.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> end().also { npc.openShop(player) }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return FishmongerEtcDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FISHMONGER_1369)
    }
}