package core

import TestUtils
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.game.world.map.RegionManager
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType
import rs09.game.interaction.InteractionListeners
import rs09.game.node.entity.skill.gather.GatheringSkillOptionListeners

class PathfinderTests {
    companion object {init {TestUtils.preTestSetup(); GatheringSkillOptionListeners().defineListeners() }}

    @Test fun getOccupiedTilesShouldReturnCorrectSetOfTilesThatAnObjectOccupiesAtAllRotations() {
        //clay fireplace - 13609 - sizex: 1, sizey: 2
        val scenery = Scenery(13609, Location.create(50, 50, 0))

        scenery.rotation = 0
        val occupiedAt0 = scenery.occupiedTiles.toTypedArray()
        Assertions.assertArrayEquals(arrayOf(Location.create(50, 50), Location.create(50,51)), occupiedAt0)

        scenery.rotation = 1
        val occupiedAt1 = scenery.occupiedTiles.toTypedArray()
        Assertions.assertArrayEquals(arrayOf(Location.create(50,50), Location.create(51,50)), occupiedAt1)

        scenery.rotation = 2
        val occupiedAt2 = scenery.occupiedTiles.toTypedArray()
        Assertions.assertArrayEquals(arrayOf(Location.create(50,50), Location.create(50,49)), occupiedAt2)

        scenery.rotation = 3
        val occupiedAt3 = scenery.occupiedTiles.toTypedArray()
        Assertions.assertArrayEquals(arrayOf(Location.create(50,50), Location.create(49,50)), occupiedAt3)
    }

    @Test fun movementPulseShouldStopEarlyIfNextToATileOccupiedByTargetObject() {
        val start = Location.create(2731, 3481)
        val dest = RegionManager.getObject(0, 2720, 3475, 1307)
        val p = TestUtils.getMockPlayer("treefindtest")
        p.location = start
        p.init()

        Assertions.assertEquals(true, InteractionListeners.run(1307, IntType.SCENERY, "chop-down", p, dest!!))
        TestUtils.advanceTicks(20, false)
        Assertions.assertEquals(Location.create(2722, 3475, 0), p.location)
    }
}