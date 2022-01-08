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
class DonalDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FacialExpression.OLD_DEFAULT,"What do you want?").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> {
                player(FacialExpression.THINKING, "Just wondering if you were still here.").also { stage++ }
            }

            1 -> {
                npc(FacialExpression.OLD_DEFAULT, "Of course I'm still here.").also { stage++ }
            }

            2 -> {
                npc(FacialExpression.OLD_DISTRESSED, "I'm not going near that crack in the wall again.").also { stage++ }
            }

            3 -> {
                npc(FacialExpression.OLD_DISTRESSED, "Rock falls and so on are fine, ", "but sea monsters in caves - never!").also { stage = 99 }
            }
            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return DonalDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DONAL_3938)
    }
}