package content.global.handlers.item

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.TeleportManager
import core.game.node.item.Item

class HouseTeleTabOptionListener : InteractionListener {
    override fun defineListeners() {
        val homeTabID = 8013
        on(homeTabID, IntType.ITEM, "break") {player, node ->
            var location = player.houseManager.location.exitLocation
            if (location == null) {
                sendMessage( player, "You must have a house to teleport to before attempting that.")
                return@on false
            }
            closeInterface(player)
            lock(player, 5)
            if (inInventory(player, node.id)) {
                if (teleport(player, location, TeleportManager.TeleportType.TELETABS)) {
                    removeItem(player, Item(node.id, 1))
                }
            }
            return@on true
        }
    }
}