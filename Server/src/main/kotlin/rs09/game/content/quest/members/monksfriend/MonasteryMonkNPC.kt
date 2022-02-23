package rs09.game.content.quest.members.monksfriend

import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.DialogueFile
import rs09.game.interaction.InteractionListener
import rs09.tools.END_DIALOGUE

/**
* Handles MonasteryMonkDialogue Dialogue
* @author Kya
*/
class MonasteryMonkDialogue : DialogueFile() {
    override fun handle(interfaceId: Int, buttonId: Int) {
        var questStage = player!!.questRepository.getStage("Monk's Friend")
        if (questStage == 0){
            when(stage) {
                0 -> npcl(FacialExpression.NEUTRAL,"Peace brother.").also { stage = END_DIALOGUE }
            }
        } else if (questStage < 100) {
            when(stage) {
                0 -> npcl(FacialExpression.FRIENDLY,"*yawn*").also{stage = END_DIALOGUE}
            }
        } else {
            when(stage) {
                0 -> npcl(FacialExpression.HAPPY,"Can't wait for the party!").also{stage = END_DIALOGUE}
            }
        }
    }
}

/**
 * Handles BrotherCedricListener to launch the dialogue
 * @author Kya
 */
class MonasteryMonkListener : InteractionListener() {
    override fun defineListeners() {
        on(NPCs.MONK_281, NPC, "talk-to"){ player, _ ->
            player.dialogueInterpreter.open(MonasteryMonkDialogue(), NPC(NPCs.MONK_281))
            return@on true
        }
    }
}
