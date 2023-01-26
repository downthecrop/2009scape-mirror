package content.region.fremennik.neitiznot.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import core.tools.END_DIALOGUE

@Initializable
class FridleifShieldsonDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {
    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return FridleifShieldsonDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Greetings!")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> playerl(core.game.dialogue.FacialExpression.FRIENDLY, "Hello!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FRIDLEIF_SHIELDSON_5505)
    }

}