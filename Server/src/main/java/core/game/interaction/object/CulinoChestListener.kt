package core.game.interaction.`object`

import api.getUsedOption
import core.game.world.map.RegionManager.getObject
import core.game.content.global.shop.CulinomancerShop.openShop
import core.plugin.Initializable
import core.game.interaction.OptionHandler
import core.plugin.Plugin
import core.cache.def.impl.SceneryDefinition
import core.game.content.global.shop.CulinomancerShop
import core.game.node.scenery.SceneryBuilder
import core.game.node.entity.player.Player
import core.game.node.Node
import core.game.world.map.Location
import org.rs09.consts.Scenery
import rs09.game.interaction.InteractionListener

/**
 * Handles the culino chest options.
 * @author Ceikry
 */
class CulinoChestListener : InteractionListener() {
    val CULINO_CHEST = Scenery.CHEST_12309

    override fun defineListeners() {
        on(CULINO_CHEST, SCENERY, "buy-items","buy-food"){player, _ ->
            openShop(player, food = getUsedOption(player).toLowerCase() == "buy-food")
            return@on true
        }

        on(CULINO_CHEST, SCENERY, "bank"){player, _ ->
            player.bank.open()
            return@on true
        }
    }
}