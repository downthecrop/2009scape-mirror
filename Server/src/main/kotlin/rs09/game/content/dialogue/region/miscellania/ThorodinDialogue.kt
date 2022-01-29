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
class ThorodinDialogue(player: Player? = null) : DialoguePlugin(player){

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
                npc(FacialExpression.OLD_DEFAULT,"We're extending the cave so more people can live in it.",
                    "These Miscellanians aren't so bad.",
                    "They appreciate the benefits of living underground.").also { stage++ }
            }

            11 -> {
                player(FacialExpression.ASKING,"...such as?").also { stage++ }
            }

            12 -> {
                npc(FacialExpression.OLD_DEFAULT,"Not getting rained on, for example.",
                    "Did you do anything about that monster Donal", "was talking about?").also { stage++ }
            }

            13 -> {
                player(FacialExpression.FRIENDLY,"It's been taken care of.").also { stage++ }
            }

            14 -> {
                npc(FacialExpression.OLD_HAPPY,"Glad to hear it.",
                    "Now we can get on with excavating.").also { stage = 99 }
            }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return ThorodinDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.THORODIN_3936)
    }
}