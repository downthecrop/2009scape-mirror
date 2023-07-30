package content.region.kandarin.quest.dwarfcannon

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
class NulodionsNotes : InteractionListener {
    companion object {
        private val TITLE = "Nulodion's notes"
        private val CONTENTS = arrayOf(
            PageSet(
                Page(
                    BookLine("Ammo for the Dwarf Multi", 55),
                    BookLine("Cannon must be made from", 56),
                    BookLine("steel bars. The bars must be", 57),
                    BookLine("heated in a furnace and used", 58),
                    BookLine("with the ammo mould.", 59),
                )
            ),
        )
    }

    private fun display(player: Player, pageNum: Int, buttonID: Int): Boolean {
        BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
        return true
    }

    override fun defineListeners() {
        on(Items.NULODIONS_NOTES_3, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}