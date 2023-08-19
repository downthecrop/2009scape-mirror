package content.region.kandarin.seers.quest.elementalworkshop

import content.global.handlers.iface.BookInterface
import core.api.setAttribute
import core.api.setQuestStage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs09.consts.Items

/**
 * Battered book handler for the Elemental Workshop I quest
 *
 *  @author Woah, with love
 */
class BatteredBookHandler : InteractionListener {

    companion object {
        private val TITLE = "Book of the elemental shield"
        private val CONTENTS = EWUtils.PAGES

        private fun display(player:Player, pageNum: Int, buttonID: Int) : Boolean {
            BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
            if (BookInterface.isLastPage(pageNum, CONTENTS.size)) {
                if (EWUtils.currentStage(player) == 0) {
                    setQuestStage(player, "Elemental Workshop I", 1)
                }
            }
            return true
        }
    }

    override fun defineListeners() {
        on(Items.BATTERED_BOOK_2886, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}