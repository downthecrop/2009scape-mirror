package content.minigame.greatorbproject

import core.api.getWorldTicks
import core.game.node.entity.Entity
import core.game.system.timer.PersistTimer
import org.json.simple.JSONObject
import kotlin.collections.set

/**
 * The penalty timer for abandoning a Great Orb Project session.
 */

// This timer is a "soft" timer, meaning it will tick down even during player delays or when an interface is open.
class OrbProjAbandonTimer(penalty: Int = 100) : PersistTimer(penalty, GOP_ABANDON, isSoft = true) {

    companion object {
        const val GOP_ABANDON = "gop_abandon"
    }

    // this save is to beat a race condition when the timer gets applied on logout
    override fun save (root: JSONObject, entity: Entity) {
        val remaining = nextExecution - getWorldTicks()

        if (remaining < 0) {
            root["ticksLeft"] = runInterval.toString()
        } else {
            root["ticksLeft"] = remaining.toString()
        }
    }

    override fun run(entity: Entity): Boolean {
       // returning false stops the timer
        return false
    }
}