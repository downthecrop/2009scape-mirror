package core.integrations.grafana

import core.ServerConstants
import core.api.StartupListener
import core.game.bots.AIRepository
import core.integrations.sqlite.SQLiteProvider
import org.json.simple.JSONObject

class Grafana : StartupListener {
    override fun startup() {
        if (!ServerConstants.GRAFANA_LOGGING) return
        db = SQLiteProvider (ServerConstants.GRAFANA_PATH + "grafana.db", expectedTables)
        db.initTables()
        db.pruneOldData(7)
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
        val pulseCounts: Array<Map.Entry<String, Int>>
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
            val cycleData = GrafanaData (
                playerTickTime,
                npcTickTime,
                playerRenderTime,
                totalTickTime,
                packetProcessTime,
                botPulseTime,
                otherPulseTime,
                pulseTimes.entries.toTypedArray(),
                pulseCounts.entries.toTypedArray()
            )
            db.runAsync { it ->
                with (it.prepareStatement(INSERT_TOP_PULSES)) {
                    val topSorted = cycleData.pulseTimes.sortedByDescending { entry -> entry.value }
                    val rootObj = JSONObject()
                    val contentObj = JSONObject()
                    for (i in 0 until 5) {
                        contentObj [topSorted[i].key] = topSorted[i].value
                    }
                    rootObj["pulses"] = contentObj
                    setString(1, rootObj.toJSONString())
                    setInt(2, getNowTime())
                    execute()
                }

                with (it.prepareStatement(INSERT_PULSE_COUNT)) {
                    val topSorted = cycleData.pulseCounts.sortedByDescending { entry -> entry.value }
                    val rootObj = JSONObject()
                    val contentObj = JSONObject()
                    for (i in 0 until 5) {
                        contentObj [topSorted[i].key] = topSorted[i].value
                    }
                    rootObj["pulses"] = contentObj
                    setString(1, rootObj.toJSONString())
                    setInt(2, getNowTime())
                    execute()
                }

                with (it.prepareStatement(INSERT_BOT_COUNT)) {
                    setInt(1, AIRepository.PulseRepository.size)
                    setInt(2, getNowTime())
                    execute()
                }

                with (it.prepareStatement(INSERT_TICK_MEAS)) {
                    setInt(1, cycleData.botPulseTime)
                    setInt(2, cycleData.otherPulseTime)
                    setInt(3, cycleData.npcTickTime)
                    setInt(4, cycleData.playerTickTime)
                    setInt(5, cycleData.playerRenderTime)
                    setInt(6, cycleData.packetProcessTime)
                    setInt(7, cycleData.totalTickTime - (cycleData.botPulseTime + cycleData.otherPulseTime + cycleData.npcTickTime + cycleData.playerTickTime + cycleData.playerRenderTime + cycleData.packetProcessTime))
                    setInt(8, getNowTime())
                    execute()
                }
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