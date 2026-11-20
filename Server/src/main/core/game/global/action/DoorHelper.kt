package core.game.global.action

import content.region.asgarnia.falador.diary.FaladorAchievementDiary
import content.region.misthalin.lumbridge.diary.LumbridgeAchivementDiary
import core.api.*
import core.api.utils.Vector
import core.game.diary.DiaryLevel
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.scenery.Layer
import core.game.node.scenery.Layer.Companion.getLayer
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.node.scenery.SceneryRestoreTimer
import core.game.system.config.DoorConfigLoader
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.RegionChunk
import core.game.world.map.path.Pathfinder
import core.tools.Log
import org.rs09.consts.Sounds
import kotlin.math.abs

/**
 * Opens/closes doors, fences and gates.
 *
 * - Door: 1 object, swings by itself.
 * - Gate: 2 objects split, swings apart.
 * - Fence: 2 objects joint, swings as one.
 * - Hinge/Pivot: where door will swing.
 * - Autowalk: door immediately closes behind.
 *
 * How doors work:
 *
 *  Let's say we have 4 tiles. The W (walls) are on the left of the door. RS does it by moving the scenery one square
 *  out. IT WILL ALWAYS MOVE 1 SQUARE.
 *  ```
 *  Here I push the door south.
 *  | === | === |    | === | === |
 *  |  W  |  _  |    |  W  |     |
 *  | === | === | => | === | === |
 *  |  W  |     |    |  W  | |   |
 *  | === | === |    | === | === |
 *  ```
 * RS also replaces the sceneryId to show different options on them. RS would basically use 25418 as the closed door
 * state, and change it to 25417 (and rotate) for the opened door state. Notice that there are mostly 2 similar door
 * sceneries in cache.
 * - [org.rs09.consts.Scenery.DOOR_25417] with "Close Door" option.
 * - [org.rs09.consts.Scenery.DOOR_25418] with "Open Door" option.
 *
 * All these gets more confusing when you have to consider the following:
 * - Doors rotate clockwise if hinges/pivots are on the right. (default)
 * - Doors rotate counter-clockwise if hinges/pivots are on the left.
 * - Autowalked doors only have one scenery with "Open Door" option.
 * - Fences have a second scenery two squares away from the hinge.
 *
 * This is where [DoorConfigLoader] comes in and loads door_configs.json.
 * - id is the original Scenery ID for the door.
 * - replaceId is the scenery to replace the door when it opens.
 * - fence is to specify if the door functions as a fence.
 * - metal is just for the sound, nothing more.
 *
 * How are double doors determined then?
 * - A function getPairedDoor() automagically finds the second door.
 * - It looks at sceneries in all 4 directions around the first door.
 * - If the name matches, it is the second door.
 *
 * This code heavily relies on the incorrect/legacy RegionChunk rotations. Here they are, for reference:
 *      public final static int NORTH_ROTATION = 0;
 *      public final static int EAST_ROTATION = 1;
 *      public final static int SOUTH_ROTATION = 2;
 *      public final static int WEST_ROTATION = 3;
 * The running thread: NORTH is default, and increment by one per quarter clockwise.
 *
 * A brief note about wrappers, which have cost me approx 12 hours of my life chasing ghosts. Players may be shown
 * entirely different scenery IDs based on varbits. Those different sceneries are _children_ of the parent, or
 * _wrapper_, scenery. The wrapper is what exists on the server-side chunk. The child is what the player actually
 * interacts with, and is what the door handlers receive from the interaction handlers. This means that scenery
 * replacement operations should apply to the wrapper, not the door that we received from higher up.
 *
 * A brief note about rotation. Edge doors are naturally on the _east_ side of the tile. This means that the door's
 * _orientation_ is given by the tile's _rotation_ incremented by 3. Don't ask why, this is just how they are stored in
 * the cache.
 */
