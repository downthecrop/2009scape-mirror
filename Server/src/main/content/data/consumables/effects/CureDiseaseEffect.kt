package content.data.consumables.effects

import core.api.*
import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.game.system.timer.impl.Disease

class CureDiseaseEffect () : ConsumableEffect() {
    override fun activate (p: Player) {
        val existingTimer = getTimer<Disease>(p)
        if (existingTimer != null) {
            existingTimer.hitsLeft -= 9
            if (existingTimer.hitsLeft <= 0) {
                sendMessage(p, "The disease has been cured.")
                removeTimer<Disease>(p)
            }else{
                sendMessage(p,"You feel slightly better.")
            }
        }
    }
}
