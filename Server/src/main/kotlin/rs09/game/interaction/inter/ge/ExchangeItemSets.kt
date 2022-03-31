package rs09.game.interaction.inter.ge

import api.getAttribute
import api.openInterface
import api.setAttribute
import core.game.component.Component
import core.game.container.Container
import core.game.container.ContainerEvent
import core.game.container.ContainerListener
import core.game.container.access.InterfaceContainer
import core.game.ge.GEItemSet
import core.game.node.entity.player.Player
import org.rs09.consts.Components
import rs09.game.interaction.InterfaceListener

class ExchangeItemSets : InterfaceListener() {
    override fun defineListeners() {
        onOpen(Components.EXCHANGE_ITEMSETS_645) { player, _ ->
            val listener: InventoryListener
            setAttribute(player, "ge-listener", InventoryListener(player).also { listener = it })
            player.inventory.listeners.add(listener)
        }

        onClose(Components.EXCHANGE_ITEMSETS_645){player, _ ->
            val listener = getAttribute<InventoryListener?>(player, "ge-listener", null)
            player.inventory.listeners.remove(listener)
            player.interfaceManager.closeSingleTab()
            player.removeAttribute("container-key")
            player.removeAttribute("ge-listener")
            return@onClose true
        }
    }

    companion object {
        @JvmStatic
        fun openFor(player: Player)
        {
            openInterface(player, Components.EXCHANGE_ITEMSETS_645)
            player.interfaceManager.openSingleTab(Component(Components.EXCHANGE_SETS_SIDE_644))
        }
    }

    private class InventoryListener(val player: Player) : ContainerListener {
        init {
            createContainers(player)
        }

        override fun update(c: Container?, event: ContainerEvent?)
        {
            createContainers(player)
        }

        override fun refresh(c: Container?) {
            createContainers(player)
        }

        private fun createContainers(player: Player)
        {
            setAttribute(player, "container-key",
                    InterfaceContainer.generateItems(
                            player,
                            player.inventory.toArray(),
                            arrayOf("Examine", "Exchange", "Components"),
                            Components.EXCHANGE_SETS_SIDE_644,
                            16,
                            15,
                            10
                    )
            )

            InterfaceContainer.generateItems(
                    player,
                    GEItemSet.getItemArray(),
                    arrayOf("Examine", "Exchange", "Components"),
                    Components.EXCHANGE_ITEMSETS_645,
                    16,
                    15,
                    10
            )
        }
    }
}