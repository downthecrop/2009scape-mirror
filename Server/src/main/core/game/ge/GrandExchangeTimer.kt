package core.game.ge

import core.api.hasAwaitingGrandExchangeCollections
import core.api.playJingle
import core.api.sendMessage
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.timer.RSTimer

class GrandExchangeTimer : RSTimer(500, "GE periodic poll", isSoft = true, isAuto = true) {
    override fun run(entity: Entity) : Boolean {
        if (entity !is Player) return false
        val player = entity
        val records = GrandExchangeRecords.getInstance(player)
        if (records.updateNotification) {
            records.updateNotification = false
            if (hasAwaitingGrandExchangeCollections(player)) {
                sendMessage(player, "One or more of your Grand Exchange offers have been updated.")
                playJingle(player, 284)
            }
        }
        return true
    }
}
