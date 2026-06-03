package content.region.wilderness.handlers.revenants

import core.game.interaction.MovementPulse
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.TeleportManager
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics
import core.tools.RandomFunction
import core.ServerConstants
import core.api.*
import core.game.node.entity.combat.InteractionType
import core.game.system.command.Privilege
import core.game.world.GameWorld
import core.game.world.map.path.Pathfinder
import core.game.world.repository.Repository
import core.tools.Log
import java.lang.Integer.min
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class RevenantController : TickListener, Commands {

    companion object {
        private val trackedRevenants = ArrayList<RevenantNPC>()
        private val taskTimeRemaining = HashMap<RevenantNPC, Int>()
        private val currentTask = HashMap<RevenantNPC, RevenantTask>()
        private var expectedRevAmount: Int = ServerConstants.REVENANT_POPULATION
        private var groupPatrolQueue = ArrayList<RevenantNPC>()

        // Movement tracking for stuck detection
        private val movementStartTick = HashMap<RevenantNPC, Int>()
        private val lastKnownLocation = HashMap<RevenantNPC, Location>()
        private val failedAttempts = HashMap<RevenantNPC, Int>()
        // Stuck detector config
        private const val STUCK_CHECK_INTERVAL = 10 // ticks
        private const val MAX_FAILED_ATTEMPTS = 5

        @JvmStatic fun registerRevenant(revenantNPC: RevenantNPC) {
            trackedRevenants.add(revenantNPC)
            taskTimeRemaining[revenantNPC] = 0
            currentTask[revenantNPC] = RevenantTask.NONE
            movementStartTick[revenantNPC] = GameWorld.ticks
            lastKnownLocation[revenantNPC] = revenantNPC.location
            failedAttempts[revenantNPC] = 0
            Repository.RENDERABLE_NPCS.add(revenantNPC)
        }

        @JvmStatic fun unregisterRevenant(revenantNPC: RevenantNPC, removeRender: Boolean = true) {
            trackedRevenants.remove(revenantNPC)
            taskTimeRemaining.remove(revenantNPC)
            currentTask.remove(revenantNPC)
            movementStartTick.remove(revenantNPC)
            lastKnownLocation.remove(revenantNPC)
            failedAttempts.remove(revenantNPC)
            groupPatrolQueue.remove(revenantNPC)
            if (removeRender)
                Repository.RENDERABLE_NPCS.remove(revenantNPC)
        }

        private fun route(vararg coords: Pair<Int, Int>): Array<Location> =
            Array(coords.size) { Location.create(coords[it].first, coords[it].second) }

        // All routes that revs attempt to path through
        val routes = listOf(
            route(
                3070 to 3651, 3083 to 3640, 3106 to 3645, 3133 to 3647, 3149 to 3642,
                3160 to 3654, 3171 to 3665, 3189 to 3663, 3202 to 3675, 3217 to 3660,
                3235 to 3661, 3235 to 3661, 3279 to 3650, 3269 to 3636, 3253 to 3632,
                3236 to 3638, 3220 to 3637, 3203 to 3634, 3187 to 3631, 3166 to 3633,
                3160 to 3616, 3148 to 3604, 3134 to 3596, 3118 to 3590, 3104 to 3597,
            ),
            route(
                3077 to 3565, 3093 to 3559, 3110 to 3566, 3127 to 3574, 3146 to 3571,
                3164 to 3575, 3183 to 3573, 3197 to 3587, 3215 to 3584, 3233 to 3576,
                3251 to 3573, 3269 to 3577, 3287 to 3569, 3305 to 3568, 3321 to 3576,
                3338 to 3584, 3352 to 3573, 3354 to 3554, 3342 to 3541, 3324 to 3536,
                3306 to 3543, 3290 to 3544, 3272 to 3545, 3255 to 3546, 3239 to 3539,
                3222 to 3543, 3206 to 3548, 3189 to 3549, 3173 to 3552, 3157 to 3549,
                3140 to 3548, 3122 to 3548, 3110 to 3555,
            ),
            route(
                3318 to 3691, 3307 to 3700, 3290 to 3696, 3277 to 3706, 3260 to 3706,
                3250 to 3707, 3245 to 3723, 3254 to 3735, 3251 to 3754, 3243 to 3768,
                3253 to 3780, 3238 to 3783, 3224 to 3793, 3206 to 3786, 3192 to 3780,
                3170 to 3787, 3156 to 3800, 3148 to 3814, 3148 to 3814, 3127 to 3840,
                3124 to 3856, 3124 to 3872, 3116 to 3892,
            ),
            route(
                2949 to 3890, 2965 to 3899, 2984 to 3900, 2998 to 3895, 3016 to 3898,
                3032 to 3893, 3048 to 3897, 3068 to 3894, 3084 to 3898, 3101 to 3895,
                3118 to 3897, 3136 to 3893, 3154 to 3900, 3172 to 3895, 3189 to 3892,
                3206 to 3897, 3222 to 3890, 3240 to 3897, 3259 to 3892, 3278 to 3895,
                3296 to 3892, 3313 to 3899, 3331 to 3888, 3345 to 3880,
            ),
            route(
                3308 to 3941, 3301 to 3925, 3287 to 3915, 3276 to 3922, 3266 to 3938,
                3267 to 3952, 3250 to 3949, 3235 to 3944, 3219 to 3944, 3206 to 3938,
                3194 to 3929, 3182 to 3921, 3174 to 3936, 3180 to 3952, 3167 to 3960,
                3155 to 3959, 3141 to 3953, 3126 to 3954, 3110 to 3961, 3093 to 3962,
                3078 to 3953, 3066 to 3942, 3059 to 3929, 3049 to 3916, 3033 to 3924,
                3020 to 3921, 3010 to 3913, 2993 to 3906, 2977 to 3911, 2970 to 3928,
            ),
            route(
                3308 to 3941, 3301 to 3925, 3297 to 3914, 3286 to 3910, 3259 to 3912,
                3267 to 3952, 3251 to 3921, 3236 to 3918, 3222 to 3922, 3209 to 3920,
                3190 to 3933, 3177 to 3939, 3175 to 3951, 3167 to 3960, 3155 to 3959,
                3135 to 3951, 3134 to 3921, 3126 to 3914, 3108 to 3907, 3087 to 3910,
                3077 to 3925, 3076 to 3937, 3066 to 3942, 3059 to 3929, 3049 to 3916,
                3033 to 3924, 3020 to 3921, 3010 to 3913, 2993 to 3906, 2977 to 3911,
                2970 to 3928,
            ),
        )

        // Routes that Dark Beasts are too fat for
        private val largeNpcIncompatibleRoutes = setOf(4)

        val spawnLocations = listOf(
            3075 to 3553, 3077 to 3563, 3077 to 3578, 3093 to 3581, 3103 to 3570,
            3101 to 3564, 3030 to 3596, 3015 to 3598, 3000 to 3593, 2986 to 3588,
            2969 to 3701, 2982 to 3689, 2967 to 3689, 2953 to 3713, 2966 to 3759,
            2989 to 3759, 2986 to 3741, 2961 to 3763, 2969 to 3808, 3004 to 3816,
        ).map { (x, y) -> Location.create(x, y) }

        /**
         * Revenant stuck detector
         * Makes sure Revenants are either moving, intentionally waiting to act, or are in legitimate combat
         * If they aren't, they're passed to the stuck handler
         */
        private fun checkIfStuck(revenantNPC: RevenantNPC): Boolean {
            val lastTick = movementStartTick[revenantNPC] ?: 0
            val lastLoc = lastKnownLocation[revenantNPC] ?: revenantNPC.location

            // Period between checks - configure above
            if (lastTick == 0 || (GameWorld.ticks - lastTick) < STUCK_CHECK_INTERVAL) {
                return false
            }

            // Check if positioned exactly at a patrol route start location (likely intentionally inactive)
            val routeStartLocations = routes.map { it[0] }
            val isAtRouteStart = routeStartLocations.any { it.equals(revenantNPC.location) }

            // Check if in legitimate combat
            val combatPulse = revenantNPC.properties.combatPulse
            val victim = combatPulse.getVictim()
            val isInLegitimateCombat = if (victim != null) {
                val interactionType = combatPulse.canInteract()
                interactionType == InteractionType.STILL_INTERACT // The merits of MOVE_INTERACT regarding establishing a legitimate combat state are covered by the distanceMoved check a few lines from now
            } else {
                false
            }

            if (isAtRouteStart || isInLegitimateCombat) { //If the Revenant has a good reason to not be moving, skip the movement check
                movementStartTick[revenantNPC] = GameWorld.ticks
                lastKnownLocation[revenantNPC] = revenantNPC.location
                failedAttempts[revenantNPC] = 0
                return false
            }

            // Check if the revenant has moved
            val distanceMoved = revenantNPC.location.getDistance(lastLoc)
            val isStuck = distanceMoved <= 2

            // Reset the timer to check if they are stuck, since we are checking now
            movementStartTick[revenantNPC] = GameWorld.ticks

            // Clear the Revenant's demerits towards being timed out if it is not stuck
            if (!isStuck) {
                lastKnownLocation[revenantNPC] = revenantNPC.location
                failedAttempts[revenantNPC] = 0
            }

            return isStuck
        }

        /**
         * Revenant stuck handler
         * Tries to help the Revenant escape after giving it a demerit
         * If the Revenant gets too many demerits, it will be timed out instead
         * NOTE: times out Revenants in unloaded regions as they have no land to path to and thus cannot move
         */
        private fun handleStuckRevenant(revenantNPC: RevenantNPC) {
            val attempts = failedAttempts[revenantNPC] ?: 0
            failedAttempts[revenantNPC] = attempts + 1

            if (attempts + 1 > MAX_FAILED_ATTEMPTS) {
                poofClear(revenantNPC)
                revenantNPC.setAttribute("done", true)
                return
            }

            // The stuck Revenant will attempt to unstick itself
            val escapeLocation = getNextLocation(revenantNPC)
            revenantNPC.pulseManager.run(object : MovementPulse(revenantNPC, escapeLocation, Pathfinder.SMART) {
                override fun pulse(): Boolean {
                    return true
                }
            })
        }

        /**
         * Finds a random location to try to move to that is within the Wilderness
         */
        fun getNextLocation(revenantNPC: RevenantNPC): Location {
            val rawNextX = RandomFunction.random(-revenantNPC.walkRadius, revenantNPC.walkRadius)
            val nextX = maxOf(rawNextX, 2951)
            val rawNextY = RandomFunction.random(-revenantNPC.walkRadius, revenantNPC.walkRadius)
            val nextY = maxOf(rawNextY, 3523)
            return revenantNPC.location.transform(nextX, nextY, 0)
        }

        /**
         * Determines how far Revenants can deviate from their path
         */
        private fun getPathVariance(revenantNPC: RevenantNPC, isGroup: Boolean): Int {
            return when {
                isGroup -> 4
                revenantNPC.size() >= 3 -> 2  // Dark Beasts are fat, so keep them close to their safe routes
                else -> 10
            }
        }

        /**
         * Gets a random route for patrols
         * Will not assign Dark Beasts to routes they are too fat for
         */
        private fun getSuitableRoute(revenantNPC: RevenantNPC): Array<Location> {
            val availableRoutes = if (revenantNPC.size() >= 3) {
                routes.filterIndexed { index, _ -> index !in largeNpcIncompatibleRoutes }
            } else {
                routes
            }
            return availableRoutes.random()
        }
    }

    override fun tick() {
        taskTimeRemaining.replaceAll { _, t -> t - 1 }
        currentTask.entries.forEach { entry ->
            if (entry.value == RevenantTask.NONE) {
                entry.setValue(assignRandomTask(entry.key))
            } else {
                entry.value.execute(entry.key)
            }
        }
        spawnMissingRevenants()
    }

    private fun spawnMissingRevenants() {
        val amountToSpawn = min(5, expectedRevAmount - trackedRevenants.size) //only spawn 5 at a time

        if (amountToSpawn <= 0) return

        for (i in 0 until amountToSpawn) {
            val type = RevenantType.values().random()
            val npc = NPC.create(type.ids[0], getRandomSpawnLocation())
            npc.init()
        }
    }

    private fun getRandomSpawnLocation(): Location {
        return spawnLocations.random()
    }

    private fun assignRandomTask(npc: RevenantNPC): RevenantTask {
        return RevenantTask.values().random().also { it.assign(npc) }
    }

    override fun defineCommands() {
        define("setrevcap", Privilege.ADMIN, "::setrevcap <lt>amount<gt>", "Sets revenant spawn cap to <lt>amount<gt>") {player, strings ->
            val amt = strings[1].toInt()
            expectedRevAmount = amt
        }

        define("listrevs", Privilege.ADMIN, description = "Logs the IDs and locations of all tracked revenants.") {player, strings ->
            for (rev in trackedRevenants) {
                log(this::class.java, Log.FINE,  "REV ${rev.id}-${rev.name} @ ${rev.location.toString()}")
            }
            log(this::class.java, Log.FINE,  "Total of ${trackedRevenants.size} revenants spawned.")
        }

        define("clearrevs", Privilege.ADMIN, description = "Despawns all revenants.") {_, _ ->
            for (rev in trackedRevenants.toTypedArray()) rev.clear()
        }
    }

    //The current task for any given revenant - execute is called every tick.
    enum class RevenantTask {
        NONE {
            override fun execute(revenantNPC: RevenantNPC) {}
        },
        RANDOM_ROAM {
            private val MAX_ROAM_TICKS: Int = 250

            override fun execute(revenantNPC: RevenantNPC) {

                if (checkIfStuck(revenantNPC)) {
                    handleStuckRevenant(revenantNPC)
                    return
                }

                if (!canMoveBasic(revenantNPC)) return

                val nextLoc = getNextLocation(revenantNPC)
                revenantNPC.pulseManager.run(object : MovementPulse(revenantNPC, nextLoc, Pathfinder.SMART) {
                    override fun pulse(): Boolean {
                        if (taskTimeRemaining[revenantNPC]!! <= 0) currentTask[revenantNPC] = NONE
                        return true
                    }
                })
            }
            private fun canMoveBasic(revenantNPC: RevenantNPC): Boolean {
                return !revenantNPC.walkingQueue.isMoving
                        && !revenantNPC.pulseManager.hasPulseRunning()
                        && !revenantNPC.properties.combatPulse.isAttacking
                        && !revenantNPC.properties.combatPulse.isInCombat
            }

            override fun assign(revenantNPC: RevenantNPC) {
                taskTimeRemaining[revenantNPC] = RandomFunction.random(MAX_ROAM_TICKS)
            }
        },
        PATROLLING_ROUTE {
            private val MAXIMUM_GROUP_PATROL_LEVEL = 105

            override fun assign(revenantNPC: RevenantNPC) {
                if (canGroup(revenantNPC)) {
                    addToPatrolGroup(revenantNPC)
                } else {
                    revenantNPC.setAttribute("route", getSuitableRoute(revenantNPC))
                    revenantNPC.setAttribute("routeidx", -1)
                }
            }

            private fun addToPatrolGroup(revenantNPC: RevenantNPC) {
                revenantNPC.setAttribute("group", true)
                groupPatrolQueue.add(revenantNPC)

                if (groupPatrolQueue.size == 3) {
                    // Gets a route suitable for the largest NPC in the group
                    val largestNPC = groupPatrolQueue.maxByOrNull { it.size() } ?: revenantNPC
                    val groupRoute = getSuitableRoute(largestNPC)
                    for (rev in groupPatrolQueue) {
                        rev.setAttribute("route", groupRoute)
                        rev.setAttribute("routeidx", -1)
                    }
                    groupPatrolQueue.clear()
                }
            }

            private fun canGroup(revenantNPC: RevenantNPC) =
                revenantNPC.properties.currentCombatLevel <= MAXIMUM_GROUP_PATROL_LEVEL && RandomFunction.nextBool()

            override fun execute(revenantNPC: RevenantNPC) {
                val isGroup = revenantNPC.getAttribute("group", false)
                val route = revenantNPC.getAttribute<Array<Location>>("route", null)
                val routeIdx = revenantNPC.getAttribute("routeidx", -1)

                if (checkIfStuck(revenantNPC)) {
                    handleStuckRevenant(revenantNPC)
                    return
                }

                if (!canMoveAdvanced(revenantNPC)) return

                if (isGroup && route == null) { //if this is a grouped rev and we are waiting on more revs still
                    taskTimeRemaining[revenantNPC] = 50 //just to make sure it doesn't time out roaming...
                    RANDOM_ROAM.execute(revenantNPC)
                    return
                }

                if (routeIdx == -1) {
                    GameWorld.Pulser.submit(object : Pulse() {
                        override fun pulse(): Boolean {
                            Graphics.send(Graphics(86), revenantNPC.location)
                            return true
                        }
                    })
                    teleport(revenantNPC, route[0], TeleportManager.TeleportType.INSTANT)
                    revenantNPC.setAttribute("routeidx", 1)
                } else {
                    if (routeIdx == route.size) {
                        poofClear(revenantNPC)
                        revenantNPC.setAttribute("done", true)
                        return
                    }
                    val pathVariance = getPathVariance(revenantNPC, isGroup)
                    val nextLoc = route[routeIdx].transform(
                        RandomFunction.random(-pathVariance, pathVariance),
                        RandomFunction.random(-pathVariance, pathVariance),
                        0
                    )
                    revenantNPC.pulseManager.run(object : MovementPulse(revenantNPC, nextLoc, Pathfinder.SMART) {
                        override fun pulse(): Boolean {
                            return true
                        }
                    })
                    revenantNPC.setAttribute("routeidx", routeIdx + 1)
                }
            }
            private fun canMoveAdvanced(revenantNPC: RevenantNPC) : Boolean {
                return !revenantNPC.walkingQueue.isMoving
                        && !revenantNPC.pulseManager.hasPulseRunning()
                        && !revenantNPC.properties.combatPulse.isAttacking
                        && !revenantNPC.properties.combatPulse.isInCombat
                        && revenantNPC.properties.teleportLocation == null
                        && !revenantNPC.getAttribute("done", false)
            }
        }
        ;

        abstract fun execute(revenantNPC: RevenantNPC)
        open fun assign(revenantNPC: RevenantNPC) {}
    }
}
