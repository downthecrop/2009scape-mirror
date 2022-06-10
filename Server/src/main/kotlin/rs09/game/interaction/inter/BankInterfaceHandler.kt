package rs09.game.interaction.inter

import api.IfaceSettingsBuilder
import core.game.component.Component
import core.game.node.entity.player.Player
import org.rs09.consts.Components
import rs09.ServerConstants
import rs09.game.interaction.InterfaceListener

class BankInterfaceHandler : InterfaceListener {

    private fun onBankInterfaceOpen(player: Player, component: Component) : Boolean {
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

        player.sendChat("onBankInterfaceOpen listener finished")
        return true
    }

    private fun closeBankHelpInterface(player: Player, component: Component, opcode: Int, buttonID: Int, slot: Int, itemID: Int) : Boolean {
        player.bank.open();
        return true;
    }

    override fun defineInterfaceListeners() {
        onOpen(Components.BANK_V2_MAIN_762, handler = ::onBankInterfaceOpen)
        on(Components.BANK_V2_HELP_767, 10, handler = ::closeBankHelpInterface)
    }
}