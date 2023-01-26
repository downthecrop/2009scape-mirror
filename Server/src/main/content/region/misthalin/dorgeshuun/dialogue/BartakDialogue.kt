package content.region.misthalin.dorgeshuun.dialogue

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
class BartakDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(core.game.dialogue.FacialExpression.OLD_DISTRESSED,"Oh no! What's broken?").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> player(core.game.dialogue.FacialExpression.ASKING, "What? Nothing's broken?").also { stage++ }
            1 -> npc(core.game.dialogue.FacialExpression.OLD_DISTRESSED, "I'm sorry. I'm just a bit jumpy.").also { stage++ }
            2 -> npcl(core.game.dialogue.FacialExpression.OLD_DISTRESSED, "I'm in charge of all the metalworking of Dorgesh-Kaan. It's a big responsibility!").also { stage++ }
            3 -> npcl(core.game.dialogue.FacialExpression.OLD_DISTRESSED, "If something metal breaks I have to fix it. And lots of things are made of metal!").also { stage++ }
            4 -> player(core.game.dialogue.FacialExpression.FRIENDLY, "Don't worry, I'm sure you're up to the task!").also { stage++ }
            5 -> npc(core.game.dialogue.FacialExpression.OLD_DISTRESSED, "I hope you're right.").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return BartakDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BARTAK_5778)
    }
}
