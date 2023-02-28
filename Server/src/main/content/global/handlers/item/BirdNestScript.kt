package content.global.handlers.item

import content.data.tables.BirdNest
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item

class BirdNestScript : InteractionListener {
    val nestIds = BirdNest.values().map { it.nest.id }.toIntArray()

    override fun defineListeners() {
        on(nestIds, IntType.ITEM, "search", handler = ::handleNest)
    }

    private fun handleNest(player: Player, node: Node) : Boolean {
        val nest = BirdNest.forNest(node as? Item ?: return false)
        nest.search(player, node as? Item ?: return false)
        return true
    }
}