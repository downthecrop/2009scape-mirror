package rs09

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import rs09.game.system.SystemLogger
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import javax.script.ScriptEngineManager

object ServerStore {
    val fileMap = HashMap<String,JSONObject>()
    var counter = 0

    fun init(){
        val dir = File(ServerConstants.DATA_PATH + File.separator + "serverstore")
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

    @JvmStatic
    fun save() {
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

    fun JSONObject.getString(key: String): String {
        return this[key] as? String ?: "nothing"
    }

    fun JSONObject.getLong(key: String): Long {
        return this[key] as? Long ?: 0L
    }

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