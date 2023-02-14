package content.global.activity.jobs

import content.global.activity.jobs.impl.Employers
import core.tools.RandomFunction

/**
 * The interface for a job that a player can be assigned.
 *
 * TODO: Once player data is stored in a database with support for migrations, implement a per-player field to store
 * the player's current employer, so that multiple employers can assign the same job, but the player will only be
 * able to turn the job into the employer that assigned it to them.
 */
interface Job {
    val type: JobType
    val lower: Int
    val upper: Int

    val employer: Employers

    fun getAmount(): Int {
        return RandomFunction.random(lower, upper + 1)
    }
}