package api.events

import core.game.node.entity.Entity

interface EventHook<T: Event> {
    fun process(entity: Entity, event: T)
}