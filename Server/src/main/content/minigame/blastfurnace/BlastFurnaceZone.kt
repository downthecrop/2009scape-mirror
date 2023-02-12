package content.minigame.blastfurnace

import core.api.*
import core.game.node.entity.Entity
import core.game.node.item.Item
import core.game.shops.Shops
import core.game.world.map.zone.ZoneBorders
import core.tools.secondsToTicks
import org.rs09.consts.NPCs
import kotlin.math.min

/**Code for defining the Blast Furnace zone, Blast Furnace will only
 * operate and run its logic if there are actual players in this zone
 * @author phil lips, ceikry*/

class BlastFurnaceZone : MapArea, TickListener {
    var pulseStarted = false
    var lastShopRestock = 0

    override fun defineAreaBorders(): Array<ZoneBorders> {
       return arrayOf(ZoneBorders(1935,4956,1956,4974))
    }

    override fun areaEnter(entity: Entity) {
        if (!pulseStarted){
            submitWorldPulse(BlastFurnace.blastPulse)
            pulseStarted = true
        }
        if (entity.isPlayer) {
            BlastFurnace.blastFurnacePlayerList.add(entity.asPlayer())
        }
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity.isPlayer) {
            BlastFurnace.blastFurnacePlayerList.remove(entity.asPlayer())
        }
    }

    override fun tick() {
        if (getWorldTicks() - lastShopRestock > secondsToTicks(600)) {
            lastShopRestock = getWorldTicks()
            restockRandomOre()
        }
    }

    private fun restockRandomOre() {
        val shop = Shops.shopsByNpc[NPCs.ORDAN_2564] ?: return
        val stockContainer = shop.stockInstances.values.firstOrNull() ?: return
        val restockThisTick = shop.stock.random()
        stockContainer.add(
            Item(
                restockThisTick.itemId,
                min(30, 100 - stockContainer.getAmount(restockThisTick.itemId))
            )
        )
    }
}