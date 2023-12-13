package content.region.asgarnia.burthorpe.dialogue

import core.api.isQuestComplete
import core.api.openDialogue
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

/**
 * Unferth Dialogue
 * @author ovenbread
 * @author Trident101
 */
@Initializable
class UnferthDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, UnferthDialogueFile(), npc)
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return UnferthDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.UNFERTH_2655)
    }
}

class UnferthDialogueFile : DialogueBuilderFile() {

    override fun create(b: DialogueBuilder) {
        b.onPredicate { player -> isQuestComplete(player, "A Tail of Two Cats") }.playerl(
            FacialExpression.FRIENDLY, "Hi Unferth. How are you doing?"
        ).npcl(
            FacialExpression.GUILTY, "It's just not the same without Bob around."
        ).playerl(
            FacialExpression.HALF_GUILTY, "I'm so sorry Unferth."
        ).npcl(
            FacialExpression.HALF_GUILTY,
            "Gertrude asked me if I'd like one of her new kittens. I don't think I'm ready for that yet."
        ).playerl(
            FacialExpression.FRIENDLY, "Give it time. Things will get better, I promise."
        ).npcl(
            FacialExpression.HALF_GUILTY, "Thanks, @name."
        ).end()

        b.defaultDialogue().npcl(
            FacialExpression.GUILTY,
            "Hello."
        ).playerl(
            FacialExpression.FRIENDLY,
            "What's wrong?"
        ).npcl(
            FacialExpression.GUILTY,
            "It's fine. Nothing for you to worry about."
        ).end()
    }
}