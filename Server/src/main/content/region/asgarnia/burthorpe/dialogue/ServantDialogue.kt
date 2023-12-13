package content.region.asgarnia.burthorpe.dialogue

import core.api.openDialogue
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * Servant Dialogue
 * @author 'Vexia
 * @author ovenbread
 * @author Trident101
 */
@Initializable
class ServantDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, ServantDialogueFile(), npc)
        return true
    }

    override fun newInstance(player: Player): DialoguePlugin {
        return ServantDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SERVANT_1081)
    }
}

class ServantDialogueFile : DialogueBuilderFile() {

    override fun create(b: DialogueBuilder) {
        b.defaultDialogue().playerl(
            FacialExpression.HALF_GUILTY,
            "Hi!"
        ).npcl(
            FacialExpression.HALF_GUILTY,
            "Hi."
        ).npcl(
            FacialExpression.HALF_GUILTY,
            "Look, I'd better not talk. I'll get in trouble."
        ).npcl(
            FacialExpression.HALF_GUILTY,
            "If you want someone to show you round the castle ask Eohric, the Head Servant."
        )
    }
}
