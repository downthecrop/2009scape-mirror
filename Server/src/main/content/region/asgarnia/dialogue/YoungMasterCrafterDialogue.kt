package content.region.asgarnia.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class YoungMasterCrafterDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun newInstance(player: Player): DialoguePlugin {
        return YoungMasterCrafterDialogue(player)
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npcl(
                FacialExpression.HALF_ASKING,
                "Yeah?"
            ).also { stage++ }

            1 -> playerl(
                FacialExpression.FRIENDLY,
                "Hello."
            ).also { stage++ }

            2 -> npcl(
                FacialExpression.HALF_ASKING,
                "Whassup?"
            ).also { stage++ }

            3 -> playerl(
                FacialExpression.NEUTRAL,
                "So... are you here to give crafting tips?"
            ).also { stage++ }

            4 -> npcl(
                FacialExpression.ANNOYED,
                "Dude, do I look like I wanna talk to you?"
            ).also { stage++ }

            5 -> playerl(
                FacialExpression.NEUTRAL,
                "I suppose not."
            ).also { stage++ }

            6 -> npcl(
                FacialExpression.NEUTRAL,
                "Right on!"
            ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MASTER_CRAFTER_2733)
    }
}