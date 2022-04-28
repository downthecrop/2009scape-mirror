import api.regionspec.*
import core.game.world.map.Region
import core.game.world.map.RegionChunk
import core.game.world.map.RegionManager
import core.game.world.map.build.DynamicRegion
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import rs09.game.system.SystemLogger

class RegionSpecificationTests {
    companion object {
        init {
            TestUtils.preTestSetup()
        }
    }

    @Test
    fun shouldCreateEmptyDynamicRegionWhenBuildWithoutArgs() {
        val specification = RegionSpecification()
        val region = specification.build()
        Assertions.assertNotNull(region)
    }

    @Test
    fun shouldCopyExistingRegionIfRequested() {
        val specification = RegionSpecification(copyOf(12850))
        val region = specification.build()
        Assertions.assertEquals(36782, RegionManager.getObject(region.baseLocation.transform(23, 17, 0))?.id)
    }

    @Test
    fun shouldAllowFillingRegionWithGivenChunk() {
        val base = RegionManager.forId(12850)
        Region.load(base)
        val chunk = base.planes[0].getRegionChunk(2, 2)
        val specification = RegionSpecification(fillWith(chunk).from(base).onPlanes(0))
        val region = specification.build()
        Assertions.assertEquals(36782, RegionManager.getObject(region.baseLocation.transform(23, 17, 0))?.id)
    }

    @Test
    fun shouldAllowCustomRulesForFillingChunks() {
        val base = RegionManager.forId(12850)
        Region.load(base)
        val chunk = base.planes[0].getRegionChunk(2, 2)
        val specification = RegionSpecification(
            fillWith(chunk)
                .from(base)
                .onPlanes(0)
                .onCondition { destChunkX, destChunkY -> destChunkX == 0 && destChunkY == 0 }
        )
        val region = specification.build()
        Assertions.assertEquals(36782, RegionManager.getObject(region.baseLocation.transform(7, 1, 0))?.id)
        Assertions.assertNull(RegionManager.getObject(region.baseLocation.transform(15, 9, 0)))
    }

    @Test
    fun shouldAllowMultipleRulesForFillingChunks() {
        val base = RegionManager.forId(12850)
        Region.load(base)
        val chunk = base.planes[0].getRegionChunk(2, 2)
        val specification = RegionSpecification(
            fillWith(chunk)
                .from(base)
                .onPlanes(0)
                .onCondition { destChunkX, destChunkY -> destChunkX == 0 && destChunkY == 0 },
            fillWith(chunk)
                .from(base)
                .onPlanes(1, 2, 3)
        )
        val region = specification.build()
        Assertions.assertEquals(36782, RegionManager.getObject(region.baseLocation.transform(7, 1, 0))?.id)
        Assertions.assertEquals(36782, RegionManager.getObject(region.baseLocation.transform(7, 1, 1))?.id)
        Assertions.assertNull(RegionManager.getObject(region.baseLocation.transform(15, 9, 0)))
    }

    @Test
    fun fillWithShouldAllowChunkDelegate() {
        val base = RegionManager.forId(12850)
        Region.load(base)
        val specification = RegionSpecification(
            fillWith { destChunkX, destChunkY, destPlane, _ ->
                base.planes[destPlane].getRegionChunk(destChunkX, destChunkY)
            }.from(base).onPlanes(0, 1, 2, 3)
        )
        val region = specification.build()
        Assertions.assertEquals(
            base.planes[0].chunks[1][1].objects[1][3]?.id,
            region.planes[0].chunks[1][1].objects[1][3]?.id
        )
    }
}
