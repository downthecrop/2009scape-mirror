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
class KelynDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(core.game.dialogue.FacialExpression.FRIENDLY,"Hello.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Huh... Oh sorry, you made me jump. I was miles away, day dreaming.").also { stage++ }
            1 -> player(core.game.dialogue.FacialExpression.FRIENDLY, "About what may I ask?").also { stage++ }
            2 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "I was thinking about the crystal spires of Prifddinas.").also { stage++ }
            3 -> player(core.game.dialogue.FacialExpression.FRIENDLY, "It must be beautiful, I've only seen the city walls.").also { stage++ }
            4 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "I have never seen it, all I know are the stories. I hope that changes one day.").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return KelynDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KELYN_2367)
    }
}
