package rs09.game.interaction

import core.game.node.Node
import core.game.node.entity.player.Player

abstract class OptionListener : Listener{
    val ITEM = 0
    val OBJECT = 1
    val NPC = 2
    fun on(id: Int, type: Int, vararg option: String,handler: (Player, Node) -> Boolean){
        Listeners.add(id,type,option,handler)
    }
    fun on(ids: IntArray, type: Int, vararg option: String, handler: (Player, Node) -> Boolean){
        Listeners.add(ids,type,option,handler)
    }
    fun on(option: String, type: Int, handler: (Player, Node) -> Boolean){
        Listeners.add(option,type,handler)
    }
    fun on(type: Int, vararg option: String, handler: (Player, Node) -> Boolean){
        Listeners.add(option,type,handler)
    }
}