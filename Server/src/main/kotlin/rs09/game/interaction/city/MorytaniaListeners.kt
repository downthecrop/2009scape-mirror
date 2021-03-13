package rs09.game.interaction.city

import core.game.node.entity.impl.ForceMovement
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import rs09.game.interaction.OptionListener

/**
 * File to be used for anything Morytania related.
 * Handles the summoning/altar cave enter and exit in Morytania.
 * @author Sir Kermit
 */

class MorytaniaListeners : OptionListener() {

    val GROTTO_ENTRANCE = 3516
    val GROTTO_EXIT = 3526
    val GROTTO_BRIDGE = 3522
    val outside = Location.create(3439, 3337, 0)
    val inside = Location.create(3442, 9734, 1)
    private val RUNNING_ANIM = Animation(1995)
    private val JUMP_ANIM = Animation(1603)

    override fun defineListeners() {
        on(GROTTO_ENTRANCE,OBJECT,"enter"){player,node ->
            player.teleport(inside)
            return@on true
        }

        on(GROTTO_EXIT,OBJECT,"exit"){player,node ->
            player.teleport(outside)
            return@on true
        }

        on(GROTTO_BRIDGE,OBJECT,"jump"){player,node ->
            if (player.location.y == 3328) {
                ForceMovement.run(player, node.location, node.location.transform(0, 3, 0), RUNNING_ANIM, JUMP_ANIM, Direction.NORTH, 15).endAnimation = Animation.RESET
            } else if (player.location.y == 3332){
                ForceMovement.run(player, node.location, node.location.transform(0, -3, 0), RUNNING_ANIM, JUMP_ANIM, Direction.SOUTH, 15).endAnimation = Animation.RESET
            }
            return@on true
        }
    }
}