package content.activity.blastfurnace

import TestUtils
import content.global.skill.smithing.smelting.Bar
import content.minigame.blastfurnace.BFPlayerState
import content.minigame.blastfurnace.BlastFurnace
import core.api.addItem
import core.api.amountInInventory
import core.api.setVarbit
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.rs09.consts.Items

class BFPlayerStateTests {
    init { TestUtils.preTestSetup() }

    @Test fun processOreIntoBarsShouldDoNothingIfBarsNotCooled() {
        TestUtils.getMockPlayer("bf-barsnotcooled").use { p ->
            val state = BlastFurnace.getPlayerState(p)
            state.container.addOre(Items.IRON_ORE_440, 28)
            Assertions.assertEquals(true, state.processOresIntoBars())
            Assertions.assertEquals(28, state.container.getBarAmount(Bar.IRON))

            state.container.addCoal(40)
            state.container.addOre(Items.RUNITE_ORE_451, 10)
            Assertions.assertEquals(false, state.processOresIntoBars())

            Assertions.assertEquals(0, state.container.getBarAmount(Bar.RUNITE))
        }
    }

    @Test fun processOreIntoBarsShouldDoNothingIfBarsUnchecked() {
        TestUtils.getMockPlayer("bf-barsnotchecked").use { p ->
            val state = BlastFurnace.getPlayerState(p)
            state.container.addOre(Items.IRON_ORE_440, 28)
            Assertions.assertEquals(true, state.processOresIntoBars())
            state.coolBars()

            state.container.addCoal(40)
            state.container.addOre(Items.RUNITE_ORE_451, 10)
            Assertions.assertEquals(false, state.processOresIntoBars())
            Assertions.assertEquals(0, state.container.getBarAmount(Bar.RUNITE))

            state.checkBars()
            Assertions.assertEquals(true, state.processOresIntoBars())
            Assertions.assertEquals(10, state.container.getBarAmount(Bar.RUNITE))
        }
    }

    @Test fun shouldBeAbleToClaimBars() {
        TestUtils.getMockPlayer("bf-barclaiminig").use { p ->
            val state = BlastFurnace.getPlayerState(p)
            state.container.addOre(Items.IRON_ORE_440, 28)
            Assertions.assertEquals(true, state.processOresIntoBars())
            state.coolBars()

            Assertions.assertEquals(true, state.claimBars(Bar.IRON, 5))
            Assertions.assertEquals(5, amountInInventory(p, Items.IRON_BAR_2351))
            Assertions.assertEquals(23, state.container.getBarAmount(Bar.IRON))
        }
    }

    @Test fun shouldNotBeAbleToClaimMoreBarsThanFreeInventorySlots() {
        TestUtils.getMockPlayer("bf-claimbarslessslots").use { p ->
            val state = BlastFurnace.getPlayerState(p)
            state.container.addOre(Items.IRON_ORE_440, 28)
            Assertions.assertEquals(true, state.processOresIntoBars())
            state.coolBars()

            addItem(p, Items.ABYSSAL_WHIP_4151, 27)

            Assertions.assertEquals(true, state.claimBars(Bar.IRON, 5))
            Assertions.assertEquals(1, amountInInventory(p, Items.IRON_BAR_2351))
            Assertions.assertEquals(27, state.container.getBarAmount(Bar.IRON))
        }
    }

    @Test fun claimBarsShouldDoNothingAndGrantNoItemIfInventoryFull() {
        TestUtils.getMockPlayer("bf-claimbarsnoslots").use { p ->
            val state = BlastFurnace.getPlayerState(p)
            state.container.addOre(Items.IRON_ORE_440, 28)
            Assertions.assertEquals(true, state.processOresIntoBars())
            state.coolBars()

            addItem(p, Items.ABYSSAL_WHIP_4151, 28)

            Assertions.assertEquals(false, state.claimBars(Bar.IRON, 5))
            Assertions.assertEquals(0, amountInInventory(p, Items.IRON_BAR_2351))
            Assertions.assertEquals(28, state.container.getBarAmount(Bar.IRON))
        }
    }

    @Test fun claimBarsShouldDoNothingIfBarsNotCooled() {
        TestUtils.getMockPlayer("bf-claimbarsnotcooled").use { p ->
            val state = BlastFurnace.getPlayerState(p)
            state.container.addOre(Items.IRON_ORE_440, 28)
            Assertions.assertEquals(true, state.processOresIntoBars())
            Assertions.assertEquals(false, state.claimBars(Bar.IRON, 5))
        }
    }
}