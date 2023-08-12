package core.game.system.timer.impl

import content.global.ame.RandomEvents
import core.api.getWorldTicks
import core.api.setAttribute
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.timer.PersistTimer
import core.tools.RandomFunction
import org.json.simple.JSONObject

class AntiMacro : PersistTimer(0, "antimacro", isAuto = true) {
    override fun run(entity: Entity): Boolean {
        if (entity !is Player) return false

        setNextExecution()
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

        if (nextExecution == getWorldTicks())
            setNextExecution()
    }

    override fun save(root: JSONObject, entity: Entity) {
        root["ticksRemaining"] = (nextExecution - getWorldTicks()).toString()
    }

    override fun parse(root: JSONObject, entity: Entity) {
        nextExecution = getWorldTicks() + (root["ticksRemaining"]?.toString()?.toIntOrNull() ?: 0)
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
    }
}