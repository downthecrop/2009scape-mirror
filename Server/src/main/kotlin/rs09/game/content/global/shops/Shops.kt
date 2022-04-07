package rs09.game.content.global.shops

import api.*
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.skill.crafting.TanningProduct
import core.game.node.item.Item
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.rs09.consts.NPCs
import rs09.ServerConstants
import rs09.game.interaction.InteractionListener
import rs09.game.system.SystemLogger
import rs09.tools.END_DIALOGUE
import java.io.FileReader

/**
 * The "controller" class for shops. Handles opening shops from various NPC interactions and updating stock, etc.
 */
class Shops : StartupListener, TickListener, InteractionListener, Commands {
    val shopsById = HashMap<Int,Shop>()
    val shopsByNpc = HashMap<Int,Shop>()

    override fun startup() {
        val path = ServerConstants.CONFIG_PATH + "shops.json"
        var shopCount = 0
        logShop("Using JSON path: $path")

        val reader = FileReader(path)
        val data = JSONParser().parse(reader) as JSONArray

        fun parseStock(stock: String): ArrayList<ShopItem>{
            val items = ArrayList<ShopItem>()
            if(stock.isEmpty()){
                return items
            }
            stock.split('-').map {
                val tokens = it.replace("{", "").replace("}", "").split(",".toRegex()).toTypedArray()
                var amount = tokens[1].trim()
                if(amount == "inf")
                    amount = "-1"
                items.add(ShopItem(tokens[0].toInt(), amount.toInt(), tokens.getOrNull(2)?.toIntOrNull() ?: 100))
            }
            return items
        }

        for(rawShop in data)
        {
            var shop: Shop?
            val shopData = rawShop as JSONObject
            val id = shopData["id"].toString().toInt()
            val title = shopData["title"].toString()
            val general = shopData["general_store"].toString().toBoolean()
            val stock = parseStock(shopData["stock"].toString()).toTypedArray()
            val npcs = if(shopData["npcs"].toString().isNotBlank()) shopData["npcs"].toString().split(",").map { it.toInt() }.toIntArray() else intArrayOf()
            val currency = shopData["currency"].toString().toInt()
            val highAlch = shopData["high_alch"].toString() == "1"
            shop = Shop(title, stock, general, currency, highAlch)

            npcs.map { shopsByNpc[it] = shop }
            shopsById[id] = shop
        }

        ++shopCount

        logShop("Parsed $shopCount shops.")
    }

    override fun tick() {
        shopsById.values.forEach(Shop::restock)
    }

    override fun defineListeners() {
        on(NPC, "trade", "shop"){player, node ->
            val npc = node as NPC
            if (npc.id == 2824) {
                TanningProduct.open(player, 2824)
                return@on true
            }
            if (npc.id == 7601) {
                openInterface(player, 732)
                return@on true
            }
            logShop("Opening shop [NPC: (${npc.name},${npc.id}), Player: ${player.username}]")
            return@on npc.openShop(player)
        }

        on(NPCs.SIEGFRIED_ERKLE_933, NPC, "trade"){ player, node ->
            val points = getQP(player)
            if(points < 40){
                sendNPCDialogue(player, NPCs.SIEGFRIED_ERKLE_933, "I'm sorry, adventurer, but you need 40 quest points to buy from me.")
                return@on true
            }
            node.asNpc().openShop(player)
            return@on true
        }

        on(NPCs.FUR_TRADER_1316, NPC,"trade") { player, node ->
            if (!isQuestComplete(player, "Fremennik Trials")) {
                sendNPCDialogue(player, NPCs.FUR_TRADER_1316, "I don't sell to outerlanders.", FacialExpression.ANNOYED).also { END_DIALOGUE }
            } else {
                END_DIALOGUE.also { node.asNpc().openShop(player) }
            }
            return@on true
        }

        on(NPCs.FISH_MONGER_1315, NPC,"trade") { player, node ->
            if (!isQuestComplete(player, "Fremennik Trials")) {
                sendNPCDialogue(player, NPCs.FISH_MONGER_1315, "I don't sell to outerlanders.", FacialExpression.ANNOYED).also { END_DIALOGUE }
            } else {
                END_DIALOGUE.also { node.asNpc().openShop(player) }
            }
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(NPC,"trade","shop"){_,node ->
            val npc = node as NPC
            if (npc.getAttribute("facing_booth", false)) {
                val offsetX = npc.direction.stepX shl 1
                val offsetY = npc.direction.stepY shl 1
                return@setDest npc.location.transform(offsetX, offsetY, 0)
            }
            return@setDest node.location
        }
    }

    fun logShop(msg: String)
    {
        SystemLogger.logInfo("[SHOPS] $msg")
    }

    override fun defineCommands() {
        define("openshop") { player, args ->
            if(args.size < 2) reject(player, "Usage: ::openshop shopId")
            val shopId = args[1].toInt()
            shopsById[shopId]?.openFor(player)
        }

        define("shopscript") { player, args ->
            val arg1 = args[1].toInt()
            player.packetDispatch.sendRunScript(25, "vg", arg1, 92) //Run CS2 script 25, with args 868? and 92(our container id)
        }
    }
}