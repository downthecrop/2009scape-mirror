package content.global.handlers.iface

import core.game.component.Component
import core.game.interaction.InterfaceListener
import org.rs09.consts.Components

class WorldMapInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.WORLDMAP_755) { player, _, _, button, _, _ ->
            if (button == 3) {
                val paneId = if (player.interfaceManager.isResizable) {
                    Components.TOPLEVEL_FULLSCREEN_746
                } else {
                    Components.TOPLEVEL_548
                }
                player.interfaceManager.openWindowsPane(Component(paneId), 2)
                player.packetDispatch.sendRunScript(1187, "ii", 0, 0)
                player.updateSceneGraph(true)
            }
            return@on true
        }
    }
}
