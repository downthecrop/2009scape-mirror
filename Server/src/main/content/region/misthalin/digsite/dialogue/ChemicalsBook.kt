package content.region.misthalin.digsite.dialogue

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.api.setAttribute
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs09.consts.Items

class ChemicalsBook : InteractionListener {
    companion object {
        private val TITLE = "Volatile chemicals Experimental Test Notes"
        private val CONTENTS = arrayOf(
            PageSet(
                Page(
                    BookLine("<col=0000FF>Volatile Chemicals:", 55),
                    BookLine("Experimental Test Notes.", 56),
                    BookLine("In order to ease the mining", 58),
                    BookLine("process, my colleagues and", 59),
                    BookLine("I decided we needed something", 60),
                    BookLine("stronger than picks to delve", 61),
                    BookLine("under the surface of the site.", 62),
                    BookLine("As I already had an", 64),
                    BookLine("intermediate knowledge", 65),
                ),
                Page(
                    BookLine("of Herblore, I experimented", 66),
                    BookLine("with certain chemicals and", 67),
                    BookLine("invented a compound of", 68),
                    BookLine("tremendous power that,", 69),
                    BookLine("if subjected to a spark,", 70),
                    BookLine("would literally explode.", 71),
                    BookLine("We used vials of this", 73),
                    BookLine("compound with great effect,", 74),
                    BookLine("as it enabled us to reach", 75),
                    BookLine("further than ever before.", 76),
                )
            ),
            PageSet(
                Page(
                    BookLine("Here is what I have left of", 55),
                    BookLine("the compound's recipe:", 56),
                    BookLine("1 measure of ammonium", 59),
                    BookLine("nitrate powder;", 60),
                    BookLine("1 measure of nitroglycerine;", 62),
                    BookLine("1 measure of ground charcoal;", 64),
                ),
                Page(
                    BookLine("1 measure of ?", 66),
                    BookLine("Unfortunately the last", 68),
                    BookLine("ingredient was not noted", 69),
                    BookLine("down, but we understand", 70),
                    BookLine("that a certain root grows", 71),
                    BookLine("around these parts that", 72),
                    BookLine("was used to very good", 73),
                    BookLine("effect...", 73),
                )
            ),
        )
    }

    private fun display(player: Player, pageNum: Int, buttonID: Int): Boolean {
        BookInterface.pageSetup(
            player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS
        )
        return true
    }

    override fun defineListeners() {
        on(Items.BOOK_ON_CHEMICALS_711, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
