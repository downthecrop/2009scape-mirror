package content.region.kandarin.seers.quest.elementalworkshop2

import content.data.Quests
import content.global.handlers.iface.*
import core.game.interaction.InteractionListener
import core.api.*
import core.game.dialogue.DialogueLabeller
import core.game.interaction.IntType
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs09.consts.Items

/**
 * Starts the Elemental Workshop II Quest
 * Found at Dig Site Library
 */

class BeatenBook : InteractionListener {
    companion object {
        private const val TITLE = "Book of the Elemental Helm"
        private val CONTENTS = arrayOf(
            // pages 1-2
            PageSet(
                Page(
                    BookLine("This is the second book in", 55),
                    BookLine("a series which aims to", 56),
                    BookLine("preserve the knowledge", 57),
                    BookLine("gained by artisans of the", 58),
                    BookLine("early Fifth Age. A", 59),
                    BookLine("thorough grasp of the", 60),
                    BookLine("processes and techniques", 61),
                    BookLine("described in my earlier", 62),
                    BookLine("work, 'Book of the", 63),
                    BookLine("Elemental Shield', is", 64),
                    BookLine("necessary to comprehend", 65),
                ),
                Page(
                    BookLine("the concepts involved in", 66),
                    BookLine("the more technical", 67),
                    BookLine("chapters of this book.", 68),
                    BookLine("", 69),
                    BookLine("", 70),
                    BookLine("", 71),
                    BookLine("", 72),
                    BookLine("", 73),
                    BookLine("", 74),
                    BookLine("", 75),
                    BookLine("", 76),
                )
            ),
            // pages 3-4
            PageSet(
                Page(
                    BookLine("Some years following the", 55),
                    BookLine("original discovery of the", 56),
                    BookLine("elemental ore, further", 57),
                    BookLine("investigation was", 58),
                    BookLine("performed by a", 59),
                    BookLine("metallurgist called", 60),
                    BookLine("Vitruvius in collaboration", 61),
                    BookLine("with a freelance mage.", 62),
                    BookLine("Over the course of two", 63),
                    BookLine("years they did many", 64),
                    BookLine("tests on the ore and", 65),
                ),
                Page(
                    BookLine("refined metal, aiming to", 66),
                    BookLine("optimise the process by", 67),
                    BookLine("which the metal was", 68),
                    BookLine("extracted from its impure", 69),
                    BookLine("ore. From the data they", 70),
                    BookLine("gathered, they discovered", 71),
                    BookLine("the optimum temperature", 72),
                    BookLine("and carbon/ore ratio for", 73),
                    BookLine("the production of", 74),
                    BookLine("elemental metal of the", 75),
                    BookLine("highest quality. The", 76),
                )
            ),
            // pages 5-6
            PageSet(
                Page(
                    BookLine("quality of the resulting", 55),
                    BookLine("metal was assessed on the", 56),
                    BookLine("basis of its ultimate tensile", 57),
                    BookLine("strength, hardness (as", 58),
                    BookLine("compared to a standard", 59),
                    BookLine("diamond-tipped probe),", 60),
                    BookLine("magical absorption", 61),
                    BookLine("capacity per unit mass,", 62),
                    BookLine("grain size and elastic", 63),
                    BookLine("deformation modulus.", 64),
                    BookLine("Their most significant", 65),
                ),
                Page(
                    BookLine("discovery in this period,", 66),
                    BookLine("however, was the novel", 67),
                    BookLine("technique of 'priming' the", 68),
                    BookLine("ore.", 69),
                    BookLine("", 70),
                    BookLine("", 71),
                    BookLine("", 72),
                    BookLine("", 73),
                    BookLine("", 74),
                    BookLine("", 75),
                    BookLine("", 76),
                )
            ),
            // pages 7-8
            PageSet(
                Page(
                    BookLine("At the time, the", 55),
                    BookLine("significance of priming", 56),
                    BookLine("was not fully appreciated.", 57),
                    BookLine("However, the records kept", 58),
                    BookLine("by Vitruvius state that his", 59),
                    BookLine("apprentice, in an", 60),
                    BookLine("unsuccessful attempt to", 61),
                    BookLine("animate a small ingot of", 62),
                    BookLine("elemental metal as part of", 63),
                    BookLine("a student prank,", 64),
                    BookLine("accidentally instilled his", 65),
                ),
                Page(
                    BookLine("entire consciousness into", 66),
                    BookLine("the primed ingot. An", 67),
                    BookLine("unfortunate side-effect of", 68),
                    BookLine("this process was to", 69),
                    BookLine("reduce the apprentice's", 70),
                    BookLine("body to a dead shell.", 71),
                    BookLine("Attempts to return his", 72),
                    BookLine("consciousness to his body", 73),
                    BookLine("were regrettably", 74),
                    BookLine("unsuccessful.", 75),
                    BookLine("", 76),
                )
            ),
            // pages 9-10
            PageSet(
                Page(
                    BookLine("Vitruvius discovered that", 55),
                    BookLine("the mental energy was so", 56),
                    BookLine("firmly bound to the", 57),
                    BookLine("elemental metal that no", 58),
                    BookLine("dissipation was detectable.", 59),
                    BookLine("Similar experiments with", 60),
                    BookLine("unprimed bars initially", 61),
                    BookLine("yielded comparable", 62),
                    BookLine("results, but the mental", 63),
                    BookLine("energy quickly left the", 64),
                    BookLine("bars and dissipated within", 65),
                ),
                Page(
                    BookLine("such a short period that", 66),
                    BookLine("no practical applications", 67),
                    BookLine("seemed feasible. However", 68),
                    BookLine("the primed elemental", 69),
                    BookLine("metal showed considerably", 70),
                    BookLine("more promise.", 71),
                    BookLine("", 72),
                    BookLine("", 73),
                    BookLine("", 74),
                    BookLine("", 75),
                    BookLine("", 76),
                )
            ),
            // pages 11-12
            PageSet(
                Page(
                    BookLine("Researchers have", 55),
                    BookLine("continued to use the", 56),
                    BookLine("terminology defined by", 57),
                    BookLine("Vitruvius for this field of", 58),
                    BookLine("study. In his own", 59),
                    BookLine("notebooks, Vitruvius", 60),
                    BookLine("referred to his", 61),
                    BookLine("preparation technique as", 62),
                    BookLine("'priming', and he referred", 63),
                    BookLine("to his apprentice as 'Idiot'.", 64),
                    BookLine("", 65),
                ),
                Page(
                    BookLine("", 66),
                    BookLine("", 67),
                    BookLine("", 68),
                    BookLine("", 69),
                    BookLine("", 70),
                    BookLine("", 71),
                    BookLine("", 72),
                    BookLine("", 73),
                    BookLine("", 74),
                    BookLine("", 75),
                    BookLine("", 76),
                )
            ),
        )
    }

