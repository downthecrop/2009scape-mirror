package content.region.kandarin.seers.quest.elementalworkshop

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.api.setAttribute
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs09.consts.Items

/**
 * Gloughs Journal book handler for The Grand Tree quest
 *
 * @author crop & szu & ovenbreado
 */
class GloughsJournal : InteractionListener {
    companion object {
        private val TITLE = "Glough's Journal"
        private val CONTENTS = arrayOf(
            PageSet(
                Page(
                    BookLine("<col=FF2D00>The migration failed!", 55),
                    BookLine("After spending half", 56),
                    BookLine("a century hiding", 57),
                    BookLine("underground you", 58),
                    BookLine("you would think", 59),
                    BookLine("that the great", 60),
                    BookLine("migration would have", 61),
                    BookLine("improved life on", 62),
                    BookLine("Gielinor for tree gnomes.", 63),
                    BookLine("However, rather", 64),
                    BookLine("than the great liberation", 65),
                ),
                Page(
                    BookLine("promised to us by", 65),
                    BookLine("King Healthorg at the", 66),
                    BookLine("end of the last age,", 67),
                    BookLine("we have been forced", 68),
                    BookLine("to live in hiding,", 69),
                    BookLine("up trees or in the", 70),
                    BookLine("gnome maze, laughed", 71),
                    BookLine("at and mocked by man.", 72),
                    BookLine("Living in constant", 73),
                    BookLine("fear of human aggression", 74),
                    BookLine("we are in a no better", 75),
                )
            ),
            PageSet(
                Page(
                    BookLine("we are in a no better", 55),
                    BookLine("situation now than", 56),
                    BookLine("when we lived", 57),
                    BookLine("in the caves!", 58),
                    BookLine("Change must come soon!", 59),
                    BookLine("<col=FF2D00>They must be stopped!", 60),
                    BookLine("Today I heard of three", 61),
                    BookLine("more gnomes slain", 62),
                    BookLine("by Khazard's human", 63),
                    BookLine("troops for fun,", 64),
                    BookLine("I can't control", 65),
                ),
                Page(
                    BookLine("my anger!", 66),
                    BookLine("Humanity seems to", 67),
                    BookLine("have acquired a", 68),
                    BookLine("level of arrogance", 69),
                    BookLine("comparable to that", 70),
                    BookLine("of Zamorak, killing", 71),
                    BookLine("and pillaging at will!", 72),
                    BookLine("We are small and at", 73),
                    BookLine("heart not warriors", 74),
                    BookLine("but something must", 75),
                )
            ),
            PageSet(
                Page(
                    BookLine("be done! We will", 55),
                    BookLine("pick up arms and", 56),
                    BookLine("go forth into the", 57),
                    BookLine("human world! We will", 58),
                    BookLine("defend ourselves and", 59),
                    BookLine("we will pursue justice", 60),
                    BookLine("for all gnomes who", 61),
                    BookLine("fell at the hands", 62),
                    BookLine("of humans!", 63),
                    BookLine("<col=FF2D00>Gaining support.", 64),
                    BookLine("", 65),
                ),
                Page(
                    BookLine("Some of the local", 66),
                    BookLine("gnomes seem strangely", 67),
                    BookLine("deluded about humans,", 68),
                    BookLine("many actually believe", 69),
                    BookLine("that humans are not", 70),
                    BookLine("all naturally evil", 71),
                    BookLine("but instead vary", 72),
                    BookLine("from person to person.", 73),
                    BookLine("This sort of talk", 74),
                    BookLine("could be the end", 75),
                )
            ),
            PageSet(
                Page(
                    BookLine("for the tree gnomes", 55),
                    BookLine("and I must continue", 56),
                    BookLine("to convince my fellow", 57),
                    BookLine("gnome folk the cold", 58),
                    BookLine("truth about these", 59),
                    BookLine("human creatures!", 60),
                    BookLine("How they will not", 61),
                    BookLine("stop until all gnome", 62),
                    BookLine("life is destroyed!", 63),
                    BookLine("Unless we can ", 64),
                    BookLine("destroy them first!", 65),
                )
            )
        )

        private fun display(player: Player, pageNum: Int, buttonID: Int): Boolean {
            BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
            return true
        }
    }

    override fun defineListeners() {
        on(Items.GLOUGHS_JOURNAL_785, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}