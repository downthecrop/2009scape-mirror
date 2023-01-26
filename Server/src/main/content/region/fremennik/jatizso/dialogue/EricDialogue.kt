package content.region.fremennik.jatizso.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import core.tools.END_DIALOGUE

@Initializable
class EricDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {
    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return EricDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npcl(core.game.dialogue.FacialExpression.HALF_GUILTY, "Spare us a few coppers mister")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        playerl(core.game.dialogue.FacialExpression.ANGRY, "NO!")
        stage = END_DIALOGUE
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ERIC_5499)
    }

}