package rs09.game.interaction

import api.ContentInterface
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import rs09.game.system.SystemLogger

interface InteractionListener : ContentInterface{
    val ITEM
        get() = IntType.ITEM
    val GROUNDITEM
        get() = IntType.GROUNDITEM
    val NPC
        get() = IntType.NPC
    val SCENERY
        get() = IntType.SCENERY

    fun on(id: Int, type: IntType, vararg option: String, handler: (player: Player, node: Node) -> Boolean){
        InteractionListeners.add(id,type.ordinal,option,handler)
    }
    fun on(ids: IntArray, type: IntType, vararg option: String, handler: (player: Player, node: Node) -> Boolean){
        InteractionListeners.add(ids,type.ordinal,option,handler)
    }
    fun on(option: String, type: IntType, handler: (player: Player, node: Node) -> Boolean){
        InteractionListeners.add(option,type.ordinal,handler)
    }
    fun on(type: IntType, vararg option: String, handler: (player: Player, node: Node) -> Boolean){
        InteractionListeners.add(option,type.ordinal,handler)
    }
    fun onUseWith(type: IntType, used: Int, vararg with: Int, handler: (player: Player, used: Node, with: Node) -> Boolean){
        InteractionListeners.add(type.ordinal,used,with,handler)
    }
    fun onUseWith(type: IntType, used: IntArray, vararg with: Int, handler: (player: Player, used: Node, with: Node) -> Boolean){
        InteractionListeners.add(type.ordinal,used,with,handler)
    }
    fun onUseAnyWith(type: IntType, vararg with: Int, handler: (player: Player, used: Node, with: Node) -> Boolean) {
        InteractionListeners.add(type.ordinal, with, handler)
    }
    fun onUseWithPlayer(vararg used: Int, handler: (player: Player, used: Node, with: Node) -> Boolean) {
        InteractionListeners.add(IntType.PLAYER.ordinal, used, handler)
    }
    // Note: wildcard listeners incur overhead on every use-with interaction, only use them as a space-time tradeoff when something
    // is actually supposed to have a response to every item used with it (e.g. imp boxes, certain quest npcs)
    fun onUseWithWildcard(type: IntType, predicate: (used: Int, with: Int) -> Boolean, handler: (player: Player, used: Node, with: Node) -> Boolean) {
        InteractionListeners.addWildcard(type.ordinal, predicate, handler)
    }
    fun onEquip(id: Int, handler: (player: Player, node: Node) -> Boolean){
        InteractionListeners.addEquip(id,handler)
    }
    fun onUnequip(id:Int, handler: (player: Player, node: Node) -> Boolean){
        InteractionListeners.addUnequip(id,handler)
    }

    fun onEquip(ids: IntArray, handler: (player: Player, node: Node) -> Boolean){
        ids.forEach { id -> InteractionListeners.addEquip(id,handler) }
    }
    fun onUnequip(ids:IntArray, handler: (player: Player, node: Node) -> Boolean){
        ids.forEach{ id -> InteractionListeners.addUnequip(id,handler) }
    }

    fun defineDestinationOverrides(){}

    fun setDest(type: IntType, id: Int,handler: (Entity, Node) -> Location){
        InteractionListeners.addDestOverride(type.ordinal,id,handler)
    }
    fun setDest(type: IntType, vararg options: String, handler: (Entity, Node) -> Location){
        InteractionListeners.addDestOverrides(type.ordinal,options,handler)
    }

    fun setDest(type: IntType, ids: IntArray, vararg options: String, handler: (Entity, Node) -> Location){
        InteractionListeners.addDestOverrides(type.ordinal,ids,options,handler)
    }

    fun onDig(location: Location,method: (player: Player) -> Unit){
        SpadeDigListener.registerListener(location,method)
    }

    fun flagInstant() {
        val name = this::class.java.name
        InteractionListeners.instantClasses.add(name)
    }

    fun defineListeners()
}
