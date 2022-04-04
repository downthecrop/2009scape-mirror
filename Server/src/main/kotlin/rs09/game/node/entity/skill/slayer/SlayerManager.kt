package rs09.game.node.entity.skill.slayer

import api.LoginListener
import api.PersistPlayer
import api.events.EventHook
import api.events.NPCKillEvent
import api.getAttribute
import api.rewardXP
import core.cache.def.impl.NPCDefinition
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.slayer.Master
import core.game.node.entity.skill.slayer.Tasks
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import rs09.game.Event
import java.util.*

/**
 * Manages the players slayer data.
 * @author Ceikry
 */
class SlayerManager(val player: Player? = null) : LoginListener, PersistPlayer, EventHook<NPCKillEvent> {
    override fun login(player: Player) {
        val instance = SlayerManager(player)
        player.hook(Event.NPCKilled, instance)
        player.setAttribute("slayer-manager", instance)
    }

    /**
     * The player's slayer flags
     */
	@JvmField
	val flags: SlayerFlags = SlayerFlags()

    override fun savePlayer(player: Player, save: JSONObject) {
        val slayer = JSONObject()
        val slayerManager = getInstance(player)
        if(slayerManager.removed.isNotEmpty()) {
            val removedTasks = JSONArray()
            slayerManager.removed.map {
                removedTasks.add(it.ordinal.toString())
            }
            slayer["removedTasks"] = removedTasks
        }
        slayer["taskStreak"] = slayerManager.flags.taskStreak.toString()
        slayer["totalTasks"] = slayerManager.flags.completedTasks.toString()
        slayer["equipmentFlags"] = slayerManager.flags.equipmentFlags
        slayer["taskFlags"] = slayerManager.flags.taskFlags
        slayer["rewardFlags"] = slayerManager.flags.rewardFlags
        save["slayer"] = slayer
    }

    override fun parsePlayer(player: Player, data: JSONObject) {
        val slayerData = data["slayer"] as JSONObject
        val m = slayerData["master"]
        val flags = getInstance(player).flags
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

    override fun process(entity: Entity, event: NPCKillEvent) {
        val npc = event.npc
        val player = entity as? Player ?: return
        val slayer = getInstance(player)
        val flags = slayer.flags

        if (slayer.hasTask() && npc.id in slayer.task!!.npcs) {
            rewardXP(player, Skills.SLAYER, npc.skills.maximumLifepoints.toDouble())
            slayer.decrementAmount(1)
            if(slayer.hasTask()) return
            flags.taskStreak = flags.taskStreak + 1
            flags.completedTasks = flags.completedTasks + 1
            if ((flags.completedTasks > 4 || flags.canEarnPoints()) && flags.getMaster() != Master.TURAEL && flags.getPoints() < 64000) {
                var points = flags.getMaster().taskPoints[0]
                if (flags.taskStreak % 50 == 0) {
                    points = flags.getMaster().taskPoints[2]
                } else if (flags.taskStreak % 10 == 0) {
                    points = flags.getMaster().taskPoints[1]
                }
                flags.incrementPoints(points)
                if (flags.getPoints() > 64000) {
                    flags.setPoints(64000)
                }
                player.sendMessages("You've completed " + flags.taskStreak + " tasks in a row and received " + points + " points, with a total of " + flags.getPoints(), "You have completed " + flags.completedTasks + " tasks in total. Return to a Slayer master.")
            } else if (flags.completedTasks == 4) {
                player.sendMessage("You've completed your task; you will start gaining points on your next task!")
                flags.flagCanEarnPoints()
            } else if (flags.getMaster() == Master.TURAEL) {
                player.sendMessages("You've completed your task; Tasks from Turael do not award points.", "Return to a Slayer master.")
            } else {
                player.sendMessages("You've completed your task; Complete " + (4 - flags.completedTasks) + " more task(s) to start gaining points.", "Return to a Slayer master.")
            }
        }
    }

    /**
     * Method used to assign a new task for a player.
     * @param master the master to give the task.
     */
    fun generate(master: Master) {
        val task = SlayerUtils.generate(player!!, master) ?: return
        SlayerUtils.assign(player!!, task, master)
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
        player!!.varpManager.get(2502).setVarbit(0, flags.taskFlags shr 4).send(player)
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

    companion object {
        @JvmStatic fun getInstance(player: Player) : SlayerManager
        {
            return getAttribute(player, "slayer-manager", SlayerManager())
        }
    }
}