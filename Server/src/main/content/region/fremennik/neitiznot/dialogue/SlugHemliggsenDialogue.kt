package content.region.fremennik.neitiznot.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class SlugHemliggsenDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return SlugHemliggsenDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npcl(FacialExpression.WORRIED, "Shhh. Go away. I'm not allowed to talk to you.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        playerl(FacialExpression.ANNOYED, "Fine, whatever ...")
        stage = END_DIALOGUE
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SLUG_HEMLIGSSEN_5520)
    }

}