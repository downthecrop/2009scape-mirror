package rs09.game.content.dialogue.region.rellekka

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * @author qmqz
 * Needs the quest to be completed to really do anything else here
 * Trade option shouldn't work either unless it's completed
 */

@Initializable
class FurTraderDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
       if (!player.questRepository.isComplete("The Fremennik Trials")) {
           npc(FacialExpression.ANNOYED, "I don't sell to outerlanders.").also { stage = 99 }
       } else {
           npc(FacialExpression.FRIENDLY,"Welcome back <Fremennik Name>! Have you seen the furs I have today?").also { stage = 10 }
       }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npc(FacialExpression.ANNOYED, "I don't sell to outerlanders.").also { stage = 99 }
            10 -> end().also { npc.openShop(player) }

            99 -> end()
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
