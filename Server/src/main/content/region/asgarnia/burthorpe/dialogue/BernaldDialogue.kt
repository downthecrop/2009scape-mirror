package content.region.asgarnia.burthorpe.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

/**
 * Bernald Dialogue
 * @author ovenbread
 *
 * https://www.youtube.com/watch?v=4odb7qhBPfA
 * https://www.youtube.com/watch?v=nisJKOtqWIo 1:08:53
 */
@Initializable
class BernaldDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
//        // Garden of Tranquility has not been implemented.
//        if(hasRequirement(player!!, "Garden of Tranquility")) {
//            if (isQuestComplete(player!!, DeathPlateau.questName)) {
//                when (stage) {
//                    0 -> playerl(FacialExpression.FRIENDLY, "How are your grapes coming along?").also { stage++ }
//                    1 -> npc(FacialExpression.FRIENDLY, "Marvellous, thanks to your help, " + player.username + "!").also { stage = END_DIALOGUE }
//                }
//            }
//        }
        when (stage) {
            START_DIALOGUE -> npcl(FacialExpression.WORRIED, "Do you know anything about grapevine diseases?").also { stage++ }
            1 -> playerl(FacialExpression.FRIENDLY, "No, I'm afraid I don't.").also { stage++ }
            2 -> npcl(FacialExpression.GUILTY, "Oh, that's a shame. I hope I find someone soon, otherwise I could lose all of this year's crop.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): DialoguePlugin {
        return BernaldDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BERNALD_2580)
    }
}
