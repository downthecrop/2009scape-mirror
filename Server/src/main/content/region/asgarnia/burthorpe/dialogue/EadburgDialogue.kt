package content.region.asgarnia.burthorpe.dialogue

import core.api.openDialogue
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

@Initializable
class EadburgDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, EadburgDialogueFile(), npc)
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return EadburgDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.EADBURG_1072)
    }
}

class EadburgDialogueFile : DialogueBuilderFile() {

    override fun create(b: DialogueBuilder) {
        b.defaultDialogue().playerl(
            FacialExpression.FRIENDLY,
            "What's cooking, good looking?"
        ).npcl(
            FacialExpression.FRIENDLY,
            "The stew for the servant's main meal."
        ).playerl(
            FacialExpression.HALF_WORRIED,
            "Um...er...see you later."
        ).npcl(
            FacialExpression.FRIENDLY,
            "Bye!"
        )
    }
}