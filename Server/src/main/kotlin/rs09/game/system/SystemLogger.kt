package rs09.game.system

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.terminal.*
import rs09.ServerConstants
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.Writer
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
    fun logInfo(clazz: Class<*>, vararg messages: String){
        for(m in messages){
            val msg = "${getTime()}: [${clazz.simpleName}] $m"
            if(m.isNotBlank()) {
                t.println(msg)
            }
        }
    }

    @JvmStatic
    fun logErr(clazz: Class<*>, message: String){
        val msg = "${getTime()}: [${clazz.simpleName}] $message"
        if(message.isNotBlank()) {
            t.println(msg)
        }
    }

    @JvmStatic
    fun logWarn(clazz: Class<*>, message: String){
        val msg = "${getTime()}: [${clazz.simpleName}] $message"
        if(message.isNotBlank()) {
            t.println(msg)
        }
    }

    @JvmStatic
    fun logAlert(clazz: Class<*>, message: String){
        val msg = "${getTime()}: [${clazz.simpleName}] $message"
        if(message.isNotBlank()) {
            t.println(msg)
        }
    }

    @JvmStatic
    fun logAI(clazz: Class<*>, message: String){
        val msg = "${getTime()}: [${clazz.simpleName}] $message"
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
                if (tradeLogWriter == null) logWarn(this::class.java, "Trade Logger is null!")
                tradeLogWriter?.write("${getTime()}: $message")
                tradeLogWriter?.newLine()
            }
        } catch(ignored: Exception){}
    }

    @JvmStatic fun logStartup(message: String)
    {
        logInfo(this::class.java, "[STARTUP] $message")
    }

    @JvmStatic fun logShutdown(message: String)
    {
        logInfo(this::class.java,"[SHUTDOWN] $message")
    }

    fun logMS(s: String) {
        if(s.isNotBlank()) t.println("${getTime()}: ${TextColors.gray("[  MS] $s")}")
    }
}