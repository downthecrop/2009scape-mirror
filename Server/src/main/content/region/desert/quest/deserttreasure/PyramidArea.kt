package content.region.desert.quest.deserttreasure

import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.tools.RandomFunction
import org.rs09.consts.Components
import org.rs09.consts.NPCs

class PyramidArea {
    companion object {
        /** All The Sarcophagus Locations */
        val sarcophagusList = arrayOf(
                // Level 1
                Location(2901, 4946, 3),
                Location(2902, 4969, 3),
                Location(2903, 4961, 3),
                Location(2909, 4954, 3),
                Location(2913, 4950, 3),
                Location(2916, 4954, 3),
                Location(2917, 4951, 3),
                Location(2917, 4959, 3),
                Location(2918, 4963, 3),
                // Level 2
                Location(2831, 4962, 2),
                Location(2837, 4970, 2),
                Location(2843, 4964, 2),
                Location(2847, 4947, 2),
                Location(2858, 4956, 2),
                Location(2858, 4973, 2),
                Location(2864, 4942, 2),
                Location(2868, 4948, 2),
                Location(2869, 4968, 2),
                // Level 3
                Location(2759, 4962, 1),
                Location(2760, 4956, 1),
                Location(2763, 4966, 1),
                Location(2764, 4941, 1),
                Location(2765, 4936, 1),
                Location(2765, 4940, 1),
                Location(2767, 4945, 1),
                Location(2768, 4947, 1),
                Location(2771, 4944, 1),
                Location(2774, 4947, 1),
                Location(2776, 4941, 1),
                Location(2786, 4974, 1),
                Location(2787, 4964, 1),
                Location(2790, 4968, 1),
                Location(2791, 4977, 1),
                Location(2798, 4947, 1),
                Location(2798, 4952, 1),
                Location(2800, 4960, 1),
                Location(2802, 4940, 1),
                Location(2806, 4936, 1),
                Location(2806, 4942, 1),
                Location(2810, 4968, 1),
                Location(2810, 4975, 1),
                // Level 4
                Location(3208, 9315, 0),
                Location(3211, 9330, 0),
                Location(3217, 9295, 0),
                Location(3218, 9281, 0),
                Location(3221, 9313, 0),
                Location(3221, 9320, 0),
                Location(3221, 9324, 0),
                Location(3222, 9281, 0),
                Location(3225, 9308, 0),
                Location(3225, 9310, 0),
                Location(3226, 9312, 0),
                Location(3226, 9318, 0),
                Location(3227, 9288, 0),
                Location(3229, 9309, 0),
                Location(3231, 9297, 0),
                Location(3233, 9309, 0),
                Location(3234, 9297, 0),
                Location(3234, 9330, 0),
                Location(3236, 9302, 0),
                Location(3237, 9309, 0),
                Location(3240, 9312, 0),
                Location(3240, 9318, 0),
                Location(3241, 9282, 0),
                Location(3242, 9302, 0),
                Location(3246, 9282, 0),
                Location(3246, 9308, 0),
                Location(3246, 9324, 0),
                Location(3247, 9323, 0),
                Location(3249, 9293, 0),
                Location(3250, 9324, 0),
                Location(3251, 9323, 0),
                Location(3251, 9330, 0),
                Location(3251, 9337, 0),
                Location(3252, 9330, 0),
                Location(3252, 9337, 0),
                Location(3253, 9301, 0),
                Location(3254, 9324, 0),
                Location(3255, 9323, 0),
                Location(3255, 9330, 0),
                Location(3255, 9337, 0),
                Location(3256, 9330, 0),
                Location(3256, 9337, 0),
                Location(3257, 9289, 0),
                Location(3259, 9310, 0),
                Location(3259, 9313, 0),
        )

        val safeZone = ZoneBorders(3227, 9310, 3239, 9320)

        // Direction.NORTH - LEFT
        // rot 0 - LEFT
        // rot 1 - NORTH
        // rot 2 - RIGHT
        // rot 3 - SOUTH
        /** Sarcophagus Opening Location (Note, they are all wrongly mapped due to Scenery) */
        fun getNewLocation(direction: Direction): Location {
            return when (direction) {
                Direction.NORTH -> Location(-1, 0)
                Direction.WEST -> Location(0, -1)
                Direction.EAST -> Location(0, 1)
                Direction.SOUTH -> Location(1, 0)
                else -> Location(0, 0)
            }
        }

        /** Sarcophagus Opening Facing (Note, they are all wrongly mapped due to Scenery) */
        fun getNewFacing(direction: Direction): Direction {
            return when (direction) {
                Direction.NORTH -> Direction.NORTH
                Direction.WEST -> Direction.NORTH_WEST
                Direction.SOUTH -> Direction.WEST
                Direction.EAST -> Direction.NORTH_EAST
                else -> direction
            }
        }

        fun nearSarcophagus(loc: Location): Location? {
            for (sarcoph in sarcophagusList) {
                if(loc.withinDistance(sarcoph, 3)) {
                    return sarcoph
                }
            }
            return null
        }

        /** Trapdoor randomly throws you out of the Pyramid. */
        fun trapdoorTrap(player: Player) {
            stopWalk(player)
            player.walkingQueue.reset()
            forceWalk(player, Location(3233, 2887, 0), "")
            lock(player, 8)
            sendMessage(player, "You accidentally trigger a trap...")
            // addScenery(6521, location) -> animateScenery(scenery, 1939), but 6522 does it for you.
            val pitfallScenery = addScenery(6522, player.location) // Scenery - Trapdoor Scenery
            animate(player, 1950) // Anim - Player Falling Animation
            queueScript(player, 4, QueueStrength.SOFT) { stage ->
                player.walkingQueue.reset()
                when (stage) {
                    0 -> {
                        sendGraphics(354, player.location) // Gfx - Puff of Smoke
                        closeOverlay(player)
                        openOverlay(player, Components.FADE_TO_BLACK_120)
                        return@queueScript delayScript(player, 2)
                    }
                    1 -> {
                        removeScenery(pitfallScenery)
                        teleport(player, Location(3233, 2887, 0))
                        sendMessage(player, "...and tumble unharmed outside the pyramid.")
                        closeOverlay(player)
                        openOverlay(player, Components.FADE_FROM_BLACK_170)
                        stopWalk(player)
                        player.walkingQueue.reset()
                        // animate(player, ??) // Anim - Player Getting Up https://www.youtube.com/watch?v=95OvIPFYCwg
                        return@queueScript delayScript(player, 3)
                    }
                    2 -> {
                        player.walkingQueue.reset()
                        forceWalk(player, Location(3233, 2887, 0), "")
                        return@queueScript stopExecuting(player)
                    }
                    else -> return@queueScript stopExecuting(player)
                }
            }
        }

        /** Mummies randomly spawns out of a sarcophagus. */
        fun spawnMummy(player: Player, sarcophagusLocation: Location) {
            val sarcophagusScenery = getScenery(sarcophagusLocation) ?: return
            val locationInFront = sarcophagusScenery.location.transform(getNewLocation(sarcophagusScenery.direction))
            // There are 6 sarcophagus, 6512 - 6517 with different door designs.
            // They map nicely to 6506 - 6511  and act like a door.
            // 6505 is the underlying hole scenery after the door opens, but it is done for you.
            replaceScenery(
                    sarcophagusScenery,
                    sarcophagusScenery.id - 6,
                    5,
                    getNewFacing(sarcophagusScenery.direction),
                    locationInFront
            )
            val mummyNpc = NPC(NPCs.MUMMY_1958)
            mummyNpc.isRespawn = false
            mummyNpc.isWalks = false
            mummyNpc.isAggressive = true
            mummyNpc.location = sarcophagusScenery.location
            mummyNpc.init()
            mummyNpc.walkingQueue.addPath(locationInFront.x, locationInFront.y)
            sendChat(mummyNpc, "Rawr!")
            lock(player, 1)
            stopWalk(player)
            queueScript(player, 2, QueueStrength.SOFT) { stage ->
                stopWalk(player)
                mummyNpc.walkingQueue.addPath(locationInFront.x, locationInFront.y)
                mummyNpc.isWalks = true
                mummyNpc.isAggressive = true
                mummyNpc.attack(player)
                return@queueScript stopExecuting(player)
            }

        }

        /** Scarabs randomly spawns somewhere near you. */
        fun spawnScarabs(player: Player) {
            stopWalk(player)
            lock(player, 3)
            val scarabsLocation = Location.getRandomLocation(player.location, 3, true)
            sendGraphics(356, scarabsLocation) // Gfx - Cracks before scarab appears.
            val scarabNpc = NPC(NPCs.SCARABS_1969)
            stopWalk(player)
            queueScript(player, 2, QueueStrength.STRONG) { stage ->
                stopWalk(player)
                when (stage) {
                    0 -> {
                        scarabNpc.isRespawn = false
                        scarabNpc.isWalks = false
                        scarabNpc.location = scarabsLocation
                        scarabNpc.init()
                        animate(scarabNpc, 1949) // Anim - Scarabs appearing from ground.
                        return@queueScript delayScript(player, 3)
                    }
                    1 -> {
                        scarabNpc.isWalks = true
                        scarabNpc.isAggressive = true
                        scarabNpc.attack(player)
                        return@queueScript stopExecuting(player)
                    }
                }
                return@queueScript stopExecuting(player)
            }
        }
    }
}

