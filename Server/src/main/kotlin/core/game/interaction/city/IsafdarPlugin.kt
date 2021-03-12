package core.game.interaction.city

import core.cache.def.impl.ObjectDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.`object`.GameObject
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin

/**
 * File to be used for anything Isafdar related.
 * Handles the summoning/altar cave enter and exit in Isafdar.
 * @author Sir Kermit
 */

@Initializable
class IsafdarPlugin : OptionHandler() {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any?> {

        //Outside Cave
        ObjectDefinition.forId(4006).handlers["option:enter"] = this

        //Inside Cave
        ObjectDefinition.forId(4007).handlers["option:exit"] = this

        return this

    }

    override fun handle(player: Player, node: Node, option: String): Boolean {

        val id = (node as GameObject).id
        val outside = Location.create(2312, 3217, 0)
        val inside = Location.create(2314, 9624, 0)

        when (id) {
            //Handles sending the player inside of the cave.
            4006 -> {
                if (node.id == 4006) {
                    player.teleport(inside)
                    return true
                }
            }
            //Handles sending the player outside of the cave.
            4007 -> {
                if (node.id == 4007) {
                    player.teleport(outside)
                    return true
                }
            }
        }
        return false
    }

}