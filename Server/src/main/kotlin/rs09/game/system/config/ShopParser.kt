package rs09.game.system.config

import core.game.content.global.shop.Shop
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import rs09.ServerConstants
import rs09.game.system.SystemLogger
import java.io.FileReader

class ShopParser{
    val parser = JSONParser()
    var reader: FileReader? = null

    companion object{
        val SHOPS = HashMap<Int, Shop>()
        val UID_SHOPS = HashMap<Int,Shop>()
        fun openUid(player: Player,uid: Int): Boolean {
            val shop = UID_SHOPS[uid] ?: return false;
            shop.open(player);
            return true;
        }
    }

    fun load(){
        var count = 0
        reader = FileReader(ServerConstants.CONFIG_PATH + "shops.json")
        val configlist = parser.parse(reader) as JSONArray
        for(config in configlist){
            var shop: Shop?
            val e = config as JSONObject
            val id = e["id"].toString().toInt()
            val title = e["title"].toString()
            val general = e["general_store"].toString().toBoolean()
            val stock = parseStock(e["stock"].toString()).toTypedArray()
            val npcs = if(e["npcs"].toString().isNotBlank()) e["npcs"].toString().split(",").map { it.toInt() }.toIntArray() else intArrayOf()
            val currency = e["currency"].toString().toInt()
            val highAlch = e["high_alch"].toString() == "1"
            if(general && stock.isEmpty()){
                shop = Shop(title,true,currency,highAlch)
            } else {
                shop = Shop(title, stock, general, currency, highAlch)
            }
            if(npcs.isNotEmpty()){
                npcs.map { SHOPS[it] = shop }
            } else {
                UID_SHOPS[id] = shop
            }
            count++
        }
        SystemLogger.logInfo("Parsed $count shops.")
    }

    fun parseStock(stock: String): ArrayList<Item>{
        val items = ArrayList<Item>()
        if(stock.isEmpty()){
            return items
        }
        stock.split('-').map {
            val tokens = it.replace("{", "").replace("}", "").split(",".toRegex()).toTypedArray()
            var amount = tokens[1].trim()
            if(amount == "inf")
                amount = "-1"
            items.add(Item(tokens[0].trim().toInt(),amount.toInt()))
        }
        return items
    }
}