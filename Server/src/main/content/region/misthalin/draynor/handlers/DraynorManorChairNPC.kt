package content.region.misthalin.draynor.handlers

import core.api.hasLineOfSight
import core.game.interaction.MovementPulse
import core.game.node.entity.Entity
import core.game.node.entity.impl.PulseType
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.path.Pathfinder
import core.plugin.Initializable

/**
 * Represents the Draynor Manor Chair NPC, these are the chairs that follow the player
 * around the manor.
 *
 * @author Broseki
 */

private const val DRAYNOR_MANOR_CHAIR_NPC_ID = 3293
private const val FOLLOWING_DISTANCE = 5
private const val STOP_FOLLOWING_DISTANCE = 30

@Initializable
class DraynorManorChairNPC(id: Int = DRAYNOR_MANOR_CHAIR_NPC_ID, location: Location? = null) :
    AbstractNPC(id, location) {

    // The player this NPC is currently following, null if nobody is being followed
    private var following: Player? = null

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return DraynorManorChairNPC(id, location)
    }

    override fun handleTickActions() {
        super.handleTickActions()
        val closestPlayer = findClosestPlayer()
        // If there is a player nearby, follow them
        if (closestPlayer != null && following != closestPlayer) {
            stopFollowing()
            following = closestPlayer
            follow(closestPlayer)
            following?.let { face(it) }
        } else {
            // If we didn't find a player within `FOLLOWING_DISTANCE`
            // but we are following a player, check that they are still
            // within `STOP_FOLLOWING_DISTANCE`, if they are gone, stop
            // trying to follow them.
            following?.let { player ->
                if (findDistanceToPlayer(player) > STOP_FOLLOWING_DISTANCE
                    || !player.isActive
                    || player.isInvisible) {
                    stopFollowing()
                } else {
                    if (!pulseManager.hasPulseRunning()) {
                        follow(player)
                    }
                    face(player)
                }
            }
        }
    }

    /**
     * Stops following `following`
     */
    fun stopFollowing() {
        following = null
        resetWalk()
        pulseManager.clear(PulseType.STANDARD)
    }

    /**
     * Finds the closest player to the current entity within `FOLLOWING_DISTANCE`
     * that is currently in the chair's line of sight.
     *
     * @return The Player object representing the closest player, or null if there are no players nearby.
     */
    fun findClosestPlayer(): Player? {
        val players = RegionManager.getLocalPlayers(this, FOLLOWING_DISTANCE)
        if (players.isEmpty()) {
            return null
        }
        
        var closestPlayer: Player? = null
        var closestDistance = Double.MAX_VALUE
        
        for (player in players) {
            // Make sure the chair does not try to start
            // following a player in another room
            if (!hasLineOfSight(this, player)) {
                continue
            }

            // If the player is invisible, don't follow them
            if (player.isInvisible) {
                continue
            }

            val distance = findDistanceToPlayer(player)
            if (distance < closestDistance) {
                closestDistance = distance
                closestPlayer = player
            }
        }
        
        return closestPlayer
    }

    /**
     * Calculates the distance between the current entity and the specified player.
     *
     * @param player The player whose distance from the current entity is to be calculated.
     * @return The distance between the current entity and the specified player as a Double value.
     */
    fun findDistanceToPlayer(player: Player): Double {
        return this.location.getDistance(player.location)
    }

    /**
     * Triggers the current entity to follow the specified player using a movement pulse.
     *
     * @param player The player that the entity should follow.
     */
    fun follow(player: Player) {
        pulseManager.run((object : MovementPulse(this, player, Pathfinder.DUMB) {
            override fun pulse(): Boolean {
                return false
            }
        }), PulseType.STANDARD)
    }

    override fun getIds(): IntArray {
        return intArrayOf(DRAYNOR_MANOR_CHAIR_NPC_ID)
    }

    override fun shouldPreventStacking(mover: Entity?): Boolean {
        return mover is DraynorManorChairNPC
    }
}