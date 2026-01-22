package content.global.skill.construction.decoration.kitchen

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs09.consts.Items
import org.rs09.consts.Scenery


class ShelfListener : InteractionListener {
    companion object {
        // Source for text https://www.youtube.com/watch?v=SNuqelEft3Y

        private val woodshelf_1_items = listOf(
            "Kettle" to Items.KETTLE_7688,
            "Teapot" to Items.TEAPOT_7702,
            "Cup" to Items.EMPTY_CUP_7728
        )

        private val woodshelf_2_items = woodshelf_1_items + listOf(
            "Beer glass" to Items.BEER_GLASS_7742
        )

        private val woodshelf_3_items = woodshelf_2_items.map { (name, itemId) ->
            when (name) {
                "Cup" -> {
                    "Porcelain cup" to Items.PORCELAIN_CUP_7732
                }

                "Teapot" -> {
                    "Teapot" to Items.TEAPOT_7714
                }

                else -> {
                    name to itemId
                }
            }
        } + listOf("Cake tin" to Items.CAKE_TIN_1887)

        private val oakshelf_1_items = woodshelf_2_items + listOf("Cake tin" to Items.CAKE_TIN_1887) + listOf("Bowl" to Items.BOWL_1923)

        private val oakshelf_2_items = oakshelf_1_items.map { (name, itemId) ->
            when (name) {
                "Cup" -> {
                    "Porcelain cup" to Items.PORCELAIN_CUP_7732
                }

                "Teapot" -> {
                    "Teapot" to Items.TEAPOT_7714
                }

                else -> {
                    name to itemId
                }
            }
        } + listOf("Pie dish" to Items.PIE_DISH_2313)

        private val teakshelf_1_items = oakshelf_2_items + listOf(
            "Pot" to Items.EMPTY_POT_1931
        )

        private val teakshelf_2_items = teakshelf_1_items.map { (name, itemId) ->
            when (name) {
                "Porcelain cup" -> {
                    "Porcelain cup" to Items.PORCELAIN_CUP_7735
                }

                "Teapot" -> {
                    "Teapot" to Items.TEAPOT_7726
                }

                else -> {
                    name to itemId
                }
            }
        } + listOf("Chef's Hat" to Items.CHEFS_HAT_1949)

        val shelves = mapOf(
            Scenery.SHELVES_13545 to woodshelf_1_items,
            Scenery.SHELVES_13546 to woodshelf_2_items,
            Scenery.SHELVES_13547 to woodshelf_3_items,
            Scenery.SHELVES_13548 to oakshelf_1_items,
            Scenery.SHELVES_13549 to oakshelf_2_items,
            Scenery.SHELVES_13550 to teakshelf_1_items,
            Scenery.SHELVES_13551 to teakshelf_2_items,
        )
    }

    override fun defineListeners() {
        on(shelves.keys.toIntArray(), IntType.SCENERY, "Search") { player, node ->
            openDialogue(player, ShelfDialogue(node.id))
            return@on true
        }
    }

    class ShelfDialogue(shelfId: Int) : AbstractContainer(shelfId, shelves, "shelf")
}