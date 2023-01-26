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
 * "As a first resort..." quest.
 *
 * @author vddCore
 */
@Initializable
class BalneaDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> playerl(
                core.game.dialogue.FacialExpression.FRIENDLY,
                "Hi there!"
            ).also { stage++ }

            1 -> npcl(
                core.game.dialogue.FacialExpression.NEUTRAL,
                "I'm ever so busy at the moment; please come back after the grand opening."
            ).also { stage++ }

            2 -> playerl(
                core.game.dialogue.FacialExpression.HALF_ASKING,
                "What grand reopening?"
            ).also { stage++ }

            3 -> npcl(
                core.game.dialogue.FacialExpression.ANNOYED,
                "I'm sorry, I really can't spare the time to talk to you."
            ).also { stage++ }

            4 -> playerl(
                core.game.dialogue.FacialExpression.THINKING,
                "Uh, sure."
            ).also { stage = END_DIALOGUE }
        }

        /* TODO: "As a First Resort..." quest dialogue file is required here. */
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.BALNEA_7047)
}