class PyramidAreaFirstThree: MapArea {

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(
                getRegionBorders(11597),
                getRegionBorders(11341),
                getRegionBorders(11085),
        )
    }

    override fun entityStep(entity: Entity, location: Location, lastLocation: Location) {
        if (entity is Player) {
            val averageLevel = (
                    getDynLevel(entity, Skills.AGILITY) +
                            getDynLevel(entity, Skills.THIEVING)
                    ) / 2.0
            val randomValue = RandomFunction.randomDouble(99.5)

            if ((1..20).random() == 1) {
                // A mummy would jump out if you walk near a sarcophagus of 2 radius.
                val sarcoph = PyramidArea.nearSarcophagus(entity.location)
                if (sarcoph != null) {
                    PyramidArea.spawnMummy(entity, sarcoph)
                }
            }

            if ((1..80).random() == 2) {
                PyramidArea.spawnScarabs(entity)
            }
            if (randomValue > averageLevel && (1..128).random() == 1) {
                PyramidArea.trapdoorTrap(entity)
            }
        }
    }
}

class PyramidAreaFinal: MapArea {

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(
                getRegionBorders(12945),
        )
    }

    override fun entityStep(entity: Entity, location: Location, lastLocation: Location) {
        if (entity is Player) {
            if (!PyramidArea.safeZone.insideBorder(entity.location)) { // Safezone is talking to Azzanadra.
                if ((1..20).random() == 1) {
                    // A mummy would jump out if you walk near a sarcophagus of 2 radius.
                    val sarcoph = PyramidArea.nearSarcophagus(entity.location)
                    if (sarcoph != null) {
                        PyramidArea.spawnMummy(entity, sarcoph)
                    }
                }
                if ((1..80).random() == 2) {
                    PyramidArea.spawnScarabs(entity)
                }
            }

            // No Trapdoor For The Final Level
        }
    }
}
