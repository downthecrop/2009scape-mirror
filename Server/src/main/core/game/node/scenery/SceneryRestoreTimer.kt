package core.game.node.scenery

import core.api.getWorldTicks
import core.game.system.timer.WorldTimer
import core.game.world.GameWorld

class SceneryRestoreTimer: WorldTimer(1, "SceneryRestoreTimer") {
    override fun run(): Boolean {
        nextExecution = Int.MAX_VALUE
        runInterval = 1
        for ((current, todo) in replacementMap.toMap()) {
            val (replacement, time) = todo
            if (time <= GameWorld.ticks) { // can be < if we halted tick processing due to internet loss
                // A replacement that was queued for this precise tick; run it
                SceneryBuilder.replace(current, replacement, -1)
                replacementMap.remove(current)
            } else if (time < nextExecution) {
                // A replacement that was queued for a later tick; let the timer sleep until then (this will be
                // overwritten by whichever queued replacement falls soonest, or left at Int.MAX_VALUE if none)
                runInterval = time - getWorldTicks()
                nextExecution = time
            }
        }
        return replacementMap.isNotEmpty()
    }

    companion object {
        /**
         * The scenery-replacement map. Keys: sceneries to replace. Values: a map of sceneries to restore to the ticks
         * at which this needs to happen. This is efficient provided that nextExecution is manipulated correctly by the
         * consuming code.
         */
        val replacementMap = mutableMapOf<Scenery, Pair<Scenery, Int>>()
    }
}
