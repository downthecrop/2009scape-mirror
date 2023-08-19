package content.region.fremennik.jatizso.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class EricDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return EricDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npcl(FacialExpression.HALF_GUILTY, "Spare us a few coppers mister")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        playerl(FacialExpression.ANGRY, "NO!")
        stage = END_DIALOGUE
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ERIC_5499)
    }

}