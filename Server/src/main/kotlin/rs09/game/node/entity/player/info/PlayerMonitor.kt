package rs09.game.node.entity.player.info

import api.getItemName
import core.game.container.Container
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import discord.Discord
import kotlinx.coroutines.*
import org.sqlite.SQLiteDataSource
import rs09.ServerConstants
import rs09.game.system.SystemLogger
import java.io.File
import java.sql.Connection
import java.util.concurrent.LinkedBlockingQueue
import kotlin.math.abs

object PlayerMonitor {
    private val eventQueue = LinkedBlockingQueue<LogEvent>()
    private var activeTask: Job? = null

    @JvmStatic fun logWealthChange(player: Player, totalWealth: Long, diff: Long) {
        val event = LogEvent.WealthLog(
            player.name,
            player.details.uid,
            totalWealth,
            diff,
            System.currentTimeMillis()
        )
        dispatch(event)
        if (abs(diff) >= 500_000L)
            Discord.postPlayerAlert(player.name, "Dubious change in wealth: $diff, total wealth: $totalWealth")
    }

    @JvmStatic fun logChat(player: Player, type: String, message: String) {
        val event = LogEvent.ChatLog(
            player.name,
            player.details.uid,
            type,
            message,
            System.currentTimeMillis()
        )
        checkForFlaggedText(player.name, message)
        dispatch(event)
    }

    @JvmStatic fun logTrade(player1: Player, player2: Player, container1: Container, container2: Container) {
        val container1String = StringBuilder()
        val container2String = StringBuilder()

        for (item in container1.toArray()) {
            item ?: continue
            container1String.append(getItemName(item.id) + "(${item.amount}), ")
        }

        for (item in container2.toArray()) {
            item ?: continue
            container2String.append(getItemName(item.id) + "(${item.amount}), ")
        }

        val event = LogEvent.TradeLog(
            player1.name,
            player1.details.uid,
            player2.name,
            player2.details.uid,
            container1String.toString(),
            container2String.toString(),
            System.currentTimeMillis()
        )
        dispatch(event)
    }

    @JvmStatic fun logPrivateChat(sender: Player, receiver: String, message: String) {
        val event = LogEvent.ChatLog(
            sender.name,
            sender.details.uid,
            "private",
            "=> $receiver: $message",
            System.currentTimeMillis()
        )
        checkForFlaggedText(sender.name, message)
        dispatch(event)
    }

    @JvmStatic fun logMisc(player: Player, type: String, details: String) {
        val event = LogEvent.MiscLog(
            player.name,
            player.details.uid,
            type,
            details,
            System.currentTimeMillis()
        )
        dispatch(event)
    }

    @JvmStatic fun logXpGains(player: Player, xpDiff: List<Pair<Int,Double>>) {
        if (player.isArtificial) return
        if (xpDiff.isEmpty()) return
        val query = StringBuilder("INSERT INTO xp_gains(player,uid,")
        val xpNames = StringBuilder()
        val xpAmounts = StringBuilder()
        for ((skillId, amount) in xpDiff) {
            xpNames.append(Skills.SKILL_NAME[skillId].lowercase() + ",")
            xpAmounts.append("$amount,")
        }
        query.append(xpNames)
        query.append("timestamp) VALUES(")
        query.append("'" + player.name + "',")
        query.append(player.details.uid.toString() + ",")
        query.append(xpAmounts)
        query.append(System.currentTimeMillis().toString())
        query.append(");")
        dispatch(LogEvent.XpLog(query.toString()))
    }

    private fun dispatch(event: LogEvent) {
        eventQueue.put(event)
        if (eventQueue.size >= 50) {
            processQueuedEvents()
        }
    }

    fun processQueuedEvents() {
        val path = ServerConstants.LOGS_PATH + "playerlogs.db"
        if (activeTask?.isActive == true)
            return
        if (!File(path).exists()) {
            createSqliteDatabase(path)
        }
        activeTask = GlobalScope.launch {
            val conn = connect(path)
            conn.use {
                while (eventQueue.isNotEmpty()) {
                    val event = withContext(Dispatchers.IO) { eventQueue.take() }
                    process(event, it)
                }
            }
        }
    }

    @JvmStatic fun flushRemainingEventsImmediately() {
        SystemLogger.logInfo(this::class.java, "Flushing player log events...")
        val path = ServerConstants.LOGS_PATH + "playerlogs.db"
        if (!File(path).exists()) {
            createSqliteDatabase(path)
        }
        if (activeTask != null)
            activeTask?.cancel("Interrupted by shutdown. This is probably fine.")
        val conn = connect(path)
        conn.use {
            while (eventQueue.isNotEmpty())
                process(eventQueue.take(), it)
        }
    }

    private fun connect(path: String) : Connection {
        val source = SQLiteDataSource()
        source.url = "jdbc:sqlite:$path"
        return source.connection
    }

