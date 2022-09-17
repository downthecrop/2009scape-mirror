package rs09.game.content.zone.wilderness

import api.Commands
import api.TickListener
import api.poofClear
import api.teleport
import core.game.interaction.MovementPulse
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.revenant.RevenantNPC
import core.game.node.entity.npc.revenant.RevenantType
import core.game.node.entity.player.link.TeleportManager
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Graphics
import core.tools.RandomFunction
import rs09.ServerConstants
import rs09.game.system.SystemLogger
import rs09.game.system.command.Privilege
import rs09.game.world.GameWorld
import rs09.game.world.repository.Repository
import java.lang.Integer.min
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.max

class RevenantController : TickListener, Commands {

    companion object {
        private val trackedRevenants = ArrayList<RevenantNPC>()
        private val taskTimeRemaining = HashMap<RevenantNPC, Int>()
        private val currentTask = HashMap<RevenantNPC, RevenantTask>()
        private var expectedRevAmount: Int = ServerConstants.REVENANT_POPULATION
        private var groupPatrolQueue = ArrayList<RevenantNPC>()

        @JvmStatic fun registerRevenant(revenantNPC: RevenantNPC) {
            trackedRevenants.add(revenantNPC)
            taskTimeRemaining[revenantNPC] = 0
            currentTask[revenantNPC] = RevenantTask.NONE
            Repository.RENDERABLE_NPCS.add(revenantNPC)
        }

        @JvmStatic fun unregisterRevenant(revenantNPC: RevenantNPC) {
            trackedRevenants.remove(revenantNPC)
            taskTimeRemaining.remove(revenantNPC)
            currentTask.remove(revenantNPC)
            Repository.RENDERABLE_NPCS.remove(revenantNPC)
        }

        val routes = listOf(
            arrayOf(Location.create(3070, 3651), Location.create(3083, 3640), Location.create(3106, 3645), Location.create(3133, 3647), Location.create(3149, 3642), Location.create(3160, 3654), Location.create(3171, 3665, 0), Location.create(3189, 3663, 0), Location.create(3202, 3675, 0), Location.create(3217, 3660, 0), Location.create(3235, 3661, 0), Location.create(3235, 3661, 0), Location.create(3279, 3650, 0), Location.create(3269, 3636, 0), Location.create(3253, 3632, 0), Location.create(3236, 3638, 0), Location.create(3220, 3637, 0), Location.create(3203, 3634, 0), Location.create(3187, 3631, 0), Location.create(3166, 3633, 0), Location.create(3160, 3616, 0), Location.create(3148, 3604, 0), Location.create(3134, 3596, 0), Location.create(3118, 3590, 0), Location.create(3104, 3597, 0)),
            arrayOf(Location.create(3077, 3565, 0), Location.create(3093, 3559, 0), Location.create(3110, 3566, 0), Location.create(3127, 3574, 0), Location.create(3146, 3571, 0), Location.create(3164, 3575, 0), Location.create(3183, 3573, 0), Location.create(3197, 3587, 0), Location.create(3215, 3584, 0), Location.create(3233, 3576, 0), Location.create(3251, 3573, 0), Location.create(3269, 3577, 0), Location.create(3287, 3569, 0), Location.create(3305, 3568, 0), Location.create(3321, 3576, 0), Location.create(3338, 3584, 0), Location.create(3352, 3573, 0), Location.create(3354, 3554, 0), Location.create(3342, 3541, 0), Location.create(3324, 3536, 0), Location.create(3306, 3543, 0), Location.create(3290, 3544, 0), Location.create(3272, 3545, 0), Location.create(3255, 3546, 0), Location.create(3239, 3539, 0), Location.create(3222, 3543, 0), Location.create(3206, 3548, 0), Location.create(3189, 3549, 0), Location.create(3173, 3552, 0), Location.create(3157, 3549, 0), Location.create(3140, 3548, 0), Location.create(3122, 3548, 0), Location.create(3110, 3555, 0)),
            arrayOf(Location.create(3318, 3691, 0), Location.create(3307, 3700, 0), Location.create(3290, 3696, 0), Location.create(3277, 3706, 0), Location.create(3260, 3706, 0), Location.create(3250, 3707, 0), Location.create(3245, 3723, 0), Location.create(3254, 3735, 0), Location.create(3251, 3754, 0), Location.create(3243, 3768, 0), Location.create(3253, 3780, 0), Location.create(3238, 3783, 0), Location.create(3224, 3793, 0), Location.create(3206, 3786, 0), Location.create(3192, 3780, 0), Location.create(3170, 3787, 0), Location.create(3156, 3800, 0), Location.create(3148, 3814, 0), Location.create(3148, 3814, 0), Location.create(3127, 3840, 0), Location.create(3124, 3856, 0), Location.create(3124, 3872, 0), Location.create(3116, 3892, 0)),
            arrayOf(Location.create(2949, 3890, 0), Location.create(2965, 3899, 0), Location.create(2984, 3900, 0), Location.create(2998, 3895, 0), Location.create(3016, 3898, 0), Location.create(3032, 3893, 0), Location.create(3048, 3897, 0), Location.create(3068, 3894, 0), Location.create(3084, 3898, 0), Location.create(3101, 3895, 0), Location.create(3118, 3897, 0), Location.create(3136, 3893, 0), Location.create(3154, 3900, 0), Location.create(3172, 3895, 0), Location.create(3189, 3892, 0), Location.create(3206, 3897, 0), Location.create(3222, 3890, 0), Location.create(3240, 3897, 0), Location.create(3259, 3892, 0), Location.create(3278, 3895, 0), Location.create(3296, 3892, 0), Location.create(3313, 3899, 0), Location.create(3331, 3888, 0), Location.create(3345, 3880, 0)),
            arrayOf(Location.create(3308, 3941, 0), Location.create(3301, 3925, 0), Location.create(3287, 3915, 0), Location.create(3276, 3922, 0), Location.create(3266, 3938, 0), Location.create(3267, 3952, 0), Location.create(3250, 3949, 0), Location.create(3235, 3944, 0), Location.create(3219, 3944, 0), Location.create(3206, 3938, 0), Location.create(3194, 3929, 0), Location.create(3182, 3921, 0), Location.create(3174, 3936, 0), Location.create(3180, 3952, 0), Location.create(3167, 3960, 0), Location.create(3155, 3959, 0), Location.create(3141, 3953, 0), Location.create(3126, 3954, 0), Location.create(3110, 3961, 0), Location.create(3093, 3962, 0), Location.create(3078, 3953, 0), Location.create(3066, 3942, 0), Location.create(3059, 3929, 0), Location.create(3049, 3916, 0), Location.create(3033, 3924, 0), Location.create(3020, 3921, 0), Location.create(3010, 3913, 0), Location.create(2993, 3906, 0), Location.create(2977, 3911, 0), Location.create(2970, 3928, 0))
        )

        val spawnLocations = listOf(Location.create(3075, 3553, 0), Location.create(3077, 3563, 0), Location.create(3077, 3578, 0), Location.create(3093, 3581, 0), Location.create(3103, 3570, 0), Location.create(3101, 3564, 0), Location.create(3030, 3596, 0), Location.create(3015, 3598, 0), Location.create(3000, 3593, 0), Location.create(2986, 3588, 0), Location.create(2969, 3701, 0), Location.create(2982, 3689, 0), Location.create(2967, 3689, 0), Location.create(2953, 3711, 0), Location.create(2966, 3759, 0), Location.create(2989, 3759, 0), Location.create(2986, 3741, 0), Location.create(2961, 3763, 0), Location.create(2969, 3808, 0), Location.create(3004, 3816, 0))
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
        define("setrevcap", Privilege.ADMIN) {player, strings ->
            val amt = strings[1].toInt()
            expectedRevAmount = amt
        }

        define("listrevs", Privilege.ADMIN) {player, strings ->
            for (rev in trackedRevenants) {
                SystemLogger.logInfo(this::class.java, "REV ${rev.id}-${rev.name} @ ${rev.location.toString()}")
            }

            SystemLogger.logInfo(this::class.java, "Total of ${trackedRevenants.size} revenants spawned.")
        }

        define("clearrevs", Privilege.ADMIN) {_, _ ->
            for (rev in trackedRevenants.toTypedArray()) rev.clear()
        }
    }

