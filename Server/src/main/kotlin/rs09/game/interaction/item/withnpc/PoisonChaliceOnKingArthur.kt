package rs09.game.interaction.item.withnpc

import core.game.content.dialogue.FacialExpression
import core.game.node.item.Item
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.DialogueFile
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType
import rs09.tools.END_DIALOGUE

class PoisonChaliceOnKingArthur : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.NPC, Items.POISON_CHALICE_197, NPCs.KING_ARTHUR_251) { player, _, with ->
            player.dialogueInterpreter.open(PoisonChaliceOnKingArthurDialogue(), with)
            return@onUseWith true
        }
    }
}

class PoisonChaliceOnKingArthurDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> npcl(
                FacialExpression.SAD,
                "You have chosen poorly."
            ).also { stage++ }

            1 -> playerl(
                FacialExpression.ANNOYED,
                "Excuse me?"
            ).also { stage++ }

            2 -> npcl(
                FacialExpression.FRIENDLY,
                "Sorry, I meant to say 'thank you'. Most refreshing."
            ).also { stage++ }

            3 -> playerl(
                FacialExpression.DISGUSTED_HEAD_SHAKE,
                "Are you sure that stuff is safe to drink?"
            ).also { stage++ }

            4 -> npcl(
                FacialExpression.HAPPY,
                "Oh yes, Stankers' creations may be dangerous for those with weak constitutions, but, personally. I find them rather invigorating."
            ).also {
                player!!.inventory.remove(Item(197, 1))
                stage = END_DIALOGUE
            }
        }
    }
}