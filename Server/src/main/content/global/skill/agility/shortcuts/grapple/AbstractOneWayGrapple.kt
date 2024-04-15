package content.global.skill.agility.shortcuts.grapple

import core.api.*
import core.game.node.entity.player.Player

abstract class AbstractOneWayGrapple : AbstractGrappleShortcut() {


    override fun animation(animationStage: Int, player: Player): Boolean {
        when (animationStage) {
            1 -> {
                // Point towards the grapple landing zone
                face(player, grappleEndLocation)
                // Start the grapple animation
                animate(player, animation)
            }

            5 -> {
                for (tgt in grappleScenery) {
                    // Add grapple effects to all scenery (for 10 ticks)
                    replaceScenery(tgt!!, tgt.id + 1, 10)
                }
            }

            5 + animationDuration -> {
                // After the animation is done teleport to the landing zone
                teleport(player, grappleEndLocation)
            }

            5 + animationDuration + 1 -> {
                // free the player
                unlock(player)
                updateDiary(player)
                return true
            }
        }
        return false
    }
}