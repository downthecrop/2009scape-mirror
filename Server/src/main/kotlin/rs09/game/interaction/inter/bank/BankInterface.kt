package rs09.game.interaction.inter.bank

import api.*
import core.game.component.Component
import core.game.container.Container
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.StringUtils
import org.rs09.consts.Components
import org.rs09.consts.Items
import rs09.ServerConstants
import rs09.game.content.dialogue.region.worldwide.bank.BankDepositDialogue
import rs09.game.content.dialogue.region.worldwide.bank.BankHelpDialogue
import rs09.game.interaction.InterfaceListener
import rs09.game.interaction.util.BankUtils

/**
 * Allows the user to interact with the Bank Interface.
 *
 * @author vddCore
 */
class BankInterface : InterfaceListener {
    companion object {
        private const val MAIN_BUTTON_CLOSE = 10
        private const val MAIN_BUTTON_INSERT_MODE = 14
        private const val MAIN_BUTTON_NOTE_MODE = 16
        private const val MAIN_BUTTON_BOB_DEPOSIT = 18
        private const val MAIN_BUTTON_SEARCH_BANK = 20
        private const val MAIN_BUTTON_HELP = 23

        private const val MENU_ELEMENT = 73
        private const val OP_AMOUNT_ONE = 155
        private const val OP_AMOUNT_FIVE = 196
        private const val OP_AMOUNT_TEN = 124
        private const val OP_AMOUNT_LAST_X = 199
        private const val OP_AMOUNT_X = 234
        private const val OP_AMOUNT_ALL = 168
        private const val OP_AMOUNT_ALL_BUT_ONE = 166
        private const val OP_EXAMINE = 9

        private const val BANK_TAB_1 = 41
        private const val BANK_TAB_2 = 39
        private const val BANK_TAB_3 = 37
        private const val BANK_TAB_4 = 35
        private const val BANK_TAB_5 = 33
        private const val BANK_TAB_6 = 31
        private const val BANK_TAB_7 = 29
        private const val BANK_TAB_8 = 27
        private const val BANK_TAB_9 = 25

        private val BANK_TABS = intArrayOf(
            BANK_TAB_1, BANK_TAB_2, BANK_TAB_3,
            BANK_TAB_4, BANK_TAB_5, BANK_TAB_6,
            BANK_TAB_7, BANK_TAB_8, BANK_TAB_9
        )

        private const val OP_SET_TAB = 155
        private const val OP_COLLAPSE_TAB = 196

        private const val THRESHOLD_TO_DISPLAY_EXACT_QUANTITY_ON_EXAMINE = 100000;
    }

    private fun onBankInterfaceOpen(player: Player, component: Component): Boolean {
        player.bank.sendBankSpace();

        val settings = IfaceSettingsBuilder()
            .enableAllOptions()
            .enableSlotSwitch()
            .setInterfaceEventsDepth(2)
            .build()

        player.packetDispatch.sendIfaceSettings(
            settings,
            73,
            Components.BANK_V2_MAIN_762,
            0,
            ServerConstants.BANK_SIZE
        )

        return false
    }

    private fun handleTabInteraction(player: Player, component: Component, opcode: Int, buttonID: Int, slot: Int, itemID: Int): Boolean {
        if (getAttribute(player, "search", false)) {
            player.bank.reopen()
        }

        val clickedTabIndex = -((buttonID - 41) / 2)

        when (opcode) {
            OP_SET_TAB -> {
                if (player.bank.tabIndex != clickedTabIndex) {
                    player.bank.tabIndex = clickedTabIndex
                }
            }

            OP_COLLAPSE_TAB -> {
                player.bank.collapseTab(clickedTabIndex)
            }
        }

        return true
    }