    //The current task for any given revenant - execute is called every tick.
    enum class RevenantTask {
        NONE {
            override fun execute(revenantNPC: RevenantNPC) {}
        },
        INTENTIONAL_IDLE {
            private val MAX_IDLE_TIME: Int = 50

            override fun execute(revenantNPC: RevenantNPC) {
                if (taskTimeRemaining[revenantNPC] == 0) currentTask[revenantNPC] = NONE
            }

            override fun assign(revenantNPC: RevenantNPC) {
                taskTimeRemaining[revenantNPC] = RandomFunction.random(MAX_IDLE_TIME)
            }
        },
        RANDOM_ROAM {
            private val MAX_ROAM_TICKS: Int = 250

            override fun execute(revenantNPC: RevenantNPC) {
                if (!canMove(revenantNPC)) return

                revenantNPC.pulseManager.run(object : MovementPulse(revenantNPC, getNextLocation(revenantNPC)) {
                    override fun pulse(): Boolean {
                        if (taskTimeRemaining[revenantNPC]!! <= 0) currentTask[revenantNPC] = NONE
                        return true
                    }
                })
            }

            override fun assign(revenantNPC: RevenantNPC) {
                taskTimeRemaining[revenantNPC] = RandomFunction.random(MAX_ROAM_TICKS)
            }

            fun canMove(revenantNPC: RevenantNPC) : Boolean {
                return  !revenantNPC.walkingQueue.isMoving
                        && !revenantNPC.properties.combatPulse.isAttacking
                        && !revenantNPC.properties.combatPulse.isInCombat
            }

            fun getNextLocation(revenantNPC: RevenantNPC) : Location {
                val nextX = RandomFunction.random(-revenantNPC.walkRadius, revenantNPC.walkRadius)
                val nextY = RandomFunction.random(-revenantNPC.walkRadius, revenantNPC.walkRadius)
                return revenantNPC.location.transform(nextX, nextY, 0)
            }
        },
        PATROLLING_ROUTE {
            private val MAXIMUM_GROUP_PATROL_LEVEL = 105

            override fun assign(revenantNPC: RevenantNPC) {
                if (canGroup(revenantNPC)) {
                    addToPatrolGroup(revenantNPC)
                } else {
                    revenantNPC.setAttribute("route", routes.random())
                    revenantNPC.setAttribute("routeidx", -1)
                }
            }

            private fun addToPatrolGroup(revenantNPC: RevenantNPC) {
                revenantNPC.setAttribute("group", true)
                groupPatrolQueue.add(revenantNPC)

                if (groupPatrolQueue.size == 3) {
                    val groupRoute = routes.random()
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

                if (!canMove(revenantNPC)) return

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
                    val pathVariance = if (isGroup) 4 else 10
                    val nextLoc = route[routeIdx].transform(
                        RandomFunction.random(-pathVariance, pathVariance),
                        RandomFunction.random(-pathVariance, pathVariance),
                        0
                    )
                    revenantNPC.pulseManager.run(object : MovementPulse(revenantNPC, nextLoc) {
                        override fun pulse(): Boolean {
                            return true
                        }
                    })
                    revenantNPC.setAttribute("routeidx", routeIdx + 1)
                }
            }

            fun canMove(revenantNPC: RevenantNPC) : Boolean {
                return  !revenantNPC.walkingQueue.isMoving
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