package content.minigame.barrows

import core.api.announceIfRare
import core.game.component.Component
import core.game.container.access.InterfaceContainer
import content.data.BossKillCounter
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs09.consts.Components
import org.rs09.consts.Items
import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem

/**
 * The reward chest.
 * @author Ceikry
 * @author kermit
 * @author Player Name
 */
object RewardChest {
    private val REGULAR_DROPS = WeightBasedTable.create(
            WeightedItem(Items.COINS_995,               1,   5306, 950.0),
            WeightedItem(Items.MIND_RUNE_558,           60,  60,   300.0),
            WeightedItem(Items.MIND_RUNE_558,           100, 850,  300.0),
            WeightedItem(Items.CHAOS_RUNE_562,          115, 720,  300.0),
            WeightedItem(Items.DEATH_RUNE_560,          15,  15,   300.0),
            WeightedItem(Items.DEATH_RUNE_560,          70,  230,  300.0),
            WeightedItem(Items.BLOOD_RUNE_565,          35,  230,  300.0),
            WeightedItem(Items.BOLT_RACK_4740,          35,  280,  300.0),
            WeightedItem(Items.TOOTH_HALF_OF_A_KEY_985, 1,    1,   25.0),
            WeightedItem(Items.LOOP_HALF_OF_A_KEY_987,  1,    1,   25.0),
            WeightedItem(Items.DRAGON_MED_HELM_1149,    1,    1,   3.0)
    )
    private val AHRIM  = arrayOf(4708, 4710, 4712, 4714)
    private val DHAROK = arrayOf(4716, 4718, 4720, 4722)
    private val GUTHAN = arrayOf(4724, 4726, 4728, 4730)
    private val KARIL  = arrayOf(4732, 4734, 4736, 4738)
    private val TORAG  = arrayOf(4745, 4747, 4749, 4751)
    private val VERAC  = arrayOf(4753, 4755, 4757, 4759)
    private val BARROWS_DROP_IDS = arrayOf(AHRIM, DHAROK, GUTHAN, KARIL, TORAG, VERAC)

    /**
     * Rewards the player.
     *
     * @param player The player.
     */
    @JvmStatic
    fun reward(player: Player) {
        var rewards: MutableList<Item> = ArrayList()

        // Roll barrows rewards
        var barrowsRewardsIDs: MutableList<Int> = ArrayList()
        for (i in 0..5) {
            if (player.savedData.activityData.barrowBrothers[i]) {
                barrowsRewardsIDs.addAll(BARROWS_DROP_IDS[i])
            }
        }
        val nKilledBrothers = barrowsRewardsIDs.size / 4
        val maxRolls = 1 + maxOf(0, RandomFunction.random(nKilledBrothers - 3, nKilledBrothers))
        var nBarrowsRewards = 0
        val barrowsItemChance = 5 * barrowsRewardsIDs.size
        for (i in 0 until maxRolls) {
            if (RandomFunction.random(3223) <= barrowsItemChance) {
                nBarrowsRewards++
            }
        }
        if (nBarrowsRewards > barrowsRewardsIDs.size) {
            nBarrowsRewards = barrowsRewardsIDs.size
        }

        // Award all non-barrows rewards (using the remaining rolls)
        val remainingRolls = maxRolls - nBarrowsRewards
        if (remainingRolls > 0) {
            val nonBarrowsRewards = REGULAR_DROPS.roll(null, remainingRolls)
            addItem@for (i in 0 until nonBarrowsRewards.size) {
                if (i > 0) {
                    // If we have already awarded this item, just combine their amounts
                    for (j in 0 until rewards.size) {
                        if (nonBarrowsRewards[i].id == rewards[j].id) {
                            rewards[j].amount += nonBarrowsRewards[i].amount
                            continue@addItem
                        }
                    }
                }
                rewards.add(nonBarrowsRewards[i])
            }
        }

        // Award a random selection of barrows rewards, if rolled
        if (nBarrowsRewards > 0) {
            barrowsRewardsIDs.shuffle()
            for (i in 0 until nBarrowsRewards) {
                rewards.add(Item(barrowsRewardsIDs[i], 1))
            }
        }

        InterfaceContainer.generateItems(player, rewards.toTypedArray(), arrayOf("Examine"), 364, 4, 3, 4)
        player.interfaceManager.open(Component(Components.TRAIL_REWARD_364))
        BossKillCounter.addtoBarrowsCount(player)
        for (item in rewards) {
            announceIfRare(player, item)
            if (!player.inventory.add(item)) {
                GroundItemManager.create(item, player)
            }
        }
    }
}
