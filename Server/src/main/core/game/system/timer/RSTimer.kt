package core.game.system.timer

import core.game.node.entity.Entity
import kotlin.reflect.full.createInstance

/**
 * Class for the timer feature of the engine. If you have some task which should repeat periodically, such as applying poison damage, etc, use a timer.
 * If the `isAuto` value of a timer is set to `true`, then the timer is automatically added to an entity on creation and started. This is separate from the 
 * default PersistTimer behavior, which automatically starts the timer only if there's saved data for that timer present. In truth, there's very few
 * timers that should have isAuto true. 
**/
abstract class RSTimer (var runInterval: Int, val identifier: String = "generictimer", val isSoft: Boolean = false, val isAuto: Boolean = false, val flags: Array<TimerFlag> = arrayOf()) {

    /**
     * Executed every time the run interval of the timer elapses.
     * Execution will be delayed if this timer has `isSoft` set to false (which 99% of timers should) if the entity has a modal open or is otherwise stalled.
     * @return whether the timer should execute again. If false, timer will be unregistered from the entity and stop executing. If true, timer will be scheduled to repeat once the runInterval elapses again.
    **/
    abstract fun run (entity: Entity) : Boolean

    /**
     * Called by core code to determine the amount of time between timer scheduling and the initial (first) run.
     * Returns the runInterval by default, but cases such as Farming require an override to sync with realtime clocks.
    **/
    open fun getInitialRunDelay() : Int { return runInterval }

    /**
     * Called by core code when the timer is first registered. Called after parse on PersistTimers.
    **/
    open fun onRegister (entity: Entity) {}

    /**
     * Called by core code when the timer is being removed.
     */
    open fun onRemoval (entity: Entity) {}

    var lastExecution: Int = 0
    var nextExecution: Int = 0

    open fun retrieveInstance() : RSTimer {
        return this::class.createInstance()
    }

    /**
     * This is called only when getTimer is called by further up code with arguments, otherwise retrieveInstance() is called if no arguments are passed.
    **/
    open fun getTimer (vararg args: Any) : RSTimer {
        return retrieveInstance()
    }
}
