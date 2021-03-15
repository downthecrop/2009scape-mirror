package rs09.game.interaction

import core.game.interaction.DestinationFlag
import core.game.interaction.MovementPulse
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.world.map.Location

object InteractionListeners {
    private val listeners = HashMap<String,(Player, Node) -> Boolean>(1000)
    private val useWithListeners = HashMap<String,(Player,Node,Node) -> Boolean>(1000)
    private val destinationOverrides = HashMap<String,(Node) -> Location>(100)

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
    fun addDestOverride(type: Int, id: Int, method: (Node) -> Location){
        destinationOverrides["$type:$id"] = method
    }

    @JvmStatic
    fun addDestOverrides(type: Int,options: Array<out String>, method: (Node) -> Location){
        for(opt in options){
            destinationOverrides["$type:${opt.toLowerCase()}"] = method
        }
    }

    @JvmStatic
    fun addDestOverrides(type: Int, ids: IntArray,options: Array<out String>, method: (Node) -> Location){
        for(id in ids){
            for(opt in options){
                destinationOverrides["$type:$id:${opt.toLowerCase()}"] = method
            }
        }
    }

    @JvmStatic
    fun getOverride(type: Int, id:Int, option: String): ((Node) -> Location)?{
        return destinationOverrides["$type:$id:${option.toLowerCase()}"]
    }

    @JvmStatic
    fun getOverride(type: Int,id: Int): ((Node) -> Location)?{
        return destinationOverrides["$type:$id"]
    }

    @JvmStatic
    fun getOverride(type: Int,option: String): ((Node) -> Location)?{
        return destinationOverrides["$type:$option"]
    }

    @JvmStatic
    fun run(used: Node, with: Node, type: Int,player: Player): Boolean{
        val flag = when(type){
            2 -> DestinationFlag.ENTITY
            1 -> DestinationFlag.OBJECT
            else -> DestinationFlag.OBJECT
        }

        if(player.locks.isInteractionLocked) return false

        var flipped = false

        val method = get(used.id,with.id,type) ?: get(with.id,used.id,type).also { flipped = true } ?: return false

        if(type != 0) {
            if(player.locks.isMovementLocked) return false
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

        if(player.locks.isInteractionLocked) return false

        val method = get(id,type,option) ?: get(option,type) ?: return false
        val destOverride = getOverride(type, id, option) ?: getOverride(type,node.id) ?: getOverride(type,option.toLowerCase())

        if(type != 0) {
            if(player.locks.isMovementLocked) return false
            player.pulseManager.run(object : MovementPulse(player, node, flag, destOverride) {
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

    fun add(type: Int, used: IntArray, with: IntArray, handler: (Player, Node, Node) -> Boolean) {
        for(u in used){
            for (w in with){
                useWithListeners["$u:$w:$type"] = handler
            }
        }
    }
}