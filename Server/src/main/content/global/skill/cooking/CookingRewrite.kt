package content.global.skill.cooking

import core.api.amountInInventory
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import org.rs09.consts.Items
import org.rs09.consts.Items.BREAD_DOUGH_2307
import org.rs09.consts.Items.RAW_BEAR_MEAT_2136
import org.rs09.consts.Items.RAW_BEEF_2132
import org.rs09.consts.Items.SEAWEED_401
import org.rs09.consts.Items.UNCOOKED_CAKE_1889

/**
 * @author Ceikry
 * @author bushtail - added bear meat for sinew making
 * @author Woah     - added single food cooking
 */
class CookingRewrite : InteractionListener {

    val RAW_FOODS: IntArray

    init {
        val list = CookableItems.values().map { it.raw }.toMutableList()
        list.add(Items.COOKED_MEAT_2142)
        list.add(RAW_BEEF_2132)
        list.add(RAW_BEAR_MEAT_2136)
        list.add(SEAWEED_401)
        RAW_FOODS = list.toIntArray()
    }

    override fun defineListeners() {

        onUseWith(IntType.SCENERY,RAW_FOODS, *COOKING_OBJs){ player, used, with ->
            val item = used.asItem()
            val obj = with.asScenery()
            val range = obj.name.toLowerCase().contains("range")
            when (item.id) {
                RAW_BEEF_2132, RAW_BEAR_MEAT_2136 -> if (range) {
                    player.dialogueInterpreter.open(CookingDialogue(item.id,9436,true,obj,item.id))
                    return@onUseWith true
                }
                BREAD_DOUGH_2307, UNCOOKED_CAKE_1889 -> if (!range) {
                    player.packetDispatch.sendMessage("You need to cook this on a range.")
                    return@onUseWith false
                }
            }

            //cook a standard item
            if (amountInInventory(player, item.id) > 1) {
                player.dialogueInterpreter.open(CookingDialogue(item.id,obj))
            } else {
            // Don't display dialogue if player only has *1* of the item
                val product = if (CookableItems.intentionalBurn(item.id)) { // checks intentional burning
                    CookableItems.getIntentionalBurn(item.id).id
                } else {
                    CookableItems.forId(item.id).cooked
                }
                cook(player, obj, item.id, product, 1)
            }
            return@onUseWith true
        }

    }

    companion object {
        val COOKING_OBJs = intArrayOf(24313,21302, 13528, 13529, 13533, 13531, 13536, 13539, 13542, 2728, 2729, 2730, 2731, 2732, 2859, 3038, 3039, 3769, 3775, 4265, 4266, 5249, 5499, 5631, 5632, 5981, 9682, 10433, 11404, 11405, 11406, 12102, 12796, 13337, 13881, 14169, 14919, 15156, 20000, 20001, 21620, 21792, 22713, 22714, 23046, 24283, 24284, 25155, 25156, 25465, 25730, 27297, 29139, 30017, 32099, 33500, 34495, 34546, 36973, 37597, 37629, 37726, 114, 4172, 5275, 8750, 16893, 22154, 34410, 34565, 114, 9085, 9086, 9087, 12269, 15398, 25440, 25441, 2724, 2725, 2726, 4618, 4650, 5165, 6093, 6094, 6095, 6096, 8712, 9374, 9439, 9440, 9441, 10824, 17640, 17641, 17642, 17643, 18039, 21795, 24285, 24329, 27251, 33498, 35449, 36815, 36816, 37426, 40110, 10377)

        @JvmStatic
        fun cook(player: Player, `object`: Scenery?, initial: Int, product: Int, amount: Int) {
            val food = Item(initial)
            if (food.name.toLowerCase().contains("pizza")) {
                player.pulseManager.run(PizzaCookingPulse(player, `object`, initial, product, amount))
            } else if (food.name.toLowerCase().contains("pie")) {
                player.pulseManager.run(PieCookingPulse(player, `object`, initial, product, amount))
            } else if (CookableItems.intentionalBurn(initial)) {
                player.pulseManager.run(IntentionalBurnPulse(player, `object`, initial, product, amount))
            } else {
                player.pulseManager.run(StandardCookingPulse(player, `object`, initial, product, amount))
            }
        }
    }
}
