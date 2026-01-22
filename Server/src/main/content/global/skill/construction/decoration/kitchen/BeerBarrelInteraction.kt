package content.global.skill.construction.decoration.kitchen

import core.api.*
import core.game.interaction.InteractionListener
import org.rs09.consts.Items

class BeerBarrelInteraction : InteractionListener {

    companion object{
        private val barrelMap = mapOf(
            org.rs09.consts.Scenery.BEER_BARREL_13568 to Items.BEER_7740,
            org.rs09.consts.Scenery.CIDER_BARREL_13569 to Items.CIDER_7752,
            org.rs09.consts.Scenery.ASGARNIAN_ALE_13570 to Items.ASGARNIAN_ALE_7744,
            org.rs09.consts.Scenery.GREENMAN_S_ALE_13571 to Items.GREENMANS_ALE_7746,
            org.rs09.consts.Scenery.DRAGON_BITTER_13572 to Items.DRAGON_BITTER_7748,
            org.rs09.consts.Scenery.CHEF_S_DELIGHT_13573 to Items.CHEFS_DELIGHT_7754,
        )
    }
    override fun defineListeners() {
        onUseWith(SCENERY, Items.BEER_GLASS_7742, *barrelMap.keys.toIntArray()){ player, used, with ->
            val drinkName  = with.name.lowercase().replace("barrel", "").trim()+"."
            if (removeItem(player, used)){
                sendMessage(player, "You fill up your glass with $drinkName")
                addItem(player, barrelMap[with.id]!!)
            }
            return@onUseWith true
        }
    }
}