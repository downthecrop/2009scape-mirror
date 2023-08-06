package core.integrations.grafana

import core.ServerConstants
import core.api.StartupListener
import core.game.bots.AIRepository
import core.integrations.sqlite.SQLiteProvider
import core.tools.cycle
import kotlinx.coroutines.Job
import org.json.simple.JSONObject
import java.util.LinkedList
import java.util.concurrent.LinkedBlockingDeque

class Grafana : StartupListener {
    override fun startup() {
        if (!ServerConstants.GRAFANA_LOGGING) return
        db = SQLiteProvider (ServerConstants.GRAFANA_PATH + "grafana.db", expectedTables)
        db.initTables()
        db.pruneOldData(ServerConstants.GRAFANA_TTL_DAYS)
    }

    data class GrafanaData (
        val playerTickTime: Int,
        val npcTickTime: Int,
        val playerRenderTime: Int,
        val totalTickTime: Int,
        val packetProcessTime: Int,
        val botPulseTime: Int,
        val otherPulseTime: Int,
        val pulseTimes: Array<Map.Entry<String, Int>>,
        val pulseCounts: Array<Map.Entry<String, Int>>,
        val botCount: Int,
        val timeSecs: Int
    )

    companion object {
        var playerTickTime = 0
        var npcTickTime = 0
        var playerRenderTime = 0
        var totalTickTime = 0
        var packetProcessTime = 0
        var botPulseTime = 0
        var otherPulseTime = 0
        lateinit var db: SQLiteProvider
        var cycleData = LinkedList<GrafanaData>()
        var currentTask: Job? = null

        var pulseTimes = HashMap<String, Int>()
        var pulseCounts = HashMap<String, Int>()

        fun addPulseLength(pulseName: String, time: Int) {
            if (!this::db.isInitialized) return
            pulseTimes[pulseName] = (pulseTimes[pulseName] ?: 0) + time
        }

        fun countPulse(pulseName: String) {
            if (!this::db.isInitialized) return
            pulseCounts[pulseName] = (pulseCounts[pulseName] ?: 0) + 1
        }

        fun startTick() {
            if (!this::db.isInitialized) return
            playerTickTime = 0
            npcTickTime = 0
            playerRenderTime = 0
            totalTickTime = 0
            packetProcessTime = 0
            botPulseTime = 0
            otherPulseTime = 0
            pulseTimes.clear()
            pulseCounts.clear()
        }

        fun endTick() {
            if (!this::db.isInitialized) return
            val thisCycle = GrafanaData (
                playerTickTime,
                npcTickTime,
                playerRenderTime,
                totalTickTime,
                packetProcessTime,
                botPulseTime,
                otherPulseTime,
                pulseTimes.entries.toTypedArray(),
                pulseCounts.entries.toTypedArray(),
                AIRepository.PulseRepository.size,
                getNowTime()
            )
            cycleData.add(thisCycle)

            if (cycleData.size < 50) return
            if (currentTask?.isActive == true) return

            currentTask = db.runAsync {
                with (it.prepareStatement(INSERT_TOP_PULSES)) {
                    for (i in 0 until 50) {
                        val topSorted = cycleData[i].pulseTimes.sortedByDescending { entry -> entry.value }
                        val rootObj = JSONObject()
                        val contentObj = JSONObject()
                        for (j in 0 until 5) {
                            contentObj[topSorted[j].key] = topSorted[j].value
                        }
                        rootObj["pulses"] = contentObj
                        setString(1, rootObj.toJSONString())
                        setInt(2, cycleData[i].timeSecs)
                        execute()
                    }
                }

                with (it.prepareStatement(INSERT_PULSE_COUNT)) {
                    for (i in 0 until 50) {
                        val topSorted = cycleData[i].pulseCounts.sortedByDescending { entry -> entry.value }
                        val rootObj = JSONObject()
                        val contentObj = JSONObject()
                        for (j in 0 until 5) {
                            contentObj[topSorted[j].key] = topSorted[j].value
                        }
                        rootObj["pulses"] = contentObj
                        setString(1, rootObj.toJSONString())
                        setInt(2, cycleData[i].timeSecs)
                        execute()
                    }
                }

                with (it.prepareStatement(INSERT_BOT_COUNT)) {
                    for (i in 0 until 50) {
                        setInt(1, cycleData[i].botCount)
                        setInt(2, cycleData[i].timeSecs)
                        execute()
                    }
                }

                with (it.prepareStatement(INSERT_TICK_MEAS)) {
                    for (i in 0 until 50) {
                        setInt(1, cycleData[i].botPulseTime)
                        setInt(2, cycleData[i].otherPulseTime)
                        setInt(3, cycleData[i].npcTickTime)
                        setInt(4, cycleData[i].playerTickTime)
                        setInt(5, cycleData[i].playerRenderTime)
                        setInt(6, cycleData[i].packetProcessTime)
                        setInt(7, cycleData[i].totalTickTime - (cycleData[i].botPulseTime + cycleData[i].otherPulseTime + cycleData[i].npcTickTime + cycleData[i].playerTickTime + cycleData[i].playerRenderTime + cycleData[i].packetProcessTime))
                        setInt(8, cycleData[i].timeSecs)
                        execute()
                    }
                }

                cycleData.removeAll(cycleData.subList(0, 49).toSet())
            }
        }

        private fun getNowTime () : Int {
            return (System.currentTimeMillis() / 1000L).toInt()
        }

        private const val INSERT_TOP_PULSES = "INSERT INTO top_pulses (pulse_info, ts) VALUES (?, ?);"
        private const val INSERT_PULSE_COUNT = "INSERT INTO high_volume_pulses (pulse_info, ts) VALUES (?, ?);"
        private const val INSERT_BOT_COUNT = "INSERT INTO bot_counts (count, ts) VALUES (?, ?);"
        private const val INSERT_TICK_MEAS = "INSERT INTO tick_lengths (bot_pulses, misc_pulses, npc_tick, player_tick, player_render, packet_incoming, other, ts) VALUES (?,?,?,?,?,?,?,?);"
        private var expectedTables = hashMapOf(
            "bot_counts" to "CREATE TABLE \"bot_counts\" (\n" +
                    "\t\"count\"\tINTEGER,\n" +
                    "\t\"ts\"\tINTEGER\n" +
                    ")",
            "high_volume_pulses" to "CREATE TABLE \"high_volume_pulses\" (\n" +
                    "\t\"pulse_info\"\tTEXT,\n" +
                    "\t\"ts\"\tINTEGER\n" +
                    ")",
            "tick_lengths" to "CREATE TABLE \"tick_lengths\" (\n" +
                    "\t\"bot_pulses\"\tINTEGER,\n" +
                    "\t\"misc_pulses\"\tINTEGER,\n" +
                    "\t\"npc_tick\"\tINTEGER,\n" +
                    "\t\"player_tick\"\tINTEGER,\n" +
                    "\t\"player_render\"\tINTEGER,\n" +
                    "\t\"packet_incoming\"\tINTEGER,\n" +
                    "\t\"other\"\tINTEGER,\n" +
                    "\t\"ts\"\tINTEGER\n" +
                    ")",
            "top_pulses" to "CREATE TABLE \"top_pulses\" (\n" +
                    "\t\"pulse_info\"\tTEXT,\n" +
                    "\t\"ts\"\tINTEGER\n" +
                    ")"
        )
    }
}