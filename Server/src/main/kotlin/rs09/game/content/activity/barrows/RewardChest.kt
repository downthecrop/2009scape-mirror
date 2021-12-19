package rs09.game.content.activity.barrows

import api.*
import core.game.component.Component
import core.game.container.access.InterfaceContainer
import core.game.content.global.BossKillCounter
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs09.consts.Components
import org.rs09.consts.Items
import rs09.game.content.global.WeightBasedTable
import rs09.game.content.global.WeightedItem
import java.util.*

/**
 * The reward chest.
 * @author Ceikry
 * and slightly kermit
 */
object RewardChest {
    /**
     * The low profit drop table.
     */
    private val DROP_TABLE = WeightBasedTable.create(
            //Weighted total = 3050
            WeightedItem(Items.COINS_995,             1, 5306, 950.0),
            WeightedItem(Items.MIND_RUNE_558,        60, 60, 300.0),
            WeightedItem(Items.MIND_RUNE_558,       100, 850, 300.0),
            WeightedItem(Items.CHAOS_RUNE_562,      115, 720, 300.0),
            WeightedItem(Items.DEATH_RUNE_560,       15, 15, 300.0),
            WeightedItem(Items.DEATH_RUNE_560,       70, 230, 300.0),
            WeightedItem(Items.BLOOD_RUNE_565,       35, 230, 300.0),
            WeightedItem(Items.BOLT_RACK_4740,   35, 280, 300.0),
            //Weight total = 975
            WeightedItem(Items.SUPER_DEFENCE2_165,    1, 1, 325.0),
            WeightedItem(Items.PRAYER_POTION2_141,    1, 1, 325.0),
            WeightedItem(Items.RESTORE_POTION2_129,   1, 1, 325.0),
            //Weight total = 53
            WeightedItem(Items.TOOTH_HALF_OF_A_KEY_985, 1, 1, 25.0),
            WeightedItem(Items.LOOP_HALF_OF_A_KEY_987,  1, 1, 25.0),
            WeightedItem(Items.DRAGON_MED_HELM_1149,   1, 1, 3.0),
            //Weight total = 120 BARROWS ITEMS V
            WeightedItem(4708, 1, 1, 5.0),
            WeightedItem(4710, 1, 1, 5.0),
            WeightedItem(4712, 1, 1, 5.0),
            WeightedItem(4714, 1, 1, 5.0),
            WeightedItem(4716, 1, 1, 5.0),
            WeightedItem(4718, 1, 1, 5.0),
            WeightedItem(4720, 1, 1, 5.0),
            WeightedItem(4722, 1, 1, 5.0),
            WeightedItem(4724, 1, 1, 5.0),
            WeightedItem(4726, 1, 1, 5.0),
            WeightedItem(4728, 1, 1, 5.0),
            WeightedItem(4730, 1, 1, 5.0),
            WeightedItem(4732, 1, 1, 5.0),
            WeightedItem(4734, 1, 1, 5.0),
            WeightedItem(4736, 1, 1, 5.0),
            WeightedItem(4738, 1, 1, 5.0),
            WeightedItem(4745, 1, 1, 5.0),
            WeightedItem(4747, 1, 1, 5.0),
            WeightedItem(4749, 1, 1, 5.0),
            WeightedItem(4751, 1, 1, 5.0),
            WeightedItem(4753, 1, 1, 5.0),
            WeightedItem(4755, 1, 1, 5.0),
            WeightedItem(4757, 1, 1, 5.0),
            WeightedItem(4759, 1, 1, 5.0)
    )

    /**
     * Rewards the player.
     *
     * @param player The player.
     */
    @JvmStatic
    fun reward(player: Player) {
        for (killed in player.savedData.activityData.barrowBrothers) {
            if (!killed){
                player.sendMessage("You can't loot the chest until you kill all 6 barrows brothers.")
                player.removeAttribute("barrow:looted")
                // Because they haven't
                // actually looted the
                // chest yet.
                return
            }
        }
        val rewards: MutableList<Item> = ArrayList()
        var maxRolls = 2 + RandomFunction.random(1,4)
        for (i in 0 until maxRolls) {
            rewards.addAll(DROP_TABLE.roll())
        }
        InterfaceContainer.generateItems(player, rewards.toTypedArray(), arrayOf("Examine"), 364, 4,3,4)
        player.interfaceManager.open(Component(Components.TRAIL_REWARD_364))
        BossKillCounter.addtoBarrowsCount(player)
        for(item in rewards){
            announceIfRare(player, item);
            if(!player.inventory.add(item)){
                GroundItemManager.create(item,player)
            }
        }
    }
}
