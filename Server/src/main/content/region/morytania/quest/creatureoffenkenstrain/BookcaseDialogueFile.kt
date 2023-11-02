package content.region.morytania.quest.creatureoffenkenstrain

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.node.entity.player.Player
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.Items

class BookcaseWestDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            START_DIALOGUE -> sendDialogueOptions(player!!, "Which book would you like to read?", "1001 Ways To Eat Fried Gizzards", "Practical Gardening For The Headless", "Human Taxidermy for Nincompoops", "The Joy of Gravedigging").also {
                setComponentVisibility(player!!, 232, 9, false)
                setComponentVisibility(player!!, 232, 8, true)
                stage++
            }
            1 -> when (buttonID) {
                1 -> sendDialogue(player!!, "This book leaves you contemplating vegetarianism.").also { stage = END_DIALOGUE }
                2 -> sendDialogue(player!!, "This book has some very enlightening points to make, but you are at a loss to know how anyone without a head could possibly read it.").also { stage = END_DIALOGUE }
                3 -> sendDialogue(player!!, "This book seems to have been read hundreds of times, and has scribbles and formulae on every page. One such scribble says None good enough - have to lock them in the caverns...").also { stage = END_DIALOGUE }
                4 -> sendDialogue(player!!, "As you pull the book a hidden latch springs into place, and the bookcase swings open, revealing a secret compartment.").also { stage++ }
            }
            2 -> sendItemDialogue(player!!, Items.MARBLE_AMULET_4187, "You find a marble amulet in the secret compartment.").also {
                addItemOrDrop(player!!, Items.MARBLE_AMULET_4187, 1)
                stage = END_DIALOGUE
            }
        }
    }
}

class BookcaseEastDialogueFile : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        val opposingGender = if (player!!.isMale) "female" else "male"
        when (stage) {
            START_DIALOGUE -> sendDialogueOptions(player!!, "Which book would you like to read?", "Men are from Morytania, Women are from Lumbridge", "Chimney Sweeping on a Budget", "Handy Maggot Avoidance Techniques", "My Family and Other Zombies").also { stage++ }
            1 -> when (buttonID) {
                1 -> sendDialogue(player!!, "You discover some fascinating insights into the mind of the $opposingGender kind.").also { stage = END_DIALOGUE }
                2 -> ChimneySweepingOnABudgetBook.display(player!!, 0, 0).also { end() }
                3 -> sendDialogue(player!!, "As you pull the book a hidden latch springs into place, and the bookcase swings open, revealing a secret compartment.").also { stage++ }
                4 -> sendDialogue(player!!, "The book is appallingly dull.").also { stage = END_DIALOGUE }
            }
            2 -> {
                if (getQuestStage(player!!, CreatureOfFenkenstrain.questName) == 2) {
                    sendItemDialogue(player!!, Items.OBSIDIAN_AMULET_4188, "You find an obsidian amulet in the secret compartment.").also {
                        addItemOrDrop(player!!, Items.OBSIDIAN_AMULET_4188, 1)
                        stage = END_DIALOGUE
                    }
                } else {
                    sendDialogue(player!!, "The secret compartment is empty.")
                }
            }
        }
    }
}


/**
 * Chimney Sweeping on a Budget Book
 * @author ovenbreado
 */
class ChimneySweepingOnABudgetBook {
    companion object {

        private val TITLE = "Chimney Sweeping on a Budget "
        val CONTENTS = arrayOf(
                PageSet(
                        Page(
                                BookLine("Page 26", 55),
                                BookLine("that sometimes a sweep", 56),
                                BookLine("may find themselves", 57),
                                BookLine("brushless and without the", 58),
                                BookLine("funds to purchase the", 59),
                                BookLine("one tool that is most", 60),
                                BookLine("essential to their trade.", 61),
                                BookLine("What is a chimney sweep", 62),
                                BookLine("without his or her brush?", 63),
                                BookLine("In this kind of situation", 64),
                                BookLine("any normal long-handled", 65),
                        ),
                        Page(
                                BookLine("brush might be a suitable", 66),
                                BookLine("replacement, although", 67),
                                BookLine("when attaching extensions", 68),
                                BookLine("to the handle make sure", 69),
                                BookLine("to use something sturdy", 70),
                                BookLine("like wire, otherwise a", 71),
                                BookLine("sweep may find", 72),
                                BookLine("themselves losing their", 73),
                                BookLine("brush and livelyhood to", 74),
                                BookLine("the forces of gravity", 75),
                        )
                ),
        )
        fun display(player: Player, pageNum: Int, buttonID: Int) : Boolean {
            BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
            return true
        }
    }
}