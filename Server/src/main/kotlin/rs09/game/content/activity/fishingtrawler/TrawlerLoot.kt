package rs09.game.content.activity.fishingtrawler

import core.game.node.entity.skill.fishing.Fish
import core.game.node.item.Item
import core.game.node.item.WeightedChanceItem
import core.tools.RandomFunction
import org.rs09.consts.Items

/**
 * Rolls/stores the loot table for fishing trawler
 * @author Ceikry
 */
object TrawlerLoot {
    val junkItems = arrayOf(Items.BROKEN_ARMOUR_698, Items.BROKEN_ARROW_687, Items.OLD_BOOT_685, Items.BROKEN_GLASS_1469, Items.BROKEN_STAFF_689, Items.BUTTONS_688,Items.DAMAGED_ARMOUR_697, Items.RUSTY_SWORD_686, Items.EMPTY_POT_1931, Items.OYSTER_407)
    val trawlerFish = arrayOf(Fish.MANTA_RAY, Fish.SEA_TURTLE, Fish.SHARK, Fish.SWORDFISH, Fish.LOBSTER, Fish.TUNA, Fish.ANCHOVIE, Fish.SARDINE, Fish.SHRIMP)

    fun rollTrawlerFish(fishLevel: Int): Item {
        while(true) {
            for(f in trawlerFish) {
                if(f.level > fishLevel) {
                    continue
                }
                val lo = 0.6133
                val hi = 0.7852
                //val chance = RandomFunction.getSkillSuccessChance(lo, hi, fishLevel)
                val chance = (fishLevel.toDouble() - 15.0)*((hi - lo) / (99.0 - 15.0)) + lo
                if(RandomFunction.random(0.0, 1.0) < chance) {
                    return f.item
                }
            }
        }
    }

    @JvmStatic
    fun getLoot(fishLevel: Int, rolls: Int, skipJunk: Boolean): ArrayList<Item>{
        val loot = ArrayList<Item>()
        for(i in 0 until rolls){
            val item = RandomFunction.rollWeightedChanceTable(listOf(*lootTable))
            if(item.id == 0) {
                val item = rollTrawlerFish(fishLevel)
                loot.add(item)
            } else {
                loot.add(item)
            }
        }
        if(skipJunk){
            val removeList = ArrayList<Item>()
            for(item in loot){
                if(junkItems.contains(item.id)) removeList.add(item)
            }
            loot.removeAll(removeList)
        }
        return loot
    }

    val lootTable = arrayOf(
            WeightedChanceItem(0,1,1430),
            WeightedChanceItem(Items.BROKEN_ARROW_687,1,70),
            WeightedChanceItem(Items.BROKEN_GLASS_1469,1,70),
            WeightedChanceItem(Items.BROKEN_STAFF_689,1,70),
            WeightedChanceItem(Items.BUTTONS_688,1,80),
            WeightedChanceItem(Items.DAMAGED_ARMOUR_697,1,70),
            WeightedChanceItem(Items.OLD_BOOT_685,1,60),
            WeightedChanceItem(Items.OYSTER_407,1,50),
            WeightedChanceItem(Items.EMPTY_POT_1931,1,50),
            WeightedChanceItem(Items.RUSTY_SWORD_686,1,50),
            //Inauthentic rewards
            WeightedChanceItem(Items.LOOP_HALF_OF_A_KEY_987,1,7),
            WeightedChanceItem(Items.TOOTH_HALF_OF_A_KEY_985,1,7),
            WeightedChanceItem(Items.CASKET_405,1,60),
            WeightedChanceItem(Items.PIRATES_HAT_2651,1,2),
            WeightedChanceItem(Items.LUCKY_CUTLASS_7140,1,2)
    )
}
