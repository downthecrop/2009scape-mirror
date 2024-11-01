package content.region.kandarin.ardougne.dialogue

import core.api.addItem
import core.api.getDynLevel
import core.api.hasAnItem
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class PenguinKeeperDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PENGUIN_KEEPER_6891)
    }

    companion object{
        const val YES = 20
        const val NO = 40
        const val FULL = 50
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> playerl(FacialExpression.FRIENDLY, "Hello there. How are the penguins doing today?").also { stage++ }
            1 -> npcl(FacialExpression.FRIENDLY, "They are doing fine, thanks.").also{
                if (getDynLevel(player, Skills.SUMMONING) < 30 || hasAnItem(player, Items.PENGUIN_EGG_12483).exists())
                    stage = END_DIALOGUE
                else
                    stage++
            }
            2 -> npcl(FacialExpression.HALF_ASKING,"Actually, you might be able to help me with something - if you are interested.").also { stage++ }
            3 -> playerl(FacialExpression.ASKING, "What do you mean?").also { stage++ }
            4 -> npcl(FacialExpression.NEUTRAL, "Well, you see, the penguins have been laying so many eggs recently that we can't afford to raise them all ourselves.").also { stage++ }
            5 -> npcl(FacialExpression.ASKING, " You seem to know a bit about raising animals - would you like to raise a penguin for us? ").also { stage++ }
            6 -> showTopics(
                Topic("Yes, of course.", YES),
                Topic("No thanks.", NO)
            )

            YES -> npcl(FacialExpression.HAPPY, "Wonderful!").also {
                if (addItem(player, Items.PENGUIN_EGG_12483))
                    stage++
                else
                    stage = FULL
            }
            YES + 1 -> npcl(FacialExpression.HAPPY, "Here you go - this egg will hatch into a baby penguin.").also { stage++ }
            YES + 2 -> npcl(FacialExpression.HAPPY, "They eat raw fish and aren't particularly fussy about anything, so it won't be any trouble to raise.").also { stage++ }
            YES + 3 -> playerl(FacialExpression.HAPPY, "Okay, thank you very much.").also { stage = END_DIALOGUE }

            NO -> npcl(FacialExpression.NEUTRAL, " Fair enough. The offer still stands if you change your mind. ").also { stage = END_DIALOGUE }

            FULL -> npcl(FacialExpression.SAD, "You don't have inventory space though.").also { stage = END_DIALOGUE }
        }
        return true
    }
}