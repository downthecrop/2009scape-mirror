package content.global.handlers.scenery

import core.api.addItem
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import org.rs09.consts.Items

class DoogleLeafInteraction : InteractionListener {
    override fun defineListeners() {
         defineInteraction(IntType.SCENERY, intArrayOf(31155), "pick-leaf", handler = ::handleDoogle)
    }

    fun handleDoogle(player: Player, node: Node, state: Int) : Boolean {
        if (!addItem(player, Items.DOOGLE_LEAVES_1573)) {
            sendMessage(player, "You don't have enough space in your inventory.")
            return true
        }
        sendMessage(player, "You pick some doogle leaves.")
        return true
    }
}