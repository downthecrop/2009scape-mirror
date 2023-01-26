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
class DonalDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(core.game.dialogue.FacialExpression.OLD_DEFAULT,"What do you want?").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> {
                player(core.game.dialogue.FacialExpression.THINKING, "Just wondering if you were still here.").also { stage++ }
            }

            1 -> {
                npc(core.game.dialogue.FacialExpression.OLD_DEFAULT, "Of course I'm still here.").also { stage++ }
            }

            2 -> {
                npc(core.game.dialogue.FacialExpression.OLD_DISTRESSED, "I'm not going near that crack in the wall again.").also { stage++ }
            }

            3 -> {
                npc(core.game.dialogue.FacialExpression.OLD_DISTRESSED, "Rock falls and so on are fine, ", "but sea monsters in caves - never!").also { stage = 99 }
            }
            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return DonalDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DONAL_3938)
    }
}