class DoorHelper {
    companion object {
        const val INTERACT_AGAIN_DELAY = 2
        const val AUTOCLOSE_DELAY = 500

        @JvmStatic
        fun handleDoor(entity: Entity, doorObject: Scenery): Boolean {
            val door = doorObject.wrapper
            val isOpening = !hasOption(door, "close")
            val config = DoorConfigLoader.forId(door.id)
            if (config == null) {
                log(this::class.java, Log.WARN, "Missing door config for $door at ${doorObject.location}")
                if (entity is Player) {
                    entity.debug("Missing door config for $door")
                }
            }
            if (isOpening && config?.isAutowalk != false) {
                return handleAutowalkDoor(entity, door)
            }
            val pairedDoor = getPairedDoor(entity, config, door)?.wrapper
            dispatchToDoorHelper(entity, config, door, pairedDoor, false, isOpening)
            return true
        }

        @JvmStatic
        fun handleAutowalkDoor(entity: Entity, doorObject: Scenery): Boolean {
            val door = doorObject.wrapper
            if (isDoorInUse(door)) {
                return false
            }
            setDoorInUse(door)
            val config = DoorConfigLoader.forId(door.id)
            val pairedDoor = getPairedDoor(entity, config, door)?.wrapper
            val dest = autowalkDestination(entity, config, door)
            val delta = Vector.betweenLocs(entity.location, dest)
            dispatchToDoorHelper(entity, config, door, pairedDoor, true, true)
            stopWalk(entity)
            queueWalk(entity, dest)
            queueScript(entity, 0, QueueStrength.SOFT) { stage ->
                if (stage == 0) {
                    setDoorNotInUse(door)
                    if (pairedDoor != null) {
                        setDoorNotInUse(pairedDoor)
                    }
                    if (entity is Player) {
                        // TODO: port these to actual event hooks
                        if (door.id == org.rs09.consts.Scenery.DOOR_2112 && entity.location.withinDistance(Location(3046, 9756, 0), 10)) {
                            finishTask(entity, DiaryType.FALADOR, DiaryLevel.HARD, FaladorAchievementDiary.Companion.HardTasks.ENTER_MINING_GUILD)
                        }
                        if (door.id in setOf(org.rs09.consts.Scenery.GATE_35549, org.rs09.consts.Scenery.GATE_35551) && entity.location.regionId == 13106) {
                            finishTask(entity, DiaryType.LUMBRIDGE, DiaryLevel.BEGINNER, LumbridgeAchivementDiary.Companion.BeginnerTasks.AL_KHARID_PASS_THROUGH_GATE)
                        }
                        if (door.id == org.rs09.consts.Scenery.DOOR_2406 && entity.location.withinDistance(Location(3202, 3169, 0), 10)) {
                            finishTask(entity, DiaryType.LUMBRIDGE, DiaryLevel.EASY, LumbridgeAchivementDiary.Companion.EasyTasks.SWAMP_SEARCH_SHED)
                        }
                    }
                    face(entity, dest.transform(delta)) // kinda a hack, makes you face the direction you were walking rather than turning back to face the door after the walk
                    if (entity is Player && config?.sound != false) {
                        return@queueScript delayScript(entity, INTERACT_AGAIN_DELAY)
                    }

                    return@queueScript stopExecuting(entity)
                }
                playCloseDoorSound(entity.asPlayer(), convertToSoundType(config))
                return@queueScript stopExecuting(entity)
            }
            return true
        }

        /** This actually has the door logic; most of it is determining which opening helper to dispatch to. */
        private fun dispatchToDoorHelper(entity: Entity, config: DoorConfigLoader.Door?, door: Scenery, pairedDoor: Scenery?, isAutowalkDoor: Boolean, isOpening: Boolean) {
            var closed = false
            if (pairedDoor == null) {
                if (isOpening) {
                    if (config?.turns == false) {
                        /**
                         * Only open without a swing if the door is explicitly defined not to. This prevents undefined
                         * doors from getting stuck in place.
                         */
                        openFixedDoor(config, door, isAutowalkDoor)
                    } else {
                        openSwingDoor(config, door, isAutowalkDoor)
                    }
                } else {
                    closed = closeDoor(entity, door)
                }
            } else {
                val pairedConfig = DoorConfigLoader.forId(pairedDoor.id)
                closed = handlePairedDoors(entity, config, door, pairedConfig, pairedDoor, isAutowalkDoor, isOpening)
            }
            if (entity is Player && config?.sound != false) {
                if (closed) playCloseDoorSound(entity, convertToSoundType(config))
                if (isOpening) playOpenDoorSound(entity, convertToSoundType(config))
            }
        }

        /**
         * Closes the door by manually performing the timed scenery replacement.
         */
        private fun closeDoor(entity: Entity?, door: Scenery): Boolean {
            val oldDoor = SceneryRestoreTimer.replacementMap[door]?.first
            if (oldDoor == null) {
                if (entity is Player) {
                    sendMessage(entity, "The door seems to be stuck.") // https://www.reddit.com/r/2007scape/comments/4mweht/suggestion_remove_the_the_door_seems_to_be_stuck/
                }
                return false
            }
            SceneryBuilder.replace(door, oldDoor)
            SceneryRestoreTimer.replacementMap.remove(door)
            return true
        }

        /** Handles paired doors. */
        private fun handlePairedDoors(entity: Entity, config: DoorConfigLoader.Door?, door: Scenery, pairedConfig: DoorConfigLoader.Door?, pairedDoor: Scenery, isAutowalkDoor: Boolean, opening: Boolean): Boolean {
            /**
             * Designate the door closest to the south-west corner as the 'primary' door and the other door as the
             * 'secondary' door. This ensures that fences rotate around the correct pivot, and that gates rotate
             * outward rather than inward.
             */
            var primary = door
            var secondary = pairedDoor
            var primaryConfig = config
            var secondaryConfig = pairedConfig
            val dx = secondary.location.x - primary.location.x
            val dy = secondary.location.y - primary.location.y
            if (dx + dy < 0) {
                primary = pairedDoor
                primaryConfig = pairedConfig
                secondary = door
                secondaryConfig = config
            }

            if (isAutowalkDoor) {
                setDoorInUse(pairedDoor)
            }
            if (opening) {
                if (config?.isFence == true) {
                    openFence(primaryConfig, primary, secondaryConfig, secondary, isAutowalkDoor)
                } else {
                    openGates(primaryConfig, primary, secondaryConfig, secondary, isAutowalkDoor)
                }
            } else if (!isAutowalkDoor) {
                val closed = closeDoor(entity, primary)
                closeDoor(null, secondary)
                return closed
            }
            return false
        }

        /** Swings open a fixed door. */
        private fun openFixedDoor(config: DoorConfigLoader.Door?, door: Scenery, isAutowalkDoor: Boolean) {
            val replaceId = config?.replaceId ?: door.id
            val restoreTicks = if (isAutowalkDoor) INTERACT_AGAIN_DELAY else AUTOCLOSE_DELAY
            replaceScenery(door, replaceId, restoreTicks, door.location, !isAutowalkDoor)
        }

        /** Determines where a swing door will open to. */
        private fun getSwingTarget(config: DoorConfigLoader.Door?, door: Scenery): Location {
            val swing = addRotation(door.rotation, config?.extraRotation, if (door.type == 9) 1 else 0)
            val pivot = getRotationPoint(swing)
            return door.location.transform(pivot)
        }

        /** Swings open a swing door. */
        private fun openSwingDoor(config: DoorConfigLoader.Door?, door: Scenery, isAutowalkDoor: Boolean) {
            val replaceId = config?.replaceId ?: door.id
            val restoreTicks = if (isAutowalkDoor) INTERACT_AGAIN_DELAY else AUTOCLOSE_DELAY
            val rotation = addRotation(door.rotation, 1)
            val target = getSwingTarget(config, door)
            replaceScenery(door, replaceId, restoreTicks, toBadlyMappedDirection(rotation), target, !isAutowalkDoor)
        }

        /** Swings open a fence (one door, two leaves, both facing the same way). */
        private fun openFence(primaryConfig: DoorConfigLoader.Door?, primary: Scenery, secondaryConfig: DoorConfigLoader.Door?, secondary: Scenery, isAutowalkDoor: Boolean) {
            val swing = addRotation(primary.rotation, primaryConfig?.extraRotation)
            val pivot = getRotationPoint(swing)
            var swingDirection = addRotation(primary.rotation, 3)

            if (primary.rotation == RegionChunk.SOUTH_ROTATION || primary.rotation == RegionChunk.WEST_ROTATION) {
                swingDirection = flipRotation(swingDirection)
            }
            var primaryTarget = primary.location
            var secondaryTarget = secondary.location
            if (primaryConfig?.turns != false) {
                primaryTarget = primary.location.transform(pivot)
                secondaryTarget = primaryTarget.transform(pivot)
            }

            val primaryReplaceId = primaryConfig?.replaceId ?: primary.id
            val secondaryReplaceId = secondaryConfig?.replaceId ?: secondary.id
            val restoreTicks = if (isAutowalkDoor) INTERACT_AGAIN_DELAY else AUTOCLOSE_DELAY
            replaceScenery(primary, primaryReplaceId, restoreTicks, toBadlyMappedDirection(swingDirection), primaryTarget, !isAutowalkDoor)
            replaceScenery(secondary, secondaryReplaceId, restoreTicks, toBadlyMappedDirection(swingDirection), secondaryTarget, !isAutowalkDoor)
        }

        /** Swings open gates (two paired doors, facing the opposite way). */
        private fun openGates(primaryConfig: DoorConfigLoader.Door?, primary: Scenery, secondaryConfig: DoorConfigLoader.Door?, secondary: Scenery, isAutowalkDoor: Boolean) {
            var primaryRotation = primary.rotation
            var secondaryRotation = secondary.rotation
            var primaryTarget = primary.location
            var secondaryTarget = secondary.location

            val dx = secondary.location.x - primary.location.x
            val dy = secondary.location.y - primary.location.y

            // Main turning logic
            if (primaryConfig?.turns != false) {
                if (getLayer(primary.type) == Layer.WALL) { // Normal door sitting at the edge of its tile, e.g. _
                    primaryRotation = addRotation(primary.rotation, 1)
                    secondaryRotation = addRotation(secondary.rotation, 1)
                    // Flips the primary leaf if it's touching the secondary
                    val offset = Direction.getDirection(dx, dy).toInteger()
                    if (primaryRotation == addRotation(offset, 1)) {
                        primaryRotation = flipRotation(primaryRotation)
                    }
                } else { // Fat door occupying a full tile, i.e. □
                    primaryRotation = addRotation(primary.rotation, 1)
                    secondaryRotation = addRotation(secondary.rotation, 1)
                }

                if (primaryConfig?.moves == true) {
                    val pivotAmt = addRotation(primary.rotation, primaryConfig.extraRotation)
                    val pivot = getRotationPoint(pivotAmt)
                    primaryTarget = primary.location.transform(pivot)
                    secondaryTarget = secondary.location.transform(pivot)
                }

                // Flips the secondary leaf if it's touching the primary
                if (secondaryRotation == primaryRotation) {
                    secondaryRotation = flipRotation(secondaryRotation)
                }
            }

            /**
             * Correction for big gates (Ape Atoll only, so we can get away with only implementing that case, ignoring
             * the potential reverse case where it's the primary that needs to displace).
             * For reference, the proper algorithm would be:
             * if (abs(dx) + abs(dy) > 0) {
             *     val transX = sign(dx) * (abs(dx) - 1) // this just subtracts 1 from the magnitude rather than the value
             *     val transY = sign(dy) * (abs(dy) - 1)
             *     if (transX > 0 || transY > 0) {
             *         secondaryTarget = secondary.location.transform(Location(transX, transY, 0))
             *     }
             *     if (transX < 0 || transY < 0) {
             *         primaryTarget = primary.location.transform(Location(transX, transY, 0))
             *     }
             * }
             * Or something like that. I don't actually care, because for the actual case where this is relevant, it all
             * simplifies to the below.
             */
            if (dx > 1) {
                val transX = dx - 1
                secondaryTarget = secondary.location.transform(Location(transX, 0, 0))
            }

            // Correction needed specifically for the Plague City gate
            primaryRotation = addRotation(primaryRotation, primaryConfig?.extraRotation)
            secondaryRotation = addRotation(secondaryRotation, secondaryConfig?.extraRotation)

            // Corrections for gates that need to swing outward rather than inward
            val extraMove = primaryConfig?.extraMove ?: 0
            if (extraMove != 0) {
                var extraX = 0
                var extraY = 0
                val effectiveRotation = if (getLayer(primary.type) == Layer.WALL) addRotation(primary.rotation, 3) else primary.rotation
                when (effectiveRotation) {
                    RegionChunk.NORTH_ROTATION -> extraY += extraMove
                    RegionChunk.EAST_ROTATION  -> extraX += extraMove
                    RegionChunk.SOUTH_ROTATION -> extraY -= extraMove
                    RegionChunk.WEST_ROTATION  -> extraX -= extraMove
                }
                val trans = Location(extraX, extraY, 0)
                primaryTarget = primaryTarget.transform(trans)
                secondaryTarget = secondaryTarget.transform(trans)
            }

            val primaryReplaceId = primaryConfig?.replaceId ?: primary.id
            val secondaryReplaceId = secondaryConfig?.replaceId ?: secondary.id
            val restoreTicks = if (isAutowalkDoor) INTERACT_AGAIN_DELAY else AUTOCLOSE_DELAY
            replaceScenery(primary, primaryReplaceId, restoreTicks, toBadlyMappedDirection(primaryRotation), primaryTarget, !isAutowalkDoor)
            replaceScenery(secondary, secondaryReplaceId, restoreTicks, toBadlyMappedDirection(secondaryRotation), secondaryTarget, !isAutowalkDoor)
        }

        /** Pivot point. */
        @JvmStatic
        fun getRotationPoint(tableKey: Int): Location {
            return when (tableKey) {
                RegionChunk.NORTH_ROTATION -> Location(-1,  0, 0)  // e.g. 3188,3272 -> 3187,3272
                RegionChunk.EAST_ROTATION -> Location( 0,  1, 0)  // e.g. 3253,3430 -> 3253,3431
                RegionChunk.SOUTH_ROTATION -> Location( 1,  0, 0)  // e.g. 3184,3412 -> 3185,3412
                RegionChunk.WEST_ROTATION -> Location( 0, -1, 0)  // e.g. 3195,3399 -> 3195,3398
                else -> Location(0, 0, 0) // impossible
            }
        }

        /** Sound type. */
        enum class SoundType {
            DEFAULT,
            IS_FENCE,
            IS_METAL
        }

        /** Converts Door to SoundType. */
        private fun convertToSoundType(doorConfig: DoorConfigLoader.Door?): SoundType {
            return when {
                doorConfig?.isFence == true -> SoundType.IS_FENCE
                doorConfig?.isMetal == true -> SoundType.IS_METAL
                else -> SoundType.DEFAULT
            }
        }

        /** Plays open-door sounds. */
        private fun playOpenDoorSound(player: Player, soundType: SoundType) {
            when (soundType) {
                SoundType.IS_METAL -> playAudio(player, Sounds.IRON_DOOR_OPEN_71)
                SoundType.IS_FENCE -> playAudio(player, Sounds.GATE_OPEN_67)
                else -> playAudio(player, Sounds.DOOR_OPEN_62)
            }
        }

        /** Plays close-door sounds. */
        private fun playCloseDoorSound(player: Player, soundType: SoundType) {
            when (soundType) {
                SoundType.IS_METAL -> playAudio(player, Sounds.IRON_DOOR_CLOSE_70)
                SoundType.IS_FENCE -> playAudio(player, Sounds.GATE_CLOSE_66)
                else -> playAudio(player, Sounds.DOOR_CLOSE_60)
            }
        }

        /** Simple check to see if door is in use based off scenery charge (used for autowalk only). */
        private const val IN_USE_CHARGE = 88
        private const val NOT_IN_USE_CHARGE = 1000
        private fun isDoorInUse(scenery: Scenery): Boolean {
            return scenery.charge == IN_USE_CHARGE
        }
        fun setDoorInUse(scenery: Scenery) {
            scenery.charge = IN_USE_CHARGE
        }
        private fun setDoorNotInUse(scenery: Scenery) {
            scenery.charge = NOT_IN_USE_CHARGE
        }

        /** Gets the paired door next to the current door. Returns null if unable. */
        private fun getPairedDoor(entity: Entity, config: DoorConfigLoader.Door?, doorScenery: Scenery): Scenery? {
            if (config?.ignoreSecondDoor == true) {
                return null
            }
            val currentDoorLocation = doorScenery.location
            val sizeX = doorScenery.definition.sizeX
            val sizeY = doorScenery.definition.sizeY
            for (nextLocation in currentDoorLocation.getCardinalTiles(sizeX, sizeY)) {
                var nextScenery = getScenery(nextLocation)
                if (nextScenery != null) {
                    if (nextScenery.name == doorScenery.name) {
                        return nextScenery
                    }
                    if (entity is Player) {
                        nextScenery = nextScenery.getChild(entity.asPlayer())
                        if (nextScenery.name == doorScenery.name) {
                            return nextScenery
                        }
                    }
                }
            }
            return null
        }

        /** Direction mapping for Scenery. This is unfortunately screwed. */
        // TODO: Change back when ContentAPI doesn't screw up mapping.
        // The assumption was that Direction follows the 8 cardinal direction.
        // Scenery fucked it up by only following 4 cardinals in Int form.
        // Passing a 8-cardinal "Direction" maps it to the wrong 4 cardinal direction Scenery uses.
        private fun toBadlyMappedDirection(rotation: Int): Direction {
            return when (rotation) { // Higher the number goes counterclockwise
                0 -> Direction.NORTH_WEST // NORTH
                1 -> Direction.NORTH // WEST
                2 -> Direction.NORTH_EAST // SOUTH
                3 -> Direction.WEST // EAST
                else -> Direction.NORTH
            }
        }

        /** Determines the autowalk destination. */
        private fun autowalkDestination(entity: Entity, config: DoorConfigLoader.Door?, scenery: Scenery): Location {
            if (scenery.type == 9) { // Diagonal doors
                val approach = Vector.betweenLocs(entity.location, scenery.location).toDirection()
                return entity.location.transform(approach, 2)
            }
            /**
             * Normal doors, which either occupy a tile edge (e.g. _), in which case only one step is necessary, or a
             * full tile (□), which will require 2 steps. Note that edge-mounted doors are WALLs, while full-tile doors
             * are GROUNDs.
             */
            var stepX = 0
            var stepY = 0
            if (getLayer(scenery.type) == Layer.WALL) {
                val rotation = addRotation(scenery.rotation, 3)
                when (rotation) {
                    RegionChunk.NORTH_ROTATION /* ‾ */ -> stepY = if (entity.location.y > scenery.location.y) -1 else  1
                    RegionChunk.EAST_ROTATION  /* ⎹ */ -> stepX = if (entity.location.x > scenery.location.x) -1 else  1
                    RegionChunk.SOUTH_ROTATION /* _ */ -> stepY = if (entity.location.y < scenery.location.y)  1 else -1
                    RegionChunk.WEST_ROTATION  /* ⎸ */ -> stepX = if (entity.location.x < scenery.location.x)  1 else -1
                }
            } else {
                // Take 2 steps in the perpendicular direction
                var dx = entity.location.x - scenery.location.x
                val dy = entity.location.y - scenery.location.y
                if (scenery.definition.sizeX > 1) {
                    /**
                     * Again we only account for the Ape Atoll door, so we don't need the full solution:
                     * if (scenery.definition.sizeX > 1) dx = shrinkMagnitude(dx, scenery.definition.sizeX - 1)
                     * if (scenery.definition.sizeY > 1) dy = shrinkMagnitude(dy, scenery.definition.sizeY - 1)
                     * We can just simplify to the below.
                     */
                    dx = 0
                }
                stepX = -dx * 2 // 2 steps in the correct direction
                stepY = -dy * 2
            }
            val extraXStep = config?.extraXStep ?: 0
            if (extraXStep != 0 && abs(stepX + extraXStep) > abs(stepX)) { // i.e. if step direction is extra-step direction
                stepX += extraXStep
            }
            return entity.location.transform(stepX, stepY, 0)
        }

        private fun addRotation(doorRotation: Int, rotationToAdd: Int? = 1, optionalSecondRotationToAdd: Int? = 0): Int {
            return (doorRotation + (rotationToAdd ?: 0) + (optionalSecondRotationToAdd ?: 0)) % 4
        }
        private fun flipRotation(doorRotation: Int): Int {
            return addRotation(doorRotation, 2)
        }

        /** Determines from where the player initiates interaction with the door. */
        @JvmStatic
        fun getDestination(entity: Entity, door: Scenery): Location {
            var loc = door.location
            val config = DoorConfigLoader.forId(door.id)
            if (getLayer(door.type) == Layer.WALL) {
                /**
                 * Wall doors. THREE valid interaction locations:
                 * - The front side. This is the door-adjacent tile. This is normal for this game.
                 * - The back side. This is the tile the door is on.
                 * - "Closing reach": if the door is currently open, you can swing it shut by extending your arm by one
                 *   tile. This prevents you from being forced to always lock yourself into buildings.
                 * We let the pathfinder figure out which option is optimal.
                 *
                 * We have to make a copy of the location due to stupidity in MovementPulse: it rejects our destination
                 * override if the location we return is reference-equal to the node's location.
                 */
                val cands = mutableSetOf(door.location.transform(0, 0, 0))
                if (door.type == 9) {
                    // Diagonal doors. The goal here is to move one tile away from their center, but not to the spot that the door will open into.
                    val exclude = getSwingTarget(config, door)
                    cands.addAll(door.location.cardinalTiles.filter { it != exclude })
                } else {
                    // Straight doors
                    val rotation = addRotation(door.rotation, 3)
                    val trans = when (rotation) {
                        RegionChunk.NORTH_ROTATION -> Location( 0,  1, 0)
                        RegionChunk.EAST_ROTATION  -> Location( 1,  0, 0)
                        RegionChunk.SOUTH_ROTATION -> Location( 0, -1, 0)
                        RegionChunk.WEST_ROTATION  -> Location(-1,  0, 0)
                        else -> Location(0, 0, 0)
                    }
                    cands += door.location.transform(trans)
                }
                /**
                 * For the "closing reach" option, we must grab the original door location from SceneryRestoreTimer.
                 * Note that it might not be there, for example for "stuck open" doors. From this, it follows that those
                 * cannot be reach-closed.
                 */
                if (hasOption(door, "close")) {
                    val orig = SceneryRestoreTimer.replacementMap[door]?.first
                    if (orig != null) {
                        if (door.type == 9) { // diagonal doors
                            cands.addAll(orig.location.cardinalTiles)
                            cands.remove(orig.location) // don't want to stand _inside_ them when they close (but this will have been added by the cardinalTiles all the way at the top)
                        } else {
                            cands.add(orig.location)
                        }
                    }
                }
                cands.map { it to Pathfinder.find(entity, it) }
                    .filter { (_, path) -> path.isSuccessful && !path.isMoveNear }
                    .minByOrNull { (_, path) -> path.points.size }
                    ?.first
                    ?.let { loc = it }
            }
            val extraXStep = config?.extraXStep ?: return loc
            if (extraXStep != 0) {
                val xDiff = entity.location.x - loc.x
                if (xDiff == extraXStep) { // i.e. player is exactly on the extra-step location
                    loc = loc.transform(extraXStep, 0, 0)
                }
            }
            return loc
        }
    }
}
