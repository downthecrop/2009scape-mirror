package content.region.misc.miscellania.dialogue

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
class JariDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(core.game.dialogue.FacialExpression.OLD_DEFAULT,"Good day, sir.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> {
                options("What are you doing down here?", "Good day.").also { stage++ }
            }

            1 -> when(buttonId){
                1 -> {
                    player(core.game.dialogue.FacialExpression.FRIENDLY, "What are you doing down here?").also { stage = 10 }
                }

                2 -> {
                    player(core.game.dialogue.FacialExpression.NEUTRAL, "Good day.").also { stage = 99 }
                }
            }

            10 -> {
                npc(core.game.dialogue.FacialExpression.OLD_DEFAULT,"I'm waiting to work on the digging.").also { stage++ }
            }

            11 -> {
                npc(core.game.dialogue.FacialExpression.OLD_HAPPY,"It's the first excavation I've worked on, ",
                    "and I'm looking forward to it.").also { stage = 99 }
            }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return JariDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.JARI_3935)
    }
}