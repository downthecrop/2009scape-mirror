package rs09.game.interaction

import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.world.map.Location

abstract class InteractionListener : Listener{
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
    fun onUseWith(type: Int, used: Int, vararg with: Int, handler: (Player, Node, Node) -> Boolean){
        Listeners.add(type,used,with,handler)
    }
    fun onUseWith(type: Int, used: IntArray, vararg with: Int, handler: (Player, Node, Node) -> Boolean){
        Listeners.add(type,used,with,handler)
    }

    open fun defineDestinationOverrides(){}

    fun setDest(type: Int, id: Int,handler: (Node) -> Location){
        Listeners.addDestOverride(type,id,handler)
    }
    fun setDest(type:Int, vararg options: String, handler: (Node) -> Location){
        Listeners.addDestOverrides(type,options,handler)
    }
}