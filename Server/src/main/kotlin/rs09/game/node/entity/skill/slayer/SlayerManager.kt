package rs09.game.node.entity.skill.slayer

import core.cache.def.impl.NPCDefinition
import core.game.node.entity.player.Player
import core.game.node.entity.skill.slayer.Master
import core.game.node.entity.skill.slayer.Tasks
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import rs09.game.Event
import java.util.*

/**
 * Manages the players slayer task.
 * @author Ceikry
 */
class SlayerManager(val player: Player) {

    init {
        player.hook(Event.NPCKilled, SlayerKillHook)
    }

    /**
     * The player's slayer flags
     */
	@JvmField
	val flags: SlayerFlags = SlayerFlags(player)

    fun parse(slayerData: JSONObject) {
        val m = slayerData["master"]
        if (m != null) {
            flags.setMaster(Master.forId(m.toString().toInt()))
        }
        val t = slayerData["taskId"]
        if (t != null) flags.setTask(Tasks.values()[t.toString().toInt()])
        val a = slayerData["taskAmount"]
        if (a != null) flags.setTaskAmount(a.toString().toInt())
        val points = slayerData["points"]
        if (points != null) {
            flags.setPoints(points.toString().toInt())
        }
        val taskStreak = slayerData["taskStreak"]
        if (taskStreak != null) {
            flags.taskStreak = taskStreak.toString().toInt()
        }
        val la = slayerData["learned_rewards"]
        if (la != null) {
            val learnedArray = slayerData["learned_rewards"] as JSONArray?
            for (i in learnedArray!!.indices) {
                val unlocked = learnedArray[i] as Boolean
                when (i) {
                    0 -> if (unlocked) flags.unlockBroads()
                    1 -> if (unlocked) flags.unlockRing()
                    2 -> if (unlocked) flags.unlockHelm()
                    else -> {}
                }
            }
        }
        val removedTasks = slayerData["removedTasks"] as JSONArray?
        if (removedTasks != null) {
            for (i in removedTasks.indices) {
                flags.removed.add(Tasks.values()[removedTasks[i].toString().toInt()])
            }
        }
        val completedTasks: Any = slayerData["totalTasks"].toString()
        flags.completedTasks = completedTasks.toString().toInt()
        if (flags.completedTasks >= 4) flags.flagCanEarnPoints()

        //New system parsing
        if (slayerData.containsKey("equipmentFlags")) flags.equipmentFlags = slayerData["equipmentFlags"].toString().toInt()
        if (slayerData.containsKey("taskFlags")) flags.taskFlags = slayerData["taskFlags"].toString().toInt()
        if (slayerData.containsKey("rewardFlags")) flags.rewardFlags = slayerData["rewardFlags"].toString().toInt()
    }

    /**
     * Method used to assign a new task for a player.
     * @param master the master to give the task.
     */
    fun generate(master: Master) {
        val task = SlayerUtils.generate(player, master) ?: return
        SlayerUtils.assign(player, task, master)
    }

    /**
     * Clears the task.
     */
    fun clear() {
        amount = 0
    }

    /**
     * Gets the task name.
     * @return the name.
     */
    val taskName: String
        get() {
            val task = flags.getTask()
            if (task.npcs == null) {
                return "no npcs report me"
            }
            return if (task.npcs.isEmpty()) {
                "npc length too small report me"
            } else NPCDefinition.forId(task.npcs[0]).name.toLowerCase()
        }

    var task: Tasks?
        get() = flags.getTask()
        set(task) {
            flags.setTask(task!!)
        }

    var master: Master?
        get() = flags.getMaster()
        set(master) {
            flags.setMaster(master!!)
        }

    /**
     * Checks if a **Player** contains a task.
     * @return `True` if so.
     */
    fun hasTask(): Boolean {
        return amount > 0
    }

    /**
     * Method used to check if the task is completed.
     * @return `True` if so.
     */
    val isCompleted: Boolean
        get() = flags.getTaskAmount() <= 0

    var amount: Int
        get() = flags.getTaskAmount()
        set(amount) {
            flags.setTaskAmount(amount)
        }

    fun decrementAmount(amount: Int) {
        flags.decrementTaskAmount(amount)
        player.varpManager.get(2502).setVarbit(0, flags.taskFlags shr 4).send(player)
    }

    /**
     * Method used to check if the player has started slayer.
     * @return `True` if so.
     */
    fun hasStarted(): Boolean {
        return flags.completedTasks > 0 || flags.getTaskAmount() > 0
    }
    /**
     * Gets the slayerPoints.
     * @return the slayerPoints.
     */
    /**
     * Sets the slayerPoints.
     * @param slayerPoints the slayerPoints to set
     */
    var slayerPoints: Int
        get() = flags.getPoints()
        set(slayerPoints) {
            flags.setPoints(slayerPoints)
        }

    /**
     * Gets the removed.
     * @return the removed.
     */
    val removed: List<Tasks>
        get() = flags.removed
    val isCanEarnPoints: Boolean
        get() = flags.canEarnPoints()
}