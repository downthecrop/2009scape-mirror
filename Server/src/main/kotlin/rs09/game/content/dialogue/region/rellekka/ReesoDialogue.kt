package rs09.game.content.dialogue.region.rellekka

import api.isQuestComplete
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import rs09.tools.END_DIALOGUE

/**
 * @author qmqz
 */

@Initializable
class ReesoDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!isQuestComplete(player, "Fremennik Trials")) {
            npcl(FacialExpression.ANNOYED, "Please do not disturb me, outerlander. I have much to do.").also { stage = END_DIALOGUE }
        } else {
            npcl(FacialExpression.STRUGGLE, "Sorry, ${player.getAttribute("fremennikname","fremmyname")}, I must get on with my work.").also { stage = END_DIALOGUE }
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return ReesoDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.REESO_3116)
    }
}
