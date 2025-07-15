package content.global.handlers.iface.tabs

import core.game.interaction.InterfaceListener
import core.game.node.entity.player.link.prayer.PrayerType
import org.rs09.consts.Components

class PrayerTabInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.PRAYER_271) { player, _, _, buttonID, _, _ ->
            val prayer = PrayerType.get(buttonID) ?: return@on true
            return@on player.prayer.toggle(prayer)
        }
    }
}