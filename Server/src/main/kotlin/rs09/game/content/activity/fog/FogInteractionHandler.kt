package rs09.game.content.activity.fog

import core.game.content.global.action.ClimbActionHandler
import core.game.interaction.DestinationFlag
import core.game.interaction.MovementPulse
import core.game.node.Node
import core.game.node.scenery.Scenery
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin
import core.game.content.quest.PluginInteraction
import core.game.content.quest.PluginInteractionManager

@Initializable
class FogInteractionHandler : PluginInteraction(30204, 30203){

    override fun handle(player: Player?, node: Node?): Boolean {
        when(node?.id){
            30204 -> player?.pulseManager?.run(ClimbPulse(player,node as Scenery)).also { return true }
            30203 -> player?.pulseManager?.run(ClimbPulse(player, node as Scenery)).also { return true }
        }
        return false
    }

    class ClimbPulse(val player: Player,val obj: Scenery) : MovementPulse(player,obj, DestinationFlag.OBJECT){
        override fun pulse(): Boolean {
            player.faceLocation(obj.location)
            when(obj.id) {
                30204 ->  ClimbActionHandler.climbLadder(player, obj, "climb-down")
                30203 -> ClimbActionHandler.climbLadder(player,obj,"climb-up")
            }
            return true
        }
    }

    override fun fireEvent(identifier: String?, vararg args: Any?): Any {
        return Unit
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        PluginInteractionManager.register(this,PluginInteractionManager.InteractionType.OBJECT)
        return this
    }

}