package content.global.skill.construction

import core.api.sendMessage
import core.game.dialogue.DialogueInterpreter
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import core.game.world.GameWorld.settings
import org.rs09.consts.Items

/**
 * Construction Guide Book
 * Used to be book:conguide?
 * @author ovenbreado
 * @author Emperor
 */

class ConstructionGuideBook : InteractionListener {
    companion object {
        private val TITLE = "Construction guide book"
        /** The resources used in beta. */
        private val RESOURCES = arrayOf(
            Item(Items.SAW_8794),
            Item(Items.HAMMER_2347),
            Item(Items.BRONZE_NAILS_4819, 500000),
            Item(Items.IRON_NAILS_4820, 500000),
            Item(Items.STEEL_NAILS_1539, 500000),
            Item(Items.BLACK_NAILS_4821, 500000),
            Item(Items.MITHRIL_NAILS_4822, 500000),
            Item(Items.ADAMANTITE_NAILS_4823, 500000),
            Item(Items.RUNE_NAILS_4824, 500000),
            Item(Items.PLANK_961, 500000),
            Item(Items.OAK_PLANK_8779, 500000),
            Item(Items.TEAK_PLANK_8781, 500000),
            Item(Items.MAHOGANY_PLANK_8783, 500000),
            Item(Items.BOLT_OF_CLOTH_8791, 500000),
            Item(Items.GOLD_LEAF_8785, 500000),
            Item(Items.MARBLE_BLOCK_8787, 500000),
            Item(Items.MAGIC_STONE_8789, 500000),  // 16
            Item(Items.BAGGED_DEAD_TREE_8418, 500000),
            Item(Items.BAGGED_NICE_TREE_8420, 500000),
            Item(Items.BAGGED_OAK_TREE_8422, 500000),
            Item(Items.BAGGED_WILLOW_TREE_8424, 500000),
            Item(Items.BAGGED_MAPLE_TREE_8426, 500000),
            Item(Items.BAGGED_YEW_TREE_8428, 500000),
            Item(Items.BAGGED_MAGIC_TREE_8430, 500000),
            Item(Items.BAGGED_PLANT_1_8432, 500000),
            Item(Items.BAGGED_PLANT_2_8434, 500000),
            Item(Items.BAGGED_PLANT_3_8436, 500000)
        )
    }

    override fun defineListeners() {
        // There is supposedly a book here.
        on(Items.CONSTRUCTION_GUIDE_8463, IntType.ITEM, "read") { player, _ ->
            if (settings!!.isDevMode && settings!!.isBeta) {
                for (item in RESOURCES) {
                    if (!player.inventory.contains(item.id, item.amount)) {
                        player.inventory.add(item, player)
                    }
                }
            }
            sendMessage(player, "Upon reading the book you discover you're supposed to use these resources to test out construction. Report all bugs on the forums.")
            return@on true
        }
    }

}