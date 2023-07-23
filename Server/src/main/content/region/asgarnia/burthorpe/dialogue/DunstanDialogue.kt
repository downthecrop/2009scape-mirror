package content.region.asgarnia.burthorpe.dialogue

import content.region.asgarnia.burthorpe.quest.deathplateau.DeathPlateau
import content.region.asgarnia.burthorpe.quest.deathplateau.DunstanDialogueFile
import core.api.isQuestInProgress
import core.api.openDialogue
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

/**
 * Dunstan main dialogue.
 * @author 'ovenbread
 *
 * https://www.youtube.com/watch?v=gbqcJp99Zd8
 * https://www.youtube.com/watch?v=ujtIALS1L7A
 */
@Initializable
class DunstanDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        if (isQuestInProgress(player!!, DeathPlateau.questName, 21, 24)) {
            // Call the dialogue file for Dunstan from the deathplateau quest folder.
            openDialogue(player!!, DunstanDialogueFile(), npc)
            return true
        }
        // Default
        when (stage) {
            START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "Hi!").also { stage++ }
            1 -> npcl(FacialExpression.FRIENDLY, "Hi! Did you want something??").also { stage++ }
            2 -> showTopics(
                    Topic(FacialExpression.THINKING, "Is it OK if I use your anvil?", 10),
                    Topic(FacialExpression.FRIENDLY, "Nothing, thanks.", END_DIALOGUE),
                    // More dialogues here (Sleds, Climbing Boots, Troll Stronghold)
            )
            10 -> npcl(FacialExpression.FRIENDLY, "So you're a smithy are you?").also { stage++ }
            11 -> playerl(FacialExpression.FRIENDLY, "I dabble.").also { stage++ }
            12 -> npcl(FacialExpression.FRIENDLY, "A fellow smith is welcome to use my anvil!").also { stage++ }
            13 -> playerl(FacialExpression.FRIENDLY, "Thanks!").also { stage++ }
            14 -> npcl(FacialExpression.FRIENDLY, "Anything else before I get on with my work?").also { stage = 2 }
        }
        return true
    }

    override fun newInstance(player: Player): DialoguePlugin {
        return DunstanDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DUNSTAN_1082)
    }
}
