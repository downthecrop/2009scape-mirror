package core.game.system.timer.impl

import content.global.ame.RandomEventNPC
import content.global.ame.RandomEvents
import core.api.getAttribute
import core.api.getTimer
import core.api.getWorldTicks
import core.api.setAttribute
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.timer.PersistTimer
import core.game.world.map.zone.ZoneRestriction
import core.tools.RandomFunction
import org.json.simple.JSONObject

class AntiMacro : PersistTimer(0, "antimacro", isAuto = true) {
    var paused = false

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
        if (entity !is Player)
            entity.timers.removeTimer(this)

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
        val skillBasedRandom = RandomEvents.getSkillBasedRandomEvent(entity.skills.lastTrainedSkill)
        val normalRandom = RandomEvents.getNonSkillRandom()
        val roll = RandomFunction.random(100)

        if (roll >= 65 && skillBasedRandom != null && getWorldTicks() - entity.skills.lastXpGain < 250)
            return skillBasedRandom
        return normalRandom
    }

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
    }
}