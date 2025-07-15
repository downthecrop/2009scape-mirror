package content.global.handlers.iface

import core.api.closeInterface
import core.game.interaction.InterfaceListener
import org.rs09.consts.Components

class DeathInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.AIDE_DEATH_153) { player, _, _, buttonID, _, _ ->
            if (buttonID == 1) {
                player.savedData.globalData.setDisableDeathScreen(true)
                closeInterface(player)
            }
            return@on true
        }
    }
}