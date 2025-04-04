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
class LonghallBouncerDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
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
