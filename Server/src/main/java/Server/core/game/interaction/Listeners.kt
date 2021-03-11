package core.game.interaction

import core.game.node.Node
import core.game.node.entity.player.Player
import core.tools.StringUtils

object Listeners {
    private val listeners = HashMap<Long,(Player, Node) -> Boolean>()

    @JvmStatic
    fun add(id: Int, type: Int, option: String, method: (Player,Node) -> Boolean){
        val key = (StringUtils.stringToLong(option.toLowerCase()) + id) shl type
        listeners[key] = method
    }

    @JvmStatic
    fun get(id: Int, type: Int, option: String): ((Player,Node) -> Boolean)?{
        return listeners[(StringUtils.stringToLong(option.toLowerCase()) + id) shl type]
    }

    @JvmStatic
    fun run(id: Int, type: Int, option: String, player: Player, node: Node): Boolean{
        val flag = when(type){
            2 -> DestinationFlag.ENTITY
            1 -> DestinationFlag.OBJECT
            else -> DestinationFlag.OBJECT
        }

        val method = get(id,type,option) ?: return false

        if(type != 0) {
            player.pulseManager.run(object : MovementPulse(player, node, flag) {
                override fun pulse(): Boolean {
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