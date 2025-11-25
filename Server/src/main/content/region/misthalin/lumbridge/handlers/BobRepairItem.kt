package content.region.misthalin.lumbridge.handlers

import content.global.handlers.item.equipment.BarrowsEquipment
import content.data.RepairItem
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.npc.NPC
import core.plugin.Plugin
import core.plugin.Initializable

private val ALL_REPAIRABLE_ITEM_IDS = (RepairItem.repairableItemIds + BarrowsEquipment.getAllRepairableBarrowsIds()).toIntArray()

/**
 * Handles items being used on Bob for repair services.
 * @author 'Vexia
 * @author Damighty - Kotlin conversion
 */
@Initializable
class BobRepairItem : UseWithHandler(*ALL_REPAIRABLE_ITEM_IDS) {

    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(519, NPC_TYPE, this)
        addHandler(3797, NPC_TYPE, this)
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val player = event.player
        val item = event.usedItem
        val npc = event.usedWith as NPC

        val isBarrowsItem = BarrowsEquipment.isBarrowsItem(item.id)

        if (isBarrowsItem) {
            if (BarrowsEquipment.isFullyRepaired(item.id)) {
                player.dialogueInterpreter.open(519, npc, true, true, item.id)
                return true
            }
        } else if (RepairItem.forId(item.id) == null) {
            player.dialogueInterpreter.open(519, npc, true, true)
            return true
        }
        
        player.dialogueInterpreter.open(519, npc, true, false, item.id, item)
        return true
    }
}