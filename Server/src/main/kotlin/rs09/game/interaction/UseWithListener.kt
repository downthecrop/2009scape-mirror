package rs09.game.interaction

import core.game.node.Node
import core.game.node.entity.player.Player

abstract class UseWithListener : Listener {
    val ITEM = 0
    val OBJECT = 1
    val NPC = 2
    fun on(used: Int, with: Int, type: Int, handler: (Player, Node, Node) -> Boolean){
        Listeners.add(used,with,type,handler)
    }
    fun on(type: Int,used: Int,vararg with: Int, handler: (Player, Node, Node) -> Boolean){
        Listeners.add(type,used,with,handler)
    }
}