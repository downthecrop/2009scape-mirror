package rs09.game.content.dialogue.region.neitiznot

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import rs09.tools.END_DIALOGUE

@Initializable
class ThakkradSigmundsonDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return ThakkradSigmundsonDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npcl(FacialExpression.FRIENDLY, "Greetings! I can cure your Yak Hides if you'd like!")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> playerl(FacialExpression.FRIENDLY, "Good to know!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.THAKKRAD_SIGMUNDSON_5506)
    }

}