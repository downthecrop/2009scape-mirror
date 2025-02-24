package content.region.fremennik.rellekka.dialogue

import core.api.isQuestComplete
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import core.tools.END_DIALOGUE
import content.data.Quests

/**
 * @author qmqz
 */

@Initializable
class FurTraderDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
       if (!isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
           npc(FacialExpression.ANNOYED, "I don't sell to outerlanders.").also { stage = END_DIALOGUE }
       } else {
           npcl(FacialExpression.FRIENDLY,"Welcome back, ${player.getAttribute("fremennikname","fremmyname")}. Have you seen the furs I have today?").also { stage = 10 }
       }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            10 -> end().also { npc.openShop(player) }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return FurTraderDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FUR_TRADER_1316)
    }
}
