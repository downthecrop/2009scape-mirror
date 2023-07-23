package content.region.asgarnia.burthorpe.dialogue

import core.api.openNpcShop
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

/**
 * Handles the TostigDialogue dialogue.
 * @author 'Vexia
 */
@Initializable
class TostigDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> playerl(FacialExpression.FRIENDLY, "Hi, what ales are you serving?").also { stage++ }
            1 -> npcl(FacialExpression.FRIENDLY, "Well ${if (player.isMale) "mister" else "miss"}, our speciality is Asgarnian Ale, we also serve Wizard's Mind Bomb and Dwarven Stout.").also { stage++ }
            2 -> npcl(FacialExpression.FRIENDLY, "Would you like to buy a drink?").also { stage++ }
            3 -> showTopics(
                Topic(FacialExpression.FRIENDLY, "Yes, please.", 10),
                Topic(FacialExpression.FRIENDLY, "No, thanks.", 20),
            )
            10 -> openNpcShop(player, npc.id).also {
                end()
                stage = END_DIALOGUE
            }
            20 -> npcl(FacialExpression.FRIENDLY, "Ah well... so um... does the grey squirrel sing in the grove?").also { stage++ }
            21 -> showTopics(
                Topic(FacialExpression.FRIENDLY, "Huh?", 30),
                Topic(FacialExpression.FRIENDLY, "Yes, and the black cat dances in the sink.", 40),
                Topic(FacialExpression.FRIENDLY, "No, squirrels can't sing.", 50),
            )
            30 -> npcl(FacialExpression.FRIENDLY, "Er... nevermind.").also { stage = END_DIALOGUE }
            40 -> npcl(FacialExpression.FRIENDLY, "Ah you'll be wanting the trapdoor behind the bar.").also { stage = END_DIALOGUE }
            50 -> npcl(FacialExpression.FRIENDLY, "No... of course they can't...").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TOSTIG_1079)
    }
}
