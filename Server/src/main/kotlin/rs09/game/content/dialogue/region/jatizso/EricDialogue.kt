package rs09.game.content.dialogue.region.jatizso

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import rs09.tools.END_DIALOGUE

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