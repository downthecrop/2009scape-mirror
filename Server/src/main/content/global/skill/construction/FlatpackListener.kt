package content.global.skill.construction

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import org.rs09.consts.Items

/**
 * Handles building furniture using flatpacks
 * @author Roderik
 * @author Bishop
 */

class FlatpackListener : InteractionListener {
    val flatpacks = Decoration.values().map { it.flatpackItemID }.filter { it != -1 }.toIntArray()
    val hotspots = BuildHotspot.values().map { it.objectId }.toIntArray()

    override fun defineListeners() {
        for (hotspot in hotspots) {
            onUseWith(IntType.SCENERY, flatpacks, hotspot) { player, used, with ->
                return@onUseWith buildFlatpackOnHotspot(player, used.asItem(), with as Scenery)
            }
        }
    }

    private fun buildFlatpackOnHotspot(player: Player, used: Item, with: Scenery): Boolean {
        val hotspotUsed = player.houseManager.getHotspot(with)
        val decorationUsed = Decoration.forFlatpackItemId(used.id)

        if (!hotspotUsed.hotspot.decorations.contains(decorationUsed)) {
            sendMessage(player, "You can't build that here.")
            return false
        }
        if (!inInventory(player, Items.HAMMER_2347) || !inInventory(player, Items.SAW_8794)) {
            sendMessage(player, "You need a hammer and a saw to build this.")
            return false
        }
        if (removeItem(player, used)) {
            BuildingUtils.buildDecoration(player, hotspotUsed, decorationUsed, with.asScenery(), true, true)
            return true
        } else {
            return false
        }
    }
}