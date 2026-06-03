package content.global.skill.cooking.recipe

import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs09.consts.Items

class PotatoListener : InteractionListener {

    val added = PotatoProduct.values().map { it.added }.toIntArray()

    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.BAKED_POTATO_6701, Items.PAT_OF_BUTTER_6697) { player, used, with ->
            val product = Items.POTATO_WITH_BUTTER_6703
            val level = 39
            val experience = 40.5
            val message = "You add a pat of butter to the potato."
            val failMessage = "You need a Cooking level of at least $level in order to do this."

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

        onUseWith(IntType.ITEM, added, Items.POTATO_WITH_BUTTER_6703) { player, used, with ->
            val topping = PotatoProduct.productMap[used.id] ?: return@onUseWith true

            val product = topping.product
            val level = topping.minimumLevel
            val experience = topping.experience
            val message = ""
            val failMessage = "You need a Cooking level of at least $level in order to do this."

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

    enum class PotatoProduct(
        val added: Int,
        val product: Int,
        val minimumLevel: Int,
        val experience: Double,
        val message: String
    ) {
        CHEESE_POTATO(Items.CHEESE_1985, Items.POTATO_WITH_CHEESE_6705, 47, 10.0, ""),
        CHILLI_POTATO(Items.CHILLI_CON_CARNE_7062, Items.CHILLI_POTATO_7054, 41, 15.0, ""),
        EGG_POTATO(Items.EGG_AND_TOMATO_7064, Items.EGG_POTATO_7056, 51, 50.0, ""),
        TUNA_POTATO(Items.TUNA_AND_CORN_7068, Items.TUNA_POTATO_7060, 68, 10.0, ""),
        MUSHROOM_POTATO(Items.MUSHROOM_AND_ONION_7066, Items.MUSHROOM_POTATO_7058, 64, 55.0, ""),
        ;

        companion object {
            val productMap = HashMap<Int, PotatoProduct>()

            init {
                for (topping in PotatoProduct.values()) {
                    productMap[topping.added] = topping
                }
            }
        }
    }
}