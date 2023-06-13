package content.global.activity.cchallange

import core.api.LoginListener
import core.game.event.Event
import core.game.event.EventHook
import core.game.event.NPCKillEvent
import core.game.node.entity.Entity
import core.game.node.entity.player.Player

/**
 * Manages the champion scrolls drop.
 * @authors Phil, Skelsoft.
 */

abstract class ChampionScrollsEventHookBase : LoginListener {

    protected companion object {
        private fun <T> forEligibleEntityDo(entity: Entity, event: T, handler: (Player, T) -> Unit) {
            if (entity !is Player) return
            if (entity.isArtificial) return
            handler(entity, event)
        }
    }

    class EventHandler<T : Event>(
        private val owner: ChampionScrollsEventHookBase,
        private val handler: (Player, T) -> Unit) : EventHook<T> {
        override fun process(entity: Entity, event: T) {
            forEligibleEntityDo(entity, event, handler)
        }
    }

    final override fun login(player: Player) {
        player.hook(core.api.Event.NPCKilled, EventHandler(this, ::onNpcKilled))
    }

    protected open fun onNpcKilled(player: Player, event: NPCKillEvent) {}

}