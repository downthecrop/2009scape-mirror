package rs09.game.interaction.`object`

import api.openBankAccount
import org.rs09.consts.Scenery
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType

private val BANK_CHESTS = intArrayOf(
    Scenery.BANK_CHEST_3194, Scenery.BANK_CHEST_4483,
    Scenery.BANK_CHEST_10562, Scenery.BANK_CHEST_14382,
    Scenery.BANK_CHEST_16695, Scenery.BANK_CHEST_16696,
    Scenery.BANK_CHEST_21301, Scenery.BANK_CHEST_27662,
    Scenery.BANK_CHEST_27663
)

/**
 * Allows the user to interact with Bank Chests.
 *
 * @author vddCore
 */
class BankChestListener : InteractionListener {
    override fun defineListeners() {
        on(BANK_CHESTS, IntType.SCENERY, "bank", "use") { player, node ->
            openBankAccount(player)
            return@on true
        }
    }
}