package content.skill.agility

import content.global.skill.agility.shortcuts.StileShortcut
import core.game.world.map.Direction
import core.game.world.map.Location
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ShortcutTests {
    @Test fun stileGetInteractionLocShouldReturnCorrectLoc() {
        data class StileTestData (val playerLoc: Location, val expectedLoc: Location, val stileLoc: Location, val stileOrientation: StileShortcut.Orientation)
        val testData = arrayOf(
            //Horizontal
            StileTestData(Location.create(3044, 3308, 0), Location.create(3045, 3305, 0), Location.create(3043, 3305, 0), StileShortcut.Orientation.Horizontal),
            StileTestData(Location.create(3048, 3303, 0), Location.create(3045, 3305, 0), Location.create(3043, 3305, 0), StileShortcut.Orientation.Horizontal),
            StileTestData(Location.create(3043, 3307, 0), Location.create(3042, 3305, 0), Location.create(3043, 3305, 0), StileShortcut.Orientation.Horizontal),
            StileTestData(Location.create(3039, 3301, 0), Location.create(3042, 3305, 0), Location.create(3043, 3305, 0), StileShortcut.Orientation.Horizontal),
            //Vertical
            StileTestData(Location.create(3203, 3276, 0), Location.create(3197, 3275, 0), Location.create(3197, 3276, 0), StileShortcut.Orientation.Vertical),
            StileTestData(Location.create(3195, 3274, 0), Location.create(3197, 3275, 0), Location.create(3197, 3276, 0), StileShortcut.Orientation.Vertical),
            StileTestData(Location.create(3191, 3280, 0), Location.create(3197, 3278, 0), Location.create(3197, 3276, 0), StileShortcut.Orientation.Vertical),
            StileTestData(Location.create(3206, 3281, 0), Location.create(3197, 3278, 0), Location.create(3197, 3276, 0), StileShortcut.Orientation.Vertical)
        )

        for ((pLoc, expLoc, sLoc, ori) in testData) {
            Assertions.assertEquals(expLoc, StileShortcut.getInteractLocation(pLoc, sLoc, ori))
        }
    }

    @Test fun stileGetOrientationShouldReturnCorrectOrientation() {
        val testData = arrayOf(
            Pair (Direction.NORTH, StileShortcut.Orientation.Vertical),
            Pair (Direction.SOUTH, StileShortcut.Orientation.Vertical),
            Pair (Direction.NORTH_WEST, StileShortcut.Orientation.Vertical),
            Pair (Direction.NORTH_EAST, StileShortcut.Orientation.Vertical),
            Pair (Direction.SOUTH_EAST, StileShortcut.Orientation.Vertical),
            Pair (Direction.SOUTH_WEST, StileShortcut.Orientation.Vertical),
            Pair (Direction.EAST, StileShortcut.Orientation.Horizontal),
            Pair (Direction.WEST, StileShortcut.Orientation.Horizontal)
        )

        for ((dir, expOri) in testData) {
            Assertions.assertEquals(expOri, StileShortcut.getOrientation(dir))
        }
    }
}