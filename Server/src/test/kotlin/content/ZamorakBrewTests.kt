package content

import TestUtils
import content.data.consumables.Consumables
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.rs09.consts.Items

class ZamorakBrewTests {
    companion object {
        init {
            TestUtils.preTestSetup()
        }
    }

    @Test
    fun zamorakBrewShouldDamageTenPercentOfCurrentLifepointsPlusTwo() {
        TestUtils.getMockPlayer("zammybrewdrinker").use { p ->
            p.skills.setStaticLevel(Skills.HITPOINTS, 99)
            p.skills.lifepoints = 6
            p.inventory.clear()
            p.inventory.add(Item(Items.ZAMORAK_BREW4_2450))

            Consumables.getConsumableById(Items.ZAMORAK_BREW4_2450)
                .consumable
                .consume(p.inventory.get(0), p)

            Assertions.assertEquals(4, p.skills.lifepoints)
        }
    }

    @Test
    fun zamorakMixDamageShouldBeBasedOnLifepointsBeforeItHeals() {
        TestUtils.getMockPlayer("zammymixdrinker").use { p ->
            p.skills.setStaticLevel(Skills.HITPOINTS, 99)
            p.skills.lifepoints = 6
            p.inventory.clear()
            p.inventory.add(Item(Items.ZAMORAK_MIX2_11521))

            Consumables.getConsumableById(Items.ZAMORAK_MIX2_11521)
                .consumable
                .consume(p.inventory.get(0), p)

            // Damage on pre-drink lifepoints (10% of 6 + 2 = 2), then heal 6: 6 - 2 + 6 = 10
            Assertions.assertEquals(10, p.skills.lifepoints)
        }
    }

    @Test
    fun zamorakMixShouldNotKillAtLowLifepoints() {
        TestUtils.getMockPlayer("zammymixlowhp").use { p ->
            p.skills.setStaticLevel(Skills.HITPOINTS, 99)
            p.skills.lifepoints = 2
            p.inventory.clear()
            p.inventory.add(Item(Items.ZAMORAK_MIX2_11521))

            Consumables.getConsumableById(Items.ZAMORAK_MIX2_11521)
                .consumable
                .consume(p.inventory.get(0), p)

            // Hitsplat shows 2 (10% of 2 + 2), but the heal lands first: 2 + 6 - 2 = 6, no death
            Assertions.assertEquals(6, p.skills.lifepoints)
        }
    }
}
