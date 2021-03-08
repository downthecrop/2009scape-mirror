package core.game.interaction.city

import core.cache.def.impl.ObjectDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.`object`.GameObject
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin

/**
 * File to be used for anything Morytania related.
 * Handles the summoning/altar cave enter and exit in Morytania.
 * @author Sir Kermit
 */

@Initializable
class MorytaniaPlugin : OptionHandler() {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any?> {
        //Outside Grotto
        ObjectDefinition.forId(3516).handlers["option:enter"] = this
        //Inside Grotto
        ObjectDefinition.forId(3526).handlers["option:exit"] = this
        //Bridge Outside Grotto
        ObjectDefinition.forId(3522).handlers["option:jump"] = this
        return this
    }
    private val RUNNING_ANIM = Animation(1995)
    private val JUMP_ANIM = Animation(1603)

    override fun handle(player: Player, node: Node, option: String): Boolean {

        val id = (node as GameObject).id
        val outside = Location.create(3439, 3337, 0)
        val inside = Location.create(3442, 9734, 1)

        when (id) {
            //Handles sending the player inside of the cave.
            3516 -> {
                if (node.id == 3516) {
                    player.teleport(inside)
                    return true
                }
            }
            //Handles sending the player outside of the cave.
            3526 -> {
                if (node.id == 3526) {
                    player.teleport(outside)
                    return true
                }
            }
            //Handles Jumping over grotto bridge
            3522 -> if (player.location.y == 3328) {
                ForceMovement.run(player, node.location, node.location.transform(0, 3, 0), RUNNING_ANIM, JUMP_ANIM, Direction.NORTH, 15).endAnimation = Animation.RESET
            } else if (player.location.y == 3332){
                ForceMovement.run(player, node.location, node.location.transform(0, -3, 0), RUNNING_ANIM, JUMP_ANIM, Direction.SOUTH, 15).endAnimation = Animation.RESET
            }

        }
        return false
    }
}