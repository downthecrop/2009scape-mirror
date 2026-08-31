package content.region.morytania.quest.hauntedmine

import core.api.*
import core.game.activity.Cutscene
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.world.map.Location

/**
 * Haunted Mine cutscene for the levers and minecarts.
 */

// Minecart cutscene: https://youtu.be/PMn0LRo4MCo?si=pi6gGDFnlgW_TPX7&t=498

// The authentic version of this cutscene has minecarts moving and shows interface updates.
// Our cutscene utils struggle as the moving minecarts can't path over the non-walkable tiles.
// I also can't get the interface working well.
// For now, I just show the camera angles.

class PointsSettingsCutscene(player: Player) : Cutscene(player) {

    override fun setup() {
        setExit(Location(2769, 4521))
        loadRegion(11078)
        closeOverlay()

        setAttribute(player, HauntedMine.ATTR_KEEP_FUNGUS, true)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                teleport(player, 17, 41)
                // put camera at start spot
                moveCamera(26, 30, 300, 1000)
                rotateCamera(26, 29, 300, 1000)
                timedUpdate(3)
            }

            1 -> {
                // push camera back a little and move the cart (if cart movement worked well)
                moveCamera(26, 34, 300, 5)
                timedUpdate(5)
            }

            2 -> {
                // cut the camera to the end of the path as cart arrives (cart does not arrive cause i can't move it on these tiles.
                moveCamera(28, 56, 300, 1000)
                rotateCamera(26, 55, 300, 1000)
                timedUpdate(3)
            }

            3 -> {
                // pan towards the end cart (replacing the authentic version where the cart is shown moving towards the end)
                moveCamera(28, 53, 300, 5)
                timedUpdate(5)
            }

            4 -> {
                // end cutscene and play player dialogue
                endWithoutFade {
                    sendPlayerDialogue(player, "I wonder if that cart is anywhere useful now?", FacialExpression.THINKING)
                    removeAttribute(player, HauntedMine.ATTR_KEEP_FUNGUS)
                }
            }
        }
    }
}