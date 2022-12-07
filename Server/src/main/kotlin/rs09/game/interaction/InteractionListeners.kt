package rs09.game.interaction

import api.events.InteractionEvent
import api.events.UseWithEvent
import core.game.interaction.DestinationFlag
import core.game.interaction.MovementPulse
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.Location

object InteractionListeners {
    private val listeners = HashMap<String,(Player, Node) -> Boolean>(1000)
    private val useWithListeners = HashMap<String,(Player,Node,Node) -> Boolean>(1000)
    private val useAnyWithListeners = HashMap<String, (Player, Node, Node) -> Boolean>(10)
    private val useWithWildcardListeners = HashMap<Int, ArrayList<Pair<(Int, Int) -> Boolean, (Player, Node, Node) -> Boolean>>>(10)
    private val destinationOverrides = HashMap<String,(Entity, Node) -> Location>(100)
    private val equipListeners = HashMap<String,(Player,Node) -> Boolean>(10)
    val instantClasses = HashSet<String>()

    @JvmStatic
    fun add(id: Int, type: Int, option: Array<out String>, method: (Player,Node) -> Boolean){
        for(opt in option) {
            val key = "$id:$type:${opt.toLowerCase()}"
            if (!validate(key)) {
                throw IllegalStateException("$opt for $id with type ${IntType.values()[type].name} already defined! Existing use: [${listeners[key]!!::class.toString()}]")
            }
            listeners[key] = method
        }
    }

    private fun validate(key: String): Boolean {
        return !listeners.containsKey(key) && !useWithListeners.containsKey(key)
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
        if (!validate(key)) {
            throw IllegalStateException("Catchall listener for $option on type ${IntType.values()[type].name} already in use: ${listeners[key]!!::class}")
        }
        listeners[key] = method
    }

    @JvmStatic
    fun add(options: Array<out String>,type: Int,method: (Player, Node) -> Boolean){
        for(opt in options){
            add(opt.toLowerCase(),type,method)
        }
    }

    @JvmStatic
    fun add(used: Int, with: Int, type: Int, method: (Player,Node,Node) -> Boolean){
        val key = "$used:$with:$type"
        val altKey = "$with:$used:$type"
        if (!validate(key) || !validate(altKey)) {
            throw IllegalStateException("Usewith using $used with $with for type ${IntType.values()[type]} already in use: [${(useWithListeners[key] ?: useWithListeners[altKey])!!::class}")
        }
        useWithListeners[key] = method
    }

    @JvmStatic
    fun add(type: Int, used: Int, with: IntArray, method: (Player, Node, Node) -> Boolean){
        for(id in with){
            add(used = used, with = id, type = type, method = method)
        }
    }

    @JvmStatic
    fun addWildcard(type: Int, predicate: (used: Int, with: Int) -> Boolean, handler: (player: Player, used: Node, with: Node) -> Boolean) {
        if(!useWithWildcardListeners.containsKey(type)) {
            useWithWildcardListeners.put(type, ArrayList())
        }
        useWithWildcardListeners[type]!!.add(Pair(predicate, handler))
    }

    @JvmStatic
    fun addEquip(id: Int,method: (Player, Node) -> Boolean){
        equipListeners["equip:$id"] = method
    }

    @JvmStatic
    fun addUnequip(id: Int, method: (Player,Node) -> Boolean){
        equipListeners["unequip:$id"] = method
    }

    @JvmStatic
    fun getEquip(id: Int): ((Player,Node) -> Boolean)? {
        return equipListeners["equip:$id"]
    }

    @JvmStatic
    fun getUnequip(id: Int): ((Player,Node) -> Boolean)? {
        return equipListeners["unequip:$id"]
    }

