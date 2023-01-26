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
 */

@Initializable
class LonghallBouncerDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!isQuestComplete(player, "Fremennik Trials")) {
            npcl(core.game.dialogue.FacialExpression.ANNOYED, "Hey, outerlander. You can't go through there. Talent only, backstage.").also { stage = END_DIALOGUE }
        } else{
            npcl(core.game.dialogue.FacialExpression.ANNOYED, "You can't go through there. Talent only, backstage.").also { stage = 0 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> player(core.game.dialogue.FacialExpression.PANICKED, "But I'm a Bard!").also { stage++ }
            1 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "No you're not. I saw your performance. I was paid well to keep you from ever setting foot on stage here again.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return LonghallBouncerDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LONGHALL_BOUNCER_1278)
    }
}
