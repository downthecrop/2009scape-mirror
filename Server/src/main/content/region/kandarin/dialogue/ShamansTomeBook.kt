package content.region.kandarin.dialogue

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.api.setAttribute
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items

@Initializable
class ShamansTomeBook : InteractionListener {
    companion object {
        private val TITLE = "Shaman's Tome"
        private val CONTENTS = arrayOf(
            PageSet(
                Page(
                    BookLine("You read the ancient", 55),
                    BookLine("shaman's tome.", 56),
                    BookLine("It is written in a strange sort", 57),
                    BookLine("of language but you manage", 58),
                    BookLine("a rough translation.", 59),
                    BookLine("", 60),
                    BookLine("...scattered are my hopes that", 61),
                    BookLine("I will ever be released from", 62),
                    BookLine("this flaming Octagram, it is", 63),
                    BookLine("the only thing which will", 64),
                    BookLine("contain this beast within.", 65),
                ),
                Page(
                    BookLine("Although its grip over me is", 66),
                    BookLine("weakened with magic, it is", 67),
                    BookLine("hopeless to know if a", 68),
                    BookLine("saviour would guess this.", 69),
                    BookLine("I am doomed...", 70),
                )
            ),
        )
    }

    private fun display(player: Player, pageNum: Int, buttonID: Int): Boolean {
        BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
        return true
    }

    override fun defineListeners() {
        on(Items.SHAMANS_TOME_729, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}