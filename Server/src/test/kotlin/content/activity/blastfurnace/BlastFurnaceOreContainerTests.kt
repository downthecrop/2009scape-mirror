package content.activity.blastfurnace

import content.global.skill.smithing.smelting.Bar
import content.minigame.blastfurnace.BFOreContainer
import content.minigame.blastfurnace.BlastConsts
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.rs09.consts.Items

class BlastFurnaceOreContainerTests {
    @Test fun shouldBeAbleToAddCoal() {
        val cont = BFOreContainer()
        cont.addCoal(15)
        Assertions.assertEquals(15, cont.coalAmount())
    }

    @Test fun addCoalShouldReturnExtraAmountIfAddingMoreThanPossible() {
        val cont = BFOreContainer()
        cont.addCoal(BlastConsts.COAL_LIMIT - 26)
        Assertions.assertEquals(2, cont.addCoal(28))
    }

    @Test fun shouldBeAbleToAddOres() {
        val cont = BFOreContainer()
        cont.addOre (Items.IRON_ORE_440, 20)
        Assertions.assertEquals(20, cont.getOreAmount(Items.IRON_ORE_440))
    }

    @Test fun addOreShouldReturnExtraAmountIfAddingMoreThanPossible() {
        val cont = BFOreContainer()
        cont.addOre(Items.IRON_ORE_440, BlastConsts.ORE_LIMIT - 2)
        Assertions.assertEquals(3, cont.addOre(Items.IRON_ORE_440, 5))
    }

    @Test fun addOreShouldReturnExtraAmountWhenAddingMoreCopperOrTinThanPossible() {
        val contTin = BFOreContainer()
        Assertions.assertEquals(28, contTin.addOre(Items.TIN_ORE_438, 56))

        val contCopper = BFOreContainer()
        Assertions.assertEquals(28, contCopper.addOre(Items.COPPER_ORE_436, 56))
    }

    @Test fun convertToBarsShouldYieldExpectedResults() {
        data class Data (val coalAmount: Int, val oreAmount: Array<Pair<Int,Int>>, val expectedOreAmounts: Array<Pair<Int,Int>>, val expectedCoalAmount: Int, val expectedBarResult: Array<Pair<Bar,Int>>)
        val testData = arrayOf(
            Data( //standard case - 20 iron with no coal creates 20 iron bars
                0, arrayOf(Pair(Items.IRON_ORE_440, 20)),
                arrayOf(Pair(Items.IRON_ORE_440, 0)), 0, arrayOf(Pair(Bar.IRON, 20))
            ),
            Data ( //edge case - 20 iron with 10 coal produces 10 steel and 10 iron
                10, arrayOf(Pair(Items.IRON_ORE_440, 20)),
                    arrayOf(Pair(Items.IRON_ORE_440, 0)), 0, arrayOf(Pair(Bar.STEEL, 10), Pair(Bar.IRON, 10))
            ),
            Data ( //standard case - 20 coal with 10 mithril produces 10 mithril bars
                20, arrayOf(Pair(Items.MITHRIL_ORE_447, 10)),
                arrayOf(Pair(Items.MITHRIL_ORE_447, 0)), 0, arrayOf(Pair(Bar.MITHRIL, 10))
            ),
            Data ( //standard case - 30 coal with 10 adamantite produces 10 addy bars
                30, arrayOf(Pair(Items.ADAMANTITE_ORE_449, 10)),
                arrayOf(Pair(Items.ADAMANTITE_ORE_449, 0)), 0, arrayOf(Pair(Bar.ADAMANT, 10))
            ),
            Data ( //standard case - 40 coal with 10 runite produces 10 runite bars
                40, arrayOf(Pair(Items.RUNITE_ORE_451, 10)),
                arrayOf(Pair(Items.RUNITE_ORE_451, 0)), 0, arrayOf(Pair(Bar.RUNITE, 10))
            ),
            Data ( //semi-edge case - 28 gold with 150 coal produces 28 gold bars with 150 coal remaining (doesn't use any coal when not needed)
                150, arrayOf(Pair(Items.GOLD_ORE_444, 28)),
                arrayOf(Pair(Items.GOLD_ORE_444, 0)), 150, arrayOf(Pair(Bar.GOLD, 28))
            ),
            Data ( //edge case - 18 silver and 10 runite with 58 coal produces 18 silver bars and 10 runite bars with 18 coal remaining
                58, arrayOf(Pair(Items.SILVER_ORE_442, 18), Pair(Items.RUNITE_ORE_451, 10)),
                arrayOf(Pair(Items.SILVER_ORE_442, 0), Pair(Items.RUNITE_ORE_451, 0)), 18, arrayOf(Pair(Bar.SILVER, 18), Pair(Bar.RUNITE, 10))
            ),
            Data ( //edge case - only 20 coal but 10 runite (half of what's needed) produces 5 runite bars, with 5 runite ore and 0 coal remaining.
                20, arrayOf(Pair(Items.RUNITE_ORE_451, 10)),
                arrayOf(Pair(Items.RUNITE_ORE_451, 5)), 0, arrayOf(Pair(Bar.RUNITE, 5))
            ),
            Data (//technically an edge case - 28 copper and 28 tin makes 28 bronze with nothing leftover.
                0, arrayOf(Pair(Items.COPPER_ORE_436, 28), Pair(Items.TIN_ORE_438, 28)),
                arrayOf(Pair(Items.COPPER_ORE_436, 0), Pair(Items.TIN_ORE_438, 0)), 0, arrayOf(Pair(Bar.BRONZE, 28))
            ),
            Data (//edge case - 10 copper and no tin makes nothing with 10 copper leftover
                0, arrayOf(Pair(Items.COPPER_ORE_436, 10)),
                arrayOf(Pair(Items.COPPER_ORE_436, 10)), 0, arrayOf(Pair(Bar.BRONZE, 0))
            ),
            Data (//edge case - 14 copper and 5 tin make 5 bronze bars with 9 copper leftover
                0, arrayOf(Pair(Items.COPPER_ORE_436, 14), Pair(Items.TIN_ORE_438, 5)),
                arrayOf(Pair(Items.COPPER_ORE_436, 9), Pair(Items.TIN_ORE_438, 0)), 0, arrayOf(Pair(Bar.BRONZE, 5))
            )
        )

        var index = 0
        for ((initialCoal, initialOres, expectedOres, expectedCoal, expectedBars) in testData) {
            val cont = BFOreContainer()
            cont.addCoal(initialCoal)
            for ((ore, amount) in initialOres) cont.addOre(ore, amount)
            cont.convertToBars()

            for ((ore, amount) in expectedOres)
                Assertions.assertEquals(amount, cont.getOreAmount(ore), "Problem testcase was $index - Missing $ore")
            for ((bar, amount) in expectedBars)
                Assertions.assertEquals(amount, cont.getBarAmount(bar), "Problem testcase was $index - Missing ${bar.name}")
            Assertions.assertEquals(expectedCoal, cont.coalAmount(), "Problem testcase was $index")
            index++
        }
    }