    private fun handleBankMenu(player: Player, component: Component, opcode: Int, buttonID: Int, slot: Int, itemID: Int): Boolean {
        val item = player.bank.get(slot)
            ?: return true

        when (opcode) {
            OP_AMOUNT_ONE -> runWorldTask { player.bank.takeItem(slot, 1) }
            OP_AMOUNT_FIVE -> runWorldTask { player.bank.takeItem(slot, 5) }
            OP_AMOUNT_TEN -> runWorldTask { player.bank.takeItem(slot, 10) }
            OP_AMOUNT_LAST_X -> runWorldTask {
                player.bank.takeItem(
                    slot,
                    player.bank.lastAmountX
                )
            }
            OP_AMOUNT_X -> runWorldTask {
                BankUtils.transferX(
                    player,
                    slot,
                    true
                )
            }
            OP_AMOUNT_ALL -> runWorldTask {
                player.bank.takeItem(
                    slot,
                    player.bank.getAmount(item)
                )
            }
            OP_AMOUNT_ALL_BUT_ONE -> runWorldTask {
                player.bank.takeItem(
                    slot,
                    player.bank.getAmount(item) - 1
                )
            }
            OP_EXAMINE -> {
                var examineText = item.definition.examine
                val id = item.definition.id
                val bank = player.bank
                if (isCoinOverrideNeeded(id, bank)) {
                    examineText = "" + bank.getAmount(id) + " x " + item.definition.name + "."
                }
                sendMessage(player, examineText)
            }
            else -> player.debug("Unknown bank menu opcode $opcode")
        }

        return true
    }

    private fun handleInventoryMenu(player: Player, component: Component, opcode: Int, buttonID: Int, slot: Int, itemID: Int): Boolean {
        val item = player.inventory.get(slot)
            ?: return true

        when (opcode) {
            OP_AMOUNT_ONE -> player.bank.addItem(slot, 1)
            OP_AMOUNT_FIVE -> player.bank.addItem(slot, 5)
            OP_AMOUNT_TEN -> player.bank.addItem(slot, 10)
            OP_AMOUNT_LAST_X -> player.bank.addItem(slot, player.bank.lastAmountX)
            OP_AMOUNT_X -> BankUtils.transferX(player, slot, false)
            OP_AMOUNT_ALL -> player.bank.addItem(slot, player.inventory.getAmount(item))
            OP_EXAMINE -> {
                var examineText = item.definition.examine
                val id = item.definition.id
                val inventory = player.inventory
                if (isCoinOverrideNeeded(id, inventory)) {
                    examineText = "" + inventory.getAmount(id) + " x " + item.definition.name + "."
                }
                sendMessage(player, examineText)
            }
            else -> player.debug("Unknown inventory menu opcode $opcode")
        }

        return true
    }

    override fun defineInterfaceListeners() {
        onOpen(Components.BANK_V2_MAIN_762, ::onBankInterfaceOpen)

        on(Components.BANK_V2_MAIN_762) { player, component, opcode, buttonID, slot, itemID ->
            when (buttonID) {
                MAIN_BUTTON_HELP -> openDialogue(player, BankHelpDialogue())
                MAIN_BUTTON_BOB_DEPOSIT -> openDialogue(player, BankDepositDialogue())
                MAIN_BUTTON_INSERT_MODE -> player.bank.isInsertItems = !player.bank.isInsertItems
                MAIN_BUTTON_NOTE_MODE -> player.bank.isNoteItems = !player.bank.isNoteItems
                MAIN_BUTTON_SEARCH_BANK -> setAttribute(player, "search", true)
                MENU_ELEMENT -> handleBankMenu(player, component, opcode, buttonID, slot, itemID)
                in BANK_TABS -> handleTabInteraction(player, component, opcode, buttonID, slot, itemID)
            }

            return@on true
        }

        on(Components.BANK_V2_HELP_767) { player, component, opcode, buttonID, slot, itemID ->
            when (buttonID) {
                MAIN_BUTTON_CLOSE -> openBankAccount(player)
            }

            return@on true
        }

        on(Components.BANK_V2_SIDE_763, ::handleInventoryMenu)
    }

    private fun isCoinOverrideNeeded(id: Int, container: Container): Boolean {
        val amount = container.getAmount(id)

        // Only COINS_995 are obtainable and bankable by player
        if (id == Items.COINS_995 && amount >= THRESHOLD_TO_DISPLAY_EXACT_QUANTITY_ON_EXAMINE) {
            return true
        }

        return false
    }
}
