package core.game.content.quest.members.thefremenniktrials

import core.game.interaction.NodeUsageEvent
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import core.game.content.quest.PluginInteraction
import core.game.content.quest.PluginInteractionManager

@Initializable
class TinderboxOnBomb : PluginInteraction(3713){

    override fun handle(player: Player?, event: NodeUsageEvent?): Boolean {
        val with = event?.usedWith
        if(with is Item && with.id.equals(590)){
            player?.inventory?.remove(event.used as Item)
            player?.inventory?.add(Item(3714))
            player?.sendMessage("You light the strange object.")
            return true
        }
        return false
    }

    override fun fireEvent(identifier: String?, vararg args: Any?): Any {
        return Unit
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        PluginInteractionManager.register(this,PluginInteractionManager.InteractionType.USEWITH)
        return this
    }

}