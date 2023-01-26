package content.region.fremennik.neitiznot.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import core.tools.END_DIALOGUE

@Initializable
class GunnarHoldstromDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {
    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return GunnarHoldstromDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npcl(core.game.dialogue.FacialExpression.HAPPY, "Ah, isn't it a lovely day?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> playerl(core.game.dialogue.FacialExpression.ASKING, "It's not bad. What puts you in such a good mood?").also { stage++ }
            1 -> npcl(core.game.dialogue.FacialExpression.HAPPY, "Oh, I just love my job. The smell of the sea breeze, the smell of the arctic pine sap, the smell of the yaks. I find it all so bracing.").also { stage++ }
            2 -> playerl(core.game.dialogue.FacialExpression.ASKING, "Bracing? Hmmm. I think I might have chosen a different word.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GUNNAR_HOLDSTROM_5511)
    }

}