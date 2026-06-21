package content.global.handlers.iface

import core.api.submitWorldPulse
import content.global.travel.glider.GliderPulse
import content.global.travel.glider.Gliders
import core.game.interaction.InterfaceListener
import org.rs09.consts.Components

class GliderInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.GLIDERMAP_138) { player, _, _, button, _, _ ->
            val glider = Gliders.forId(button) ?: return@on true
            submitWorldPulse(GliderPulse(1, player, glider))
            return@on true
        }
    }
}
