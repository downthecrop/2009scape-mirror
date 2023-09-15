package content.region.kandarin.pisc.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class FranklinCaranosDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> npcl(
                FacialExpression.FRIENDLY,
                "Hello again, " + player.username + "."
            ).also { stage++ }

            1 -> playerl(
                FacialExpression.ASKING,
                "Hello. How's the repair work going?"
            ).also { stage++ }

            2 -> npcl(
                FacialExpression.NEUTRAL,
                "I'm working on it. I can always do with more iron sheets, so if you've got any more, I'll give you 20 gp per sheet."
            ).also { stage++ }

            3 -> playerl(
                FacialExpression.NEUTRAL,
                "Thanks, I'll remember that."
            ).also { stage = END_DIALOGUE }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf((NPCs.FRANKLIN_CARANOS_3823))
}