package rs09.game.interaction

import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.world.map.Location

abstract class InteractionListener : Listener{
    companion object {
        val ITEM = 0
        val SCENERY = 1
        val NPC = 2
    }
    fun on(id: Int, type: Int, vararg option: String,handler: (player: Player, node: Node) -> Boolean){
        InteractionListeners.add(id,type,option,handler)
    }
    fun on(ids: IntArray, type: Int, vararg option: String, handler: (player: Player, node: Node) -> Boolean){
        InteractionListeners.add(ids,type,option,handler)
    }
    fun on(option: String, type: Int, handler: (player: Player, node: Node) -> Boolean){
        InteractionListeners.add(option,type,handler)
    }
    fun on(type: Int, vararg option: String, handler: (player: Player, node: Node) -> Boolean){
        InteractionListeners.add(option,type,handler)
    }
    fun onUseWith(type: Int, used: Int, vararg with: Int, handler: (player: Player, used: Node, with: Node) -> Boolean){
        InteractionListeners.add(type,used,with,handler)
    }
    fun onUseWith(type: Int, used: IntArray, vararg with: Int, handler: (player: Player, used: Node, with: Node) -> Boolean){
        InteractionListeners.add(type,used,with,handler)
    }
    fun onEquip(id: Int, handler: (player: Player, node: Node) -> Unit){
        InteractionListeners.addEquip(id,handler)
    }
    fun onUnequip(id:Int, handler: (player: Player, node: Node) -> Unit){
        InteractionListeners.addUnequip(id,handler)
    }

    open fun defineDestinationOverrides(){}

    fun setDest(type: Int, id: Int,handler: (Node) -> Location){
        InteractionListeners.addDestOverride(type,id,handler)
    }
    fun setDest(type:Int, vararg options: String, handler: (Node) -> Location){
        InteractionListeners.addDestOverrides(type,options,handler)
    }

    fun setDest(type: Int, ids: IntArray, vararg options: String, handler: (Node) -> Location){
        InteractionListeners.addDestOverrides(type,ids,options,handler)
    }

    fun onDig(location: Location,method: (player: Player) -> Unit){
        SpadeDigListener.registerListener(location,method)
    }
}