    private fun createSqliteDatabase(path: String) {
        if (!File(ServerConstants.LOGS_PATH).exists())
            File(ServerConstants.LOGS_PATH).mkdirs()
        val connection = connect(path)
        val stmt = connection.createStatement()
        stmt.execute(
            """
            CREATE TABLE "chat_logs" ( "player" TEXT, "uid" INTEGER, "type" TEXT, "message" TEXT, "timestamp" NUMERIC );
            """)
        stmt.execute(
            """
            CREATE TABLE "misc_logs" ( "player" TEXT, "uid" INTEGER, "type" TEXT, "details" TEXT , "timestamp" NUMERIC); 
            """)
        stmt.execute(
            """
            CREATE TABLE "trade_logs" ( "player_a" TEXT, "player_b" TEXT, "uid_a" INTEGER, "uid_b" INTEGER, "items_a" TEXT, "items_b" TEXT, "timestamp" NUMERIC ); 
            """)
        stmt.execute(
            """
            CREATE TABLE "xp_gains" ( "player" TEXT, "uid" INTEGER, "attack" INTEGER, "defence" INTEGER, "strength" INTEGER, "hitpoints" INTEGER, "ranged" INTEGER, "prayer" INTEGER, "magic" INTEGER, "cooking" INTEGER, "woodcutting" INTEGER, "fletching" INTEGER, "fishing" INTEGER, "firemaking" INTEGER, "crafting" INTEGER, "smithing" INTEGER, "mining" INTEGER, "herblore" INTEGER, "agility" INTEGER, "thieving" INTEGER, "slayer" INTEGER, "farming" INTEGER, "runecrafting" INTEGER, "hunter" INTEGER, "construction" INTEGER, "summoning" INTEGER , "timestamp" NUMERIC) 
            """)
        stmt.execute(
            """
            CREATE TABLE "wealth_logs" ( "player" TEXT, "uid" INTEGER, "total" NUMERIC, "diff" NUMERIC, "timestamp" NUMERIC ) 
            """)
        stmt.close()
        connection.close()
    }

    private fun process(event: LogEvent, conn: Connection) {
        when (event) {
            is LogEvent.ChatLog -> {
                val stmt = conn.prepareStatement(CHAT_LOG_INSERT)
                stmt.setString(1, event.player)
                stmt.setInt(2, event.uid)
                stmt.setString(3, event.type)
                stmt.setString(4, event.message)
                stmt.setLong(5, event.timestamp)
                stmt.execute()
            }
            is LogEvent.TradeLog -> {
                val stmt = conn.prepareStatement(TRADE_LOG_INSERT)
                stmt.setString(1, event.player1)
                stmt.setString(2, event.player2)
                stmt.setInt(3, event.uid1)
                stmt.setInt(4, event.uid2)
                stmt.setString(5, event.items1)
                stmt.setString(6, event.items2)
                stmt.setLong(7, event.timestamp)
                stmt.execute()
            }
            is LogEvent.MiscLog -> {
                val stmt = conn.prepareStatement(MISC_LOG_INSERT)
                stmt.setString(1, event.player)
                stmt.setInt(2, event.uid)
                stmt.setString(3, event.type)
                stmt.setString(4, event.details)
                stmt.setLong(5, event.timestamp)
                stmt.execute()
            }
            is LogEvent.XpLog -> {
                val stmt = conn.createStatement()
                stmt.execute(event.query)
            }
            is LogEvent.WealthLog -> {
                val stmt = conn.prepareStatement(WEALTH_LOG_INSERT)
                stmt.setString(1, event.player)
                stmt.setInt(2, event.uid)
                stmt.setLong(3, event.total)
                stmt.setLong(4, event.diff)
                stmt.setLong(5, event.timeStamp)
                stmt.execute()
            }
        }
    }
    
    fun checkForFlaggedText(username: String, message: String) {
        if (message.contains("dupe") || message.contains("bug") || message.contains("exploit") || message.contains("glitch")) {
            Discord.postPlayerAlert(username, "Flagged Chat - $message")
        }
    }

    private sealed class LogEvent {
        data class ChatLog(val player: String, val uid: Int, val type: String, val message: String, val timestamp: Long) : LogEvent()
        data class TradeLog(val player1: String, val uid1: Int, val player2 : String, val uid2: Int, val items1: String, val items2: String, val timestamp: Long) : LogEvent()
        data class MiscLog(val player: String, val uid: Int, val type: String, val details: String, val timestamp: Long) : LogEvent()
        data class XpLog(val query: String) : LogEvent()
        data class WealthLog(val player: String, val uid: Int, val total: Long, val diff: Long, val timeStamp: Long) : LogEvent()
    }

    private const val CHAT_LOG_INSERT = "INSERT INTO chat_logs(player,uid,type,message,timestamp) VALUES (?,?,?,?,?);"
    private const val TRADE_LOG_INSERT = "INSERT INTO trade_logs(player_a,player_b,uid_a,uid_b,items_a,items_b,timestamp) VALUES (?,?,?,?,?,?,?);"
    private const val MISC_LOG_INSERT = "INSERT INTO misc_logs(player,uid,type,details,timestamp) VALUES (?,?,?,?,?);"
    private const val WEALTH_LOG_INSERT = "INSERT INTO wealth_logs(player,uid,total,diff,timestamp) VALUES (?,?,?,?,?);"

}