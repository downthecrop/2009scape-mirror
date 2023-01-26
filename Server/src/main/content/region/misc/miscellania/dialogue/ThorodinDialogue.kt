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
class ThorodinDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

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
                npc(core.game.dialogue.FacialExpression.OLD_DEFAULT,"We're extending the cave so more people can live in it.",
                    "These Miscellanians aren't so bad.",
                    "They appreciate the benefits of living underground.").also { stage++ }
            }

            11 -> {
                player(core.game.dialogue.FacialExpression.ASKING,"...such as?").also { stage++ }
            }

            12 -> {
                npc(core.game.dialogue.FacialExpression.OLD_DEFAULT,"Not getting rained on, for example.",
                    "Did you do anything about that monster Donal", "was talking about?").also { stage++ }
            }

            13 -> {
                player(core.game.dialogue.FacialExpression.FRIENDLY,"It's been taken care of.").also { stage++ }
            }

            14 -> {
                npc(core.game.dialogue.FacialExpression.OLD_HAPPY,"Glad to hear it.",
                    "Now we can get on with excavating.").also { stage = 99 }
            }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return ThorodinDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.THORODIN_3936)
    }
}