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
class FerdDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(core.game.dialogue.FacialExpression.OLD_DEFAULT,"Good day, sir.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> {
                player(core.game.dialogue.FacialExpression.THINKING, "What are you doing down here?.").also { stage++ }
            }

            1 -> {
                npc(core.game.dialogue.FacialExpression.OLD_DEFAULT, "Shoring up the walls.").also { stage++ }
            }

            2 -> {
                player(core.game.dialogue.FacialExpression.ASKING, "What does that do?").also { stage++ }
            }

            3 -> {
                npc(core.game.dialogue.FacialExpression.OLD_DEFAULT, "Stops them falling down.").also { stage = 99 }
            }

            4 -> {
                player(core.game.dialogue.FacialExpression.ASKING, "Oh, I see.").also { stage++ }
            }

            5 -> {
                npc(core.game.dialogue.FacialExpression.OLD_NOT_INTERESTED, "Aye.",
                    "If you want to chatter, you'd better talk to ",
                    "Thorodin over there. I'm working.").also { stage = 99 }
            }

            6 -> {
                player(core.game.dialogue.FacialExpression.ASKING, "Okay then.").also { stage++ }
            }
            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return FerdDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FERD_3937)
    }
}