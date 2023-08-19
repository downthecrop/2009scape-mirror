package content.global.handlers.iface

import core.api.closeInterface
import core.api.openInterface
import core.game.node.entity.player.Player
import org.rs09.consts.Components

/**
 * Interface listener for Scrolls
 * Use this to set up a simple scroll display.
 *
 * Currently, scrolls do not happen to have any button interactions, so no listeners are here yet.
 * This is generally a helper class until certain scrolls require interactions.
 *
 * @author ovenbreado
 */
class ScrollInterface {

    companion object {
        /** This is a 15-Lines scroll */
        private val MESSAGESCROLL_220_LINE_IDS = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)

        fun scrollSetup(player: Player, scrollComponent: Int, contents: Array<ScrollLine>) {
            closeInterface(player) // Important: Close previous interfaces.
            if (scrollComponent == Components.MESSAGESCROLL_220) {
                openInterface(player, Components.MESSAGESCROLL_220)
                setPageContent(player, Components.MESSAGESCROLL_220, MESSAGESCROLL_220_LINE_IDS, contents)
            }
        }

        /** Set text contents of scroll */
        fun setPageContent(player: Player, componentId: Int, scrollLineIds: Array<Int>, contents: Array<ScrollLine>) {
            for (line in contents) {
                // This is to prevent error child lines being set and crashing the client.
                if (scrollLineIds.contains(line.child)) {
                    player.packetDispatch.sendString(line.message, componentId, line.child)
                }
            }
        }
    }
}

/** Constructs a new ScrollLine object. */
class ScrollLine(
    val message: String,
    val child: Int
)