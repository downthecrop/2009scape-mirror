package content.region.misc.zanaris.dialogue

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
class BlaecDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when ((1..3).random()) {
            1 -> npc(core.game.dialogue.FacialExpression.FRIENDLY,"Wunnerful weather we're having today!").also { stage = 99 }
            2 -> npc(core.game.dialogue.FacialExpression.FRIENDLY,"Greetin's " + player.name + ", fine day today!").also { stage = 99 }
            3 -> npcl(core.game.dialogue.FacialExpression.ANNOYED,"Please leave me alone, I'm busy trapping the pygmy shrews.").also { stage = 99 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return BlaecDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BLAEC_3115)
    }
}
