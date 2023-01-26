package content.region.tirranwn.dialogue

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
class GoreuDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(core.game.dialogue.FacialExpression.FRIENDLY,"Hello.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Good day, can I help you?").also { stage++ }
            1 -> playerl(core.game.dialogue.FacialExpression.FRIENDLY, "No thanks I'm just watching the world go by.").also { stage++ }
            2 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Well I can think of no better place to do it, it is beautiful here is it not?").also { stage++ }
            3 -> playerl(core.game.dialogue.FacialExpression.FRIENDLY, "Indeed it is.").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return GoreuDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GOREU_2363)
    }
}
