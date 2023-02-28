package content.global.handlers.scenery

import core.api.restrictForIronman
import core.game.component.CloseEvent
import core.game.component.Component
import core.game.container.access.InterfaceContainer
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.IronmanMode
import org.rs09.consts.Components
import org.rs09.consts.Scenery
import core.game.interaction.InteractionListener
import core.game.interaction.IntType

private val BANK_DEPOSIT_BOXES = intArrayOf(
    Scenery.BANK_DEPOSIT_BOX_9398,
    Scenery.BANK_DEPOSIT_BOX_20228,
    Scenery.BANK_DEPOSIT_BOX_25937,
    Scenery.BANK_DEPOSIT_BOX_26969,
    Scenery.BANK_DEPOSIT_BOX_34755,
    Scenery.BANK_DEPOSIT_BOX_36788,
    Scenery.BANK_DEPOSIT_BOX_39830
)

/**
 * Allows the user to interact with bank deposit boxes.
 *
 * @author vddCore
 */
class BankDepositBoxListener : InteractionListener {

    private fun openDepositBox(player: Player, node: Node, state: Int) : Boolean {
        restrictForIronman(player, IronmanMode.ULTIMATE) {
            player.interfaceManager.open(Component(Components.BANK_DEPOSIT_BOX_11)).closeEvent = CloseEvent { p, _ ->
                p.interfaceManager.openDefaultTabs()
                return@CloseEvent true
            }

            player.interfaceManager.removeTabs(0, 1, 2, 3, 4, 5, 6)
            InterfaceContainer.generateItems(
                player,
                player.inventory.toArray(),
                arrayOf(
                    "Examine",
                    "Deposit-X",
                    "Deposit-All",
                    "Deposit-10",
                    "Deposit-5",
                    "Deposit-1"
                ), 11, 15, 5, 7
            )
        }

        return true
    }

    override fun defineListeners() {
        defineInteraction(IntType.SCENERY, BANK_DEPOSIT_BOXES,  "deposit", handler = ::openDepositBox)
    }
}