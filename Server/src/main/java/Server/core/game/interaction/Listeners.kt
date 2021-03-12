package core.game.interaction

import core.game.node.Node
import core.game.node.entity.player.Player

object Listeners {
    private val listeners = HashMap<String,(Player, Node) -> Boolean>()

    @JvmStatic
    fun add(id: Int, type: Int, option: Array<out String>, method: (Player,Node) -> Boolean){
        for(opt in option) {
            val key = "$id:$type:${opt.toLowerCase()}"
            listeners[key] = method
        }
    }

    @JvmStatic
    fun add(ids: IntArray, type: Int, option: Array<out String>, method: (Player,Node) -> Boolean){
        for(id in ids){
            add(id,type,option,method)
        }
    }

    @JvmStatic
    fun add(option: String,type: Int, method: (Player,Node) -> Boolean){
        val key = "$type:${option.toLowerCase()}"
        listeners[key] = method
    }

    @JvmStatic
    fun get(id: Int, type: Int, option: String): ((Player,Node) -> Boolean)?{
        return listeners["$id:$type:${option.toLowerCase()}"]
    }

    @JvmStatic
    fun get(option: String,type: Int): ((Player,Node) -> Boolean)?{
        return listeners["$type:${option.toLowerCase()}"]
    }

    @JvmStatic
    fun run(id: Int, type: Int, option: String, player: Player, node: Node): Boolean{
        val flag = when(type){
            2 -> DestinationFlag.ENTITY
            1 -> DestinationFlag.OBJECT
            else -> DestinationFlag.OBJECT
        }

        val method = get(id,type,option) ?: get(option,type) ?: return false

        if(type != 0) {
            player.pulseManager.run(object : MovementPulse(player, node, flag) {
                override fun pulse(): Boolean {
                    player.faceLocation(node.location)
                    method.invoke(player,node)
                    return true
                }
            })
        } else {
            method.invoke(player,node)
        }
        return true
    }
}