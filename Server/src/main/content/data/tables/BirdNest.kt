package content.data.tables

import core.api.addItemOrDrop
import core.api.asItem
import core.api.freeSlots
import core.api.lock
import core.api.playAudio
import core.api.replaceSlot
import core.api.sendMessage
import core.game.node.entity.player.Player
import core.game.node.item.ChanceItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.tools.RandomFunction
import core.tools.StringUtils
import core.tools.colorize
import org.rs09.consts.Items
import org.rs09.consts.Sounds

/**
 * Enumerates the various bird nests.
 * @author Bishop
 */

enum class BirdNest(@JvmField val nest: ChanceItem, vararg val loot: ChanceItem) {
    SEED(
        ChanceItem(Items.BIRDS_NEST_5073,        1,  1,  65.0),
        ChanceItem(Items.ACORN_5312,             1,  1,  750.0),
        ChanceItem(Items.APPLE_TREE_SEED_5283,   1,  1,  750.0),
        ChanceItem(Items.WILLOW_SEED_5313,       1,  1,  750.0),
        ChanceItem(Items.BANANA_TREE_SEED_5284,  1,  1,  750.0),
        ChanceItem(Items.ORANGE_TREE_SEED_5285,  1,  1,  750.0),
        ChanceItem(Items.CURRY_TREE_SEED_5286,   1,  1,  750.0),
        ChanceItem(Items.MAPLE_SEED_5314,        1,  1,  150.0),
        ChanceItem(Items.PINEAPPLE_SEED_5287,    1,  1,  150.0),
        ChanceItem(Items.PAPAYA_TREE_SEED_5288,  1,  1,  150.0),
        ChanceItem(Items.YEW_SEED_5315,          1,  1,  15.0),
        ChanceItem(Items.PALM_TREE_SEED_5289,    1,  1,  15.0),
        ChanceItem(Items.CALQUAT_TREE_SEED_5290, 1,  1,  15.0),
        ChanceItem(Items.SPIRIT_SEED_5317,       1,  1,  5.0),
        ChanceItem(Items.MAGIC_SEED_5316,        1,  1,  5.0)
    ),
    RING(
        ChanceItem(Items.BIRDS_NEST_5074,        1,  1,  30.0),
        ChanceItem(Items.GOLD_RING_1635,         1,  1,  750.0),
        ChanceItem(Items.SAPPHIRE_RING_1637,     1,  1,  750.0),
        ChanceItem(Items.EMERALD_RING_1639,      1,  1,  750.0),
        ChanceItem(Items.RUBY_RING_1641,         1,  1,  750.0),
        ChanceItem(Items.DIAMOND_RING_1643,      1,  1,  15.0)
    ),
    RED(
        ChanceItem(Items.BIRDS_NEST_5070,        1,  1,  5.0),
        ChanceItem(Items.BIRDS_EGG_5076,         1,  1,  1.0)
    ),
    GREEN(
        ChanceItem(Items.BIRDS_NEST_5071,        1,  1,  5.0),
        ChanceItem(Items.BIRDS_EGG_5078,         1,  1,  1.0)
    ),
    BLUE(
        ChanceItem(Items.BIRDS_NEST_5072,        1,  1,  5.0),
        ChanceItem(Items.BIRDS_EGG_5077,         1,  1,  1.0)
    ),
    RAVEN(
        ChanceItem(Items.BIRDS_NEST_11966,       1,  1,  5.0),
        ChanceItem(Items.RAVEN_EGG_11964,        1,  1,  1.0)
    ),
    WYSON(
        ChanceItem(Items.BIRDS_NEST_7413,        1,  1,  1.0),
        ChanceItem(Items.POTATO_SEED_5318,       14, 14, 750.0),
        ChanceItem(Items.SWEETCORN_SEED_5320,    3,  3,  750.0),
        ChanceItem(Items.TOMATO_SEED_5322,       6,  6,  750.0),
        ChanceItem(Items.CABBAGE_SEED_5324,      9,  9,  750.0),
        ChanceItem(Items.LIMPWURT_SEED_5100,     2,  2,  750.0),
        ChanceItem(Items.ONION_SEED_5319,        11, 11, 750.0),
        ChanceItem(Items.STRAWBERRY_SEED_5323,   3,  3,  750.0),
        ChanceItem(Items.WATERMELON_SEED_5321,   2,  2,  750.0),
        ChanceItem(Items.ACORN_5312,             1,  1,  15.0),
        ChanceItem(Items.RANARR_SEED_5295,       1,  1,  15.0),
        ChanceItem(Items.WILLOW_SEED_5313,       1,  1,  15.0),
        ChanceItem(Items.MAPLE_SEED_5314,        1,  1,  15.0),
        ChanceItem(Items.YEW_SEED_5315,          1,  1,  15.0),
        ChanceItem(Items.MAGIC_SEED_5316,        1,  1,  5.0),
        ChanceItem(Items.SPIRIT_SEED_5317,       1,  1,  5.0)
    );

    companion object {
        private fun getNestByItem(item: Item): BirdNest? {
            for (birdNest in values()) {
                if (birdNest.nest.id == item.id) {
                    return birdNest
                }
            }
            return null
        }

        private val nestChanceItems = values().filter { it != WYSON }.map { it.nest }.toTypedArray()

        @JvmStatic
        fun getRandomNest(wyson: Boolean): BirdNest {
            val selectedItem = RandomFunction.getChanceItem(nestChanceItems)
            var selectedNest = values().first { it.nest == selectedItem }
            if (wyson && selectedNest == SEED) {
                selectedNest = WYSON
            }
            return selectedNest
        }

        @JvmStatic
        fun drop(player: Player) {
            val birdNest = getRandomNest(false)
            GroundItemManager.create(birdNest.nest, player)
            playAudio(player, Sounds.CUCKOO_1_1997)
            sendMessage(player, colorize("%RA bird's nest falls out of the tree."))
        }

        fun search(player: Player, item: Item) {
            val birdNest = getNestByItem(item) ?: return
            val loot = RandomFunction.getChanceItem(birdNest.loot)
            if (freeSlots(player) < loot.amount) {
                sendMessage(player, "You don't have enough inventory space.")
                return
            }
            lock(player, 1)
            addItemOrDrop(player, loot.id, loot.amount)
            replaceSlot(player, item.slot, Items.BIRDS_NEST_5075.asItem())
            sendMessage(player, "You take ${if (StringUtils.isPlusN(loot.name)) "an" else "a"} ${loot.name.lowercase()} out of the bird's nest.")
        }
    }
}