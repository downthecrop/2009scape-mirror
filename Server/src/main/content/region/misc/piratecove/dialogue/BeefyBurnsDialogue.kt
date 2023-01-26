package content.region.misc.piratecove.dialogue

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
class BeefyBurnsDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(core.game.dialogue.FacialExpression.HALF_ASKING, "What are you cooking?").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npc(core.game.dialogue.FacialExpression.LAUGH, "My speciality! What else could I be cooking?").also { stage++ }
            1 -> player(core.game.dialogue.FacialExpression.THINKING, "Ok, and your speciality is...?").also { stage++ }
            2 -> npcl(core.game.dialogue.FacialExpression.LAUGH, "Boiled shark guts with a hint of rosemary and a dash of squid ink.").also { stage++ }
            3 -> player(core.game.dialogue.FacialExpression.FRIENDLY,"I think I'll stick to making my own food.").also { stage++ }
            4 -> npc(core.game.dialogue.FacialExpression.FRIENDLY, "Your loss!").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return BeefyBurnsDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BEEFY_BURNS_4541)
    }
}
