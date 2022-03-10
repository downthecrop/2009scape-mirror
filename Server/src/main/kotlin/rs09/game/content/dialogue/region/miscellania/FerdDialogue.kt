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
class FerdDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FacialExpression.OLD_DEFAULT,"Good day, sir.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> {
                player(FacialExpression.THINKING, "What are you doing down here?.").also { stage++ }
            }

            1 -> {
                npc(FacialExpression.OLD_DEFAULT, "Shoring up the walls.").also { stage++ }
            }

            2 -> {
                player(FacialExpression.ASKING, "What does that do?").also { stage++ }
            }

            3 -> {
                npc(FacialExpression.OLD_DEFAULT, "Stops them falling down.").also { stage = 99 }
            }

            4 -> {
                player(FacialExpression.ASKING, "Oh, I see.").also { stage++ }
            }

            5 -> {
                npc(FacialExpression.OLD_NOT_INTERESTED, "Aye.",
                    "If you want to chatter, you'd better talk to ",
                    "Thorodin over there. I'm working.").also { stage = 99 }
            }

            6 -> {
                player(FacialExpression.ASKING, "Okay then.").also { stage++ }
            }
            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return FerdDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FERD_3937)
    }
}