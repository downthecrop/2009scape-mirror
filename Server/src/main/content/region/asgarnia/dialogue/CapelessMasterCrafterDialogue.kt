package content.region.asgarnia.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class CapelessMasterCrafterDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun newInstance(player: Player): DialoguePlugin {
        return CapelessMasterCrafterDialogue(player)
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        npcl(
            FacialExpression.FRIENDLY,
            "Hello, and welcome to the Crafting Guild. Accomplished crafters from all " +
                    "over the land come here to use our top notch workshops."
        ).also { stage = END_DIALOGUE }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MASTER_CRAFTER_2732)
    }
}