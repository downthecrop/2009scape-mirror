package rs09.game.interaction.`object`

import core.game.node.scenery.SceneryBuilder
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener

/**
 * @author Woah, with love
 * Item(s):
 *  Swamp tar - ID: 1939
 * Object(s):
 *  Tar barrel (full) - ID: 16860 | Tar barrel (empty) - ID: 16688
 * Option(s):
 *  "Take-from"
 */
class TarBarrelListener : InteractionListener() {

    val FULL_TAR_BARREL_16860 = 16860
    val EMPTY_TAR_BARREL_16688 = 16688
    val EMPTY_BARREL_STRING = "The barrel became empty!"

    override fun defineListeners() {

        on(FULL_TAR_BARREL_16860,SCENERY,"take-from") { player, node ->
            val incrementAmount = RandomFunction.random(83, 1000)

            if (player.inventory.isFull) {
                player.sendMessage("Not enough space in your inventory!")
                return@on true
            }

            if (node.asScenery().charge >= 0) {
                node.asScenery().charge = node.asScenery().charge - incrementAmount
                player.inventory.add(Item(Items.SWAMP_TAR_1939))

                if (node.asScenery().charge <= 0) {
                    player.sendMessage(EMPTY_BARREL_STRING)
                    SceneryBuilder.replace(node.asScenery(), node.asScenery().transform(EMPTY_TAR_BARREL_16688), 38)
                    node.asScenery().charge = 1000
                }
            }

            return@on true
        }

    }
}