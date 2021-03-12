package core.game.system

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.terminal.Terminal
import core.ServerConstants
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import java.io.*
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Handles server log printing
 * @author Ceikry
 * Thanks to the awesome library made by AJ Alt
 */
object SystemLogger {
    val t = Terminal()
    val formatter = SimpleDateFormat("HH:mm:ss")
    var tradeLog: Writer? = null
    var tradeLogWriter: BufferedWriter? = null

    @JvmStatic
    fun initTradeLogger(){
        if(!File(ServerConstants.LOGS_PATH + "trade" + File.separator + "test").exists()){
            File(ServerConstants.LOGS_PATH + "trade" + File.separator + "test").mkdirs()
        }
        tradeLog = FileWriter(ServerConstants.LOGS_PATH + "trade" + File.separator + SimpleDateFormat("dd-MM-yyyy").format(System.currentTimeMillis()) + ".log")
        tradeLogWriter = BufferedWriter(tradeLog!!)
    }

    @JvmStatic()
    fun flushLogs() {
        tradeLogWriter?.flush()
        tradeLogWriter?.close()
    }

    fun getTime(): String{
        return "[" + formatter.format(Date(System.currentTimeMillis())) +"]"
    }

    @JvmStatic
    fun logInfo(vararg messages: String){
        for(m in messages){
            if(m.isNotBlank()) t.println("${getTime()}: [INFO] $m")
        }
    }

    @JvmStatic
    fun logErr(message: String){
        if(message.isNotBlank()) t.println("${getTime()}: ${TextColors.brightRed("[ ERR] $message")}")
    }

    @JvmStatic
    fun logWarn(message: String){
        if(message.isNotBlank()) t.println("${getTime()}: ${TextColors.yellow("[WARN] $message")}")
    }

    @JvmStatic
    fun logAlert(message: String){
        if(message.isNotBlank()) t.println("${getTime()}: ${TextColors.brightYellow("[ALRT] $message")}")
    }

    @JvmStatic
    fun logAI(message: String){
        if(message.isNotBlank()) t.println("${getTime()}: ${TextColors.gray("[AIPL] $message")}")
    }

    @JvmStatic
    fun logTrade(message: String){
        if(message.isNotBlank()){
            if(tradeLogWriter == null) logWarn("Trade Logger is null!")
            tradeLogWriter?.write("${getTime()}: $message")
            tradeLogWriter?.newLine()
        }
    }
}