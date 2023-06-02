package content.region.morytania.burghderott.dialogue

import core.api.openDialogue
import core.game.dialogue.DialogueFile
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.tools.END_DIALOGUE
import org.rs09.consts.Items


class DustyScroll : InteractionListener {
    // Receive during the In Aid of the Myreque quest.
    companion object {
        const val DUSTY_SCROLL = Items.DUSTY_SCROLL_7629
        class DustyScrollContent : DialogueFile() {
            override fun handle(componentID: Int, buttonID: Int) {
                when (stage) {
                    0 -> player!!.dialogueInterpreter.sendItemMessage(DUSTY_SCROLL, "", "You take the scroll and gingerly unravel it. It seems", "very old and the language is not easy to make out.", "You begin to see that it looks like a letter of some kind.").also { stage++ }
                    1 -> player!!.dialogueInterpreter.sendItemMessage(DUSTY_SCROLL, "My dearest Lantania,", "I know not what these dark hours bring. I will come for", "thee as soon as I am able, I care no longer for the", "possessions which tie me to this wasted land and the").also { stage++ }
                    2 -> player!!.dialogueInterpreter.sendItemMessage(DUSTY_SCROLL, "living which I earned from it.", "Prepare for leaving soon, I shall be there with haste,", "and with Saradomin's blessings we shall make our way out of Hallowvale westwards over the Salve towards").also { stage++ }
                    3 -> player!!.dialogueInterpreter.sendItemMessage(DUSTY_SCROLL, "Misthalin and hopefully into the arms of our brethren.", "I also offer up a prayer to Saradomin that he may help", "Queen Efaritay in her hour of need and somehow fight", "these beasts back to the dead place from where they").also { stage++ }
                    4 -> player!!.dialogueInterpreter.sendItemMessage(DUSTY_SCROLL, "came.", "I cry at the plight of poor Ascertes and offer up a", "prayer for their children in this darkest of hours.", "So make ready your plans and so soon as you see me,").also { stage++ }
                    5 -> player!!.dialogueInterpreter.sendItemMessage(DUSTY_SCROLL, "so we shall set for Misthalin, bring only what you need.", "We shall need all good fortune and speed in this, our", "last chance at a life together.", "Yours, most faithfully,").also { stage++ }
                    6 -> player!!.dialogueInterpreter.sendItemMessage(DUSTY_SCROLL, "", "Sialathin").also { stage = END_DIALOGUE }
                }
            }
        }
    }

    override fun defineListeners() {
        on(DUSTY_SCROLL, IntType.ITEM, "read"){ player, _ ->
            openDialogue(player, DustyScrollContent())
            return@on true
        }
    }
}