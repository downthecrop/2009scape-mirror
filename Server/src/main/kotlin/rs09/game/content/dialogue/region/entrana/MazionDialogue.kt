package rs09.game.content.dialogue.region.entrana

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
class MazionDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when ((1..3).random()) {
            1 -> npc(FacialExpression.FRIENDLY, "Nice weather we're having today!").also { stage = 99 }
            2 -> npc(FacialExpression.FRIENDLY, "Hello " + player.name + ", fine day today!").also { stage = 99 }
            3 -> npc(FacialExpression.ANNOYED, "Please leave me alone, a parrot stole my banana.").also { stage = 99 }
        }
            return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return MazionDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MAZION_3114)
    }
}
