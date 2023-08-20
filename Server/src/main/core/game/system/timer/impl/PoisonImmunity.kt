package core.game.system.timer.impl

import core.game.system.timer.*
import core.api.*
import core.tools.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import org.json.simple.*
import org.rs09.consts.Sounds

/**
 * A timer that replicates the behavior of poison immunity mechanics. Runs every tick.
 * Will notify the player of various levels of remaining poison immunity, and then remove itself once it has run out.
 * This timer is a "soft" timer, meaning it will tick down even while other timers would normally stall (e.g. during entity delays or when the entity has a modal open.)
**/
class PoisonImmunity : PersistTimer (1, "poison:immunity", isSoft = true, flags = arrayOf(TimerFlag.ClearOnDeath)) {
    var ticksRemaining = 0

    override fun save (root: JSONObject, entity: Entity) {
        root["ticksRemaining"] = ticksRemaining.toString()
    }

    override fun parse (root: JSONObject, entity: Entity) {
        ticksRemaining = root["ticksRemaining"].toString().toInt()
    }

    override fun onRegister (entity: Entity) {
        removeTimer<Poison>(entity)
    }

    override fun run (entity: Entity) : Boolean {
        ticksRemaining--

        if (entity is Player && ticksRemaining == secondsToTicks(30)) {
            sendMessage(entity, colorize("%RYou have 30 seconds remaining on your poison immunity."))
            playAudio(entity, Sounds.CLOCK_TICK_1_3120, 0, 3)
        }
        else if (entity is Player && ticksRemaining == 0) {
            sendMessage(entity, colorize("%RYour poison immunity has expired."))
            playAudio(entity, Sounds.DRAGON_POTION_FINISHED_2607)
        }

        return ticksRemaining > 0
    }

    override fun getTimer (vararg args: Any) : RSTimer {
        val t =  PoisonImmunity()
        t.ticksRemaining = args.getOrNull(0) as? Int ?: 100
        return t
    }
}
