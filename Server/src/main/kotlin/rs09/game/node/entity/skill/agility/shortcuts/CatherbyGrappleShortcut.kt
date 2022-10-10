package rs09.game.node.entity.skill.agility.shortcuts

import api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import org.rs09.consts.Scenery
import rs09.game.interaction.IntType
import rs09.game.interaction.InteractionListener

/**
 * Handles the Catherby to Taverley grapple shortcut
 * @author Byte
 */
class CatherbyGrappleShortcut : InteractionListener {

    companion object {
        private val START_LOCATION: Location = Location.create(2866, 3429, 0)
        private val END_LOCATION: Location = Location.create(2869,3430,0)

        private val REQUIREMENTS = hashMapOf(
            Skills.AGILITY to 32,
            Skills.RANGE to 35,
            Skills.STRENGTH to 35
        )

        private val VALID_CROSSBOWS = intArrayOf(
            Items.MITH_CROSSBOW_9181,
            Items.ADAMANT_CROSSBOW_9183,
            Items.RUNE_CROSSBOW_9185,
            Items.DORGESHUUN_CBOW_8880
        )
    }

    private var rocks = getScenery(Location.create(2869,3429, 0))

    override fun defineListeners() {
        flagInstant() // execute listeners instantly without determining path

        on(Scenery.ROCKS_17042, IntType.SCENERY, "grapple") { player, _ ->
            if (isPlayerInRangeToGrapple(player)) {
                forceWalk(player, START_LOCATION, "smart")
            } else {
                sendMessage(player, "Nothing interesting happens.")
                return@on true
            }

            if (!doesPlayerHaveRequiredItemsEquipped(player)) {
                sendDialogue(player, "You need a Mithril crossbow and a Mithril grapple in order to do this.")
                return@on true
            }

            if (!doesPlayerHaveRequiredLevels(player)) {
                sendDialogueLines(player,
                    "You need at least " +
                        REQUIREMENTS[Skills.AGILITY] + " " + Skills.SKILL_NAME[Skills.AGILITY] + ", " +
                        REQUIREMENTS[Skills.RANGE] + " " + Skills.SKILL_NAME[Skills.RANGE] + ", ",
                    "and " +
                        REQUIREMENTS[Skills.STRENGTH] + " " + Skills.SKILL_NAME[Skills.STRENGTH] + " to use this shortcut."
                )
                return@on true
            }

            lock(player, 15)
            submitWorldPulse(object : Pulse(2) {
                var counter = 0
                override fun pulse() : Boolean {
                    when (counter++) {
                        1 -> {
                            face(player, END_LOCATION)
                            // Audit: shows player climbing (probably a wall), need a cliff climb animation
                            animate(player, Animation(4455))
                        }
                        3 -> {
                            // Audit: shows grapple on rocks, but there is no rope
                            replaceScenery(rocks!!, rocks!!.id + 1, 10)
                        }
                        8 -> {
                            teleport(player, END_LOCATION)
                        }
                        9 -> {
                            sendMessage(player, "You successfully grapple the rock and climb the cliffside.")
                            unlock(player)
                            return true
                        }
                    }
                    return false
                }
            })

            return@on true
        }
    }

    private fun doesPlayerHaveRequiredItemsEquipped(player: Player): Boolean {
        return isEquipped(player, Items.MITH_GRAPPLE_9419) && areAnyEquipped(player, *VALID_CROSSBOWS)
    }

    private fun doesPlayerHaveRequiredLevels(player: Player): Boolean {
        for ((skill, requiredLevel) in REQUIREMENTS) {
            if (!hasLevelDyn(player, skill, requiredLevel)) {
                return false
            }
        }
        return true
    }

    private fun isPlayerInRangeToGrapple(player: Player): Boolean {
        return inBorders(player,START_LOCATION.x - 2, START_LOCATION.y - 2, START_LOCATION.x, START_LOCATION.y)
    }
}
