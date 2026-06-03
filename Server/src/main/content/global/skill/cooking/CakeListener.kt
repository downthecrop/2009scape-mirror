package content.global.skill.cooking

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import org.rs09.consts.Items

class CakeListener : InteractionListener {
    private val cakeArr = intArrayOf(
        Items.EGG_1944,
        Items.BUCKET_OF_MILK_1927,
        Items.POT_OF_FLOUR_1933
    )

    override fun defineListeners() {
        onUseWith(IntType.ITEM, cakeArr, Items.CAKE_TIN_1887) { player, _, _ ->
            if (getDynLevel(player, Skills.COOKING) < 40) {
                sendMessage(player, "You need a Cooking level of at least 40 in order to do this.")
                return@onUseWith true
            }

            if(inInventory(player, Items.EGG_1944) && inInventory(player, Items.BUCKET_OF_MILK_1927) && inInventory(player, Items.POT_OF_FLOUR_1933) && inInventory(player, Items.CAKE_TIN_1887)) {
                if(removeItem(player, Items.EGG_1944) && removeItem(player, Items.BUCKET_OF_MILK_1927) && removeItem(player, Items.POT_OF_FLOUR_1933) && removeItem(player, Items.CAKE_TIN_1887)){
                    addItem(player, Items.EMPTY_POT_1931)
                    addItem(player, Items.UNCOOKED_CAKE_1889)
                    addItem(player, Items.BUCKET_1925)
                    sendMessage(player, "You mix the milk, flour and egg together to make a raw cake mix.")

                }
            }
            else{
                // Not sure about the authentic message
                sendMessage(player, "You don't have enough ingredients to make that.")
            }
            return@onUseWith true
        }
    }
}