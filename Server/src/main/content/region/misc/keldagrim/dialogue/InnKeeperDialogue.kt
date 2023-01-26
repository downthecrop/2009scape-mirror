package content.region.misc.keldagrim.dialogue

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
class InnKeeperDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(core.game.dialogue.FacialExpression.FRIENDLY,"Hello.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> {
                npc(core.game.dialogue.FacialExpression.CHILD_NEUTRAL, "Welcome to the King's Axe inn!", "What can I help you with?").also { stage++ }
            }

            1 -> {
                player(core.game.dialogue.FacialExpression.ASKING, "Can I have some beer please?").also { stage++ }
            }

            2 -> {
                npc(core.game.dialogue.FacialExpression.CHILD_NORMAL, "Go to the bar downstairs.", "I only deal with residents.").also { stage++ }
            }

            3 -> {
                player(core.game.dialogue.FacialExpression.THINKING, "Residents? People live here?").also { stage++ }
            }

            4 -> {
                npc(core.game.dialogue.FacialExpression.CHILD_LOUDLY_LAUGHING, "No, just guests that stay the night.").also { stage = 99 }
            }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return InnKeeperDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.INN_KEEPER_2177)
    }
}