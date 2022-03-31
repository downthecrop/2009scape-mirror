package rs09.game.system

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.terminal.Terminal
import gui.GuiEvent
import rs09.ServerConstants
import java.io.*
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
        try {
            tradeLogWriter?.flush()
            tradeLogWriter?.close()
        } catch(ignored: Exception) {}
    }

    fun getTime(): String{
        return "[" + formatter.format(Date(System.currentTimeMillis())) +"]"
    }

    @JvmStatic
    fun logInfo(vararg messages: String){
        for(m in messages){
            val msg = "${getTime()}: [INFO] $m"
            if(m.isNotBlank()) {
                t.println(msg)
            }
        }
    }

    @JvmStatic
    fun logErr(message: String){
        val msg = "${getTime()}: [ ERR] $message"
        if(message.isNotBlank()) {
            t.println(msg)
        }
    }

    @JvmStatic
    fun logWarn(message: String){
        val msg = "${getTime()}: [WARN] $message"
        if(message.isNotBlank()) {
            t.println(msg)
        }
    }

    @JvmStatic
    fun logAlert(message: String){
        val msg = "${getTime()}: [ALRT] $message"
        if(message.isNotBlank()) {
            t.println(msg)
        }
    }

    @JvmStatic
    fun logAI(message: String){
        val msg = "${getTime()}: [AIPL] $message"
        if(message.isNotBlank()) {
            t.println(msg)
        }
    }

    @JvmStatic
    fun logRE(message: String){
        if(message.isNotBlank()) t.println("${getTime()}: ${TextColors.gray("[RAND] $message")}")
    }

    @JvmStatic
    fun logGE(message: String){
        if(message.isNotBlank()) t.println("${getTime()}: ${TextColors.gray("[  GE] $message")}")
    }

    @JvmStatic
    fun logTrade(message: String){
        try {
            if (message.isNotBlank()) {
                if (tradeLogWriter == null) logWarn("Trade Logger is null!")
                tradeLogWriter?.write("${getTime()}: $message")
                tradeLogWriter?.newLine()
            }
        } catch(ignored: Exception){}
    }

    @JvmStatic fun logStartup(message: String)
    {
        SystemLogger.logInfo("[STARTUP] $message")
    }

    @JvmStatic fun logShutdown(message: String)
    {
        SystemLogger.logInfo("[SHUTDOWN] $message")
    }
}