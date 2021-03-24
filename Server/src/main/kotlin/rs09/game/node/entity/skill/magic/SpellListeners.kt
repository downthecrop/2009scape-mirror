package rs09.game.node.entity.skill.magic

import core.game.node.Node
import core.game.node.entity.player.Player
import rs09.game.system.SystemLogger

object SpellListeners {
    val castMap = HashMap<String,(Player, Node?) -> Unit>()

    fun add(spellID: Int, type: Int, book: String, method: (Player,Node?) -> Unit){
        castMap["$book:$spellID:$type"] = method
    }

    fun add(spellID: Int, type: Int, ids: IntArray, book: String, method: (Player, Node?) -> Unit){
        for(id in ids) {
            castMap["$book:$spellID:$type:$id"] = method
        }
    }

    fun get(spellID: Int, type: Int, book: String): ((Player,Node?) -> Unit)?{
        SystemLogger.logInfo("Getting $book:$spellID:$type")
        return castMap["$book:$spellID:$type"]
    }

    fun get(spellID: Int, type: Int, id: Int, book: String): ((Player,Node?) -> Unit)?{
        SystemLogger.logInfo("Getting $book:$spellID:$type:$id")
        return castMap["$book:$spellID:$type:$id"]
    }

    @JvmStatic
    fun run(button: Int, type: Int, book: String, player: Player, node: Node? = null){
        val method = get(button,type,node?.id ?: 0,book) ?: get(button,type,book) ?: return
        try {
            method.invoke(player, node)
        } catch (e: IllegalStateException){
            player.removeAttribute("spell:runes")
            return
        }
    }
}