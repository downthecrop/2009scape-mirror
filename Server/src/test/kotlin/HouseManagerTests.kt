import core.game.node.entity.player.link.music.MusicEntry
import core.game.node.entity.skill.construction.HouseLocation
import core.game.node.entity.skill.construction.HouseManager
import core.game.node.entity.skill.construction.Servant
import core.game.node.entity.skill.construction.ServantType
import core.game.world.map.RegionManager
import core.game.world.map.path.Pathfinder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class HouseManagerTests {
    companion object {
        init {TestUtils.preTestSetup()}
    }

    val manager = HouseManager()
    val testPlayer = TestUtils.getMockPlayer("test")

    @Test fun constructShouldLoadTheConstructedRegion() {
        val newManager = HouseManager()
        newManager.createNewHouseAt(HouseLocation.RIMMINGTON) //add a room to it, already tested below
        newManager.construct()
        Assertions.assertNotEquals(0, newManager.houseRegion.planes[0].getRegionChunk(4, 3).objects.size)
    }

    @Test fun constructShouldRegisterNewRegionToRegionManager() {
        val newManager = HouseManager()
        newManager.construct()
        Assertions.assertEquals(true, RegionManager.forId(newManager.houseRegion.id) == newManager.houseRegion)
    }

    @Test fun constructShouldSetTheRegionInTheHouseManager() {
        val newManager = HouseManager()
        newManager.construct()
        Assertions.assertNotEquals(null, newManager.houseRegion)
    }

    @Test fun constructShouldSetTheRegionBorders() {
        val newManager = HouseManager()
        newManager.construct()
        Assertions.assertNotEquals(null, newManager.houseRegion.borders)
    }

    @Test fun constructShouldSetUpdateAllPlanes() {
        val newManager = HouseManager()
        newManager.construct()
        Assertions.assertEquals(true, newManager.houseRegion.isUpdateAllPlanes)
    }

    @Test fun constructShouldReplacePlanes1And2UnusedChunksAndAllPlane3ChunksWithEmptyChunks() {
        val newManager = HouseManager()
        newManager.construct()
        for(z in 1..3)
            for (objs in newManager.houseRegion.planes[z].objects)
                for (obj in objs) Assertions.assertEquals(null, obj)
    }

    @Test fun leaveShouldPlaceThePlayerAtTheHouseLocationExitLocation() {
        val newManager = HouseManager()
        val newPlayer = TestUtils.getMockPlayer("test3")
        newManager.construct()
        newManager.enter(newPlayer, false)
        TestUtils.advanceTicks(5)
        HouseManager.leave(newPlayer)
        Assertions.assertEquals(newManager.location.exitLocation, newPlayer.location)
    }

    @Test fun toggleBuildingModeShouldChangeBuildingMode() {
        val newManager = HouseManager()
        val newPlayer = TestUtils.getMockPlayer("test4")
        newManager.enter(newPlayer, false)
        TestUtils.advanceTicks(5)
        newManager.toggleBuildingMode(newPlayer, true)
        Assertions.assertEquals(true, newManager.isBuildingMode)
    }

    @Test fun createShouldPlaceGardenInRooms() {
        manager.createNewHouseAt(HouseLocation.RIMMINGTON)
        Assertions.assertEquals(true, manager.hasRoomAt(0, 4, 3))
    }

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

    @Test fun enterShouldSendServantIfHasOne() {
        manager.servant = Servant(ServantType.BUTLER)
        manager.enter(testPlayer, false)
        TestUtils.advanceTicks(5)
        Assertions.assertEquals(true, manager.servant.isActive)
    }

    @Test fun enterShouldSetBuildModeAndRoomAmountVarps() {
        manager.enter(testPlayer, false)
        Assertions.assertEquals(true, testPlayer.varpManager.get(261).varbits.isNotEmpty())
        Assertions.assertEquals(true, testPlayer.varpManager.get(262).varbits.isNotEmpty())
    }

    @Test fun enterShouldUnlockPOHMusicTrack() {
        manager.enter(testPlayer, false)
        Assertions.assertEquals(true, testPlayer.musicPlayer.unlocked.contains(MusicEntry.forId(454).index))
    }

    @Test fun reloadShouldPreserveLocalPlayerLocation() {
        val separateManager = HouseManager()
        val separatePlayer = TestUtils.getMockPlayer("test2")
        separateManager.enter(separatePlayer, false)
        TestUtils.advanceTicks(5)
        Pathfinder.find(separatePlayer, separatePlayer.location.transform(10,10,0)).walk(separatePlayer)
        TestUtils.advanceTicks(20)
        val localX = separatePlayer.location.localX
        val localY = separatePlayer.location.localY
        separateManager.reload(separatePlayer, true)
        TestUtils.advanceTicks(20)
        Assertions.assertEquals(localX, separatePlayer.location.localX)
        Assertions.assertEquals(localY, separatePlayer.location.localY)
    }
}