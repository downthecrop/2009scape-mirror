package content.region.misc.miscellania.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * Dialogue for Derrik in Misc.
 * @author qmqz
 */

@Initializable
class DerrikDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(core.game.dialogue.FacialExpression.FRIENDLY,"Good day, Sir. Can I help you with anything?").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){

            0 -> {
                options("Can I use your anvil?", "Nothing, thanks.").also { stage++ }
            }

            1 -> when(buttonId){
                1 -> {
                    player(core.game.dialogue.FacialExpression.ASKING, "Can I use your anvil?").also { stage = 5 }
                }
                2 -> {
                    player(core.game.dialogue.FacialExpression.NEUTRAL, "Nothing, thanks.").also { stage = 99 }
                }
            }

            5 -> {
                npc(core.game.dialogue.FacialExpression.NEUTRAL, "You may.").also { stage = 99 }
            }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return DerrikDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DERRIK_1376)
    }
}