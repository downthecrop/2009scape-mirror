package content.region.kandarin.feldip.ooglog.dialogue

import core.api.sendNPCDialogue
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE

/**
 * Provides dialogue tree for the Ogress Bankers in the city of Oo'glog.
 *
 * @author vddCore
 */
@Initializable
class OgressBankerDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {
    override fun getIds(): IntArray = intArrayOf(
        NPCs.OGRESS_BANKER_7049,
        NPCs.OGRESS_BANKER_7050
    )

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> npcl(
                core.game.dialogue.FacialExpression.ANNOYED,
                "..."
            ).also { stage++ }

            1 -> playerl(
                core.game.dialogue.FacialExpression.ANNOYED,
                "Excuse me, can I get some service here, please?"
            ).also { stage++ }

            2 -> npcl(
                core.game.dialogue.FacialExpression.ANGRY,
                "GRAAAAAH! You go away, human! Me too busy with training to talk to puny thing like you."
            ).also { stage++ }

            3 -> sendNPCDialogue(
                player,
                NPCs.BALNEA_7047,
                "I do apologise, sir. We're temporarily unable to meet your banking needs.",
                core.game.dialogue.FacialExpression.NEUTRAL,
            ).also { stage++ }

            4 -> sendNPCDialogue(
                player,
                NPCs.BALNEA_7047,
                "We'll be open as soon as we realize our customer experience goals " +
                "and can guarantee the high standards of service that you expect from all " +
                "branches of the Bank of Gielinor.",
                core.game.dialogue.FacialExpression.NEUTRAL
            ).also { stage++ }

            5 -> playerl(
                core.game.dialogue.FacialExpression.THINKING,
                "What did you just say to me?"
            ).also { stage++ }

            6 -> sendNPCDialogue(
                player,
                NPCs.BALNEA_7047,
                "We're closed until I can teach these wretched creatures some manners.",
                core.game.dialogue.FacialExpression.ANNOYED
            ).also { stage++ }

            7 -> playerl(
                core.game.dialogue.FacialExpression.NEUTRAL,
                "Ah, right. Good luck with that."
            ).also { stage = END_DIALOGUE }
        }

        /* TODO: "As a First Resort..." quest dialogue file is required here. */
        return true
    }
}