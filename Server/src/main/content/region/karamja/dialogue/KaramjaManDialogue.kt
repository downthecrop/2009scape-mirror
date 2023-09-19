package content.region.karamja.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class KaramjaManDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun newInstance(player: Player?): DialoguePlugin {
        return KaramjaManDialogue(player)
    }


    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        playerl(
            FacialExpression.FRIENDLY, "Hello, how's it going?"
        ).also { stage = START_DIALOGUE }
        return true
    }


    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npcl(
                FacialExpression.HALF_GUILTY,
                "Not too bad, but I'm a little worried about the increase of goblins these days."
            ).also { stage++ }

            1 -> playerl(
                FacialExpression.HAPPY, "Don't worry, I'll kill them."
            ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MAN_3915)
    }
}