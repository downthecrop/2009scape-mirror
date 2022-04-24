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
        init {
            ServerConfigParser.parse("worldprops/default.conf")
            XteaParser().load()
            Cache.init(this.javaClass.getResource("cache")?.path.toString())
        }
    }

    @Test fun testRegionLoad() {
        val region = RegionManager.forId(12850)
        Region.load(region)
        Assertions.assertNotEquals(0, region.objectCount, "Region has no objects! (Failed to parse?)")
    }

    @Test fun testDynamicRegionHasSameObjects() {
        val base = RegionManager.forId(12850)
        Region.load(base)
        val dynamic = DynamicRegion.create(12850)
        Region.load(dynamic)

        Assertions.assertEquals(base.objectCount, dynamic.objectCount, "Dynamic and standard have differing object counts!")
    }

    @Test fun testObjectExistsInStandardRegion() {
        val base = RegionManager.forId(12850)
        Region.load(base)

        val targetLoc = base.baseLocation.transform(23, 17, 0) //location of a bush
        Assertions.assertNotEquals(null, RegionManager.getObject(targetLoc), "Object does not exist at expected location!")
    }

    @Test fun testObjectExistsInDynamicRegion() {
        val base = RegionManager.forId(12850)
        val dynamic = DynamicRegion.create(12850)
        Region.load(base)
        Region.load(dynamic)

        val targetLoc = dynamic.baseLocation.transform(23, 17, 0)
        Assertions.assertNotEquals(null, RegionManager.getObject(targetLoc), "Object does not exist at expected location!")
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
        Assertions.assertNotEquals(null, RegionManager.getObject(targetLoc), "Object does not exist at expected location!")
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
        Assertions.assertNotEquals(null, RegionManager.getObject(targetLoc), "Object does not exist at expected location!")
    }
}