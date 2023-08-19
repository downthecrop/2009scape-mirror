package content.global.handlers.iface

import core.api.closeInterface
import core.api.getAttribute
import core.api.openInterface
import core.api.setAttribute
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player

/**
 * Interface listener for Books
 * Use this to listen and interact with buttons on book interfaces.
 *
 * This will handle component(26), component(27) and component(49) globally.
 * DO NOT extend this class or override on(26), on(27), on(49) for defineInterfaceListeners.
 *
 * Instead, simply call BookInterface.openBook(...) in the listener and set the following attributes:
 * bookInterfaceCallback - callback function for the page to display(player: Player, pageNum: Int, buttonID: Int) : Boolean
 * You must create that display() function so that can be passed in the callback and display contents of each page.
 *
 * You may at any time after pageSetup,
 * - call any functions below
 * - call player.packetDispatch.sendInterfaceConfig
 * - call player.packetDispatch.sendString
 *
 * @see content.region.desert.quest.thegolem.VarmensNotes for a quest-like example.
 * @see content.global.handlers.item.book.GeneralRuleBook for an interactive page-jumping book.
 *
 * @author ovenbreado
 */
class BookInterface : InterfaceListener {

    companion object {
        const val CALLBACK_ATTRIBUTE = "bookInterfaceCallback"
        const val CURRENT_PAGE_ATTRIBUTE = "bookInterfaceCurrentPage"

        /* These should be in org.rs09.consts.Components but currently are not. */
        const val FANCY_BOOK_26 = 26 // This is a 15-Lines per page book.
        const val FANCY_BOOK_2_27 = 27 // This is a 15-Lines per page book with index and row clickable listeners.
        const val FANCY_BOOK_3_49 = 49 // This is an 11-Lines per page book.

        /* Book Lines. [Title, Left button text, Right button text, ...lines 1 to X] */
        val FANCY_BOOK_26_LINE_IDS = arrayOf(101, 65, 66,  97, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81,  82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96)
        val FANCY_BOOK_2_27_LINE_IDS = arrayOf(163, 5, 6,  38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52,  53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67)
        val FANCY_BOOK_3_49_LINE_IDS = arrayOf(6, 77, 78,  55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65,  66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76)

        /* Button IDs. [Left button, Right button,  (opt)index button, ...(opt)click lines 1 to X] */
        val FANCY_BOOK_26_BUTTON_IDS = arrayOf(61, 63)
        val FANCY_BOOK_2_27_BUTTON_IDS = arrayOf(1, 3,  159,  100, 102, 104, 106, 108, 110, 112, 114, 116, 118, 120, 122, 124, 126, 128,  130, 132, 134, 136, 138, 140, 142, 144, 146, 148, 150, 152, 154, 156, 158)
        val FANCY_BOOK_3_49_BUTTON_IDS = arrayOf(51, 53)

        /* Image IDs. [...lines 1 to X] */
        val FANCY_BOOK_2_27_IMAGE_ENABLE_DRAW_IDS = arrayOf(9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31, 33, 35, 37,  69, 71, 73, 75, 77, 79, 81, 83, 85, 87, 89, 91, 93, 95, 97)
        val FANCY_BOOK_2_27_IMAGE_DRAW_IDS = arrayOf(10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 68,  70, 72, 74, 76, 78, 80, 82, 84, 86, 88, 90, 92, 94, 96, 98)

        /** Opens the book interface. Call this only once in the defineListeners() at the start of opening a book. */
        fun openBook(player: Player, bookComponent: Int, displayCallback: (player: Player, pageNum: Int, buttonId: Int) -> Boolean) {
            closeInterface(player) // Important: Closes the previous interface.
            setAttribute(player, CURRENT_PAGE_ATTRIBUTE, 0) // Resets the book to the first page.
            setAttribute(player, CALLBACK_ATTRIBUTE, displayCallback) // Sets the display callback
            if (bookComponent == FANCY_BOOK_26) {
                openInterface(player, FANCY_BOOK_26) // Important: Opens the current interface.
            } else if (bookComponent == FANCY_BOOK_2_27) {
                openInterface(player, FANCY_BOOK_2_27) // Important: Opens the current interface.
            } else if (bookComponent == FANCY_BOOK_3_49) {
                openInterface(player, FANCY_BOOK_3_49) // Important: Opens the current interface.
            }
            displayCallback.invoke(player, 0, 0)
        }

        /** Sets up title, pagination and content. Call this in the display callback every time the page changes. */
        fun pageSetup(player: Player, bookComponent: Int, title: String, contents: Array<PageSet>, hasPagination: Boolean = true) {
            val currentPage = getAttribute(player, CURRENT_PAGE_ATTRIBUTE, 0)
            if (bookComponent == FANCY_BOOK_26) {
                clearBookLines(player, FANCY_BOOK_26, FANCY_BOOK_26_LINE_IDS)
                clearButtons(player, FANCY_BOOK_26, FANCY_BOOK_26_BUTTON_IDS)
                setTitle(player, FANCY_BOOK_26, FANCY_BOOK_26_LINE_IDS, title)
                if (hasPagination) {
                    setPagination(player, FANCY_BOOK_26, FANCY_BOOK_26_LINE_IDS, FANCY_BOOK_26_BUTTON_IDS, currentPage, contents.size, contents[currentPage].pages.size == 1)
                }
                setPageContent(player, FANCY_BOOK_26, FANCY_BOOK_26_LINE_IDS, FANCY_BOOK_26_BUTTON_IDS, currentPage, contents)
            } else if (bookComponent == FANCY_BOOK_2_27) {
                clearBookLines(player, FANCY_BOOK_2_27, FANCY_BOOK_2_27_LINE_IDS)
                clearButtons(player, FANCY_BOOK_2_27, FANCY_BOOK_2_27_BUTTON_IDS)
                setTitle(player, FANCY_BOOK_2_27, FANCY_BOOK_2_27_LINE_IDS, title)
                if (hasPagination) {
                    setPagination(player, FANCY_BOOK_2_27, FANCY_BOOK_2_27_LINE_IDS, FANCY_BOOK_2_27_BUTTON_IDS, currentPage, contents.size, contents[currentPage].pages.size == 1)
                }
                setPageContent(player, FANCY_BOOK_2_27, FANCY_BOOK_2_27_LINE_IDS, FANCY_BOOK_2_27_BUTTON_IDS, currentPage, contents)
            } else if (bookComponent == FANCY_BOOK_3_49) {
                clearBookLines(player, FANCY_BOOK_3_49, FANCY_BOOK_3_49_LINE_IDS)
                clearButtons(player, FANCY_BOOK_3_49, FANCY_BOOK_3_49_BUTTON_IDS)
                setTitle(player, FANCY_BOOK_3_49, FANCY_BOOK_3_49_LINE_IDS, title)
                if (hasPagination) {
                    setPagination(player, FANCY_BOOK_3_49, FANCY_BOOK_3_49_LINE_IDS, FANCY_BOOK_3_49_BUTTON_IDS, currentPage, contents.size, contents[currentPage].pages.size == 1)
                }
                setPageContent(player, FANCY_BOOK_3_49, FANCY_BOOK_3_49_LINE_IDS, FANCY_BOOK_3_49_BUTTON_IDS, currentPage, contents)
            }
        }

        /** Clear all lines rather than leaving "Line X" on all visible rows. */
        fun clearBookLines(player: Player, componentId: Int, bookLineIds: Array<Int>) {
            openInterface(player, componentId) // Important: Opens the current interface.
            for (i in bookLineIds) {
                player.packetDispatch.sendString("", componentId, i)
            }
        }

        /** Clear all buttons rather than leaving invisible button rows. */
        fun clearButtons(player: Player, componentId: Int, bookButtonIds: Array<Int>) {
            for (i in bookButtonIds) {
                player.packetDispatch.sendInterfaceConfig(componentId, i, true)
            }
        }

        /** Set title of book. */
        fun setTitle(player: Player, componentId: Int, bookLineIds: Array<Int>, title: String) {
            player.packetDispatch.sendString(title, componentId, bookLineIds[0])
        }

        /** Set pagination numbers of current page in book. CurrentPage is 0 index. */
        fun setPagination(player: Player, componentId: Int, bookLineIds: Array<Int>, bookButtonIds: Array<Int>, currentPage: Int, totalPages: Int, hasRightPage: Boolean) {
            player.packetDispatch.sendInterfaceConfig(componentId, bookButtonIds[0], currentPage <= 0)
            player.packetDispatch.sendInterfaceConfig(componentId, bookButtonIds[1], currentPage >= totalPages - 1)
            player.packetDispatch.sendString("" + (currentPage * 2 + 1), componentId, bookLineIds[1])
            player.packetDispatch.sendString("" + (currentPage * 2 + 2), componentId, bookLineIds[2])
            if (hasRightPage) {
                // If there's no right side page, remove the page number. Usually for odd paged books.
                if (componentId == FANCY_BOOK_26) {
                    player.packetDispatch.sendString("", componentId, FANCY_BOOK_26_LINE_IDS[2])
                } else if (componentId == FANCY_BOOK_2_27) {
                    player.packetDispatch.sendString("", componentId, FANCY_BOOK_2_27_LINE_IDS[2])
                } else if (componentId == FANCY_BOOK_3_49) {
                    player.packetDispatch.sendString("", componentId, FANCY_BOOK_3_49_LINE_IDS[2])
                }
            }
        }

        /** Set text contents of book. CurrentPage is 0 index. */
        fun setPageContent(player: Player, componentId: Int, bookLineIds: Array<Int>, bookButtonIds: Array<Int>, currentPage: Int, contents: Array<PageSet>) {
            for (page in contents[currentPage].pages) {
                for (line in page.lines) {
                    // This is to prevent error child lines being set and crashing the client.
                    if (bookLineIds.contains(line.child)) {
                        player.packetDispatch.sendString(line.message, componentId, line.child)
                    }
                    if (bookButtonIds.contains(line.child)) {
                        player.packetDispatch.sendInterfaceConfig(componentId, line.child, false)
                        player.packetDispatch.sendString(line.message, componentId, line.child)
                    }
                }

            }
        }

        /** Sets models(pictures) on lineId of pageSet (0 index). Call this in the display function after pageSetup. */
        fun setModelOnPage(player: Player, pageSet: Int, modelId: Int, componentId: Int, enableLineId: Int, drawLineId: Int, zoom: Int, pitch: Int, yaw: Int) {
            if (pageSet == getAttribute(player, CURRENT_PAGE_ATTRIBUTE, 0)) {
                player.packetDispatch.sendInterfaceConfig(componentId, enableLineId, false)
                player.packetDispatch.sendModelOnInterface(modelId, componentId, drawLineId, 0)
                player.packetDispatch.sendAngleOnInterface(componentId, drawLineId, zoom, pitch, yaw)
            } else {
                player.packetDispatch.sendInterfaceConfig(componentId, enableLineId, true)
            }
        }

        /** Function to check if player read to the last page. For quest triggers.  */
        fun isLastPage(pageNum: Int, totalPages: Int): Boolean {
            return pageNum == totalPages - 1
        }

        /** PRIVATE: Increments the current page and invokes the callback function. */
        private fun changePageAndCallback(player: Player, increment: Int, buttonId: Int) {
            val callback: ((player: Player, pageNum: Int, buttonId: Int) -> Boolean)? =
                getAttribute(player, CALLBACK_ATTRIBUTE, null)
            val currentPage = getAttribute(player, CURRENT_PAGE_ATTRIBUTE, 0)
            setAttribute(player, CURRENT_PAGE_ATTRIBUTE, currentPage + increment)

            callback?.invoke(player, currentPage + increment, buttonId)
        }
    }

