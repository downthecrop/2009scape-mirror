package core.game.content.ame.events

import core.game.component.Component
import core.game.interaction.DestinationFlag
import core.game.interaction.MovementPulse
import core.game.interaction.Option
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.plugin.Initializable
import core.plugin.Plugin
import core.game.content.quest.PluginInteraction
import core.game.content.quest.PluginInteractionManager

@Initializable
class SandwichLadyHandler : PluginInteraction(3117){
    override fun handle(player: Player?, npc: NPC?, option: Option?): Boolean {
        class MoveTo : MovementPulse(player,DestinationFlag.ENTITY.getDestination(player,npc)){
            override fun pulse(): Boolean {
                if(player?.antiMacroHandler?.hasEvent()!! && player.antiMacroHandler.event.name == "Sandwich Lady") {
                    player.interfaceManager?.open(Component(297))
                    npc?.clear()
                } else {
                    player.sendMessage("She isn't interested in you.")
                }
                return true
            }
        }

        if(option?.name?.toLowerCase() == "talk-to"){
            GameWorld.submit(MoveTo())
            return true
        }
        return false
    }

    override fun fireEvent(identifier: String?, vararg args: Any?): Any {
        return Unit
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        PluginInteractionManager.register(this,PluginInteractionManager.InteractionType.NPC)
        return this
    }

}