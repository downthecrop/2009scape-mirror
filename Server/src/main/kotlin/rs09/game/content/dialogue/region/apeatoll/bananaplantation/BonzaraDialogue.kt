package rs09.game.content.dialogue.region.apeatoll.bananaplantation

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
class BonzaraDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FacialExpression.OLD_DEFAULT,"It looks like you're trying to escape. Would you like some help?").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> options("Yes", "No").also { stage++ }
            1 -> when (buttonId) {
                1 -> player(FacialExpression.WORRIED, "I ... uh ... yes.").also { stage = 10 }
                2 -> playerl(FacialExpression.ASKING, "No thank you. Who are you by the way?").also { stage = 20 }
            }

            10 -> npc(FacialExpression.OLD_DEFAULT, "Right you are.").also { stage++ }
            11 -> {
                end()
                //teleport to ape atoll
            }

            20 -> npcl(FacialExpression.OLD_DEFAULT, "Never mind that child. You should worry more about who you are and the nature of the forces that have driven you here.").also { stage++ }
            21 -> player(FacialExpression.THINKING, "I'll ... keep that in mind, thanks.").also { stage++ }
            22 -> npc(FacialExpression.OLD_DEFAULT, "We WILL meet again, " + player.name + ".").also { stage++ }
            23 -> player(FacialExpression.SUSPICIOUS, "Ok...").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return BonzaraDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BONZARA_1468)
    }
}
