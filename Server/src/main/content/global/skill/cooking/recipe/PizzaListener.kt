package content.global.skill.cooking.recipe

import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs09.consts.Items

class PizzaListener : InteractionListener {

    val base = PizzaProduct.values().map { it.base }.toIntArray()
    val added = PizzaProduct.values().map { it.added }.toIntArray()

    override fun defineListeners() {
        onUseWith(IntType.ITEM, base, *added) { player, used, with ->
            val pizza = PizzaProduct.productMap[used.id] ?: return@onUseWith true

            if (pizza.added != with.id) {
                return@onUseWith false
            }

            val product = pizza.product
            val level = pizza.minimumLevel
            val experience = pizza.experience
            val message = "You add the ${pizza.message} to the pizza."
            val failMessage = "You need a Cooking level of at least ${pizza.minimumLevel} in order to do this."

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

    enum class PizzaProduct(
        val base: Int,
        val added: Int,
        val product: Int,
        val minimumLevel: Int,
        val experience: Double,
        val message: String,
    ) {
        PIZZA_1(Items.TOMATO_1982, Items.PIZZA_BASE_2283, Items.INCOMPLETE_PIZZA_2285, 35, 0.0,"tomato"),
        PIZZA_2(Items.CHEESE_1985, Items.INCOMPLETE_PIZZA_2285, Items.UNCOOKED_PIZZA_2287, 35, 0.0, "cheese"),
        MEAT_PIZZA_1(Items.COOKED_MEAT_2142, Items.PLAIN_PIZZA_2289, Items.MEAT_PIZZA_2293, 45, 26.0, "meat"),
        MEAT_PIZZA_2(Items.COOKED_CHICKEN_2140, Items.PLAIN_PIZZA_2289, Items.MEAT_PIZZA_2293, 45, 26.0, "cooked chicken"),
        ANCHOVY_PIZZA(Items.ANCHOVIES_319, Items.PLAIN_PIZZA_2289, Items.ANCHOVY_PIZZA_2297, 55, 39.0, "anchovies"),
        PINEAPPLE_PIZZA_1(Items.PINEAPPLE_CHUNKS_2116, Items.PLAIN_PIZZA_2289, Items.PINEAPPLE_PIZZA_2301, 65, 52.0, "pineapple"),
        PINEAPPLE_PIZZA_2(Items.PINEAPPLE_RING_2118, Items.PLAIN_PIZZA_2289, Items.PINEAPPLE_PIZZA_2301, 65, 52.0, "pineapple"),
        ;

        companion object {
            val productMap = HashMap<Int, PizzaProduct>()

            init {
                for (pizza in PizzaProduct.values()) {
                    productMap[pizza.base] = pizza
                }
            }
        }
    }
}