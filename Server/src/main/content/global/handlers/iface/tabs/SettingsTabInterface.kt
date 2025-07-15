package content.global.handlers.iface.tabs

import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.link.IronmanMode
import org.rs09.consts.Components

class SettingsTabInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.OPTIONS_261) { player, _, _, buttonID, _, _ ->
            when (buttonID) {
                RUN -> player.settings.toggleRun()
                CHAT_EFFECTS -> player.settings.toggleChatEffects()
                SPLIT_PM -> player.settings.toggleSplitPrivateChat()
                MOUSE -> player.settings.toggleMouseButton()
                AID -> restrictForIronman(player, IronmanMode.STANDARD) { player.settings.toggleAcceptAid() }
                HOUSE -> openSingleTab(player, Components.POH_HOUSE_OPTIONS_398)
                GRAPHICS -> openInterface(player, Components.GRAPHICS_OPTIONS_742)
                AUDIO -> openInterface(player, Components.SOUND_OPTIONS_743)
            }
            return@on true
        }
    }

    companion object {
        const val RUN = 3
        const val CHAT_EFFECTS = 4
        const val SPLIT_PM = 5
        const val MOUSE = 6
        const val AID = 7
        const val HOUSE = 8
        const val GRAPHICS = 16
        const val AUDIO = 18
    }
}