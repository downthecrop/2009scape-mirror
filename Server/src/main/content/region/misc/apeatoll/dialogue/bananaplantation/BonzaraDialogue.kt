package content.region.misc.apeatoll.dialogue.bananaplantation

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
class BonzaraDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(core.game.dialogue.FacialExpression.OLD_DEFAULT,"It looks like you're trying to escape. Would you like some help?").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> options("Yes", "No").also { stage++ }
            1 -> when (buttonId) {
                1 -> player(core.game.dialogue.FacialExpression.WORRIED, "I ... uh ... yes.").also { stage = 10 }
                2 -> playerl(core.game.dialogue.FacialExpression.ASKING, "No thank you. Who are you by the way?").also { stage = 20 }
            }

            10 -> npc(core.game.dialogue.FacialExpression.OLD_DEFAULT, "Right you are.").also { stage++ }
            11 -> {
                end()
                //teleport to ape atoll
            }

            20 -> npcl(core.game.dialogue.FacialExpression.OLD_DEFAULT, "Never mind that child. You should worry more about who you are and the nature of the forces that have driven you here.").also { stage++ }
            21 -> player(core.game.dialogue.FacialExpression.THINKING, "I'll ... keep that in mind, thanks.").also { stage++ }
            22 -> npc(core.game.dialogue.FacialExpression.OLD_DEFAULT, "We WILL meet again, " + player.name + ".").also { stage++ }
            23 -> player(core.game.dialogue.FacialExpression.SUSPICIOUS, "Ok...").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return BonzaraDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BONZARA_1468)
    }
}
