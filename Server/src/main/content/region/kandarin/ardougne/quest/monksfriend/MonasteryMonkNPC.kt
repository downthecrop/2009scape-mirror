package content.region.kandarin.ardougne.quest.monksfriend

import core.game.node.entity.npc.NPC
import org.rs09.consts.NPCs
import core.game.dialogue.DialogueFile
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import core.tools.END_DIALOGUE

/**
* Handles MonasteryMonkDialogue Dialogue
* @author Kya
*/
class MonasteryMonkDialogue : DialogueFile() {
    override fun handle(interfaceId: Int, buttonId: Int) {
        var questStage = player!!.questRepository.getStage("Monk's Friend")
        if (questStage == 0){
            when(stage) {
                0 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL,"Peace brother.").also { stage = END_DIALOGUE }
            }
        } else if (questStage < 100) {
            when(stage) {
                0 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY,"*yawn*").also{stage = END_DIALOGUE }
            }
        } else {
            when(stage) {
                0 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"Can't wait for the party!").also{stage = END_DIALOGUE }
            }
        }
    }
}

/**
 * Handles BrotherCedricListener to launch the dialogue
 * @author Kya
 */
class MonasteryMonkListener : InteractionListener {
    override fun defineListeners() {
        on(NPCs.MONK_281, IntType.NPC, "talk-to"){ player, _ ->
            player.dialogueInterpreter.open(MonasteryMonkDialogue(), NPC(NPCs.MONK_281))
            return@on true
        }
    }
}
