package rs09.game.content.dialogue.region.neitiznot

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import rs09.tools.END_DIALOGUE

@Initializable
class FridleifShieldsonDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return FridleifShieldsonDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npcl(FacialExpression.FRIENDLY, "Greetings!")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> playerl(FacialExpression.FRIENDLY, "Hello!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FRIDLEIF_SHIELDSON_5505)
    }

}