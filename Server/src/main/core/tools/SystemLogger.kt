package core.tools

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.terminal.*
import com.google.protobuf.ByteString.Output
import core.ServerConstants
import core.game.world.GameWorld
import core.api.*
import java.io.OutputStream
import java.io.PrintStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * Handles server log printing
 * @author Ceikry
 * Thanks to the awesome library made by AJ Alt
 */
object SystemLogger {
    val t = Terminal()
    val errT = t.forStdErr()
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXX")

    private fun getTime(): String{
        return "[" + formatter.format(Date(System.currentTimeMillis())) +"]"
    }

    @JvmStatic
    fun processLogEntry(clazz: Class<*>, log: Log, message: String) {
        when (log) {
            Log.DEBUG -> {
                if (GameWorld.settings?.isDevMode != true)
                    return
                val msg = TextColors.cyan("${getTime()}: [${clazz.simpleName}] $message")
                t.println(msg)
            }

            Log.FINE -> {
                if (ServerConstants.LOG_LEVEL < LogLevel.VERBOSE)
                    return
                val msg = TextColors.gray("${getTime()}: [${clazz.simpleName}] $message")
                t.println(msg)
            }

            Log.INFO -> {
                if (ServerConstants.LOG_LEVEL < LogLevel.DETAILED)
                    return

                val msg = "${getTime()}: [${clazz.simpleName}] $message"
                t.println(msg)
            }

            Log.WARN -> {
                if (ServerConstants.LOG_LEVEL < LogLevel.CAUTIOUS)
                    return

                val msg = TextColors.yellow("${getTime()}: [${clazz.simpleName}] $message")
                t.println(msg)
            }

            Log.ERR -> {
                val msg = "${getTime()}: [${clazz.simpleName}] $message"
                errT.println(msg)
            }
        }
    }

    @JvmStatic
    fun logGE(message: String){
        log(this::class.java, Log.FINE, "[  GE] $message")
    }

    @JvmStatic fun logStartup(message: String)
    {
        log(this::class.java, Log.FINE, "[STARTUP] $message")
    }

    @JvmStatic fun logShutdown(message: String)
    {
        log(this::class.java, Log.FINE, "[SHUTDOWN] $message")
    }

    fun logMS(s: String) {
        log(this::class.java, Log.FINE, "[  MS] $s")
    }
}

enum class LogLevel {
    SILENT,
    CAUTIOUS,
    DETAILED,
    VERBOSE
}

enum class Log {
    FINE,
    INFO,
    WARN,
    ERR,
    DEBUG
}
