package core.game.global.action

import TestUtils
import content.global.handlers.scenery.DoorManagingPlugin
import core.api.addScenery
import core.api.removeScenery
import core.game.global.action.DoorHelper.Companion.setDoorInUse
import core.game.node.scenery.Scenery
import core.game.system.config.DoorConfigLoader
import core.game.world.map.Location
import core.game.world.map.RegionChunk
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DoorDestinationTests {
    init {
        TestUtils.preTestSetup()
        DoorManagingPlugin().newInstance(null) // registers the generic open/close/shut/go-through handlers
    }

    @Test
    fun cannotTakeWallDoorsDiagonally() {
        val exemptFromTesting = setOf(
            2117,  // Scorpion Catcher
            4173,  // nothing
            91,    // nothing
            2341,  // Black Knights' Fortress
            26940, // Hazeel Cult
            4577,  // Lighthouse door
            2535,  // nothing
            2629,  // Says GWD listener in the code but it triggers charlie the cook lol
            31534, // nothing
            5052,  // Barrows tunnel shortcut
            5132,  // nothing
            5268,  // Something in Port Phasmatys according to the code, is actually nothing
            7435,  // as above
            1586,  // Clock Tower
            3748,  // nothing
            3944,  // Arandar gates
            3945   // as above
        )

        val doorLoc = Location(3211, 3203, 0)
        val playerLoc = doorLoc.transform(0, 1, 0)
        val failures = mutableListOf<String>()
        for (cfg in DoorConfigLoader.DOORS.values) {
            if (cfg.id in exemptFromTesting) {
                continue
            }
            TestUtils.getMockPlayer("door man", isBot = true).use { player -> // bots get no randoms etc
                player.disableDebugLogging() // too much spam of more than 1k interaction details
                player.location = playerLoc
                val door = Scenery(cfg.id, doorLoc, 0, 0)
                val firstOption = door.definition.options[0] ?: return@use
                setDoorInUse(door) // stops autowalk doors
                addScenery(door)
                TestUtils.simulateInteraction(player, door, 0)
                TestUtils.advanceTicks(2, false)
                if (player.location == playerLoc) {
                    failures += "player did not move to interact with door ${cfg.id} (option $firstOption)"
                }
                // playerLoc.chunk.objects.forEach { removeScenery(it) } // after SceneryList MR; for now, instead:
                val LAYERS = 4
                for (i in 0 until RegionChunk.SIZE) {
                    for (j in 0 until RegionChunk.SIZE) {
                        for (k in 0 until LAYERS) {
                            val o = player.location.chunk.objects[i][j][k] ?: continue
                            removeScenery(o)
                        }
                    }
                }
            }
        }
        assertTrue(failures.isEmpty(), "${failures.size} door interactions from wrong tile:\n" + failures.joinToString("\n"))
    }
}