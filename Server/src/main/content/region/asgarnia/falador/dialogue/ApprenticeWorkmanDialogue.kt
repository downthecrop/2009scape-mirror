package content.region.asgarnia.falador.dialogue

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
class ApprenticeWorkmanDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(core.game.dialogue.FacialExpression.FRIENDLY,"Hiya.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> {
                npc(core.game.dialogue.FacialExpression.FRIENDLY, "Sorry, I haven't got time to chat.",
                    "We've only just finished a collossal order of furniture",
                    "for the Varrock area, and already there's more work",
                    "coming in.").also { stage++ }
            }

            1 -> {
                player(core.game.dialogue.FacialExpression.ASKING, "Varrock?").also { stage++ }
            }

            2 -> {
                npc(core.game.dialogue.FacialExpression.FRIENDLY, "Yeah, the Council's had it redecorated.").also { stage++ }
            }

            3 -> {
                npc(3236, "Oi - stop gabbing and get that chair finished!").also { stage++ }
            }

            4 -> {
                npc(core.game.dialogue.FacialExpression.FRIENDLY, "You'd better let me get on with my work.").also { stage++ }
            }

            5 -> {
                player(core.game.dialogue.FacialExpression.FRIENDLY, "Ok, bye.").also { stage = 99 }
            }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return ApprenticeWorkmanDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.APPRENTICE_WORKMAN_3235)
    }
}