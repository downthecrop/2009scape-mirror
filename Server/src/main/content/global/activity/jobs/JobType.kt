package content.global.activity.jobs

/**
 * An enum of the different types of jobs that a player can be assigned.
 *
 * Note: Due to how player save files keep track of the player's current job, it is essential that
 * new entries are only appended to the end of this enum, and the ordering of existing entries is not changed.
 */
enum class JobType {
    PRODUCTION,
    COMBAT,
    BONE_BURYING
}