package content.region.misthalin.draynor.dialogue

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.api.setAttribute
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs09.consts.Items

class FolkloreBook : InteractionListener {

    // Book found on the bookshelves in the Wise Old Man's house in Draynor Village.

    companion object {
        private val TITLE = "The Myth of the Elder-dragons"
        private val CONTENTS = arrayOf(
            PageSet(
                Page(
                    BookLine("Although the Elder-dragons", 55),
                    BookLine("are generally considered", 56),
                    BookLine("to be a myth, stories tell", 57),
                    BookLine("of an ancient race that dwelt", 58),
                    BookLine("on Gielinor before any other.", 59),
                    BookLine("In some arcane way their", 60),
                    BookLine("lifeforce was linked to the", 61),
                    BookLine("life of the world itself;", 62),
                    BookLine("it is written that, as long", 63),
                    BookLine("as the Elder-dragons", 64),
                    BookLine("continue to live,", 65),
                ),
                Page(
                    BookLine("the world shall not end.", 66),
                    BookLine("However, sceptics claim", 67),
                    BookLine("that the myth of the", 68),
                    BookLine("Elder-dragons was invented", 69),
                    BookLine("to cast doubt on claims", 70),
                    BookLine("that this realm was lifeless", 71),
                    BookLine("until the gods first arrived.", 72),
                ),
            )
        )
    }

    private fun display(player: Player, pageNum: Int, buttonID: Int): Boolean {
        BookInterface.pageSetup(
            player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS
        )
        return true
    }

    override fun defineListeners() {
        on(Items.BOOK_OF_FOLKLORE_5508, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
