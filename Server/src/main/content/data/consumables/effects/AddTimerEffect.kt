package content.data.consumables.effects

import core.api.registerTimer
import core.api.removeTimer
import core.api.spawnTimer
import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player

class AddTimerEffect (val identifier: String, vararg val args: Any) : ConsumableEffect() {
    override fun activate (p: Player) {
        removeTimer (p, identifier)
        val timer = spawnTimer (identifier, *args) ?: return
        registerTimer (p, timer)
    }
}
