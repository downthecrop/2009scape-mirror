package rs09.game.content.dialogue

import api.*
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
                    dumpContainer(player, player.inventory)
                }
            }
            2 -> {
                if (player.equipment.isEmpty) {
                    player.packetDispatch.sendMessage("You have no equipment to deposit.")
                } else {
                    dumpContainer(player, player.equipment)
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
}