package content.region.kandarin.feldip.ooglog.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE

/**
 * Provides dialogue tree for Balnea NPC involved in the
 * "As a First Resort..." quest.
 *
 * @author vddCore
 */
@Initializable
class BalneaDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> playerl(
                FacialExpression.FRIENDLY,
                "Hi there!"
            ).also { stage++ }

            1 -> npcl(
                FacialExpression.NEUTRAL,
                "I'm ever so busy at the moment; please come back after the grand opening."
            ).also { stage++ }

            2 -> playerl(
                FacialExpression.HALF_ASKING,
                "What grand reopening?"
            ).also { stage++ }

            3 -> npcl(
                FacialExpression.ANNOYED,
                "I'm sorry, I really can't spare the time to talk to you."
            ).also { stage++ }

            4 -> playerl(
                FacialExpression.THINKING,
                "Uh, sure."
            ).also { stage = END_DIALOGUE }
        }

        /* TODO: "As a First Resort" quest dialogue file is required here. */
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.BALNEA_7047)
}