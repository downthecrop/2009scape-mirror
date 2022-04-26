import core.game.node.entity.skill.construction.HouseManager
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class HouseManagerTests {
    companion object {
        init {TestUtils.preTestSetup()}
    }

    val manager = HouseManager()
    val testPlayer = TestUtils.getMockPlayer("test")

    @Test fun enterShouldConstructDynamicRegionIfItHasNotBeenConstructed() {
        manager.enter(testPlayer, false)
        Assertions.assertEquals(true, manager.isLoaded)
    }

    @Test fun enterShouldOpenHouseLoadInterfaceAndThenCloseAutomatically() {
        manager.enter(testPlayer, false)
        Assertions.assertEquals(399, testPlayer.interfaceManager.opened.id)
        TestUtils.advanceTicks(5)
        Assertions.assertNotEquals(null, testPlayer.interfaceManager.opened)
    }
}