package core.game.system.timer

import core.game.node.entity.Entity
import kotlin.reflect.full.createInstance

/**
 * Class for the timer feature of the engine. If you have some task which should repeat periodically, such as applying
 * poison damage, etc, use a timer. The "Timer" class here is a general type of timer used by the engine. If you're a
 * content developer, YOU SHOULD NOT USE THIS. Instead, use either an RSTimer (for entities, i.e. players or npcs) or a
 * WorldTimer (a less capable type of timer that is attached to the game world itself rather than a specific entity).
**/
open class Timer(var runInterval: Int, val identifier: String = "generictimer") {
    var nextExecution: Int = 0

    /**
      * Called by core code to determine the amount of time between timer scheduling and the initial (first) run.
      * Returns the runInterval by default, but cases such as Farming require an override to sync with realtime clocks.
      * Executed every time the run interval of the timer elapses.
      * Execution will be delayed if this timer has `isSoft` set to false (which 99% of timers should) if the entity has a modal open or is otherwise stalled.
      * @return whether the timer should execute again. If false, timer will be unregistered from the entity and stop executing. If true, timer will be scheduled to repeat once the runInterval elapses again.
     **/
    open fun getInitialRunDelay(): Int {
        return runInterval
    }

    /**
     * This is called only when getTimer is called by further up code with arguments, otherwise retrieveInstance() is called if no arguments are passed.
     **/
    open fun getTimer(vararg args: Any): Timer {
        return retrieveInstance()
    }
    open fun retrieveInstance(): Timer {
        return this::class.createInstance()
    }
}

/**
 * If the `isAuto` value of a timer is set to `true`, then the timer is automatically added to an entity on creation and started. This is separate from the
 * default PersistTimer behavior, which automatically starts the timer only if there's saved data for that timer present. In truth, there's very few
 * timers that should have isAuto true.
**/
abstract class RSTimer(runInterval: Int, identifier: String = "generictimer", val isSoft: Boolean = false, val isAuto: Boolean = false, val flags: Array<TimerFlag> = arrayOf()): Timer(runInterval, identifier) {
    /**
     * Executed every time the run interval of the timer elapses.
     * Execution will be delayed if this timer has `isSoft` set to false (which 99% of timers should) if the entity has a modal open or is otherwise stalled.
     * @return whether the timer should execute again. If false, timer will be unregistered from the entity and stop executing. If true, timer will be scheduled to repeat once the runInterval elapses again.
    **/
    abstract fun run(entity: Entity): Boolean

    /**
     * Called by core code before the timer is first registered. Called after parse on PersistTimers.
     * Called before the timer has been added to the timer list.
     **/
    open fun beforeRegister(entity: Entity) {}

    /**
     * Called by core code when the timer is first registered. Called after parse on PersistTimers.
     * Called after the timer has been added to the timer list.
    **/
    open fun onRegister(entity: Entity) {}

    /**
     * Called by core code when the timer is being removed.
     */
    open fun onRemoval(entity: Entity) {}
}

/**
 * A more stripped-down version of the above RSTimer intended for very specific world-wide use cases. Not tied to a
 * specific entity. Does not support all kinds of bells and whistles that RSTimer does.
 */
abstract class WorldTimer(runInterval: Int, identifier: String = "generictimer"): Timer(runInterval, identifier) {
    abstract fun run(): Boolean
}
