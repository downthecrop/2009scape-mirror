package rs09.game.content.dialogue.region.dorgeshuun

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
class BartakDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FacialExpression.OLD_DISTRESSED,"Oh no! What's broken?").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> player(FacialExpression.ASKING, "What? Nothing's broken?").also { stage++ }
            1 -> npc(FacialExpression.OLD_DISTRESSED, "I'm sorry. I'm just a bit jumpy.").also { stage++ }
            2 -> npcl(FacialExpression.OLD_DISTRESSED, "I'm in charge of all the metalworking of Dorgesh-Kaan. It's a big responsibility!").also { stage++ }
            3 -> npcl(FacialExpression.OLD_DISTRESSED, "If something metal breaks I have to fix it. And lots of things are made of metal!").also { stage++ }
            4 -> player(FacialExpression.FRIENDLY, "Don't worry, I'm sure you're up to the task!").also { stage++ }
            5 -> npc(FacialExpression.OLD_DISTRESSED, "I hope you're right.").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return BartakDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BARTAK_5778)
    }
}
