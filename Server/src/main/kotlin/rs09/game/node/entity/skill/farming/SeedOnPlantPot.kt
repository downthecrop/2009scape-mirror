package rs09.game.node.entity.skill.farming

import api.addItem
import api.inInventory
import api.removeItem
import api.sendDialogue
import core.game.node.Node
import core.game.node.entity.player.Player
import org.rs09.consts.Items
import rs09.game.interaction.IntType
import rs09.game.interaction.InteractionListener
import rs09.game.node.entity.state.newsys.states.SeedlingState

class SeedlingListener : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.ITEM, TREE_SEEDS, Items.PLANT_POT_5354, handler = ::addSeedToPot)
        onUseWith(IntType.ITEM, TREE_SEEDLINGS, *WATERING_CANS, handler = ::waterSeedling)
    }

    fun addSeedToPot(player: Player, used: Node, with: Node) : Boolean {
        val seed = used.asItem() ?: return false
        val pot = with.asItem() ?: return false

        if(!inInventory(player, Items.GARDENING_TROWEL_5325)){
            sendDialogue(player, "You need a gardening trowel on you to do this.")
            return false
        }

        val seedling = getSeedling(seed.id)
        if (seedling == -1) return false
        if (!removeItem(player, seed.id) || !removeItem(player, pot)) return true
        addItem(player, seedling)
        return true
    }

    fun waterSeedling(player: Player, used: Node, with: Node) : Boolean {
        val seedling = used.asItem() ?: return false
        val can = with.asItem() ?: return false

        val nextCan = can.id.getNext()
        val wateredSeedling = if (seedling.id > 5400) seedling.id + 8 else seedling.id + 6

        if (!removeItem(player, can) || !removeItem(player, seedling)) return false
        addItem(player, wateredSeedling)
        addItem(player, nextCan)

        var state = player.states["seedling"] as SeedlingState?

        if (state != null) {
            state.addSeedling(wateredSeedling)
            return true
        }

        state = player.registerState("seedling") as SeedlingState?
        state?.addSeedling(wateredSeedling)
        state?.init()

        return true
    }

    private fun Int.getNext(): Int{
        val index = WATERING_CANS.indexOf(this)
        if (index == -1) return Items.WATERING_CAN_5331
        return if (index != WATERING_CANS.size -1) WATERING_CANS[index + 1] else Items.WATERING_CAN_5331
    }
    fun getSeedling(id: Int) : Int {
        return when (id) {
            Items.ACORN_5312 ->            Items.OAK_SEEDLING_5358
            Items.WILLOW_SEED_5313 ->      Items.WILLOW_SEEDLING_5359
            Items.MAPLE_SEED_5314 ->       Items.MAPLE_SEEDLING_5360
            Items.YEW_SEED_5315 ->         Items.YEW_SEEDLING_5361
            Items.MAGIC_SEED_5316 ->       Items.MAGIC_SEEDLING_5362
            Items.APPLE_TREE_SEED_5283 ->  Items.APPLE_SEEDLING_5480
            Items.BANANA_TREE_SEED_5284 -> Items.BANANA_SEEDLING_5481
            Items.ORANGE_TREE_SEED_5285 -> Items.ORANGE_SEEDLING_5482
            Items.CURRY_TREE_SEED_5286 ->  Items.CURRY_SEEDLING_5483
            Items.PINEAPPLE_SEED_5287 ->   Items.PINEAPPLE_SEEDLING_5484
            Items.PAPAYA_TREE_SEED_5288 -> Items.PAPAYA_SEEDLING_5485
            Items.PALM_TREE_SEED_5289 ->   Items.PALM_SEEDLING_5486
            Items.SPIRIT_SEED_5317 ->      Items.SPIRIT_SEEDLING_5363
            else -> -1
        }
    }

    val TREE_SEEDS = intArrayOf(
        Items.ACORN_5312,
        Items.WILLOW_SEED_5313,
        Items.MAPLE_SEED_5314,
        Items.YEW_SEED_5315,
        Items.MAGIC_SEED_5316,
        Items.APPLE_TREE_SEED_5283,
        Items.BANANA_TREE_SEED_5284,
        Items.ORANGE_TREE_SEED_5285,
        Items.CURRY_TREE_SEED_5286,
        Items.PINEAPPLE_SEED_5287,
        Items.PAPAYA_TREE_SEED_5288,
        Items.PALM_TREE_SEED_5289,
        Items.SPIRIT_SEED_5317
    )

    val TREE_SEEDLINGS = intArrayOf(
        Items.OAK_SEEDLING_5358,
        Items.WILLOW_SEEDLING_5359,
        Items.MAPLE_SEEDLING_5360,
        Items.YEW_SEEDLING_5361,
        Items.MAGIC_SEEDLING_5362,
        Items.APPLE_SEEDLING_5480,
        Items.BANANA_SEEDLING_5481,
        Items.ORANGE_SEEDLING_5482,
        Items.CURRY_SEEDLING_5483,
        Items.PINEAPPLE_SEEDLING_5484,
        Items.PAPAYA_SEEDLING_5485,
        Items.PALM_SEEDLING_5486,
        Items.SPIRIT_SEEDLING_5363
    )

    private val WATERING_CANS = intArrayOf(Items.WATERING_CAN8_5340,Items.WATERING_CAN7_5339,Items.WATERING_CAN6_5338,Items.WATERING_CAN5_5337,Items.WATERING_CAN4_5336,Items.WATERING_CAN3_5335,Items.WATERING_CAN2_5334,Items.WATERING_CAN1_5333)
}