package content.region.asgarnia.falador.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

/**
 * @author qmqz
 * https://youtu.be/DwQgAQqaXos
 */

@Initializable
class ApprenticeWorkmanDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> player(FacialExpression.FRIENDLY, "Hiya.").also { stage++ }
            1 -> npc(FacialExpression.SAD, "Sorry, I haven't got time to chat. We've only just",
                    "finished a collossal order of furniture for the Varrock",
                    "area, and already there's more work coming in.").also { stage++ }
            2 -> player(FacialExpression.ASKING, "Varrock?").also { stage++ }
            3 -> npc(FacialExpression.ROLLING_EYES, "Yeah, the Council's had it redecorated.").also { stage++ }
            4 -> npc(3236, "Oi - stop gabbing and get that chair finished!").also { stage++ } // ANNOYED
            5 -> npc(FacialExpression.SAD, "You'd better let me get on with my work.").also { stage++ }
            6 -> player(FacialExpression.NEUTRAL, "Ok, bye.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return ApprenticeWorkmanDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.APPRENTICE_WORKMAN_3235)
    }
}