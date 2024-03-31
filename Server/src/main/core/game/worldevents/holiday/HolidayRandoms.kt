package core.game.worldevents.holiday

import content.global.ame.RandomEventNPC
import core.ServerConstants
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.command.Privilege
import core.game.system.timer.PersistTimer
import core.game.world.map.zone.ZoneRestriction
import core.game.world.repository.Repository
import core.tools.RandomFunction
import core.tools.colorize
import core.tools.minutesToTicks
import org.json.simple.JSONObject
import java.time.LocalDate
import java.time.Month

class HolidayRandoms() : PersistTimer(0, "holiday", isAuto = true), Commands {
    var paused = false
    var nextRandom: HolidayRandomEvents? = null
    var currentHoliday: String? = null
    val halloweenStartDate = LocalDate.of(LocalDate.now().year, Month.OCTOBER, 17)
    val halloweenEndDate = LocalDate.of(LocalDate.now().year, Month.NOVEMBER, 7)
    val christmasStartDate = LocalDate.of(LocalDate.now().year, Month.DECEMBER, 1)
    val christmasEndDate = LocalDate.of(LocalDate.now().year, Month.DECEMBER, 31)

    override fun run(entity: Entity): Boolean {
        if (entity !is Player) return false

        setNextExecution()

        if (!canSpawn(entity)) {
            delayNextExecution()
            return true
        }

        val eventSelection = rollEventPool()
        val eventNpc = eventSelection.npc.create(entity)
        if (eventNpc.spawnLocation == null) {
            entity.debug("[HolidayRandom] Attempted to spawn random, but spawnLoc was null.")
            delayNextExecution()
            return true
        }

        eventNpc.init()
        setAttribute(entity, EVENT_NPC, eventNpc)
        setAttribute(eventNpc, EVENT_NPC, eventNpc)
        entity.debug("[HolidayRandom] Fired ${eventSelection.name}.")
        return true
    }

    override fun onRegister(entity: Entity) {
        if (entity !is Player || entity.isArtificial || !ServerConstants.HOLIDAY_EVENT_RANDOMS) {
            entity.timers.removeTimer(this)
            return
        }

        val player = entity.asPlayer()
        when (checkIfHoliday()) {
            "halloween" -> {
                sendMessage(player, colorize("%OA chill goes down your spine..."))
                currentHoliday = "halloween"
            }
            "christmas" -> {
                sendMessage(player, colorize("%GHappy Holidays."))
                currentHoliday = "christmas"
            }
            "none" -> player.timers.removeTimer(this)
        }

        if (runInterval == 0)
            setNextExecution()
    }

    fun checkIfHoliday(): String {
        val currentDate = LocalDate.now()
        if ((!currentDate.isBefore(halloweenStartDate) && !currentDate.isAfter(halloweenEndDate)) || ServerConstants.FORCE_HALLOWEEN_EVENTS)
            return "halloween"

        if ((!currentDate.isBefore(christmasStartDate) && !currentDate.isAfter(christmasEndDate)) || ServerConstants.FORCE_CHRISTMAS_EVENTS)
            return "christmas"

        return "none"
    }

    override fun save(root: JSONObject, entity: Entity) {
        root["ticksRemaining"] = (nextExecution - getWorldTicks()).toString()
    }

    override fun parse(root: JSONObject, entity: Entity) {
        runInterval = (root["ticksRemaining"]?.toString()?.toIntOrNull() ?: 0)
        nextExecution = getWorldTicks() + runInterval
    }

    private fun canSpawn(entity: Entity) : Boolean {
        if (entity.zoneMonitor.isRestricted(ZoneRestriction.RANDOM_EVENTS) || getAttribute<RandomEventNPC?>(entity, "re-npc", null) != null)
            return false

        val current = getAttribute<HolidayRandomEventNPC?>(entity, EVENT_NPC, null)
        if (current != null)
            return false

        if (paused || entity.inCombat())
            return false

        return true
    }

    private fun delayNextExecution() {
        runInterval = 50
        nextExecution = getWorldTicks() + runInterval

    }

    private fun setNextExecution() {
        runInterval = RandomFunction.random(MIN_DELAY_TICKS, MAX_DELAY_TICKS + 1)
        nextExecution = getWorldTicks() + runInterval
    }

    private fun rollEventPool() : HolidayRandomEvents {
        if (nextRandom != null) {
            val result = nextRandom!!
            nextRandom = null
            return result
        }
        return when (currentHoliday) {
            "halloween" -> HolidayRandomEvents.getHolidayRandom("halloween")
            "christmas" -> HolidayRandomEvents.getHolidayRandom("christmas")
            else -> throw Exception("Invalid event type!")
        }
    }

