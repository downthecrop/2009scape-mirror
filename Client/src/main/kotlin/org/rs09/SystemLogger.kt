package org.rs09

import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.rendering.TextColors.*
import org.rs09.client.config.GameConfig
import java.text.SimpleDateFormat
import java.util.*

/**
 * Handles client logging
 * @author Ceikry
 * Thanks to Aj Alt for the awesome Mordant library!
 */
object SystemLogger {
    val formatter = SimpleDateFormat("HH:mm:ss")
    val t = Terminal()

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
        if(message.isNotBlank()) t.println("${getTime()}: ${brightRed("[ ERR] $message")}")
    }

    @JvmStatic
    fun logWarn(message: String){
        if(message.isNotBlank()) t.println("${getTime()}: ${yellow("[WARN] $message")}")
    }

    @JvmStatic
    fun logDiscord(message: String){
        t.println("${getTime()}: ${brightBlue("[DISC] $message")}")
    }

    @JvmStatic
    fun logVerbose(message: String){
        if(GameConfig.VERBOSE_LOGGING){
            logInfo(message)
        }
    }
}