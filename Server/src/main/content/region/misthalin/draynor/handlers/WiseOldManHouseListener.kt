package content.region.misthalin.draynor.handlers

import core.api.addItem
import core.api.inInventory
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs09.consts.Items
import org.rs09.consts.Scenery

class WiseOldManHouseListener : InteractionListener {

    // Searching the bookshelves in the Wise Old Man's house

    override fun defineListeners() {

        // All shelves in the room
        val bookshelves = intArrayOf(
            Scenery.OLD_BOOKSHELF_7058, Scenery.OLD_BOOKSHELF_7071,
            Scenery.OLD_BOOKSHELF_7072, Scenery.OLD_BOOKSHELF_7073,
            Scenery.OLD_BOOKSHELF_7074, Scenery.OLD_BOOKSHELF_7076,
            Scenery.OLD_BOOKSHELF_7077, Scenery.OLD_BOOKSHELF_7078,
            Scenery.OLD_BOOKSHELF_7079, Scenery.OLD_BOOKSHELF_7081,
            Scenery.OLD_BOOKSHELF_7082, Scenery.OLD_BOOKSHELF_7089,
            Scenery.OLD_BOOKSHELF_9146, Scenery.OLD_BOOKSHELF_9147,
        )

        // Shelves contains unique books
        val containBookshelf = intArrayOf(
            Scenery.OLD_BOOKSHELF_7065, // STRANGE BOOK
            Scenery.OLD_BOOKSHELF_7066, // FOLKLORE BOOK
            Scenery.OLD_BOOKSHELF_7068, // CHICKEN BOOK
        )

        val strangeBook = Items.STRANGE_BOOK_5507
        val folkloreBook = Items.BOOK_OF_FOLKLORE_5508
        val chickenBook = Items.BOOK_ON_CHICKENS_7464


        on(Scenery.OLD_BOOKSHELF_7065, IntType.SCENERY, "search") { player, _ ->
            if (!inInventory(player, strangeBook)) {
                sendMessage(player, "You search the bookcase and find a book named 'Strange Book'.")
                addItem(player, strangeBook)
            } else {
                sendMessage(player, "You search the bookcase and find nothing of interest.")
            }
            return@on true
        }

        on(Scenery.OLD_BOOKSHELF_7066, IntType.SCENERY, "search") { player, _ ->
            if (!inInventory(player, folkloreBook)) {
                sendMessage(player, "You search the bookcase and find a book named 'Book of folklore'.")
                addItem(player, folkloreBook)
            } else {
                sendMessage(player, "You search the bookcase and find nothing of interest.")
            }
            return@on true
        }

        on(Scenery.OLD_BOOKSHELF_7068, IntType.SCENERY, "search") { player, _ ->
            if (!inInventory(player, chickenBook)) {
                sendMessage(player, "You search the bookcase and find a book named 'Book on chickens'.")
                addItem(player, chickenBook)
            } else {
                sendMessage(player, "You search the bookcase and find nothing of interest.")
            }
            return@on true
        }

    }
}