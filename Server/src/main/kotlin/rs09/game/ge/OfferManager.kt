package rs09.game.ge

import api.ContentAPI
import rs09.ServerConstants
import core.cache.def.impl.ItemDefinition
import core.game.content.eco.EcoStatus
import core.game.content.eco.EconomyManagement
import core.game.ge.BuyingLimitation
import core.game.ge.GrandExchangeDatabase
import core.game.ge.OfferState
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.PlayerDetails
import core.game.node.entity.player.link.audio.Audio
import core.game.node.item.Item
import rs09.game.system.SystemLogger
import core.game.system.task.Pulse
import rs09.game.world.GameWorld
import core.game.world.callback.CallBack
import rs09.game.world.repository.Repository
import core.net.packet.PacketRepository
import core.net.packet.context.ContainerContext
import core.net.packet.out.ContainerPacket
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import rs09.game.ai.AIPlayer
import rs09.game.system.config.ItemConfigParser
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.lang.Integer.max
import java.lang.Integer.min
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import javax.script.ScriptEngineManager
import kotlin.collections.ArrayList

object OfferManager {
    /**
     * The update notification.
     */
    private const val UPDATE_NOTIFICATION = "One or more of your grand exchange offers have been updated."

    /**
     * The database path.
     */
    private val DB_PATH = ServerConstants.GRAND_EXCHANGE_DATA_PATH + "offer_dispatch.json"

    /**
     * Bot DB path
     */
    private val BOT_DB_PATH = ServerConstants.GRAND_EXCHANGE_DATA_PATH + "bot_offers.json"

    /**
     * The offset of the offer UIDs.
     */
    private var offsetUID: Long = 1

    /**
     * The mapping of all current offers. Stored in multiple maps.
     *
     * First map is offsetID, Offer
     * Second is itemID, offsetID, Offer
     * Final is playerID, offsetID, Offer
     */
    @JvmStatic
    val OFFER_MAPPING: MutableMap<Long, GrandExchangeOffer> = HashMap()
    val OFFERS_BY_ITEMID: MutableMap<Int, MutableList<GrandExchangeOffer>> = HashMap()
    private val GE_OFFER_LOCK = ReentrantLock()

    /**
     * Bot offers are sorted by itemID.
     * the second int shows the offer amount. Negative is buying positive selling.
     */
    public val BOT_OFFERS: HashMap<Int, Int> = HashMap()

    /**
     * If the database should be dumped.
     */
    public var dumpDatabase = false

    /**
     * Initializes the Grand Exchange.
     */
    fun init() {
        GE_OFFER_LOCK.lock()
        val file = File(DB_PATH)

        if(file.exists() && file.length() != 0L) {
            val parser = JSONParser()
            val reader: FileReader? = FileReader(DB_PATH)
            val saveFile = parser.parse(reader) as JSONObject

            offsetUID = saveFile["offsetUID"].toString().toLong()

            if (saveFile.containsKey("offers")) {
                val offers = saveFile["offers"] as JSONArray
                for (offer in offers) {
                    val o = offer as JSONObject
                    // Copy all the bot offers from the file
                    if (o["playerUID"].toString().toInt() == 0) {
                        addBotOffer(o["itemId"].toString().toInt(), o["amount"].toString().toInt() - o["completedAmount"].toString().toInt())
                    }
                    val no = GrandExchangeOffer()
                    no.itemID = o["itemId"].toString().toInt()
                    no.sell = o["sale"] as Boolean
                    no.offeredValue = o["offeredValue"].toString().toInt()
                    no.amount = o["amount"].toString().toInt()
                    no.timeStamp = o["timeStamp"].toString().toLong()
                    no.uid = o["uid"].toString().toLong()
                    no.completedAmount = o["completedAmount"].toString().toInt()
                    no.playerUID = o["playerUID"].toString().toInt()
                    no.offerState = OfferState.values()[o["offerState"].toString().toInt()]
                    no.totalCoinExchange = o["totalCoinExchange"].toString().toInt()
                    val withdrawData = o["withdrawItems"] as JSONArray
                    for ((index, data) in withdrawData.withIndex()) {
                        val item = data as JSONObject
                        val it = Item(item["id"].toString().toInt(), item["amount"].toString().toInt())
                        no.withdraw[index] = it
                    }
                    addEntry(no)
                }
            }
        }

        if(File(BOT_DB_PATH).exists()) {
            try {
                val botReader: FileReader? = FileReader(BOT_DB_PATH)
                val botSave = JSONParser().parse(botReader) as JSONObject
                if (botSave.containsKey("offers")) {
                    val offers = botSave["offers"] as JSONArray
                    for (offer in offers) {
                        val o = offer as JSONObject
                        addBotOffer(o["item"].toString().toInt(), o["qty"].toString().toInt())
                    }
                }
            } catch (e: IOException) {
                SystemLogger.logWarn("Unable to load bot offers. Perhaps it doesn't exist?")
            }
        }
        GE_OFFER_LOCK.unlock()
    }

