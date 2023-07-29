package content.data.consumables.effects

import core.api.*
import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player

class RemoveTimerEffect (val identifier: String) : ConsumableEffect() {
    override fun activate (p: Player) {
        removeTimer (p, identifier)
    }
}
