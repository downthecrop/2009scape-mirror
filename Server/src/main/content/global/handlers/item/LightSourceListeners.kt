package content.global.handlers.item

import content.data.LightSource
import core.api.*
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import org.rs09.consts.Items
import kotlin.collections.toIntArray

class LightSourceListeners : InteractionListener {
    override fun defineListeners() {
        val lightSourceProducts = LightSource.values().map { it.product.id }.toIntArray()

        on(lightSourceProducts, ITEM, "drop") { player, light ->
            val active = LightSource.getActiveLightSource(player).product.id
            if (player.location.isInRegion(10648) && light.id == active) { //Temple of Ikov dungeon
                sendMessage(player, "Dropping the " + LightSource.getActiveLightSource(player).product.name.lowercase() + " would leave you without a light source.")
                return@on false
            }
            val removed = removeItem(player, light.id)
            if (removed) {
                var droppedId = light.id
                if (light.id == Items.MINING_HELMET_5013) {
                    droppedId = Items.MINING_HELMET_5014
                    sendMessage(player, "The helmet goes out as you drop it.")
                }
                produceGroundItem(player, droppedId)
            }
            return@on true
        }
    }
}