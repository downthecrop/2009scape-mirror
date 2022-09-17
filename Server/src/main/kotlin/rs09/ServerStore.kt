package rs09

import api.PersistWorld
import api.ShutdownListener
import api.StartupListener
import api.getItemName
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import rs09.ServerStore.Companion.addToList
import rs09.game.system.SystemLogger
import rs09.game.system.SystemLogger.logShutdown
import rs09.game.system.SystemLogger.logStartup
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import javax.script.ScriptEngineManager

class ServerStore : PersistWorld {
    override fun parse() {
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
                SystemLogger.logErr(this::class.java, "Failed parsing ${storeFile.name} - stack trace below.")
                e.printStackTrace()
                return@forEach
            }
        }

        logStartup("Initialized $counter store files.")
    }

    override fun save() {
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

        inline fun <reified T> JSONObject.getList(key: String) : List<T> {
            val array = this[key] as? JSONArray ?: JSONArray()
            val list = ArrayList<T>()
            for(element in array) list.add(element as T)
            return list
        }

        fun JSONObject.addToList(key: String, value: Any) {
            val array = this.getOrPut(key) {JSONArray()} as JSONArray
            array.add(value)
        }

        /** NPCItemMemory
         * These next methods handle the NPCItemMemory database. these are server-stored JSON objects,
         * which are based on an npc and an item.
         * each NPC can have a JSON object for itself for any item.
         * the NPC Item object can store integer values for individual players, using their names as keys.
         * the first methods handle the serverstore filename and accessing the JSON object.
         * these methods are wrapped by more convenient ones that allow access for a particular player, see below.
         */
        fun NPCItemFilename(npc: Int, item: Int, period: String = "daily"): String {
            val itemName = getItemName(item).lowercase().replace(" ","-")
            val npcName = NPC(npc).name.lowercase()
            return "$period-$npcName-$itemName"
        }
        fun NPCItemMemory(npc: Int, item: Int, period: String = "daily"): JSONObject {
            return getArchive(NPCItemFilename(npc,item,period))
        }

        /** NPCItemMemory Player Access
         * These next functions handle individual player access to the NPC Item Memory database.
         * each of the functions has at least 3 mandatory arguments: npc, item, player.
         * These functions can be used to allow players to access a daily- or weekly-reset stock of a particular item overseen by a particular NPC.
         *
         * note that none of these functions perform tangible actions on the player.
         * whoever calls these functions is responsible for handling tangible actions like putting items in an inventory.
         * these functions simply return and update the NPCItemMemory database numbers in useful ways.
         */

        /** getNPCItemStock:
         * gets the available stock of a particular item at a particular NPC for a particular player.
         */
        fun getNPCItemStock(npc: Int, item: Int, limit: Int, player: Player, period: String = "daily"): Int {
            val itemMemory = NPCItemMemory(npc,item)
            val key = player.name
            var stock = limit-itemMemory.getInt(key)
            stock = maxOf(stock,0)
            return stock
        }
        /** getNPCItemAmount:
         * gets the usable amount of a particular item from a particular npc for a particular player.
         * this is essentially just a requested amount cut to conform to stock and nonnegative requirements.
         * what this function does is it takes a requested amount, cuts it down to be equal to available stock if
         * available stock is less than the requested amount, and then additionally sets a minimum of 0.
         */
        fun getNPCItemAmount(npc: Int, item: Int, limit: Int, player: Player, amount: Int, period: String = "daily"): Int {
            val stock = getNPCItemStock(npc,item,limit,player,period)
            var realamount = minOf(amount,stock)
            realamount = maxOf(realamount,0)
            return realamount
        }
        /** addNPCItemAmount:
         * this function updates the NPC Item Memory database entry for a particular player.
         * it is intended to be called after all the actions that use the number have been successfully completed.
         * this function will *add* the amount argument to the current npc item memory entry for that player. this means that
         * the amount argument is the amount to add, not the absolute final amount.
         * this function will handle correctly setting the limit and any other required semantics.
         */
        fun addNPCItemAmount(npc: Int, item: Int, limit: Int, player: Player, amount: Int, period: String =  "daily") {
            val itemMemory = NPCItemMemory(npc,item,period)
            val key = player.name
            var realamount = itemMemory.getInt(key) + amount
            realamount = minOf(realamount,limit)
            realamount = maxOf(realamount,0)
            itemMemory[key] = realamount
        }
    }
}

