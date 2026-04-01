package core.game.system.timer.impl

import content.global.skill.summoning.familiar.Familiar
import core.game.system.timer.*
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.npc.NPC
import core.game.node.entity.skill.Skills
import core.game.world.repository.Repository
import org.json.simple.*

/**
 * A timer that replicates the behavior of poison mechanics. Runs every 30 ticks.
 * If you wish to construct an instance of this Timer, consider using the ContentAPI function `applyPoison` instead.
 * If that doesn't suit your needs, then use `Poison.getTimer (source, severity)` and then `registerTimer(entity, timer)`
 * Poison mechanics are driven by the `severity` value, which is not a 1:1 representation of damage. Instead the formula `floor((severity + 4) / 5)` is used.
 * Every time the damage is applied, the severity decreases by 1. Poison ends when severity reaches 0.
 * Example: 30 Severity. Deals 6 damage 5 times, then 5 damage 5 times, and so on.
**/
class Poison : PersistTimer (30, "poison", flags = arrayOf(TimerFlag.ClearOnDeath)) {
    lateinit var damageSource: Entity

    var severity = 0
    override fun save (root: JSONObject, entity: Entity) {
        root["source-uid"] = (damageSource as? Player)?.details?.uid ?: -1
        root["severity"] = severity.toString()
    }

    override fun parse (root: JSONObject, entity: Entity) {
        val uid = root["source-uid"].toString().toInt()
        damageSource = Repository.getPlayerByUid (uid) ?: entity
        severity = root["severity"].toString().toInt()
    }
	
	override fun onRegister(entity : Entity) {
		if (entity is Player) {
			sendMessage(entity, "You have been poisoned.")
			entity.debug("[Poison] -> Received for $severity severity.")
		}
		if (damageSource is Player) {
			(damageSource as? Player)?.debug("[Poison] -> Applied for $severity severity.")
			// this is ass, and preventing poisoning slayer monster player lacks lv req for should be done but prob not here & not like this. same for familiars
			if (entity is NPC && entity.task != null && entity.task.levelReq > damageSource.skills.getLevel(Skills.SLAYER)) {
				(damageSource as? Player)?.debug("[Poison] -> Removed poison from ${entity.name} ${entity.location} due to Slayer reqs!")
				removeTimer(entity, this)
			}
		}
		if (damageSource is Familiar) {
			val owner : Player? = (damageSource as Familiar).owner
			if (owner != null && entity is NPC && entity.task != null && entity.task.levelReq > owner.skills.getLevel(Skills.SLAYER)) {
				owner.debug("[Poison] -> Removed poison from ${entity.name} ${entity.location} due to Slayer reqs!")
				removeTimer(entity, this)
			}
		}
	}

    override fun run (entity: Entity) : Boolean {
        entity.impactHandler.manualHit (
            damageSource, 
            getDamageFromSeverity (severity--), 
            ImpactHandler.HitsplatType.POISON
        )

        if (severity == 0 && entity is Player)
            sendMessage(entity, "The poison has worn off.")
        return severity > 0
    }

    override fun getTimer (vararg args: Any) : RSTimer {
        val timer = Poison()
        timer.damageSource = args[0] as? Entity ?: return timer
        timer.severity = args[1] as? Int ?: return timer
        return timer
    }

    private fun getDamageFromSeverity (severity: Int) : Int {
        return (severity + 4) / 5
    }
}
