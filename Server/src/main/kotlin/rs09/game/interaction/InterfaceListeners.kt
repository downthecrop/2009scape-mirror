package rs09.game.interaction

import core.game.component.Component
import core.game.node.entity.player.Player

object InterfaceListeners {
    val buttonListeners = HashMap<String,(Player, Component, Int, Int, Int, Int) -> Boolean>(1000)
    val openListeners = HashMap<String,(Player,Component) -> Boolean>(100)

    @JvmStatic
    fun add(componentID: Int, buttonID: Int, handler: (Player,Component,Int,Int,Int,Int) -> Boolean){
        buttonListeners["$componentID:$buttonID"] = handler
    }

    @JvmStatic
    fun add(componentID: Int, handler: (Player, Component, Int, Int, Int, Int) -> Boolean){
        buttonListeners["$componentID"] = handler
    }

    @JvmStatic
    fun addOpenListener(componentID: Int, handler: (Player,Component) -> Boolean){
        openListeners["$componentID"] = handler
    }

    @JvmStatic
    fun addCloseListener(componentID: Int, handler: (Player, Component) -> Boolean){
        openListeners["close:$componentID"] = handler
    }

    @JvmStatic
    fun get(componentID: Int, buttonID: Int): ((Player,Component,Int,Int,Int,Int) -> Boolean)?{
        return buttonListeners["$componentID:$buttonID"]
    }

    @JvmStatic
    fun get(componentID: Int): ((Player,Component,Int,Int,Int,Int) -> Boolean)?{
        return buttonListeners["$componentID"]
    }

    @JvmStatic
    fun getOpenListener(componentID: Int): ((Player,Component) -> Boolean)?{
        return openListeners["$componentID"]
    }

    @JvmStatic
    fun getCloseListener(componentID: Int): ((Player,Component) -> Boolean)?{
        return openListeners["close:$componentID"]
    }

    @JvmStatic
    fun runOpen(player: Player,component: Component): Boolean{
        val method = getOpenListener(component.id) ?: return false
        return method.invoke(player,component)
    }

    @JvmStatic
    fun runClose(player: Player,component: Component): Boolean{
        val method = getCloseListener(component.id) ?: return true
        return method.invoke(player,component)
    }

    @JvmStatic
    fun run(player: Player, component: Component, opcode: Int, buttonID: Int, slot: Int, itemID: Int): Boolean{
        val method = get(component.id,buttonID) ?: get(component.id) ?:  return false
        return method.invoke(player,component,opcode,buttonID,slot,itemID)
    }
}