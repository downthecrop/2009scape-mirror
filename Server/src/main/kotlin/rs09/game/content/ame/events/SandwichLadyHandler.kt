package rs09.game.content.ame.events

import core.game.component.Component
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener

class SandwichLadyHandler : InteractionListener(){

    val SANDWICH_LADY = NPCs.SANDWICH_LADY_3117
    override fun defineListeners() {

        on(SANDWICH_LADY,NPC,"talk-to"){player, node ->
            if(player.antiMacroHandler?.hasEvent()!! && player.antiMacroHandler.event.name == "Sandwich Lady") {
                player.interfaceManager?.open(Component(297))
                node.asNpc().clear()
            } else {
                player.sendMessage("She isn't interested in you.")
            }
            return@on true
        }
    }
}