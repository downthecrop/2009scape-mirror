package core.game.system.config

import core.game.node.entity.player.Player
import org.json.simple.JSONArray
import org.json.simple.parser.JSONParser
import core.ServerConstants
import core.api.log
import core.tools.Log
import core.tools.SystemLogger
import java.io.FileReader

class ShopParser{
    val parser = JSONParser()
    var reader: FileReader? = null

    companion object {
/*        val SHOPS = HashMap<Int, Shop>()
        val UID_SHOPS = HashMap<Int,Shop>()*/
        fun openUid(player: Player,uid: Int): Boolean {
/*            val shop = UID_SHOPS[uid] ?: return false;
            shop.open(player);*/
            return true
}
    }

    fun load(){
        var count = 0
        reader = FileReader(ServerConstants.CONFIG_PATH + "shops.json")
        val configlist = parser.parse(reader) as JSONArray
        for(config in configlist){

            count++
        }
        log(this::class.java, Log.FINE,  "Parsed $count shops.")
    }
}