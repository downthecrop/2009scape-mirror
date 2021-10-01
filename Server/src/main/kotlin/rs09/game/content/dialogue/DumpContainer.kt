package rs09.game.content.dialogue

import core.game.container.Container
import core.game.container.impl.EquipmentContainer
import core.game.content.dialogue.DialoguePlugin
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
/**
 * Represents the dialogue plugin used to handle the deposit all/deposit inventory/etc button
 * @author Splinter
 * @author James Triantafylos
 */
class DumpContainer(player: Player? = null) : DialoguePlugin(player) {
    val ID = 628371

    override fun newInstance(player: Player?): DialoguePlugin {
        return DumpContainer(player)
    }

    override fun open(vararg args: Any?): Boolean {
        options("Deposit Inventory", "Deposit Equipment", "Deposit Beast of Burden", "Cancel")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (buttonId) {
            1 -> {
                if (player.inventory.isEmpty) {
                    player.packetDispatch.sendMessage("You have nothing in your inventory to deposit.")
                } else {
                    dump(player.inventory)
                }
            }
            2 -> {
                if (player.equipment.isEmpty) {
                    player.packetDispatch.sendMessage("You have no equipment to deposit.")
                } else {
                    dump(player.equipment)
                }
            }
            3 -> {
                player.familiarManager.dumpBob()
            }
            4 -> {
            }
        }
        end()
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(ID)
    }

    /**
     * Dumps a container.
     * @param inventory the player's inventory.
     * @author ceik
     * @author James Triantafylos
     */
    fun dump(inventory: Container) {
        val bank = player.bank
        for (item: Item in inventory.toArray().filterNotNull()) {
            if (!bank.hasSpaceFor(item)) {
                player.packetDispatch.sendMessage("You have no more space in your bank.")
                return
            }
            if (!bank.canAdd(item)) {
                player.packetDispatch.sendMessage("A magical force prevents you from banking your " + item.name + ".")
            } else {
                if (inventory is EquipmentContainer) {
                    val plugin = item.definition.handlers["equipment"]
                    if (plugin != null && plugin is Plugin<*>) {
                        plugin.fireEvent("unequip", player, item)
                    }
                }
                inventory.remove(item)
                bank.add(if (item.definition.isUnnoted) item else Item(item.noteChange, item.amount))
            }
        }
        inventory.update()
        bank.update()
    }

}