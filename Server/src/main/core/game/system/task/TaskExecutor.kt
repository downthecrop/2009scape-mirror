package core.game.system.task

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * A class holding methods to execute tasks.
 * @author Emperor
 */
object TaskExecutor {

    /**
     * Executes an SQL handling task.
     * @param task The task.
     */
    @JvmStatic
    fun executeSQL(task: () -> Unit) {
        GlobalScope.launch {
            task.invoke()
        }
    }

    /**
     * Executes the task.
     * @param task The task to execute.
     */
    @JvmStatic
    fun execute(task: () -> Unit) {
        GlobalScope.launch {
            task.invoke()
        }
    }
}