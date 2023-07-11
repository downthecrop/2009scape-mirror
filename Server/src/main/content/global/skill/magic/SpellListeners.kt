package content.global.skill.magic

import core.game.event.SpellCastEvent
import core.api.*
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager
import core.tools.Log
import core.tools.SystemLogger
import core.game.interaction.*
import core.game.world.map.path.Pathfinder

object SpellListeners {
    val castMap = HashMap<String,(Player, Node?) -> Unit>()
    val spellRanges = HashMap<String, Int>()

    fun add(spellID: Int, type: Int, book: String, distance: Int, method: (Player,Node?) -> Unit){
        castMap["$book:$spellID:$type"] = method
        spellRanges["$book:$spellID:$type"] = distance
    }

    fun add(spellID: Int, type: Int, ids: IntArray, book: String, distance: Int, method: (Player, Node?) -> Unit){
        for(id in ids) {
            castMap["$book:$spellID:$type:$id"] = method
            spellRanges["$book:$spellID:$type:$id"] = distance
        }
    }

    fun get(spellID: Int, type: Int, book: String): Pair<Int, ((Player,Node?) -> Unit)?> {
        log(this::class.java, Log.FINE, "Getting $book:$spellID:$type")
        return Pair (spellRanges["$book:$spellID:$type"] ?: 10, castMap["$book:$spellID:$type"])
    }

    fun get(spellID: Int, type: Int, id: Int, book: String): Pair<Int, ((Player,Node?) -> Unit)?> {
        log(this::class.java, Log.FINE, "Getting $book:$spellID:$type:$id")
        return Pair (spellRanges["$book:$spellID:$type:$id"] ?: 10, castMap["$book:$spellID:$type:$id"])
    }

    @JvmStatic
    fun run(button: Int, type: Int, book: String, player: Player, node: Node? = null){
        var (range, method) = get (button, type, node?.id ?: 0, book)
        if (method == null) {
            var next = get (button, type, book)
            range = next.first
            method = next.second ?: return
        }

        if (type in intArrayOf (SpellListener.NPC, SpellListener.OBJECT, SpellListener.PLAYER, SpellListener.GROUND_ITEM)) {
            player.pulseManager.run (object : MovementPulse (player, node, Pathfinder.SMART) {
                override fun pulse() : Boolean {
                    try {
                        method?.invoke (player, node)
                    } catch (e: IllegalStateException) {
                        player.removeAttribute ("spell:runes")
                        return true
                    }
                    return true
                }

                override fun update () : Boolean {
                    if (player.location.withinMaxnormDistance (node!!.centerLocation, range) && hasLineOfSight (player, node!!)) {
                        player.faceLocation (node.getFaceLocation(player.location))
                        player.walkingQueue.reset()
                        pulse()
                        stop()
                        return true
                    }
                    return super.update()
                }
            })
        } else {
            try {
                method?.invoke(player, node)
                player.dispatch(SpellCastEvent(SpellBookManager.SpellBook.valueOf(book.uppercase()), button, node))
            } catch (e: IllegalStateException){
                player.removeAttribute("spell:runes")
                return
            }
        }
    }
}
