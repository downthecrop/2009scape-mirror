package core.game.interaction.`object`

import core.cache.def.impl.ObjectDefinition
import core.game.interaction.OptionHandler
import core.game.interaction.OptionListener
import core.game.node.Node
import core.game.node.`object`.ObjectBuilder
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.Items
import core.tools.RandomFunction

/**
 * @author Woah, with love
 * Item(s):
 *  Swamp tar - ID: 1939
 * Object(s):
 *  Tar barrel (full) - ID: 16860 | Tar barrel (empty) - ID: 16688
 * Option(s):
 *  "Take-from"
 */
class TarBarrelListener : OptionListener() {

    val FULL_TAR_BARREL_16860 = 16860
    val EMPTY_TAR_BARREL_16688 = 16688
    val EMPTY_BARREL_STRING = "The barrel became empty!"

    override fun defineListeners() {

        on(FULL_TAR_BARREL_16860,OBJECT,"take-from") { player, node ->
            val incrementAmount = RandomFunction.random(83, 1000)

            if (player.inventory.isFull) {
                player.sendMessage("Not enough space in your inventory!")
                return@on true
            }

            if (node.asObject().charge >= 0) {
                node.asObject().charge = node.asObject().charge - incrementAmount
                player.inventory.add(Item(Items.SWAMP_TAR_1939))

                if (node.asObject().charge <= 0) {
                    player.sendMessage(EMPTY_BARREL_STRING)
                    ObjectBuilder.replace(node.asObject(), node.asObject().transform(EMPTY_TAR_BARREL_16688), 38)
                    node.asObject().charge = 1000
                }
            }

            return@on true
        }

    }
}