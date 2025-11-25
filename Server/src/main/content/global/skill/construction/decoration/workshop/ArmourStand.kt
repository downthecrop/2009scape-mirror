package content.global.skill.construction.decoration.workshop

import core.game.dialogue.DialoguePlugin
import content.data.RepairItem
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import content.global.handlers.item.equipment.BarrowsEquipment
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import kotlin.math.ceil
import org.rs09.consts.Items

private val ALL_REPAIRABLE_ITEM_IDS = (RepairItem.repairableItemIds + BarrowsEquipment.getAllRepairableBarrowsIds()).toIntArray()

@Initializable
class ArmourStand : UseWithHandler(*ALL_REPAIRABLE_ITEM_IDS) {
    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(13715, OBJECT_TYPE, this)
        return this
    }

    override fun handle(event: NodeUsageEvent?): Boolean {
        event ?: return false
        val player = event.player
        val usedItem = event.used.asItem()

        var baseCost = 0
        var product: Item? = null

        val repairItem = RepairItem.forId(usedItem.id)
        val barrowsDef = BarrowsEquipment.getDefinition(usedItem.id)

        if (repairItem != null) {
            baseCost = repairItem.cost
            product = repairItem.product
        } else if (barrowsDef != null) {
            if (BarrowsEquipment.isFullyRepaired(event.used.id)) {
                player.sendMessage("That item can't be repaired.")
                return true
            }
            baseCost = BarrowsEquipment.getRepairCost(usedItem)
            product = Item(barrowsDef.repairedId)
        } else {
            player.sendMessage("That item can't be repaired.")
            return true
        }

        val discountMultiplier = (100.0 - (player.skills.getLevel(Skills.SMITHING) / 2.0)) / 100.0
        val cost = ceil(discountMultiplier * baseCost).toInt()

        player.dialogueInterpreter.open(58824213,usedItem, cost, product)
        return true
    }
    @Initializable
    class RepairDialogue(player: Player? = null) : DialoguePlugin(player) {
        
        private var item: Item? = null
        private var cost: Int = 0
        private var product: Item? = null

        override fun newInstance(player: Player?): DialoguePlugin = RepairDialogue(player)

        override fun open(vararg args: Any?): Boolean {
            item = args[0] as Item
            cost = args[1] as Int
            product = args[2] as Item
            
            val itemName = item?.name?.lowercase() ?: "item"
            player.dialogueInterpreter.sendDialogue(
                "Would you like to repair your $itemName",
                "for $cost gp?"
            )
            stage = 0
            return true
        }

        override fun handle(interfaceId: Int, buttonId: Int): Boolean {
            val currentItem = item ?: return false
            val currentProduct = product ?: return false
            
            when (stage) {
                0 -> {
                    options("Yes, please", "No, thanks")
                    stage++
                }
                1 -> when (buttonId) {
                    1 -> {
                        exchangeItems(currentItem, cost, currentProduct)
                        end()
                    }
                    2 -> end()
                }
            }
            return true
        }

        private fun exchangeItems(item: Item, cost: Int, product: Item) {
            val coins = Item(Items.COINS_995, cost)
            
            if (player.inventory.containsItem(coins) && player.inventory.containsItem(item)) {
                if (player.inventory.remove(item, coins)) {
                    if (player.inventory.add(product)) {
                        val costText = if (cost > 0) "${cost}gp" else "free"
                        player.sendMessage("You repair your ${product.name.lowercase()} for $costText.")
                        return
                    }
                }
                player.sendMessage("Report this to an administrator!")
            } else {
                player.sendMessage("You can't afford that.")
            }
        }

        override fun getIds(): IntArray {
            return intArrayOf(58824213)
        }
    }
}