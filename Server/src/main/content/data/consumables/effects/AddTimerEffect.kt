package content.data.consumables.effects

import core.api.*
import core.game.system.timer.impl.PoisonImmunity
import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player

class AddTimerEffect (val identifier: String, vararg val args: Any) : ConsumableEffect() {
    override fun activate (p: Player) {
        val timer = spawnTimer (identifier, args) ?: return
        registerTimer (p, timer)
    }
}
