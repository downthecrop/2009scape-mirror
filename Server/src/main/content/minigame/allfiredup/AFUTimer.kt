package content.minigame.allfiredup

import core.api.getAttribute
import core.api.log
import core.api.removeAttribute
import core.api.sendMessage
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.timer.PersistTimer
import core.tools.Log
import core.tools.colorize
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.rs09.consts.Items

class AFUTimer : PersistTimer(1, "AFU timer", isSoft = true) {
    private val burnTicks = Array(14) { 0 }

    fun addTime(index: Int, logsId: Int, numLogs: Int) {
        val ticksPerLog = when (logsId) {
            Items.LOGS_1511 -> 65
            Items.OAK_LOGS_1521 -> 68
            Items.WILLOW_LOGS_1519 -> 73
            Items.MAPLE_LOGS_1517 -> 79
            Items.YEW_LOGS_1515 -> 83
            Items.MAGIC_LOGS_1513 -> 90
            else -> 0
        }
        burnTicks[index] += numLogs * ticksPerLog
    }

    fun getBonusExperience(): Double {
        return when (getLitBeacons()) {
            1 -> 608.4
            2 -> 1622.4
            3 -> 1987.4
            4 -> 2149.6
            5 -> 2149.6
            6 -> 2514.6
            7 -> 2555.2
            8 -> 2758.0
            9 -> 2839.1
            10 -> 3041.9
            11 -> 3123.0
            12 -> 3244.7
            13 -> 3366.4
            14 -> 4867.4
            else -> 0.0
        }
    }

    fun getLitBeacons(): Int {
        return burnTicks.count { it > 0 }
    }

    override fun save(root: JSONObject, entity: Entity) {
        val beaconArray = JSONArray()
        for (i in 0..13) {
            beaconArray.add(burnTicks[i])
        }
        root["beacons"] = beaconArray
    }

    override fun parse(root: JSONObject, entity: Entity) {
        val beacons = root["beacons"] as JSONArray
        for (i in 0..13) {
            burnTicks[i] = beacons[i].toString().toInt()
        }
    }

    override fun run(entity: Entity) : Boolean {
        if (entity !is Player) {
            log(this::class.java, Log.ERR, "How did a non-Player light an AFU beacon?")
            return false
        }
        val player = entity
        var keepRunning = false
        for (i in 0..13) {
            val beacon = AFUBeacon.values()[i]
            if (burnTicks[i] > 0) {
                burnTicks[i]--
                if (burnTicks[i] > 0) {
                    keepRunning = true
                }
                if (burnTicks[i] == 300) {
                    val backupLogsId = getAttribute(player, "/save:beacon:$i:backupLogsId", 0)
                    if (backupLogsId != 0) {
                        addTime(i, backupLogsId, 5)
                        removeAttribute(player, "/save:beacon:$i:backupLogsId")
                        sendMessage(player, colorize("%RThe beacon watcher ${beacon.title} has used your backup logs."))
                    } else {
                        beacon.diminish(player)
                        sendMessage(player, colorize("%RThe beacon ${beacon.title} is dying!"))
                    }
                } else if (burnTicks[i] == 0) {
                    beacon.extinguish(player)
                    removeAttribute(player, "/save:beacon:$i:logsId")
                    sendMessage(player, colorize("%RThe beacon ${beacon.title} has gone out!"))
                }
            }
        }
        return keepRunning
    }
}