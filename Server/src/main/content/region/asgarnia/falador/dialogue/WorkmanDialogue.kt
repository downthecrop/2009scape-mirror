package content.region.asgarnia.falador.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
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
class WorkmanDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> player(FacialExpression.FRIENDLY, "Hiya.").also { stage++ }
            1 -> npc(FacialExpression.ANNOYED, "What do you want? I've got work to do!").also { stage++ }
            2 -> player(FacialExpression.ASKING, "Can you teach me anything?").also { stage++ }
            3 -> npcl(FacialExpression.ANNOYED, "No - I've got one lousy apprentice already, and that's quite enough hassle! Go away!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return WorkmanDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.WORKMAN_3236)
    }
}