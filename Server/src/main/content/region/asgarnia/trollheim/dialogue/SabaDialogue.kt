package content.region.asgarnia.trollheim.dialogue

import content.region.asgarnia.burthorpe.quest.deathplateau.DeathPlateau
import content.region.asgarnia.burthorpe.quest.deathplateau.SabaDialogueFile
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
 * Represents the dialogue plugin used for the tenzing npc.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
class SabaDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        if (getQuestStage(player!!, DeathPlateau.questName) >= 19) {
            openDialogue(player!!, SabaDialogueFile(), npc)
            return true
        }
        // Fallback to default.
        when (stage) {
            START_DIALOGUE -> playerl(FacialExpression.HALF_GUILTY, "Hi!").also { stage++ }
            1 -> npcl(FacialExpression.ANNOYED, "Why won't people leave me alone?!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return SabaDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SABA_1070)
    }
}