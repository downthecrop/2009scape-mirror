package rs09.game.interaction

import core.game.component.Component
import core.game.node.entity.player.Player
import rs09.game.world.GameWorld

/**
 * Handles the corporeal beast warning interface
 * @author Ceikry
 */
class CorporealBeastWarningInterface : InterfaceListener(){

    val COMPONENT_ID = 650

    override fun defineListeners() {
        on(COMPONENT_ID,17){player,component,_,_,_,_ ->
            if(player.getAttribute("corp-beast-cave-delay",0) <= GameWorld.ticks) {
                player.properties.teleportLocation = player.location.transform(4, 0, 0).also { close(player,component) }
                player.setAttribute("corp-beast-cave-delay",GameWorld.ticks + 5)
            } else {
                close(player,component)
            }
            return@on true
        }

        on(COMPONENT_ID){player, component, _, _, _, _ ->
            close(player,component)
            return@on true
        }
    }

    fun close(player: Player,component: Component){
        player.interfaceManager.close(component)
    }
}