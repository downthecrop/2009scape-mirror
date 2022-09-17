package rs09.game.content.quest.members.elementalworkshop

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.book.Book
import core.game.content.dialogue.book.Page
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items

/**
 * Slashed book handler for the Elemental Workshop I quest
 *
 *  @author Woah, with love
 */
@Initializable
class SlashedBookHandler : Book {
    constructor(player: Player?) : super(player, "Book of the elemental shield", Items.SLASHED_BOOK_9715, *EWUtils.PAGES) {}

    constructor() {
        /**
         * empty.
         */
    }

    override fun finish() {}

    override fun display(set: Array<Page>) {
        player.lock()
        player.interfaceManager.open(getInterface())

        for (i in 55..76) {
            player.packetDispatch.sendString("", getInterface().id, i)
        }
        player.packetDispatch.sendString("", getInterface().id, 77)
        player.packetDispatch.sendString("", getInterface().id, 78)
        player.packetDispatch.sendString(getName(), getInterface().id, 6)
        for (page in set) {
            for (line in page.lines) {
                player.packetDispatch.sendString(line.message, getInterface().id, line.child)
            }
        }
        player.packetDispatch.sendInterfaceConfig(getInterface().id, 51, index < 1)
        val lastPage = index == sets.size - 1
        player.packetDispatch.sendInterfaceConfig(getInterface().id, 53, lastPage)
        if (lastPage) {
            finish()
        }
        player.unlock()
    }

    override fun newInstance(player: Player): DialoguePlugin {
        return BatteredBookHandler(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(EWUtils.SLASHED_BOOK_ID)
    }
}