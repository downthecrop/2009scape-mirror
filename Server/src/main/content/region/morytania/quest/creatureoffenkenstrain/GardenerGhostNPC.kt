package content.region.morytania.quest.creatureoffenkenstrain

import core.api.teleport
import core.game.interaction.MovementPulse
import core.game.node.entity.impl.PulseType
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.path.Pathfinder
import core.plugin.Initializable
import core.tools.secondsToTicks
import org.rs09.consts.NPCs

@Initializable
class GardenerGhostNPC : AbstractNPC {
    var target: Player? = null
    var ticksLeft = secondsToTicks(0)
    val graveLocation = Location(3608, 3490)

    constructor() : super(NPCs.GARDENER_GHOST_1675, null, true) {}
    private constructor(id: Int, location: Location) : super(id, location) {}

    override fun construct(id: Int, location: Location, vararg objects: Any?): AbstractNPC {
        return GardenerGhostNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GARDENER_GHOST_1675)
    }

    override fun handleTickActions() {
        if (target == null) {
            super.handleTickActions()
        } else {
            val distance = location.getDistance(target!!.location)
            if (distance > 10) {
                teleport(this, target!!.location)
            }
            if (ticksLeft > 0) {
                ticksLeft--
            }

            if (this.location.withinDistance(graveLocation, 5) && ticksLeft > secondsToTicks(5)) {
                // If gardener is about to reach his grave.
                ticksLeft = secondsToTicks(4)
                sendChat("Here is the place where I met me' maker.")
            }
            if (!properties.spawnLocation.withinDistance(target!!.location, 160) && ticksLeft > secondsToTicks(5)) {
                // If person is about to walk outside of the range
                ticksLeft = secondsToTicks(4)
                sendChat("Fare thee well - oi must return to me' garden.")
            }
            if (ticksLeft == secondsToTicks(5)) {
                // https://www.youtube.com/watch?v=RoR6zLGrRoY 2:07
                sendChat("Fare thee well - oi must return to me' garden.")
            }
            if (ticksLeft == 0) {
                // No time left, reset him back.
                target = null
                pulseManager.clear(PulseType.STANDARD)
                walkRadius = 11
                teleport(properties.spawnLocation)
            }
        }
    }

    fun startFollowing(player: Player) {
        ticksLeft = secondsToTicks(120) // You have two minutes to let him show you where his grave is.
        target = player
        walkRadius = 200

        pulseManager.run((object : MovementPulse(this, player, Pathfinder.DUMB) {
            override fun pulse(): Boolean {
                face(player)
                return false
            }
        }), PulseType.STANDARD)
    }

    fun continueFollowing(player: Player) {
        pulseManager.run((object : MovementPulse(this, player, Pathfinder.DUMB) {
            override fun pulse(): Boolean {
                face(player)
                return false
            }
        }), PulseType.STANDARD)
    }
}
