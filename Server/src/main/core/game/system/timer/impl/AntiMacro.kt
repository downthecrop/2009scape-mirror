package core.game.system.timer.impl

import content.global.ame.RandomEventNPC
import content.global.ame.RandomEvents
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.game.node.item.Item
import core.game.system.command.Privilege
import core.game.system.timer.PersistTimer
import core.game.world.map.zone.ZoneRestriction
import core.game.world.repository.Repository
import core.tools.RandomFunction
import core.tools.colorize
import org.json.simple.JSONObject

class AntiMacro : PersistTimer(0, "antimacro", isAuto = true), Commands {
    var paused = false
    var nextRandom: RandomEvents? = null

    override fun run(entity: Entity): Boolean {
        if (entity !is Player) return false

        setNextExecution()

        if (!canSpawn(entity)) return true
        val eventSelection = rollEventPool(entity)
        val eventNpc = eventSelection.npc.create(entity, eventSelection.loot, eventSelection.type)
        if (eventNpc.spawnLocation == null) {
            entity.debug("[AntiMacro] Attempted to spawn random, but spawnLoc was null.")
            return true
        }

        eventNpc.init()
        setAttribute(entity, EVENT_NPC, eventNpc)
        entity.debug("[AntiMacro] Fired ${eventSelection.name}.")
        return true
    }

    override fun onRegister(entity: Entity) {
        if (entity !is Player || entity.isArtificial)
            entity.timers.removeTimer(this)
        if (entity is Player && entity.rights == Rights.ADMINISTRATOR)
            paused = true

        if (runInterval == 0)
            setNextExecution()
    }

    override fun save(root: JSONObject, entity: Entity) {
        root["ticksRemaining"] = (nextExecution - getWorldTicks()).toString()
    }

    override fun parse(root: JSONObject, entity: Entity) {
        runInterval = (root["ticksRemaining"]?.toString()?.toIntOrNull() ?: 0)
        nextExecution = getWorldTicks() + runInterval
    }

    private fun canSpawn(entity: Entity) : Boolean {
        if (entity.zoneMonitor.isRestricted(ZoneRestriction.RANDOM_EVENTS))
            return false

        val current = getAttribute<RandomEventNPC?>(entity, EVENT_NPC, null)
        if (current != null)
            return false

        if (paused)
            return false

        return true
    }

    private fun setNextExecution() {
        runInterval = RandomFunction.random(MIN_DELAY_TICKS, MAX_DELAY_TICKS + 1)
        nextExecution = getWorldTicks() + runInterval
    }

    private fun rollEventPool(entity: Entity) : RandomEvents {
        if (nextRandom != null) {
            val result = nextRandom!!
            nextRandom = null
            return result
        }

        val skillBasedRandom = RandomEvents.getSkillBasedRandomEvent(entity.skills.lastTrainedSkill)
        val normalRandom = RandomEvents.getNonSkillRandom()
        val roll = RandomFunction.random(100)

        if (roll >= 65 && skillBasedRandom != null && getWorldTicks() - entity.skills.lastXpGain < 250)
            return skillBasedRandom
        return normalRandom
    }

    override fun defineCommands() {
        define("revent", Privilege.ADMIN, "::revent [-p] <lt>player name<gt> [-e <lt>event name<gt>]", "Spawns a random event for the target player.<br>Optional -e parameter to pass a specific event.") {player, args ->
            if (args.size == 1) {
                val possible = RandomEvents.values()
                for (event in possible) {
                    notify(player, event.name.lowercase())
                }
                return@define
            }

            val arg = parseCommandArgs(args.joinToString(" "))
            val target = Repository.getPlayerByName(arg.targetPlayer)
            if (target == null)
                reject(player, "Unable to find user ${arg.targetPlayer}.")
            if (target!!.rights == Rights.ADMINISTRATOR) {
                unpause(target)
                sendMessage(target, colorize("%RAntiMacro timer unpaused until next login."))
            }

            forceEvent(target!!, arg.targetEvent)
        }
    }

    data class CommandArgs (val targetPlayer: String, val targetEvent: RandomEvents?)

    companion object {
        const val EVENT_NPC = "re-npc"
        const val MIN_DELAY_TICKS = 3000
        const val MAX_DELAY_TICKS = 9000

        fun terminateEventNpc (player: Player) {
            getEventNpc(player)?.terminate()
        }

        fun rollEventLoot (player: Player) : ArrayList<Item> {
            return getEventNpc(player)?.loot?.roll(player) ?: ArrayList()
        }

        fun getEventNpc (player: Player) : RandomEventNPC? {
            return getAttribute<RandomEventNPC?>(player, EVENT_NPC, null)
        }

        fun pause (player: Player) {
            val timer = getTimer<AntiMacro>(player) ?: return
            timer.paused = true
        }

        fun unpause (player: Player) {
            val timer = getTimer<AntiMacro>(player) ?: return
            timer.paused = false
        }

        fun forceEvent (player: Player, event: RandomEvents? = null) {
            val timer = getTimer<AntiMacro>(player) ?: return
            timer.nextExecution = getWorldTicks()
            timer.nextRandom = event
        }

        fun parseCommandArgs (args: String, commandName: String = "revent") : CommandArgs {
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

            var event: RandomEvents? = null

            try { event = RandomEvents.valueOf(eventName) } catch (_: Exception) {}

            return CommandArgs(username, event)
        }
    }
}