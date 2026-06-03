package content.global.skill.cooking.recipe

import core.api.inInventory
import core.api.sendDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs09.consts.Items

class ToppingListener : InteractionListener {

    val base = ToppingProduct.values().map { it.base }.toIntArray()
    val added = ToppingProduct.values().map { it.added }.toIntArray()

    override fun defineListeners() {
        onUseWith(IntType.ITEM, base, *added) { player, used, with ->
            val topping = ToppingProduct.productMap[used.id] ?: return@onUseWith true
            val verb = if (with.id == Items.COOKED_MEAT_2142) "cut" else "slice"

            if (topping.added != with.id) {
                return@onUseWith false
            }

            if (topping.cut_name != "" && !inInventory(player, Items.KNIFE_946)) {
                sendDialogue(player, "You need a knife in order to $verb up the ${topping.cut_name}.")
                return@onUseWith true
            }

            val product = topping.product
            val level = topping.minimumLevel
            val experience = topping.experience
            val message = topping.message
            val failMessage = "You need a Cooking level of at least ${topping.minimumLevel} in order to do this."

            return@onUseWith standardMix(
                player,
                used.asItem(),
                with.asItem(),
                product,
                level,
                experience,
                message,
                failMessage
            )
        }
    }

    enum class ToppingProduct(
        val base: Int,
        val added: Int,
        val product: Int,
        val minimumLevel: Int,
        val experience: Double,
        val message: String,
        val cut_name: String
    ) {
        //Tuna and Corn: https://www.youtube.com/watch?v=wAavERc9p2c
        //Uncooked Egg: https://www.youtube.com/watch?v=LiLq6PhCc2M
        //Bowl of sweetcorn (OSRS source is only I could find) https://www.youtube.com/watch?v=pz8epXjkKYE

        UNCOOKED_EGG(Items.EGG_1944, Items.BOWL_1923, Items.UNCOOKED_EGG_7076, 1, 0.0, "You carefully break the egg into the bowl.", ""),
        BOWL_OF_SWEETCORN(Items.COOKED_SWEETCORN_5988, Items.BOWL_1923, Items.SWEETCORN_7088, 1, 0.0, "You put the cooked sweetcorn into the bowl.", ""),
        SLICED_MUSHROOM(Items.MUSHROOM_6004, Items.BOWL_1923, Items.SLICED_MUSHROOMS_7080, 1, 0.0, "You chop the mushrooms into the bowl.", "mushrooms"),
        CHOPPED_TUNA(Items.TUNA_361, Items.BOWL_1923, Items.CHOPPED_TUNA_7086, 1, 0.0, "You chop the tuna into the bowl.", "tuna"),
        CHOPPED_ONION(Items.ONION_1957, Items.BOWL_1923, Items.CHOPPED_ONION_1871, 1, 0.0, "You chop the onion into the bowl.", "onion"),
        CHOPPED_GARLIC(Items.GARLIC_1550, Items.BOWL_1923, Items.CHOPPED_GARLIC_7074, 1, 0.0, "You chop the garlic into the bowl.", "garlic"),
        SPICY_SAUCE(Items.CHOPPED_GARLIC_7074, Items.GNOME_SPICE_2169, Items.SPICY_SAUCE_7072, 9, 25.0, "You mix the ingredients to make the topping.", ""), //inauthentic, used generic mixing message
        CHILLI_CON_CARNE(Items.SPICY_SAUCE_7072, Items.COOKED_MEAT_2142, Items.CHILLI_CON_CARNE_7062, 9, 0.0, "You mix the ingredients to make the topping.", "meat"), //inauthentic, used generic mixing message
        EGG_AND_TOMATO(Items.TOMATO_1982, Items.SCRAMBLED_EGG_7078, Items.EGG_AND_TOMATO_7064, 23, 50.0, "You mix the ingredients to make the topping.", ""),
        MUSHROOM_AND_ONION(Items.FRIED_MUSHROOMS_7082, Items.FRIED_ONIONS_7084, Items.MUSHROOM_AND_ONION_7066, 57, 120.0, "You mix the ingredients to make the topping.", ""),
        TUNA_AND_CORN_1(Items.CHOPPED_TUNA_7086, Items.COOKED_SWEETCORN_5988, Items.TUNA_AND_CORN_7068, 67, 204.0, "You mix the ingredients to make the topping.", ""),
        TUNA_AND_CORN_2(Items.SWEETCORN_7088, Items.TUNA_361, Items.TUNA_AND_CORN_7068, 67, 204.0, "You mix the ingredients to make the topping.", "tuna"),
        ;

        companion object {
            val productMap = HashMap<Int, ToppingProduct>()

            init {
                for (topping in ToppingProduct.values()) {
                    productMap[topping.base] = topping
                }
            }
        }
    }
}