    fun update(){
        for (offer in OFFER_MAPPING.values) {
            if (offer.isActive) {
                updateOffer(offer)
            }
        }
    }

    fun buyFromBots(offer: GrandExchangeOffer) {
        if (BOT_OFFERS[offer.itemID] == null) {
            return
        }
        val botPrice = getRecommendedPrice(offer.itemID, true)
        if (offer.offeredValue < botPrice) {
            return
        }
        val amount = min(BOT_OFFERS[offer.itemID]!!, getBuylimitAmount(offer))
        val botOffer = GrandExchangeOffer()
        botOffer.sell = true
        botOffer.amount = amount
        botOffer.offerState = OfferState.REGISTERED
        botOffer.offeredValue = botPrice
        exchange(offer, botOffer)
        BOT_OFFERS[offer.itemID] = BOT_OFFERS[offer.itemID]!! - amount
    }

    private fun buyFromBotsWithItem(itemID: Int) {
        if (OFFERS_BY_ITEMID[itemID] == null || BOT_OFFERS[itemID] == null) {
            return
        }
        for (trade in OFFERS_BY_ITEMID[itemID]!!) {
            if (!trade.sell) {
                buyFromBots(trade)
            }
        }
    }

    fun addBotOffer(itemID: Int, qty: Int): Boolean {
        if (GrandExchangeDatabase.getDatabase()[itemID] == null) {
            SystemLogger.logWarn("Bot attempted to sell invalid item $itemID")
            return false
        }

        if (BOT_OFFERS[itemID] == null) {
            BOT_OFFERS[itemID] = qty
        } else {
            BOT_OFFERS[itemID] = (qty + BOT_OFFERS[itemID]!!)
        }
        buyFromBotsWithItem(itemID)
        return true
    }

    fun amtBotsSelling(itemID: Int): Int {
        if (BOT_OFFERS[itemID] == null) {
            return 0
        }
        if (BOT_OFFERS[itemID]!! <= 0) {
            return 0
        }
        return BOT_OFFERS[itemID]!!
    }

    fun setIndex(offerID: Long, idx: Int) {
        if (!OFFER_MAPPING.containsKey(offerID)) {
            println("ERROR. GE Entry $offerID not found in database. Playerdata may be corrupted.")
            return
        }
        OFFER_MAPPING[offerID]!!.index = idx
    }

    fun removeEntry(offer: GrandExchangeOffer): Boolean{
        println("REMOVING ENTRY of ID " + offer.itemID)
        GE_OFFER_LOCK.lock()
        if (!OFFER_MAPPING.containsKey(offer.uid)){
            GE_OFFER_LOCK.unlock()
            return false
        }
        OFFER_MAPPING.remove(offer.uid)
        OFFERS_BY_ITEMID[offer.itemID]!!.remove(offer)
        GE_OFFER_LOCK.unlock()
        return true
    }

    fun addEntry(offer: GrandExchangeOffer){
        GE_OFFER_LOCK.lock()
        OFFER_MAPPING[offer.uid] = offer
        if (!OFFERS_BY_ITEMID.containsKey(offer.itemID)) {
            OFFERS_BY_ITEMID[offer.itemID] = mutableListOf()
        }
        OFFERS_BY_ITEMID[offer.itemID]!!.add(offer)
        GE_OFFER_LOCK.unlock()
    }

    fun getQuantitySoldForItem(item: Int): Int {
        var qty = 0
        val offs = getOffersForItem(item)
        for (o in offs) {
            if (o.sell) {
                qty += o.amountLeft
            }
        }
        qty += amtBotsSelling(item)
        return qty
    }

    @JvmStatic
    fun getOffersForItem(item: Int): MutableList<GrandExchangeOffer> {
        if (OFFERS_BY_ITEMID.containsKey(item)) {
            return OFFERS_BY_ITEMID[item]!!
        }
        return mutableListOf()
    }