    private fun display(player: Player, pageNum: Int, buttonID: Int): Boolean {
        BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
        return true
    }

    override fun defineListeners() {
        on(Items.BEATEN_BOOK_9717, IntType.ITEM, "read") { player, node ->

            // check the player is not already reading the book
            if (!player.interfaceManager.isOpened) {
                // increment quest stage if needed
                if (getQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II) == 1) {
                    setQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II, 2)
                }

                storeBookInHouse(player, node)

                // if the player doesn't have the scroll, drop it
                if (hasAnItem(player, Items.SCROLL_9721).container == null) {
                    sendMessage(player, "The book has two parts; an introduction and an instruction section.")
                    sendMessage(player, "You flip the book open to the introduction and start reading.")
                    addItemOrDrop(player, Items.SCROLL_9721) // scroll needs to be added here because otherwise in the afterClose block it'll give you 3 lmao
                    openDialogue(player, object : DialogueLabeller() {
                        override fun addConversation() {
                            item(Item(Items.SCROLL_9721), "You find a slip of paper that has been used", "for a bookmark. You put it in your pack to", "study later.")
                            afterClose { player ->
                                BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
                            }
                        }
                    })

                    // if player has scroll, open book
                } else {
                    sendMessage(player, "The book has two parts; an introduction and an instruction section.")
                    sendMessage(player, "You flip the book open to the introduction and start reading.")
                    BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
                }
            } else sendMessage(player, "Please finish what you're doing first.") // this is the same message/logic used for clue scrolls

            return@on true
        }
    }
}