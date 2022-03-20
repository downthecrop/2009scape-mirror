package org.rs09.client

import org.rs09.SystemLogger
import org.rs09.client.config.GameConfig
import org.runite.client.GameShell
import java.io.File
import java.io.FileOutputStream

object GameLaunch {

    /**
     * The main method.
     * r @param args the arguments casted on runtime.
     * r_game
     */
    @JvmStatic
    fun main(args: Array<String>) {
        for (i in args.indices) {
            val cmd = args[i].split("=").toTypedArray()
            when (cmd[0]) {
                "ip" -> GameConfig.IP_ADDRESS = cmd[1]
                "world" -> GameConfig.WORLD_OVERRIDE = cmd[1].toInt()
                else -> GameConfig.configLocation = cmd[0]
            }
        }
        try {
            SystemLogger.logInfo("Trying to parse config at " + GameConfig.configLocation)
            GameConfig.parse(GameConfig.configLocation)
            GameConfig.implementHoliday()
            GameConfig.extendRenderDistance()
        } catch (e: Exception){
            GameConfig.IP_ADDRESS = "73.67.76.208"
            GameConfig.IP_MANAGEMENT = "73.67.76.208"
            GameConfig.JS5_SERVER_PORT = 43593
            GameConfig.SERVER_PORT = 43594
            GameConfig.WL_PORT = 5555
            GameConfig.RCM_STYLE_PRESET = "classic"
            GameConfig.RCM_TITLE = "<col=5d5447>Choose Option</col>"
            GameConfig.HOLIDAYS_ENABLED = true
            GameConfig.implementHoliday()
            GameConfig.RENDER_DISTANCE_INCREASE = true
            GameConfig.extendRenderDistance()
            SystemLogger.logWarn("Config file ${GameConfig.configLocation} not found, using defaults.")
        }
        /**
         * Launches the client
         */
        if(System.getProperty("os.name").toLowerCase().contains("nux")){ //Fixes crashing due to XInitThreads not being called - JVM bug
            val istream = javaClass.classLoader.getResourceAsStream("libfixXInitThreads.so")
            val buff = ByteArray(1024)
            var read = -1
            val temp = File.createTempFile("libfixXInitThreads.so","")
            val fos = FileOutputStream(temp)

            read = istream.read(buff)
            while(read != -1){
                fos.write(buff, 0, read)
                read = istream.read(buff)
            }

            fos.close()
            istream.close()

            System.load(temp.absolutePath)
        }

        //Force IPv4 because sometimes Windows insists on using IPv6 regardless of what a service actually offers
        System.setProperty("java.net.preferIPv4Stack" , "true");

        GameShell.launchDesktop()
    }

}