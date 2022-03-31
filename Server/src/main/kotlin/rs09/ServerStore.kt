package rs09

import api.ShutdownListener
import api.StartupListener
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import rs09.game.system.SystemLogger
import rs09.game.system.SystemLogger.logShutdown
import rs09.game.system.SystemLogger.logStartup
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import javax.script.ScriptEngineManager

class ServerStore : StartupListener, ShutdownListener {
    override fun startup() {
        logStartup("Parsing server store...")
        val dir = File(ServerConstants.STORE_PATH!!)
        if(!dir.exists()){
            dir.mkdirs()
            return
        }

        var parser: JSONParser
        var reader: FileReader

        dir.listFiles()?.forEach { storeFile ->
            val key = storeFile.nameWithoutExtension

            reader = FileReader(storeFile)
            parser = JSONParser()

            try {
                val data = parser.parse(reader) as JSONObject
                fileMap[key] = data
                counter++
            } catch (e: Exception){
                SystemLogger.logErr("Failed parsing ${storeFile.name} - stack trace below.")
                e.printStackTrace()
                return@forEach
            }
        }
    }

    override fun shutdown() {
        logShutdown("Saving server store...")
        val dir = File(ServerConstants.DATA_PATH + File.separator + "serverstore")
        if(!dir.exists()){
            dir.mkdirs()
            return
        }

        val manager = ScriptEngineManager()
        val scriptEngine = manager.getEngineByName("JavaScript")

        fileMap.forEach { (name, data) ->
            val path = dir.absolutePath + File.separator + name + ".json"

            scriptEngine.put("jsonString", data.toJSONString())
            scriptEngine.eval("result = JSON.stringify(JSON.parse(jsonString), null, 2)")
            val prettyPrintedJson = scriptEngine["result"] as String

            FileWriter(path).use { it.write(prettyPrintedJson); it.flush(); it.close() }
        }
    }

    companion object {
        val fileMap = HashMap<String,JSONObject>()
        var counter = 0

        @JvmStatic
        fun getArchive(name: String): JSONObject {
            if(fileMap[name] == null){
                fileMap[name] = JSONObject()
            }

            return fileMap[name]!!
        }

        fun setArchive(name: String, data: JSONObject){
            fileMap[name] = data
        }

        fun clearDailyEntries() {
            fileMap.keys.toTypedArray().forEach {
                if(it.toLowerCase().contains("daily")) fileMap[it]?.clear()
            }
        }

        fun clearWeeklyEntries() {
            fileMap.keys.toTypedArray().forEach {
                if(it.toLowerCase().contains("weekly")) fileMap[it]?.clear()
            }
        }

        @JvmStatic
        fun JSONObject.getInt(key: String): Int {
            return when(val value = this[key]){
                is Long -> value.toInt()
                is Double -> value.toInt()
                is Float -> value.toInt()
                is Int -> value
                is Nothing -> 0
                else -> 0
            }
        }

        @JvmStatic
        fun JSONObject.getString(key: String): String {
            return this[key] as? String ?: "nothing"
        }

        @JvmStatic
        fun JSONObject.getLong(key: String): Long {
            return this[key] as? Long ?: 0L
        }

        @JvmStatic
        fun JSONObject.getBoolean(key: String): Boolean {
            return this[key] as? Boolean ?: false
        }

        fun List<Int>.toJSONArray(): JSONArray{
            val jArray = JSONArray()
            for(i in this){
                jArray.add(i)
            }
            return jArray
        }
    }
}