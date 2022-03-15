package rs09.game.node.entity.skill.slayer

import core.game.node.entity.player.Player
import core.game.node.entity.skill.slayer.Master
import core.game.node.entity.skill.slayer.Tasks

/**
 * Bitflag-based system for storing slayer-related data for a player and various helper functions.
 * @author Ceikry
 */
class SlayerFlags(val player: Player) {
    var taskFlags = 0
    var rewardFlags = 0
    var equipmentFlags = 0
    var completedTasks = 0
    var taskStreak = 0

    /**
     * The removed tasks.
     */
    val removed: ArrayList<Tasks> = ArrayList(4)

    /**
     * Get/set master flags
     */
    fun getMaster(): Master {
        val ordinal = taskFlags and 0xF
        return Master.values()[ordinal]
    }

    fun setMaster(master: Master){
        taskFlags = (taskFlags - (taskFlags and 0xF)) or master.ordinal
    }

    /**
     *==============================================
     */


    /**
     * Get/set task flags
     */
    fun getTask(): Tasks {
        val ordinal = (taskFlags shr 4) and 0x7F
        return Tasks.values()[ordinal]
    }

    fun setTask(tasks: Tasks){
        taskFlags = (taskFlags - (getTask().ordinal shl 4)) or (tasks.ordinal shl 4)
    }

    /**
     *==============================================
     */

    /**
     * Get/set/decrement task amount flag
     */
    fun getTaskAmount(): Int {
        return (taskFlags shr 11) and 0xFF
    }

    fun setTaskAmount(amount: Int) {
        taskFlags = (taskFlags - (getTaskAmount() shl 11)) or (amount shl 11)
    }

    fun decrementTaskAmount(amount: Int){
        setTaskAmount(getTaskAmount() - amount)
    }
    /**
     *==============================================
     */

    /**
     * Get/Set canEarnPoints flag
     */
    fun canEarnPoints(): Boolean {
        return (taskFlags shr 16) and 1 == 1
    }

    fun flagCanEarnPoints() {
        taskFlags = taskFlags or (1 shl 16)
    }
    /**
     *==============================================
     */

    /**
     * Get/set reward unlock flags
     */
    fun isBroadsUnlocked(): Boolean{
        return rewardFlags and 1 == 1
    }

    fun unlockBroads() {
        rewardFlags = rewardFlags or 1
    }

    fun isRingUnlocked(): Boolean {
        return (rewardFlags shr 1) and 1 == 1
    }

    fun unlockRing() {
        rewardFlags = rewardFlags or (1 shl 1)
    }

    fun isHelmUnlocked(): Boolean {
        return (rewardFlags shr 2) and 1 ==1
    }

    fun unlockHelm() {
        rewardFlags = rewardFlags or (1 shl 2)
    }

    /**
     *==============================================
     */

    /**
     * Get/set/increment points flag
     */
    fun setPoints(amount: Int) {
        rewardFlags = (rewardFlags - (getPoints() shl 15)) or (amount shl 15)
    }

    fun getPoints(): Int {
        return (rewardFlags shr 15) and 0xFFFF
    }

    fun incrementPoints(amount: Int){
        setPoints(getPoints() + amount)
    }
    /**
     *==============================================
     */

    /**
     * Reset task and task amount to 0
     */
    fun clearTask() {
        setTask(Tasks.values()[0])
        setTaskAmount(0)
    }

    /**
     * Checks if we have a task
     */
    fun hasTask(): Boolean{
        return getTaskAmount() != 0
    }

}