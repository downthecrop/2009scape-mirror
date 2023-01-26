package content.region.fremennik.rellekka.dialogue

import core.api.isQuestComplete
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import core.tools.END_DIALOGUE

/**
 * @author qmqz
 * There is no available dialogue for after Fremennik Trials,
 * only after Hero's Welcome which isn't in this revision.
 */

@Initializable
class TalkToChiefDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!isQuestComplete(player, "Fremennik Trials")) {
            npcl(core.game.dialogue.FacialExpression.ANNOYED, "I cannot speak to you outerlander! Talk to Brundt, the Chieftain!").also { stage = END_DIALOGUE }
        } else {
            player(core.game.dialogue.FacialExpression.FRIENDLY, "Hello.").also { stage = 0 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npc(core.game.dialogue.FacialExpression.FRIENDLY, "Hello to you, too!").also { stage = END_DIALOGUE }
         }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return TalkToChiefDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BORROKAR_1307, NPCs.FREIDIR_1306, NPCs.INGA_1314,
            NPCs.JENNELLA_1312, NPCs.LANZIG_1308, NPCs.PONTAK_1309,
            NPCs.SASSILIK_1313)
    }
}
