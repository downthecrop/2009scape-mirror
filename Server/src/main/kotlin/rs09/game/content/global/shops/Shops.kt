package rs09.game.content.global.shops

import api.*
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.crafting.TanningProduct
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.rs09.consts.Components
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.ServerConstants
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType
import rs09.game.interaction.InterfaceListener
import rs09.game.system.SystemLogger
import rs09.game.system.command.Privilege
import rs09.tools.END_DIALOGUE
import java.io.FileReader

/**
 * The "controller" class for shops. Handles opening shops from various NPC interactions and updating stock, etc.
 * Note: If you wish to enable personalized shops, add and set the personalized_shops entry to true in the world section of the server config.
 * ex:
 * ```toml
 * [world]
 * personalized_shops = true
 * ```
 */
class Shops : StartupListener, TickListener, InteractionListener, InterfaceListener, Commands {
    companion object {
        @JvmStatic val personalizedShops = "world.personalized_shops"
        @JvmStatic val shopsById = HashMap<Int,Shop>()
        @JvmStatic val shopsByNpc = HashMap<Int,Shop>()

        @JvmStatic fun openId(player: Player, id: Int)
        {
            shopsById[id]?.openFor(player)
        }

        fun logShop(msg: String)
        {
            SystemLogger.logInfo(this::class.java, "[SHOPS] $msg")
        }

        fun parseStock(stock: String, id: Int): ArrayList<ShopItem>{
            val items = ArrayList<ShopItem>()
            val idsInStock = HashMap<Int, Boolean>()
            if(stock.isEmpty()){
                return items
            }
            stock.split('-').map {
                val tokens = it.replace("{", "").replace("}", "").split(",".toRegex()).toTypedArray()
                var amount = tokens[1].trim()
                if(amount == "inf")
                    amount = "-1"
                val item = tokens[0].toInt()
                if(idsInStock[item] != null) {
                    SystemLogger.logWarn(this::class.java, "[SHOPS] MALFORMED STOCK IN SHOP ID $id FOR ITEM $item")
                    items.forEach { if(it.itemId == item) {
                        it.amount += amount.toInt()
                        return@map
                    }}
                } else {
                    items.add(ShopItem(item, amount.toInt(), tokens.getOrNull(2)?.toIntOrNull() ?: 100))
                    idsInStock[item] = true
                }
            }
            return items
        }
    }

    override fun startup() {
        val path = ServerConstants.CONFIG_PATH + "shops.json"
        var shopCount = 0
        logShop("Using JSON path: $path")

        val reader = FileReader(path)
        val data = JSONParser().parse(reader) as JSONArray

        for(rawShop in data)
        {
            var shop: Shop?
            val shopData = rawShop as JSONObject
            val id = shopData["id"].toString().toInt()
            val title = shopData["title"].toString()
            val general = shopData["general_store"].toString().toBoolean()
            val stock = parseStock(shopData["stock"].toString(), id).toTypedArray()
            val npcs = if(shopData["npcs"].toString().isNotBlank()) shopData["npcs"].toString().split(",").map { it.toInt() }.toIntArray() else intArrayOf()
            val currency = shopData["currency"].toString().toInt()
            val highAlch = shopData["high_alch"].toString() == "1"
            shop = Shop(title, stock, general, currency, highAlch)

            npcs.map { shopsByNpc[it] = shop }
            shopsById[id] = shop
            ++shopCount
        }

        logShop("Parsed $shopCount shops.")
    }

    override fun tick() {
        shopsById.values.forEach(Shop::restock)
    }

    override fun defineListeners() {
        on(IntType.NPC, "trade", "shop"){player, node ->
            val npc = node as NPC
            if (npc.id == 2824) {
                TanningProduct.open(player, 2824)
                return@on true
            }
            if (npc.id == 7601) {
                openInterface(player, 732)
                return@on true
            }
            shopsByNpc[npc.id]?.openFor(player) ?: return@on false
            return@on true
        }

        on(NPCs.SIEGFRIED_ERKLE_933, IntType.NPC, "trade"){ player, node ->
            val points = getQP(player)
            if(points < 40){
                sendNPCDialogue(player, NPCs.SIEGFRIED_ERKLE_933, "I'm sorry, adventurer, but you need 40 quest points to buy from me.")
                return@on true
            }
            shopsByNpc[node.id]?.openFor(player)
            return@on true
        }

        on(NPCs.FUR_TRADER_1316, IntType.NPC, "trade") { player, node ->
            if (!isQuestComplete(player, "Fremennik Trials")) {
                sendNPCDialogue(player, NPCs.FUR_TRADER_1316, "I don't sell to outerlanders.", FacialExpression.ANNOYED).also { END_DIALOGUE }
            } else {
                shopsByNpc[node.id]?.openFor(player)
            }
            return@on true
        }
    }