    @Test fun convertToBarsShouldNotConsumeMaterialsForAlreadyFilledBarType() {
        val cont = BFOreContainer()
        cont.addOre(Items.IRON_ORE_440, 28)
        cont.convertToBars()
        Assertions.assertEquals(28, cont.getBarAmount(Bar.IRON))

        cont.addOre(Items.IRON_ORE_440, 28)
        cont.convertToBars()
        Assertions.assertEquals(28, cont.getBarAmount(Bar.IRON))
        Assertions.assertEquals(28, cont.getOreAmount(Items.IRON_ORE_440))
    }

    @Test fun oreContainerShouldCleanlySerializeAndDeserializeFromJson() {
        val cont = BFOreContainer()
        cont.addOre(Items.IRON_ORE_440, 28)
        cont.convertToBars()

        cont.addOre(Items.RUNITE_ORE_451, 15)
        cont.addOre(Items.MITHRIL_ORE_447, 13)
        cont.addCoal(150)

        val json = cont.toJson()
        val deserialized = BFOreContainer.fromJson(json)

        Assertions.assertEquals(28, deserialized.getBarAmount(Bar.IRON))
        Assertions.assertEquals(15, cont.getOreAmount(Items.RUNITE_ORE_451))
        Assertions.assertEquals(13, cont.getOreAmount(Items.MITHRIL_ORE_447))
        Assertions.assertEquals(150, cont.coalAmount())
    }

    @Test fun shouldBeAbleToRemoveBars() {
        val cont = BFOreContainer()
        cont.addOre(Items.IRON_ORE_440, 28)
        cont.convertToBars()

        val bars = cont.takeBars(Bar.IRON, 15)
        Assertions.assertEquals(15, bars?.amount)
        Assertions.assertEquals(13, cont.getBarAmount(Bar.IRON))
    }

    @Test fun shouldNotBeAbleToRemoveMoreBarsThanPossible() {
        val cont = BFOreContainer()
        cont.addOre(Items.IRON_ORE_440, 28)
        cont.convertToBars()

        val bars = cont.takeBars(Bar.IRON, 50)
        Assertions.assertEquals(28, bars?.amount)
        Assertions.assertEquals(0, cont.getBarAmount(Bar.IRON))
    }

    @Test fun convertToBarsShouldReturnXPReward() {
        val cont = BFOreContainer()
        cont.addOre(Items.IRON_ORE_440, 28)

        Assertions.assertEquals(350.0, cont.convertToBars())
    }

    @Test fun removingBarsWithNoStockReturnsNull() {
        val cont = BFOreContainer()
        Assertions.assertEquals(null, cont.takeBars(Bar.RUNITE, 1))
    }
}