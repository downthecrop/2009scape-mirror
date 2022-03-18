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
class LonghallBouncerDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!isQuestComplete(player, "Fremennik Trials")) {
            npcl(FacialExpression.ANNOYED, "Hey, outerlander. You can't go through there. Talent only, backstage.").also { stage = END_DIALOGUE }
        } else{
            npcl(FacialExpression.ANNOYED, "You can't go through there. Talent only, backstage.").also { stage = 0 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> player(FacialExpression.PANICKED, "But I'm a Bard!").also { stage++ }
            1 -> npcl(FacialExpression.FRIENDLY, "No you're not. I saw your performance. I was paid well to keep you from ever setting foot on stage here again.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return LonghallBouncerDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LONGHALL_BOUNCER_1278)
    }
}
