package ms09

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.terminal.Terminal
import java.io.BufferedWriter
import java.io.Writer
import java.text.SimpleDateFormat
import java.util.*

object MSLogger {
    val t = Terminal()
    var tradeLog: Writer? = null
    var tradeLogWriter: BufferedWriter? = null


    fun getTime(): String{
        val formatter = SimpleDateFormat("HH:mm:ss")
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
}