    @JvmStatic
    fun get(used: Int, with: Int, type: Int): ((Player,Node,Node) -> Boolean)? {
        val method = useWithListeners["$used:$with:$type"] ?: useAnyWithListeners["$with:$type"]
        if(method != null) {
            return method
        }
        val handlers = useWithWildcardListeners[type] ?: return null
        for(pair in handlers) {
            if(pair.first(used, with)) {
                return pair.second
            }
        }
        return null
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
    fun addDestOverride(type: Int, id: Int, method: (Entity,Node) -> Location){
        destinationOverrides["$type:$id"] = method
    }

    @JvmStatic
    fun addDestOverrides(type: Int,options: Array<out String>, method: (Entity,Node) -> Location){
        for(opt in options){
            destinationOverrides["$type:${opt.toLowerCase()}"] = method
        }
    }

    @JvmStatic
    fun addDestOverrides(type: Int, ids: IntArray,options: Array<out String>, method: (Entity,Node) -> Location){
        for(id in ids){
            for(opt in options){
                destinationOverrides["$type:$id:${opt.toLowerCase()}"] = method
            }
        }
    }

    @JvmStatic
    fun getOverride(type: Int, id:Int, option: String): ((Entity, Node) -> Location)?{
        return destinationOverrides["$type:$id:${option.toLowerCase()}"]
    }

    @JvmStatic
    fun getOverride(type: Int,id: Int): ((Entity,Node) -> Location)?{
        return destinationOverrides["$type:$id"]
    }

    @JvmStatic
    fun getOverride(type: Int,option: String): ((Entity,Node) -> Location)?{
        return destinationOverrides["$type:$option"]
    }

    @JvmStatic
    fun run(id: Int, player: Player, node: Node, isEquip: Boolean): Boolean{
        player.dispatch(InteractionEvent(node, if(isEquip) "equip" else "unequip"))
        if(isEquip){
            return equipListeners["equip:$id"]?.invoke(player,node) ?: true
        } else {
            return equipListeners["unequip:$id"]?.invoke(player,node) ?: true
        }
    }

    @JvmStatic
    fun run(used: Node, with: Node, type: IntType, player: Player): Boolean{
        val flag = when(type){
            IntType.NPC, IntType.PLAYER -> DestinationFlag.ENTITY
            IntType.SCENERY -> DestinationFlag.OBJECT
            IntType.GROUNDITEM -> DestinationFlag.ITEM
            else -> DestinationFlag.OBJECT
        }

        if(player.locks.isInteractionLocked) return false

        var flipped = false

        val method = if (with is Player) get(-1, used.id, 4) ?: return false
                     else get(used.id,with.id,type.ordinal) ?:
                     if (type == IntType.ITEM)
                         get(with.id,used.id,type.ordinal).also { flipped = true } ?: return false
                     else return false

        val destOverride = if(flipped) {
            getOverride(type.ordinal, used.id, "use") ?: getOverride(type.ordinal, with.id) ?: getOverride(type.ordinal, "use")
        } else {
            getOverride(type.ordinal, with.id, "use") ?: getOverride(type.ordinal, used.id) ?: getOverride(type.ordinal, "use")
        }


        if(type != IntType.ITEM && !isUseWithInstant(method)) {
            if(player.locks.isMovementLocked) return false
            player.pulseManager.run(object : MovementPulse(player, with, flag, destOverride) {
                override fun pulse(): Boolean {
                    if (player.zoneMonitor.useWith(used.asItem(), with)) {
                        return true
                    }
                    player.faceLocation(with.location)
                    if(flipped) player.dispatch(UseWithEvent(with.id, used.id))
                    else player.dispatch(UseWithEvent(used.id, with.id))
                    if(flipped) method.invoke(player,with,used)
                    else method.invoke(player,used,with)
                    return true
                }
            })
        } else {
            if(flipped) player.dispatch(UseWithEvent(with.id, used.id))
            else player.dispatch(UseWithEvent(used.id, with.id))
            if(flipped) method.invoke(player,with,used)
            else method.invoke(player,used,with)
        }
        return true
    }

    @JvmStatic
    fun run(id: Int, type: IntType, option: String, player: Player, node: Node): Boolean{
        val flag = when(type){
            IntType.PLAYER -> DestinationFlag.ENTITY
            IntType.GROUNDITEM -> DestinationFlag.ITEM
            IntType.NPC -> DestinationFlag.ENTITY
            IntType.SCENERY -> null
            else -> DestinationFlag.OBJECT
        }

        if(player.locks.isInteractionLocked) return false

        val method = get(id,type.ordinal,option) ?: get(option,type.ordinal) ?: return false
        val destOverride = getOverride(type.ordinal, id, option) ?: getOverride(type.ordinal,node.id) ?: getOverride(type.ordinal,option.toLowerCase())

        player.setAttribute("interact:option", option)

        if(option.toLowerCase() == "attack") //Attack needs special handling >.>
        {
            player.dispatch(InteractionEvent(node, option.toLowerCase()))
            method.invoke(player, node)
            return true
        }

        if(type != IntType.ITEM && !isInstant(method)) {
            if(player.locks.isMovementLocked) return false
            player.pulseManager.run(object : MovementPulse(player, node, flag, destOverride) {
                override fun pulse(): Boolean {
                    if(player.zoneMonitor.interact(node, Option(option, 0))) return true
                    player.faceLocation(node.location)
                    player.dispatch(InteractionEvent(node, option.toLowerCase()))
                    method.invoke(player,node)
                    return true
                }
            })
        } else {
            method.invoke(player,node)
            player.dispatch(InteractionEvent(node, option.toLowerCase()))
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

    fun add(type: Int, with: IntArray, handler: (Player, Node, Node) -> Boolean) {
        for (w in with) {
            useAnyWithListeners["$w:$type"] = handler
        }
    }

    fun isInstant(handler: (Player, Node) -> Boolean): Boolean {
        val className = handler.javaClass.name.substringBefore("$")
        return instantClasses.contains(className)
    }

    fun isUseWithInstant(handler: (player: Player, used: Node, with: Node) -> Boolean): Boolean {
        val className = handler.javaClass.name.substringBefore("$")
        return instantClasses.contains(className)
    }
}
