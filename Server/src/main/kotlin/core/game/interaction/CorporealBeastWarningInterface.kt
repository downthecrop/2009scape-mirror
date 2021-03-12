package core.game.interaction

import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
/**
 * Handles the corporeal beast warning interface
 * @author Ceikry
 */
class CorporealBeastWarningInterface : ComponentPlugin(){
    companion object{
        const val COMPONENT_ID = 650
    }

    override fun handle(player: Player?, component: Component?, opcode: Int, button: Int, slot: Int, itemId: Int): Boolean {
        player ?: return false
        when(button){
            17 -> {
                if(player.getAttribute("corp-beast-cave-delay",0) <= GameWorld.ticks) {
                    player.properties.teleportLocation = player.location.transform(4, 0, 0).also { close(player) }
                    player.setAttribute("corp-beast-cave-delay",GameWorld.ticks + 5)
                } else {
                    close(player)
                }
            }
            else -> close(player)
        }
        return true
    }

    fun close(player: Player){
        player.interfaceManager.close(Component(650))
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(COMPONENT_ID,this)
        return this
    }

}