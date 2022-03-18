package rs09.game.content.dialogue.region.miscellania

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
class FishmongerMiscDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!isQuestComplete(player, "Throne of Miscellania")) {
            npcl(FacialExpression.FRIENDLY,"Greetings, Sir. Get your fresh fish here! I've heard that the Etceterian fish is stored in a cow shed.").also { stage = 0 }
        } else {
            npcl(FacialExpression.FRIENDLY,"Greetings, Your Highness. Have some fresh fish! I've heard that the Etceterian fish is stored in a cow shed.").also { stage = 0 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> end().also { npc.openShop(player) }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return FishmongerMiscDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FISHMONGER_1393)
    }
}