    override fun defineInterfaceListeners() {
        on(FANCY_BOOK_26) { player, _, _, buttonID, _, _ ->
            when (buttonID) {
                FANCY_BOOK_26_BUTTON_IDS[0] -> { changePageAndCallback(player, -1, buttonID) }
                FANCY_BOOK_26_BUTTON_IDS[1] -> { changePageAndCallback(player, 1, buttonID) }
                else -> (changePageAndCallback(player, 0, buttonID))
            }
            return@on true
        }
        on(FANCY_BOOK_2_27) { player, _, _, buttonID, _, _ ->
            when (buttonID) {
                FANCY_BOOK_2_27_BUTTON_IDS[0] -> { changePageAndCallback(player, -1, buttonID) }
                FANCY_BOOK_2_27_BUTTON_IDS[1] -> { changePageAndCallback(player, 1, buttonID) }
                else -> (changePageAndCallback(player, 0, buttonID))
            }
            return@on true
        }
        on(FANCY_BOOK_3_49) { player, _, _, buttonID, _, _ ->
            when (buttonID) {
                FANCY_BOOK_3_49_BUTTON_IDS[0] -> { changePageAndCallback(player, -1, buttonID) }
                FANCY_BOOK_3_49_BUTTON_IDS[1] -> { changePageAndCallback(player, 1, buttonID) }
                else -> (changePageAndCallback(player, 0, buttonID))
            }
            return@on true
        }
    }
}

/** Constructs a new PageSet object. */
class PageSet(vararg pages: Page) {
    val pages: Array<Page>

    init {
        this.pages = pages as Array<Page>
    }
}

/** Constructs a new Page object. */
class Page(vararg lines: BookLine) {
    val lines: Array<BookLine>

    init {
        this.lines = lines as Array<BookLine>
    }
}

/** Constructs a new BookLine object. */
class BookLine(
    val message: String,
    val child: Int
)