    override fun defineInterfaceListeners() {
        on(Components.SHOP_TEMPLATE_620){player, _, opcode, buttonID, slot, _ ->
            val OP_VALUE = 155
            val OP_BUY_1 = 196
            val OP_BUY_5 = 124
            val OP_BUY_10 = 199
            val OP_BUY_X = 234
            val OP_EXAMINE = 9

            val shop = getAttribute<Shop?>(player, "shop", null) ?: return@on false
            val isMainStock = getAttribute(player, "shop-main", true)

            when(buttonID)
            {
                26 -> shop.showTab(player, false).also { return@on true }
                25 -> shop.showTab(player, true).also { return@on true }
                27,29 -> return@on true
            }

            val price = shop.getBuyPrice(player, slot)

            when(opcode)
            {
                OP_VALUE -> sendMessage(player, "${getItemName(if (isMainStock) shop.stock[slot].itemId else shop.playerStock[slot].id)}: This item currently costs ${price.amount} ${price.name.toLowerCase()}.")
                OP_BUY_1 -> shop.buy(player, slot, 1)
                OP_BUY_5 -> shop.buy(player, slot, 5)
                OP_BUY_10 -> shop.buy(player, slot, 10)
                OP_BUY_X -> sendInputDialogue(player, true, "Enter the amount to buy:"){value ->
                    val amt = value as Int
                    shop.buy(player, slot, amt)
                }
                OP_EXAMINE -> sendMessage(player, itemDefinition(if (isMainStock) shop.stock[slot].itemId else shop.playerStock[slot].id).examine)
            }

            return@on true
        }

        onOpen(Components.SHOP_TEMPLATE_SIDE_621) {player, _ ->
            val settings = IfaceSettingsBuilder()
                .enableOptions(0 until 9)
                .build()
            player.packetDispatch.sendIfaceSettings(settings, 0, 621, 0, 28)
            player.packetDispatch.sendRunScript(150, "IviiiIsssssssss", "", "", "", "", "Sell X", "Sell 10", "Sell 5", "Sell 1", "Value", -1, 0, 7, 4, 93, 621 shl 16);
            return@onOpen true
        }

        onClose(Components.SHOP_TEMPLATE_620) { player, _ ->
            val shop = getAttribute<Shop?>(player, "shop", null) ?: return@onClose true
            val listener = Shop.listenerInstances[player.details.uid] ?: return@onClose true

            if(getServerConfig().getBoolean(personalizedShops, false))
                shop.stockInstances[player.details.uid]?.listeners?.remove(listener)
            else
                shop.stockInstances[ServerConstants.SERVER_NAME.hashCode()]!!.listeners.remove(listener)

            shop.playerStock.listeners.remove(listener)
            player.interfaceManager.closeSingleTab()
            return@onClose true
        }

        on(Components.SHOP_TEMPLATE_SIDE_621){player, component, opcode, buttonID, slot, itemID ->
            val OP_VALUE = 155
            val OP_SELL_1 = 196
            val OP_SELL_5 = 124
            val OP_SELL_10 = 199
            val OP_SELL_X = 234

            val shop = getAttribute<Shop?>(player, "shop", null) ?: return@on false

            val (_,price) = shop.getSellPrice(player, slot)
            val def = itemDefinition(player.inventory[slot].id)

            val valueMsg = when {
                (price.amount == -1)
                || !def.isTradeable
                || def.id in intArrayOf(Items.COINS_995, Items.TOKKUL_6529, Items.ARCHERY_TICKET_1464)
                || def.hasDestroyAction()  -> "This shop will not buy that item."
                else -> "${player.inventory[slot].name}: This shop will buy this item for ${price.amount} ${price.name.lowercase()}."
            }

            when(opcode)
            {
                OP_VALUE -> sendMessage(player, valueMsg)
                OP_SELL_1 -> shop.sell(player, slot, 1)
                OP_SELL_5 -> shop.sell(player, slot, 5)
                OP_SELL_10 -> shop.sell(player, slot, 10)
                OP_SELL_X -> sendInputDialogue(player, true, "Enter the amount to sell:"){value ->
                    val amt = value as Int
                    shop.sell(player, slot, amt)
                }
            }

            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.NPC,"trade","shop"){ _, node ->
            val npc = node as NPC
            if (npc.getAttribute("facing_booth", false)) {
                val offsetX = npc.direction.stepX shl 1
                val offsetY = npc.direction.stepY shl 1
                return@setDest npc.location.transform(offsetX, offsetY, 0)
            }
            return@setDest node.location
        }
    }

    override fun defineCommands() {
        define("openshop", Privilege.ADMIN) { player, args ->
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