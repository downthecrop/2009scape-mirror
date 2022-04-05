package rs09.game.interaction

import api.ContentInterface
import core.game.component.Component
import core.game.node.entity.player.Player

/**
 * An interface for writing content that allows the class to handle game interface interactions
 *
 * Interactions should be defined in the required [defineInterfaceListeners] method.
 */
interface InterfaceListener : ContentInterface {
    fun defineInterfaceListeners()

    fun on(componentID: Int, buttonID: Int, handler: (player: Player, component: Component, opcode: Int, buttonID: Int, slot: Int, itemID: Int) -> Boolean){
        InterfaceListeners.add(componentID,buttonID,handler)
    }
    fun on(componentID: Int, handler: (player: Player, component: Component, opcode: Int, buttonID: Int, slot: Int, itemID: Int) -> Boolean){
        InterfaceListeners.add(componentID,handler)
    }
    fun onOpen(componentID: Int,handler: (player: Player, component: Component) -> Boolean){
        InterfaceListeners.addOpenListener(componentID, handler)
    }
    fun onClose(componentID: Int,handler: (player: Player, component: Component) -> Boolean){
        InterfaceListeners.addCloseListener(componentID,handler)
    }
}