package rs09.game.content.dialogue.region.miscellania

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
class AlvissDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FacialExpression.OLD_DEFAULT,"Good day, sir.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> {
                options("What are you doing down here?", "Good day.").also { stage++ }
            }

            1 -> when(buttonId){
                1 -> {
                    player(FacialExpression.FRIENDLY, "What are you doing down here?").also { stage = 10 }
                }

                2 -> {
                    player(FacialExpression.NEUTRAL, "Good day.").also { stage = 99 }
                }
            }

            10 -> {
                npc(FacialExpression.OLD_DEFAULT,"I'm waiting for my shift, of course.",
                    "We can't dig all the time, you know.").also { stage++ }
            }

            11 -> {
                npc(FacialExpression.OLD_DEFAULT," I'm also researching the links between the ",
                    "Fremenniks and the Dwarves.").also { stage++ }
            }

            12 -> {
                npc(FacialExpression.OLD_DEFAULT,"I've found that we have some mythology in common.").also { stage = 99 }
            }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return AlvissDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ALVISS_3933)
    }
}