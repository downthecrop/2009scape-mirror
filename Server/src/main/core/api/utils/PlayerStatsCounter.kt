package core.api.utils

import core.api.StartupListener
import core.game.node.entity.player.Player
import java.io.File
import java.io.FileReader
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import core.ServerConstants
import core.api.log
import core.game.node.item.Item
import core.game.world.GameWorld
import core.integrations.sqlite.SQLiteProvider
import core.tools.Log
import core.tools.SystemLogger.logStartup
import kotlin.io.path.Path

class PlayerStatsCounter(
    private val dbPath: String = Path(ServerConstants.DATA_PATH ?: "", "playerstats", "player_stats.db").toString()
) : StartupListener {

    override fun startup() {
        logStartup("Loading Player Stats")

        db = SQLiteProvider(dbPath, expectedTables)
        db.initTables()

        if (!tableHasData()) { // TODO: Remove check, porter and raw inserts once SQLite tracking is proven and the live server has been updated
            portLegacyKillCounterJsonToSQLite()
        }
    }

    companion object {
        lateinit var db: SQLiteProvider

        private fun resolveUIDFromPlayerUsername(playerUsername: String): Int {
            return GameWorld.accountStorage.getAccountInfo(playerUsername).uid
        }

        private fun portLegacyKillCounterJsonToSQLite() {
            val file = File(Path(ServerConstants.DATA_PATH ?: "", "global_kill_stats.json").toString())
            if (!file.exists()) {
                return
            }

            val reader = FileReader(file)
            val parser = JSONParser()
            try {
                val data = parser.parse(reader) as JSONObject
                val json_kills = data.get("kills")
                if (json_kills != null && json_kills is JSONObject) {
                    var progress = 1
                    val totalPlayers = json_kills.size
                    for ((player, killStats) in json_kills.asIterable()) {
                        log(
                            PlayerStatsCounter::class.java,
                            Log.INFO,
                            "Porting kill counters for player $progress/$totalPlayers"
                        )
                        if (player is String) {
                            val playerUid = resolveUIDFromPlayerUsername(player)
                            log(
                                PlayerStatsCounter::class.java,
                                Log.INFO,
                                "Player $player ($playerUid)"
                            )
                            for ((npc_id, count) in (killStats as JSONObject).asIterable()) {
                                log(
                                    PlayerStatsCounter::class.java,
                                    Log.INFO,
                                    "Inserting kill for $player ($playerUid, $npc_id, $count)"
                                )
                                incrementKills(
                                    playerUid,
                                    (npc_id as String).toInt(),
                                    count as Long
                                )
                            }
                        }
                        progress++
                    }
                }
                val json_rare_drops = data.get("rare_drops")
                if (json_rare_drops != null && json_rare_drops is JSONObject) {
                    var progress = 1
                    val totalPlayers = json_rare_drops.size
                    for ((player, rareDrops) in json_rare_drops.asIterable()) {
                        log(
                            PlayerStatsCounter::class.java,
                            Log.INFO,
                            "Porting rare drops for player $progress/$totalPlayers"
                        )
                        if (player is String) {
                            val playerUid = resolveUIDFromPlayerUsername(player)
                            log(
                                PlayerStatsCounter::class.java,
                                Log.INFO,
                                "Player $player ($playerUid)"
                            )
                            for ((item_id, count) in (rareDrops as JSONObject).asIterable()) {
                                log(
                                    PlayerStatsCounter::class.java,
                                    Log.INFO,
                                    "Inserting rare drop for $player ($playerUid, $item_id, $count)"
                                )
                                incrementRareDrop(
                                    playerUid,
                                    (item_id as String).toInt(),
                                    count as Long
                                )
                            }
                        }
                        progress++
                    }
                }
            } catch (e: Exception) {
                log(this::class.java, Log.ERR, "Failed parsing ${file.name} - stack trace below.")
                e.printStackTrace()
            }
        }

        private val killsTableDefinition = """
            CREATE TABLE kills(
                    player_uid INTEGER,
                    entity_id INTEGER,
                    kills INTEGER,
                    PRIMARY KEY(player_uid, entity_id))
        """.trimIndent()

        private val rareDropsTableDefinition = """
            CREATE TABLE rare_drops(
                    player_uid INTEGER,
                    item_id INTEGER,
                    amount INTEGER,
                    PRIMARY KEY (player_uid, item_id))
        """.trimIndent()

        private val getKillsRowCountSql = """
            SELECT COUNT(1) FROM kills;
        """.trimIndent()

        private val insertOrIncrementKillSql = """
            INSERT INTO kills (player_uid, entity_id, kills) 
            VALUES (?, ?, ?)
            ON CONFLICT(player_uid, entity_id) DO UPDATE SET kills = kills + ?;
        """.trimIndent()

        private val insertOrIncrementRareDropSql = """
            INSERT INTO rare_drops (player_uid, item_id, amount) 
            VALUES (?, ?, ?)
            ON CONFLICT(player_uid, item_id) DO UPDATE SET amount = amount + ?;
        """.trimIndent()

        private val getRareDropsSql = """
            SELECT amount FROM rare_drops WHERE player_uid=? AND item_id=?
        """.trimIndent()

        private fun createGetKillsSql(countOfEntitiesToSearchFor: Int): String {
            if (countOfEntitiesToSearchFor < 1) {
                throw Exception("Should be at least 1")
            }
            val entitySearchParameterMarkers = "?" + ",?".repeat(countOfEntitiesToSearchFor - 1)
            return """
                SELECT SUM(kills) FROM kills WHERE player_uid=? AND entity_id IN ($entitySearchParameterMarkers)
            """.trimIndent()
        }

        private val expectedTables = hashMapOf(
            "kills" to killsTableDefinition,
            "rare_drops" to rareDropsTableDefinition,
        )

        private fun tableHasData(): Boolean {
            var hasData = false
            db.run { conn ->
                val statement = conn.prepareStatement(getKillsRowCountSql)
                val result = statement.executeQuery()
                if (result.next()) {
                    val rowCount = result.getInt(1)
                    hasData = rowCount > 0
                }
            }
            return hasData
        }

        @JvmStatic
        private fun incrementKills(playerUid: Int, npcId: Int, kills: Long) {
            db.run { conn ->
                val statement = conn.prepareStatement(insertOrIncrementKillSql)
                statement.setInt(1, playerUid)
                statement.setInt(2, npcId)
                statement.setLong(3, kills)
                statement.setLong(4, kills)
                statement.execute()
            }
        }

        @JvmStatic
        fun incrementKills(player: Player, npcId: Int) {
            incrementKills(player.details.uid, npcId, 1)
        }

        @JvmStatic
        private fun incrementRareDrop(playerUid: Int, itemId: Int, amount: Long) {
            db.run { conn ->
                val statement = conn.prepareStatement(insertOrIncrementRareDropSql)
                statement.setInt(1, playerUid)
                statement.setInt(2, itemId)
                statement.setLong(3, amount)
                statement.setLong(4, amount)
                statement.execute()
            }
        }

        @JvmStatic
        fun incrementRareDrop(player: Player, item: Item) {
            incrementRareDrop(player.details.uid, item.id, item.amount.toLong())
        }

        @JvmStatic
        fun getKills(player: Player, npcIds: IntArray): Long {
            var kills: Long = 0
            db.run { conn ->
                val statement = conn.prepareStatement(createGetKillsSql(npcIds.size))
                statement.setInt(1, player.details.uid)
                for (npcIdParamIndex in npcIds.indices) {
                    // +2 because the statement parameterIndexes start at 1 and the first param is the player uid
                    statement.setInt(npcIdParamIndex + 2, npcIds[npcIdParamIndex])
                }
                val result = statement.executeQuery()
                if (result.next()) {
                    kills = result.getLong(1)
                }
            }
            return kills
        }

        @JvmStatic
        fun getRareDrops(player: Player, itemId: Int): Long {
            var rareDropsForItem: Long = 0
            db.run { conn ->
                val statement = conn.prepareStatement(getRareDropsSql)
                statement.setInt(1, player.details.uid)
                statement.setInt(2, itemId)
                val result = statement.executeQuery()
                if (result.next()) {
                    rareDropsForItem = result.getLong(1)
                }
            }
            return rareDropsForItem
        }
    }
}
