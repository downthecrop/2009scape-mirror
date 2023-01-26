package content.region.misc.miscellania.dialogue

import core.api.isQuestComplete
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
class FishmongerMiscDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!isQuestComplete(player, "Throne of Miscellania")) {
            npcl(core.game.dialogue.FacialExpression.FRIENDLY,"Greetings, Sir. Get your fresh fish here! I've heard that the Etceterian fish is stored in a cow shed.").also { stage = 0 }
        } else {
            npcl(core.game.dialogue.FacialExpression.FRIENDLY,"Greetings, Your Highness. Have some fresh fish! I've heard that the Etceterian fish is stored in a cow shed.").also { stage = 0 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> end().also { npc.openShop(player) }
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return FishmongerMiscDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FISHMONGER_1393)
    }
}