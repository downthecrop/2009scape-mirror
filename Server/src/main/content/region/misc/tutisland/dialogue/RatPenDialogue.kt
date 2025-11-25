package content.region.misc.tutisland.dialogue

import content.region.misc.tutisland.handlers.sendStageDialog
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller

/**
 * Vannaka's angry dialogue when you try to enter his rat pen before you're supposed to
 */
class RatPenDialogue : DialogueLabeller() {
    override fun addConversation() {
        npc(ChatAnim.ANGRY, "Oi, get away from there!", "Don't enter my rat pen unless I say so!")
        exec { player, _ -> sendStageDialog(player) }
    }
}
