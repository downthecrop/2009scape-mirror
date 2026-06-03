package content.global.skill.cooking.recipe

import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs09.consts.Items

class StewListener : InteractionListener {

    val base = StewProduct.values().map { it.base }.toIntArray()
    val added = StewProduct.values().map { it.added }.toIntArray()

    override fun defineListeners() {
        onUseWith(IntType.ITEM, base, *added) { player, used, with ->
            val stew = StewProduct.productMap[used.id] ?: return@onUseWith true

            if (stew.added != with.id) {
                return@onUseWith false
            }

            val product = stew.product
            val level = stew.minimumLevel
            val experience = 0.0
            val message = "You cut up the ${stew.message} and put it into the stew."
            val failMessage = "You need a Cooking level of at least ${stew.minimumLevel} in order to do this."

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

    enum class StewProduct(
        val base: Int,
        val added: Int,
        val product: Int,
        val minimumLevel: Int,
        val message: String,
    ) {
        STEW_1(Items.POTATO_1942, Items.BOWL_OF_WATER_1921, Items.INCOMPLETE_STEW_1997, 25, "potato"),
        STEW_2(Items.INCOMPLETE_STEW_1997, Items.COOKED_MEAT_2142, Items.UNCOOKED_STEW_2001, 25, "meat"),
        STEW_3(Items.COOKED_MEAT_2142, Items.BOWL_OF_WATER_1921, Items.INCOMPLETE_STEW_1999, 25, "meat"),
        STEW_4(Items.INCOMPLETE_STEW_1999, Items.POTATO_1942, Items.UNCOOKED_STEW_2001, 25, "potato"),
        ;

        companion object {
            val productMap = HashMap<Int, StewProduct>()

            init {
                for (stew in StewProduct.values()) {
                    productMap[stew.base] = stew
                }
            }
        }
    }
}