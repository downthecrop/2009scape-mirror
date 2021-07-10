package rs09.game.content.dialogue.region.neitiznot

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import rs09.tools.END_DIALOGUE

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