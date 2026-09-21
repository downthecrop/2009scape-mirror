package content.region.kandarin.gnomestronghold.handlers

import core.game.global.action.ClimbActionHandler
import core.game.interaction.MovementPulse
import core.game.interaction.PluginInteraction
import core.game.interaction.PluginInteractionManager
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.Scenery

@Initializable
class GnomeStrongholdLadderListener : PluginInteraction(Scenery.LADDER_1747, Scenery.LADDER_1746) {
    private val lowerLadder = Location.create(2408, 3435, 0)
    private val upperLadder = Location.create(2408, 3435, 1)
    private val lowerLanding = Location.create(2408, 3434, 0)
    private val upperLanding = Location.create(2408, 3436, 1)

    override fun handle(player: Player, node: Node): Boolean {
        val climbingUp = node.id == Scenery.LADDER_1747 && node.location == lowerLadder
        val climbingDown = node.id == Scenery.LADDER_1746 && node.location == upperLadder
        if (!climbingUp && !climbingDown) return false
        if (player.locks.isInteractionLocked || player.locks.isMovementLocked) return true

        val approach = if (climbingUp) lowerLanding else upperLanding
        val destination = if (climbingUp) upperLanding else lowerLanding
        val animation = if (climbingUp) ClimbActionHandler.CLIMB_UP else ClimbActionHandler.CLIMB_DOWN
        player.pulseManager.run(object : MovementPulse(player, node, null, { _, _ -> approach }) {
            override fun pulse(): Boolean {
                player.faceLocation(node.location)
                ClimbActionHandler.climb(player, animation, destination)
                return true
            }
        })
        return true
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        PluginInteractionManager.register(this, PluginInteractionManager.InteractionType.OBJECT)
        return this
    }

    override fun fireEvent(identifier: String?, vararg args: Any?): Any = Unit
}
