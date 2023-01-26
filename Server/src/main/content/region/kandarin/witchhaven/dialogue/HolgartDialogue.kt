package content.region.kandarin.witchhaven.dialogue

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
class HolgartDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){
    fun gender (male : String = "sir", female : String = "madam") = if (player.isMale) male else female

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(core.game.dialogue.FacialExpression.FRIENDLY,"Hello there.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npc(core.game.dialogue.FacialExpression.FRIENDLY, "Well hello " + gender() + ", beautiful day isn't it?").also { stage++ }
            1 -> player(core.game.dialogue.FacialExpression.FRIENDLY, "Not bad I suppose.").also { stage++ }
            2 -> npc(core.game.dialogue.FacialExpression.FRIENDLY, "Just smell that sea air... beautiful.").also { stage++ }
            3 -> player(core.game.dialogue.FacialExpression.FRIENDLY, "Hmm... lovely...").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return HolgartDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HOLGART_4866)
    }
}
