package content.global.handlers.item

import content.data.tables.BirdNest
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item

class BirdNestListener : InteractionListener {
    val nestIds = BirdNest.values().map { it.nest.id }.toIntArray()

    override fun defineListeners() {
        on(nestIds, IntType.ITEM, "search") { player, node ->
            BirdNest.search(player, node as Item)
            return@on true
        }
    }
}