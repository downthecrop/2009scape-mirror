package content.global.skill.cooking.recipe

import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs09.consts.Items

class PieListener : InteractionListener {

    val base = PieProduct.values().map { it.base }.toIntArray()
    val added = PieProduct.values().map { it.added }.toIntArray()

    override fun defineListeners() {
        onUseWith(IntType.ITEM, base, *added) { player, used, with ->
            val pie = PieProduct.productMap[used.id] ?: return@onUseWith true

            if (pie.added != with.id) {
                return@onUseWith false
            }

            val product = pie.product
            val level = pie.minimumLevel
            val experience = 0.0
            val message = "You fill the pie with ${pie.ingredientMessage}."
            val failMessage = "You need a Cooking level of ${pie.minimumLevel} to make this."

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

    enum class PieProduct(
        val base: Int,
        val added: Int,
        val product: Int,
        val minimumLevel: Int,
        val ingredientMessage: String,
    ) {
        REDBERRY_PIE(Items.REDBERRIES_1951, Items.PIE_SHELL_2315, Items.UNCOOKED_BERRY_PIE_2321, 10, "redberries"),
        MEAT_PIE(Items.COOKED_MEAT_2142, Items.PIE_SHELL_2315, Items.UNCOOKED_MEAT_PIE_2319, 20, "meat"),
        MUD_PIE_1(Items.COMPOST_6032, Items.PIE_SHELL_2315, Items.PART_MUD_PIE_7164, 29, "compost"),
        MUD_PIE_2(Items.PART_MUD_PIE_7164, Items.BUCKET_OF_WATER_1929, Items.PART_MUD_PIE_7166, 29, "water"),
        MUD_PIE_3(Items.PART_MUD_PIE_7166, Items.CLAY_434, Items.RAW_MUD_PIE_7168, 29, "clay"),
        APPLE_PIE(Items.COOKING_APPLE_1955, Items.PIE_SHELL_2315, Items.UNCOOKED_APPLE_PIE_2317, 30, "cooking apple"),
        GARDEN_PIE_1(Items.TOMATO_1982, Items.PIE_SHELL_2315, Items.PART_GARDEN_PIE_7172, 34, "tomato"),
        GARDEN_PIE_2(Items.PART_GARDEN_PIE_7172, Items.ONION_1957, Items.PART_GARDEN_PIE_7174, 34, "onion"),
        GARDEN_PIE_3(Items.PART_GARDEN_PIE_7174, Items.CABBAGE_1965, Items.RAW_GARDEN_PIE_7176, 34, "cabbage"),
        FISH_PIE_1(Items.TROUT_333, Items.PIE_SHELL_2315, Items.PART_FISH_PIE_7182, 47, "trout"),
        FISH_PIE_2(Items.PART_FISH_PIE_7182, Items.COD_339, Items.PART_FISH_PIE_7184, 47, "cod"),
        FISH_PIE_3(Items.PART_FISH_PIE_7184, Items.POTATO_1942, Items.RAW_FISH_PIE_7186, 47, "potato"),
        ADMIRAL_PIE_1(Items.SALMON_329, Items.PIE_SHELL_2315, Items.PART_ADMIRAL_PIE_7192, 70, "salmon"),
        ADMIRAL_PIE_2(Items.PART_ADMIRAL_PIE_7192, Items.TUNA_361, Items.PART_ADMIRAL_PIE_7194, 70, "tuna"),
        ADMIRAL_PIE_3(Items.PART_ADMIRAL_PIE_7194, Items.POTATO_1942, Items.RAW_ADMIRAL_PIE_7196, 70, "potato"),
        WILD_PIE_1(Items.RAW_BEAR_MEAT_2136, Items.PIE_SHELL_2315, Items.PART_WILD_PIE_7202, 85, "raw bear meat"),
        WILD_PIE_2(Items.PART_WILD_PIE_7202, Items.RAW_CHOMPY_2876, Items.PART_WILD_PIE_7204, 85, "raw chompy"),
        WILD_PIE_3(Items.PART_WILD_PIE_7204, Items.RAW_RABBIT_3226, Items.RAW_WILD_PIE_7206, 85, "raw rabbit"),
        SUMMER_PIE_1(Items.STRAWBERRY_5504, Items.PIE_SHELL_2315, Items.PART_SUMMER_PIE_7212, 95, "strawberry"),
        SUMMER_PIE_2(Items.PART_SUMMER_PIE_7212, Items.WATERMELON_5982, Items.PART_SUMMER_PIE_7214, 95, "watermelon"),
        SUMMER_PIE_3(Items.PART_SUMMER_PIE_7214, Items.COOKING_APPLE_1955, Items.RAW_SUMMER_PIE_7216, 95, "apple"),
        ;

        companion object {
            val productMap = HashMap<Int, PieProduct>()

            init {
                for (pie in PieProduct.values()) {
                    productMap[pie.base] = pie
                }
            }
        }
    }
}