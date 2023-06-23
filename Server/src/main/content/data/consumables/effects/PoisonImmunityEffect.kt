package content.data.consumables.effects

import core.api.*
import core.game.system.timer.impl.PoisonImmunity
import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.game.node.entity.state.EntityState

class PoisonImmunityEffect (val ticks: Int) : ConsumableEffect() {
    override fun activate (p: Player) {
        val timer = PoisonImmunity.getTimer (ticks)
        registerTimer (p, timer)
    }
}
