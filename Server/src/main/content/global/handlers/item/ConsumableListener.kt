package content.global.handlers.item

import content.data.consumables.Consumables
import core.api.delayAttack
import core.api.getUsedOption
import core.api.getWorldTicks
import core.api.stopExecuting
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.GameWorld

class ConsumableListener : InteractionListener {
    override fun defineListeners() {
        on(IntType.ITEM, "eat", "drink", handler = ::handleConsumable)
    }

    private fun handleConsumable(player: Player, node: Node) : Boolean {
        val consumable = Consumables.getConsumableById(node.id) ?: return true

        val food = getUsedOption(player) == "eat"
        val isIgnoreMainClock = consumable.isIgnoreMainClock

        if (food) {
            if (isIgnoreMainClock && player.clocks[Clocks.NEXT_CONSUME] < GameWorld.ticks) {
                consumable.consumable.consume(node as? Item ?: return true, player)
                player.clocks[Clocks.NEXT_CONSUME] = getWorldTicks() + 2
                player.clocks[Clocks.NEXT_EAT] = getWorldTicks() + 2
                delayAttack(player, 3)
            } else if (player.clocks[Clocks.NEXT_CONSUME] < getWorldTicks() && player.clocks[Clocks.NEXT_EAT] < getWorldTicks()) {
                consumable.consumable.consume(node as? Item ?: return true, player)
                player.clocks[Clocks.NEXT_EAT] = getWorldTicks() + 2
                delayAttack(player, 3)
            }
        } else {
            if (isIgnoreMainClock && player.clocks[Clocks.NEXT_CONSUME] < getWorldTicks()) {
                consumable.consumable.consume(node as? Item ?: return true, player)
                player.clocks[Clocks.NEXT_CONSUME] = getWorldTicks() + 1
                player.clocks[Clocks.NEXT_DRINK] = getWorldTicks() + 1
            } else if (player.clocks[Clocks.NEXT_CONSUME] < getWorldTicks() && player.clocks[Clocks.NEXT_DRINK] < getWorldTicks()) {
                consumable.consumable.consume(node as? Item ?: return true, player)
                player.clocks[Clocks.NEXT_DRINK] = getWorldTicks() + 1
            }
        }

        return true
    }
}
