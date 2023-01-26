package content.region.fremennik.rellekka.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import core.tools.END_DIALOGUE

/**
 * @author qmqz
 */

@Initializable
class WaitingOnTheShowDialogues(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(core.game.dialogue.FacialExpression.ANNOYED, "Shhh! I'm waiting for the show!").also { stage = END_DIALOGUE }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return WaitingOnTheShowDialogues(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FRIDGEIR_1277, NPCs.OSPAK_1274, NPCs.STYRMIR_1275)
    }
}