    @JvmStatic
    fun save(){
        GE_OFFER_LOCK.lock()
        val root = JSONObject()
        val offers = JSONArray()

        if(OFFER_MAPPING.isEmpty() && BOT_OFFERS.isEmpty()){
            return
        }

        for(entry in OFFER_MAPPING){
            val offer = entry.value
            if (offer.offerState == OfferState.REMOVED || entry.value.playerUID == PlayerDetails.getDetails("2009scape").uid) {
                continue
            }
            val o = JSONObject()
            o["uid"] = entry.key.toString()
            o["itemId"] = offer.itemID.toString()
            o["sale"] = offer.sell
            o["amount"] = offer.amount.toString()
            o["completedAmount"] = offer.completedAmount.toString()
            o["offeredValue"] = offer.offeredValue.toString()
            o["timeStamp"] = offer.timeStamp.toString()
            o["offerState"] = offer.offerState.ordinal.toString()
            o["totalCoinExchange"] = offer.totalCoinExchange.toString()
            o["playerUID"] = offer.playerUID.toString()
            val withdrawItems = JSONArray()
            for(item in offer.withdraw){
                item ?: continue
                val it = JSONObject()
                it["id"] = item.id.toString()
                it["amount"] = item.amount.toString()
                withdrawItems.add(it)
            }
            o["withdrawItems"] = withdrawItems
            offers.add(o)
        }
        root["offsetUID"] = offsetUID.toString()
        root["offers"] = offers

        val manager = ScriptEngineManager()
        val scriptEngine = manager.getEngineByName("JavaScript")
        scriptEngine.put("jsonString", root.toJSONString())
        scriptEngine.eval("result = JSON.stringify(JSON.parse(jsonString), null, 2)")
        val prettyPrintedJson = scriptEngine["result"] as String

        val botRoot = JSONObject()
        val botOffers = JSONArray()

        for ((item, qty) in BOT_OFFERS) {
            val o = JSONObject()
            o["item"] = item
            o["qty"] = qty
            botOffers.add(o)
        }
        botRoot["offers"] = botOffers

        scriptEngine.put("jsonString", botRoot.toJSONString())
        scriptEngine.eval("result = JSON.stringify(JSON.parse(jsonString), null, 2)")
        val botJson = scriptEngine["result"] as String


        try {
            FileWriter(DB_PATH).use { file ->
                file.write(prettyPrintedJson)
                file.flush()
                file.close()
            }
            FileWriter(BOT_DB_PATH).use { file ->
                file.write(botJson)
                file.flush()
                file.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        GE_OFFER_LOCK.unlock()
    }

    /**
     * Dispatches an offer.
     * @param player The player.
     * @param offer The grand exchange offer.
     * @return `True` if successful.
     */
    @JvmStatic
    fun dispatch(player: Player, offer: GrandExchangeOffer): Boolean {
        if (offer.amount < 1) {
            player.packetDispatch.sendMessage("You must choose the quantity you wish to buy!")
            println("amountthing")
            return false
        }
        if (offer.offeredValue < 1) {
            player.packetDispatch.sendMessage("You must choose the price you wish to buy for!")
            println("pricethng")
            return false
        }
        if (offer.offerState != OfferState.PENDING || offer.uid != 0L) {
            println("pendingthing")
            return false
        }
        if (player.isArtificial) {
            offer.playerUID = PlayerDetails.getDetails("2009scape").uid
            // Repository.sendNews("2009scape wants " + offer.amount + " " + ItemDefinition.forId(offer.itemID).name.toLowerCase() + " for " + offer.offeredValue + "each.")
        } else {
            offer.playerUID = player.details.uid
        }
        offer.uid = nextUID()
        offer.offerState = OfferState.REGISTERED
        addEntry(offer)
        offer.timeStamp = System.currentTimeMillis()
        player.playerGrandExchange.update(offer)
        if (offer.sell) {
            Repository.sendNews(player.username + " just offered " + offer.amount + " " + ItemDefinition.forId(offer.itemID).name.toLowerCase() + " on the GE.")
        }
        if(player !is AIPlayer) {
            SystemLogger.logTrade("[GE] ${player.username} ${if (offer.sell) "listed for sale" else "listed buy offer for"} ${offer.amount} ${ItemDefinition.forId(offer.itemID).name.toLowerCase()}")
        }
        dumpDatabase = true
        return true
    }

    /**
     * Updates the offer.
     * @param offer The G.E. offer to update.
     */
    @JvmStatic
    fun updateOffer(offer: GrandExchangeOffer) {
        if (!offer.isActive) {
            return
        }
        GE_OFFER_LOCK.lock()
        for (o in OFFERS_BY_ITEMID[offer.itemID]!!) {
            if (o.sell != offer.sell && o.isActive) {
                exchange(offer, o)
                if (offer.offerState == OfferState.COMPLETED) {
                    break
                }
            }
        }
        buyFromBots(offer)
        GE_OFFER_LOCK.unlock()
    }

    private fun getBuylimitAmount(offer: GrandExchangeOffer): Int {
        var left = offer.amountLeft
        if (!offer.sell && left > 0) {
            val maximum = BuyingLimitation.getMaximumBuy(offer.itemID, offer.playerUID)
            if (left >= maximum) {
                left = maximum
                offer.isLimitation = true
            }
        }
        return left
    }

    /**
     * Exchanges between 2 offers.
     * @param offer The grand exchange offer to update.
     * @param o The other offer to exchange with.
     */
    private fun exchange(offer: GrandExchangeOffer, o: GrandExchangeOffer) {
        if (o.sell == offer.sell) {
            return
        }
        if (offer.sell && o.offeredValue < offer.offeredValue || !offer.sell && o.offeredValue > offer.offeredValue) {
            return
        }
        var amount = min(getBuylimitAmount(offer), getBuylimitAmount(o))
        if (amount < 1) {
            return
        }
        var coinDifference = if (offer.sell) o.offeredValue - offer.offeredValue else offer.offeredValue - o.offeredValue
        if (coinDifference < 0) {
            return
        }
        if (EconomyManagement.getEcoState() == EcoStatus.DRAINING) {
            coinDifference *= (1.0 - EconomyManagement.getModificationRate()).toInt()
        }
        offer.completedAmount = offer.completedAmount + amount
        o.completedAmount = o.completedAmount + amount
        offer.offerState = if (offer.amountLeft < 1) OfferState.COMPLETED else OfferState.UPDATED
        o.offerState = if (o.amountLeft < 1) OfferState.COMPLETED else OfferState.UPDATED
        if (offer.sell) {
            if (offer.amountLeft < 1 && offer.player != null) {
                offer.player!!.audioManager.send(Audio(4042, 1, 1))
            }
            addWithdraw(offer,995, amount * offer.offeredValue)
            addWithdraw(o, o.itemID, amount)
            BuyingLimitation.updateBoughtAmount(o.itemID, o.playerUID, amount)
        } else {
            if (o.amountLeft < 1 && o.player != null) {
                o.player!!.audioManager.send(Audio(4042, 1, 1))
            }
            addWithdraw(offer, offer.itemID, amount)
            addWithdraw(o, 995, amount * o.offeredValue)
            BuyingLimitation.updateBoughtAmount(offer.itemID, offer.playerUID, amount)
        }
        if (coinDifference > 0) {
            if (offer.sell) {
                addWithdraw(o, 995, coinDifference * amount)
            } else {
                addWithdraw(offer, 995, coinDifference * amount)
            }
        }
        GrandExchangeDatabase.getDatabase()[o.itemID]?.influenceValue(o.offeredValue)
        offer.player?.packetDispatch?.sendMessage(UPDATE_NOTIFICATION)
        o.player?.packetDispatch?.sendMessage(UPDATE_NOTIFICATION)
        o.player?.playerGrandExchange?.update(o)
        offer.player?.playerGrandExchange?.update(offer)
        dumpDatabase = true
    }

    /**
     * Adds a new item to withdraw.
     * @param itemId The item id.
     * @param amount The amount to add.
     * @param abort If the item is added due to abort.
     */
    fun addWithdraw(offer: GrandExchangeOffer, itemId: Int, amount: Int, abort: Boolean = false) {
        if (!abort) {
            if (offer.sell) {
                if (itemId == 995) {
                    offer.totalCoinExchange += amount
                }
            } else {
                if (itemId == 995) {
                    offer.totalCoinExchange -= amount
                } else {
                    offer.totalCoinExchange += offer.offeredValue * amount
                }
            }
        }
        for (i in offer.withdraw.indices) {
            if (offer.withdraw[i] == null) {
                offer.withdraw[i] = Item(itemId, amount)
                break
            }
            if (offer.withdraw[i]!!.id == itemId) {
                offer.withdraw[i]!!.amount = offer.withdraw[i]!!.amount + amount
                break
            }
        }
        if (offer.player != null) {
            PacketRepository.send(
                ContainerPacket::class.java,
                ContainerContext(offer.player, -1, -1757, 523 + offer.index, offer.withdraw, false)
            )
        }
    }

    /**
     * Gets the next UID.
     * @return The UID.
     */
    private fun nextUID(): Long {
        val id = offsetUID++
        return if (id == 0L) {
            nextUID()
        } else id
    }

    private fun getItemDefPrice(itemID: Int): Int {
        return max(ContentAPI.itemDefinition(itemID).getConfiguration(ItemConfigParser.GE_PRICE) ?: 0, ContentAPI.itemDefinition(itemID).value)
    }

    @JvmStatic
    fun getRecommendedPrice(itemID: Int, from_bot: Boolean = false): Int {
        val base = max(GrandExchangeDatabase.getDatabase()[itemID]?.value ?: 0, getItemDefPrice(itemID))
        return if(from_bot) (base + (base * 0.1).toInt())
        else base
    }
}
