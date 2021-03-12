package rs09.game.node.entity.skill.gather

import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin

/**
 * Repairs pickaxes after being broken
 * @author Ceikry
 */
@Initializable
class PickaxeRepairPlugin : UseWithHandler(480,482,484,486,488,490){
    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(466, ITEM_TYPE,this)
        return this
    }

    override fun handle(event: NodeUsageEvent?): Boolean {
        event ?: return false
        val used = event.used

        val product =
        when(used.id){
            480 -> 1265
            482 -> 1267
            484 -> 1269
            488 -> 1271
            486 -> 1273
            490 -> 1275
            else -> 0
        }
        event.player.inventory.remove(Item(466))
        event.player.inventory.remove(used.asItem())
        event.player.inventory.add(Item(product))
        event.player.sendMessage("You carefully reattach the head to the handle.")
        event.player.audioManager.send(10)
        return true
    }

}