    override fun defineCommands() {
        define("hrevent", Privilege.ADMIN, "::hrevent [-p] <lt>player name<gt> [-e <lt>event name<gt>]", "Spawns a holiday random event for the target player.<br>Optional -e parameter to pass a specific event.") { player, args ->
            if (args.size == 1) {
                val possible = HolidayRandomEvents.values()
                for (event in possible) {
                    notify(player, event.name.lowercase())
                }
                return@define
            }

            val arg = parseCommandArgs(args.joinToString(" "))
            val target = Repository.getPlayerByName(arg.targetPlayer)

            if (getTimer<HolidayRandoms>(player) == null)
                reject(player, "No holiday random events are active. To force a holiday's random events use ::forcehrevents")

            if (target == null)
                reject(player, "Unable to find user ${arg.targetPlayer}.")

            forceEvent(target!!, arg.targetEvent)
        }

        define("forcehrevents", Privilege.ADMIN, "::forcehrevents [eventname]", "Force enable holiday random events.") { player, args ->
            if (args.size == 1) {
                notify(player, "Holidays: halloween, christmas")
                return@define
            }
            val event = args[1]
            if (checkIfHoliday() != "none")
                reject(player, "Holiday randoms are already enabled: ${checkIfHoliday()}. Use ::stophrevents first.")
            ServerConstants.HOLIDAY_EVENT_RANDOMS = true
            when (event) {
                "halloween" -> {
                    ServerConstants.FORCE_HALLOWEEN_EVENTS = true
                    for (p in Repository.players) {
                        if (getTimer<HolidayRandoms>(p) != null || p.isArtificial) {
                            continue
                        }
                        notify(p, colorize("%RHalloween Randoms are now enabled!"))
                        registerTimer(p, HolidayRandoms())
                    }
                }
                "christmas" -> {
                    ServerConstants.FORCE_CHRISTMAS_EVENTS = true
                    for (p in Repository.players) {
                        if (getTimer<HolidayRandoms>(p) != null || p.isArtificial) {
                            continue
                        }
                        notify(p, colorize("%GChristmas Randoms are now enabled!"))
                        registerTimer(p, HolidayRandoms())
                    }
                }
                else -> reject(player, "Invalid event!")
            }
        }

        define("stophrevents", Privilege.ADMIN, "::stophrevents", "Stops all holiday random events.") { player, _ ->
            if (checkIfHoliday() == "none" || !ServerConstants.HOLIDAY_EVENT_RANDOMS)
                reject(player, "No holiday random events are currently active.")
            ServerConstants.HOLIDAY_EVENT_RANDOMS = false
            ServerConstants.FORCE_HALLOWEEN_EVENTS = false
            ServerConstants.FORCE_CHRISTMAS_EVENTS = false
            for (p in Repository.players) {
                if (getTimer<HolidayRandoms>(p) == null) {
                    continue
                }
                removeHolidayTimer(p)
                notify(p, "Holiday random events are now disabled!")
            }
        }
    }

    data class CommandArgs (val targetPlayer: String, val targetEvent: HolidayRandomEvents?)

    companion object {
        const val EVENT_NPC = "holiday-npc"
        val MIN_DELAY_TICKS = minutesToTicks(30)
        val MAX_DELAY_TICKS = minutesToTicks(90)

        fun terminateEventNpc (player: Player) {
            getEventNpc(player)?.terminate()
        }

        fun getEventNpc (player: Player) : HolidayRandomEventNPC? {
            return getAttribute<HolidayRandomEventNPC?>(player, EVENT_NPC, null)
        }

        fun pause (player: Player) {
            val timer = getTimer<HolidayRandoms>(player) ?: return
            timer.paused = true
        }

        fun unpause (player: Player) {
            val timer = getTimer<HolidayRandoms>(player) ?: return
            timer.paused = false
        }

        fun removeHolidayTimer (player: Player) {
            val timer = getTimer<HolidayRandoms>(player) ?: return
            removeTimer(player, timer)
        }

        fun forceEvent (player: Player, event: HolidayRandomEvents? = null) {
            val timer = getTimer<HolidayRandoms>(player) ?: return
            timer.nextExecution = getWorldTicks()
            timer.nextRandom = event
        }

        fun parseCommandArgs (args: String, commandName: String = "hrevent") : CommandArgs {
            val tokens = args.split(" ")
            val modeTokens = arrayOf("-p", "-e")

            var userString = ""
            var eventString = ""
            var lastMode = "-p"

            for (token in tokens) {
                when (token) {
                    commandName -> continue
                    in modeTokens -> lastMode = token
                    else -> when (lastMode) {
                        "-p" -> userString += "$token "
                        "-e" -> eventString += "$token "
                    }
                }
            }

            val username = userString.trim().lowercase().replace(" ", "_")
            val eventName = eventString.trim().uppercase().replace(" ", "_")

            var event: HolidayRandomEvents? = null

            try { event = HolidayRandomEvents.valueOf(eventName) } catch (_: Exception) {}

            return CommandArgs(username, event)
        }
    }
}