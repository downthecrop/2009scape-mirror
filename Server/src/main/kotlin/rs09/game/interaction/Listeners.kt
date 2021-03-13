package rs09.game.interaction

import core.game.interaction.DestinationFlag
import core.game.interaction.MovementPulse
import core.game.node.Node
import core.game.node.entity.player.Player

object Listeners {
    private val listeners = HashMap<String,(Player, Node) -> Boolean>(1000)
    private val useWithListeners = HashMap<String,(Player,Node,Node) -> Boolean>(1000)

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
    fun add(options: Array<out String>,type: Int,method: (Player, Node) -> Boolean){
        for(opt in options){
            add(opt,type,method)
        }
    }

    @JvmStatic
    fun add(used: Int, with: Int, type: Int, method: (Player,Node,Node) -> Boolean){
        useWithListeners["$used:$with:$type"] = method
    }

    @JvmStatic
    fun add(type: Int, used: Int, with: IntArray, method: (Player, Node, Node) -> Boolean){
        for(id in with){
            useWithListeners["$used:$id:$type"] = method
        }
    }

    @JvmStatic
    fun get(used: Int, with: Int, type: Int): ((Player,Node,Node) -> Boolean)?{
        return useWithListeners["$used:$with:$type"]
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
    fun run(used: Node, with: Node, type: Int,player: Player): Boolean{
        val flag = when(type){
            2 -> DestinationFlag.ENTITY
            1 -> DestinationFlag.OBJECT
            else -> DestinationFlag.OBJECT
        }

        var flipped = false

        val method = get(used.id,with.id,type) ?: get(with.id,used.id,type).also { flipped = true } ?: return false

        if(type != 0) {
            player.pulseManager.run(object : MovementPulse(player, with, flag) {
                override fun pulse(): Boolean {
                    player.faceLocation(with.location)
                    if(flipped) method.invoke(player,with,used)
                    else method.invoke(player,used,with)
                    return true
                }
            })
        } else {
            if(flipped) method.invoke(player,with,used)
            else method.invoke(player,used,with)
        }
        return true
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