package content.region.kandarin.ardougne.dialogue

import core.api.openNpcShop
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class ArdougneBakerDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun newInstance(player: Player?): DialoguePlugin {
        return ArdougneBakerDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(
            FacialExpression.HAPPY,
            "Good day, monsieur. Would you like ze nice freshly-baked bread? Or perhaps a nice piece of cake?"
        ).also { stage = START_DIALOGUE }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("Let's see what you have.", "No thank you.").also { stage++ }
            1 -> when (buttonId) {
                1 -> {
                    openNpcShop(player, NPCs.BAKER_571)
                    end()
                }

                2 -> playerl(
                    FacialExpression.FRIENDLY, "No thank you."
                ).also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BAKER_571)
    }
}