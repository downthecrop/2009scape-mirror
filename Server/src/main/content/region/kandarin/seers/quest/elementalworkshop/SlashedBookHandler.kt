package content.region.kandarin.seers.quest.elementalworkshop

import content.global.handlers.iface.BookInterface
import core.api.setAttribute
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs09.consts.Items

/**
 * Slashed book handler for the Elemental Workshop I quest
 *
 *  @author Woah, with love
 */
class SlashedBookHandler : InteractionListener {
    companion object {
        private val TITLE = "Book of the elemental shield"
        private val CONTENTS = EWUtils.PAGES;

        private fun display(player:Player, pageNum: Int, buttonID: Int) : Boolean {
            BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
            return true
        }
    }

    override fun defineListeners() {
        on(Items.SLASHED_BOOK_9715, IntType.ITEM, "read") { player, _ ->
            setAttribute(player, "bookInterfaceCallback", ::display)
            setAttribute(player, "bookInterfaceCurrentPage", 0)
            display(player, 0, 0)
            return@on true
        }
    }
}