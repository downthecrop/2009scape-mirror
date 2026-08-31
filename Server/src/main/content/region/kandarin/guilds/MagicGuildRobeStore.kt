package content.region.kandarin.guilds

import core.api.Event
import core.api.MapArea
import core.api.addItemOrDrop
import core.game.event.EventHook
import core.game.event.ItemShopPurchaseEvent
import core.game.global.Skillcape
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.shops.Shops
import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class MagicGuildRobeStore : InteractionListener, MapArea {

    override fun defineListeners() {
        on(NPCs.ROBE_STORE_OWNER_1658, IntType.NPC, "trade") { player, _ ->
            openRobeStore(player)
            return@on true
        }
    }

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(ZoneBorders(2585, 3082, 2596, 3093, 1))
    }

    override fun areaEnter(entity: Entity) {
        if (entity is Player) {
            entity.hook(Event.ItemPurchased, MagicCapePurchaseHook)
        }
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity is Player) {
            entity.unhook(MagicCapePurchaseHook)
        }
    }

    private object MagicCapePurchaseHook : EventHook<ItemShopPurchaseEvent> {
        override fun process(entity: Entity, event: ItemShopPurchaseEvent) {
            if (entity !is Player || (event.itemId != Items.MAGIC_CAPE_9762 && event.itemId != Items.MAGIC_CAPET_9763)) return
            addItemOrDrop(entity, Items.MAGIC_HOOD_9764, event.amount)
        }
    }

    companion object {
        private const val ROBE_SHOP_ID = 257

        @JvmStatic
        fun openRobeStore(player: Player) {
            val shop = Shops.shopsById[ROBE_SHOP_ID] ?: return
            shop.openFor(player) { viewer, item ->
                when {
                    item.id != Items.MAGIC_CAPE_9762 -> item
                    !Skillcape.isMaster(viewer, Skills.MAGIC) -> null
                    viewer.skills.masteredSkills > 1 -> Item(Items.MAGIC_CAPET_9763, item.amount)
                    else -> item
                }
            }
        }
    }
}
