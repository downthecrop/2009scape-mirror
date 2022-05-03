import core.cache.Cache
import core.game.world.map.Region
import core.game.world.map.RegionChunk
import core.game.world.map.RegionManager
import core.game.world.map.build.DynamicRegion
import core.game.world.map.build.LandscapeParser
import org.junit.Assert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import rs09.game.system.config.ServerConfigParser
import rs09.game.system.config.XteaParser

class RegionTests {
    companion object {
        init {TestUtils.preTestSetup();}
    }

    @Test fun testRegionLoad() {
        val region = RegionManager.forId(12850)
        Region.load(region)
        Assertions.assertNotEquals(0, region.objectCount, "Region has no objects! (Failed to parse?)")
    }

    @Test fun shouldHaveDifferentIdForDynamicCopy() {
        val region = RegionManager.forId(12850)
        val dynamic = DynamicRegion.create(12850)
        Assertions.assertNotEquals(region.regionId, (dynamic.x shl 8) or dynamic.y)
    }

    @Test fun dynamicRegionCreationShouldNotReplaceOriginal() {
        val region = RegionManager.forId(12850)
        val dynamic = DynamicRegion.create(12850)
        Assertions.assertEquals(false, RegionManager.forId(12850) is DynamicRegion)
    }

    @Test fun testDynamicRegionHasSameObjects() {
        val base = RegionManager.forId(12850)
        Region.load(base)
        val dynamic = DynamicRegion.create(12850)
        Region.load(dynamic)

        Assertions.assertEquals(true, dynamic.objectCount > 0, "Dynamic and standard have differing object counts!")
    }

    @Test fun testObjectExistsInStandardRegion() {
        val base = RegionManager.forId(12850)
        Region.load(base)

        val targetLoc = base.baseLocation.transform(23, 17, 0) //location of a bush
        Assertions.assertEquals(36782, RegionManager.getObject(targetLoc)?.id ?: -1, "Object does not exist at expected location!")
    }

    @Test fun testObjectExistsInDynamicRegion() {
        val base = RegionManager.forId(12850)
        val dynamic = DynamicRegion.create(12850)
        Region.load(base)
        Region.load(dynamic)

        val targetLoc = dynamic.baseLocation.transform(23, 17, 0)
        Assertions.assertEquals(36782, RegionManager.getObject(targetLoc)?.id ?: -1, "Object does not exist at expected location!")
    }

    @Test fun testObjectExistsInCopiedChunk() {
        val base = RegionManager.forId(12850)
        val dynamic = DynamicRegion.create(12851)
        Region.load(base)
        Region.load(dynamic)
        val targetLoc = dynamic.baseLocation.transform(23, 17, 0)
        Assertions.assertEquals(null, RegionManager.getObject(targetLoc), "Object exists pre-copy?")
        val replacement = base.planes[0].getRegionChunk(2, 2)
        dynamic.replaceChunk(0, 2, 2, replacement.copy(dynamic.planes[0]), base)
        Assertions.assertEquals(36782, RegionManager.getObject(targetLoc)?.id ?: -1, "Object does not exist at expected location!")
    }

    @Test fun testObjectExistsInCopiedChunkUsingBuildFlag() {
        val base = RegionManager.forId(12850)
        val dynamic = DynamicRegion.create(12851)
        Region.load(base, true)
        Region.load(dynamic, true)
        val targetLoc = dynamic.baseLocation.transform(23, 17, 0)
        Assertions.assertEquals(null, RegionManager.getObject(targetLoc), "Object exists pre-copy?")
        val replacement = base.planes[0].getRegionChunk(2, 2)
        dynamic.replaceChunk(0, 2, 2, replacement.copy(dynamic.planes[0]), base)
        Assertions.assertEquals(36782, RegionManager.getObject(targetLoc)?.id ?: -1, "Object does not exist at expected location!")
    }

    @Test fun testObjectExistsInCopiedChunkCopiedIntoBlankRegion() {
        val base = RegionManager.forId(12850)
        val borders = DynamicRegion.reserveArea(8,8)
        val dynamic = DynamicRegion(-1, borders.southWestX shr 6, borders.southWestY shr 6)
        dynamic.borders = borders
        dynamic.isUpdateAllPlanes = true
        RegionManager.addRegion(dynamic.id, dynamic)
        val targetLoc = dynamic.baseLocation.transform(23, 17, 0)
        val replacement = base.planes[0].getRegionChunk(2,2)
        dynamic.replaceChunk(0, 2, 2, replacement.copy(dynamic.planes[0]), base)
        Assertions.assertEquals(36782, RegionManager.getObject(targetLoc)?.id ?: -1, "Object does not exist at expected location!")
    }

    @Test fun testObjectExistsInCopiedChunkInLinkedRegion() {
        val base = DynamicRegion.create(12850)
        val borders = DynamicRegion.reserveArea(8,8)
        val dynamic = DynamicRegion(-1, borders.southWestX shr 6, borders.southWestY shr 6)
        dynamic.borders = borders
        dynamic.isUpdateAllPlanes = true
        RegionManager.addRegion(dynamic.id, dynamic)
        val targetLoc = dynamic.baseLocation.transform(23, 17, 0)
        val replacement = base.planes[0].getRegionChunk(2,2)
        dynamic.replaceChunk(0, 2, 2, replacement.copy(dynamic.planes[0]), base)
        base.link(dynamic)
        Assertions.assertEquals(36782, RegionManager.getObject(targetLoc)?.id ?: -1, "Object does not exist at expected location!")
    }
}