package rs09.game.interaction

import api.ContentInterface
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.Location
import rs09.game.system.SystemLogger

interface InteractionListener : ContentInterface{
    val ITEM: Int
        get() = 0
    val SCENERY: Int
        get() = 1
    val NPC: Int
        get() = 2
    val GROUNDITEM: Int
        get() = 3
    val PLAYER: Int
        get() = 4

    fun on(id: Int, type: Int, vararg option: String, handler: (player: Player, node: Node) -> Boolean){
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
    fun onUseAnyWith(type: Int, vararg with: Int, handler: (player: Player, used: Node, with: Node) -> Boolean) {
        InteractionListeners.add(type, with, handler)
    }
    fun onUseWithPlayer(vararg used: Int, handler: (player: Player, used: Node, with: Node) -> Boolean) {
        InteractionListeners.add(PLAYER, used, handler)
    }
    // Note: wildcard listeners incur overhead on every use-with interaction, only use them as a space-time tradeoff when something
    // is actually supposed to have a response to every item used with it (e.g. imp boxes, certain quest npcs)
    fun onUseWithWildcard(type: Int, predicate: (used: Int, with: Int) -> Boolean, handler: (player: Player, used: Node, with: Node) -> Boolean) {
        InteractionListeners.addWildcard(type, predicate, handler)
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

    fun setDest(type: Int, id: Int,handler: (Entity, Node) -> Location){
        InteractionListeners.addDestOverride(type,id,handler)
    }
    fun setDest(type:Int, vararg options: String, handler: (Entity, Node) -> Location){
        InteractionListeners.addDestOverrides(type,options,handler)
    }

    fun setDest(type: Int, ids: IntArray, vararg options: String, handler: (Entity, Node) -> Location){
        InteractionListeners.addDestOverrides(type,ids,options,handler)
    }

    fun onDig(location: Location,method: (player: Player) -> Unit){
        SpadeDigListener.registerListener(location,method)
    }

    fun flagInstant() {
        val name = this::class.java.name
        InteractionListeners.instantClasses.add(name)
    }

    fun defineListeners()

    companion object
    {
        val ITEM = 0
        val SCENERY = 1
        val NPC = 2
        val GROUNDITEM = 3
        val PLAYER = 4
    }
}
