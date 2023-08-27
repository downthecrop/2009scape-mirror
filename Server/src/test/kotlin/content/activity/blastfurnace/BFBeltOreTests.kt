package content.activity.blastfurnace

import TestUtils
import content.minigame.blastfurnace.BFBeltOre
import content.minigame.blastfurnace.BlastFurnace
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.rs09.consts.Items

class BFBeltOreTests {
    init {TestUtils.preTestSetup()}

    @Test fun oreShouldMoveCloserToPotWhenTicked() {
        TestUtils.getMockPlayer("bf-oremove-test").use { p ->
            p.location = BFBeltOre.ORE_START_LOC
            val ore = BlastFurnace.addOreToBelt(p, Items.IRON_ORE_440, 5)
            Assertions.assertEquals(BFBeltOre.ORE_START_LOC, ore.location)
            Assertions.assertEquals(BFBeltOre.ORE_START_LOC, ore.npcInstance?.location)

            ore.tick()
            val expectedLoc = BFBeltOre.ORE_START_LOC.transform(0, -1, 0)
            Assertions.assertEquals(expectedLoc, ore.location)
            Assertions.assertEquals(expectedLoc, ore.npcInstance?.properties?.teleportLocation)

            TestUtils.advanceTicks(1, false)
            ore.tick()
            Assertions.assertEquals(expectedLoc, ore.npcInstance?.location)
        }
    }

    @Test fun oreShouldBeAddedToOreContainerAfterReachingEnd() {
        TestUtils.getMockPlayer("bf-oremoveadd-test").use { p ->
            p.location = BFBeltOre.ORE_START_LOC
            val ore = BlastFurnace.addOreToBelt(p, Items.IRON_ORE_440, 5)

            for (i in 0 until 4)
                if (ore.tick()) BlastFurnace.getPlayerState(p).oresOnBelt.remove(ore)

            val container = BlastFurnace.getOreContainer(p)
            Assertions.assertEquals(5, container.getOreAmount(Items.IRON_ORE_440))
            Assertions.assertEquals(0, BlastFurnace.getAmountOnBelt(p, Items.IRON_ORE_440))
        }
    }
}