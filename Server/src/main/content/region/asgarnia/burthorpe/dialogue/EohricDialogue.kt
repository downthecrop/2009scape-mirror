package content.region.asgarnia.burthorpe.dialogue

import content.region.asgarnia.burthorpe.quest.deathplateau.DeathPlateau
import content.region.asgarnia.burthorpe.quest.deathplateau.EohricDialogueFile
import core.api.getQuestStage
import core.api.openDialogue
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

/**
 * Eohric main dialogue.
 * @author ovenbread
 */
@Initializable
class EohricDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        if (getQuestStage(player!!, DeathPlateau.questName) >= 5) {
            // Call the dialogue file for Eohric from the deathplateau quest folder.
            openDialogue(player!!, EohricDialogueFile(), npc)
            return true
        }
        // Fallback to default.
        when (stage) {
            START_DIALOGUE -> playerl(FacialExpression.HALF_GUILTY, "Hi!").also { stage++ }
            1 -> npc(FacialExpression.ASKING, "Hello. Can I help?").also { stage++ }
            2 -> options("What is this place?", "That's quite an outfit.", "Goodbye.").also { stage++ }
            3 -> when (buttonId) {
                1 -> npcl(FacialExpression.FRIENDLY, "This is Burthorpe Castle, home to His Royal Highness Prince Anlaf, heir to the throne of Asgarnia.").also { stage = 10 }
                2 -> npcl(FacialExpression.HAPPY, "Why, thank you. I designed it myself. I've always found purple such a cheerful colour!").also { stage = 2 }
                3 -> player(FacialExpression.FRIENDLY, "Goodbye.").also { stage = END_DIALOGUE }
            }
            10 -> npc(FacialExpression.FRIENDLY, "No doubt you're impressed.").also { stage++ }
            11 -> options("Where is the prince?", "Goodbye.").also { stage++ }
            12 -> when (buttonId) {
                1 -> npcl(FacialExpression.SUSPICIOUS, "I cannot disclose the prince's exact whereabouts for fear of compromising his personal safety.").also { stage = 20 }
                2 -> player(FacialExpression.FRIENDLY, "Goodbye.").also { stage = END_DIALOGUE }
            }
            20 -> npcl(FacialExpression.FRIENDLY, "But rest assured that he is working tirelessly to maintain the safety and wellbeing of Burthorpe's people.").also { stage = 2 }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return EohricDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.EOHRIC_1080)
    }
}

