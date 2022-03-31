package rs09.game.ge

import api.*
import core.cache.def.impl.ItemDefinition
import core.game.component.CloseEvent
import core.game.component.Component
import core.game.component.InterfaceType
import core.game.container.Container
import core.game.container.ContainerEvent
import core.game.container.ContainerListener
import core.game.container.access.BitregisterAssembler
import core.game.container.access.InterfaceContainer
import core.game.ge.GEItemSet
import core.game.ge.GrandExchangeDatabase
import core.game.ge.GrandExchangeEntry
import core.game.ge.OfferState
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.audio.Audio
import core.game.node.item.Item
import core.game.system.monitor.PlayerMonitor
import core.net.packet.PacketRepository
import core.net.packet.context.ConfigContext
import core.net.packet.context.ContainerContext
import core.net.packet.out.Config
import core.net.packet.out.ContainerPacket
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.rs09.consts.Components
import org.rs09.consts.Items
import rs09.game.ai.AIPlayer
import rs09.game.system.SystemLogger
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


/**
 * Handles the grand exchange interfaces for the player.
 *
 * @author Emperor
 * @author Angle
 */

class GrandExchangeRecords(private val player: Player? = null) : PersistPlayer, LoginListener {
    var history = arrayOfNulls<GrandExchangeOffer>(5)
    val offerRecords = arrayOfNulls<OfferRecord>(6)

    override fun login(player: Player) {
        val instance = GrandExchangeRecords(player)
        instance.init()
        player.setAttribute("ge-records", instance)
    }

    override fun parsePlayer(player: Player, data: JSONObject) {
        /**
         * Parse history from JSON
         */
        val historyRaw = data["ge-history"]
        if(historyRaw != null){
            val history = historyRaw as JSONArray
            for (i in history.indices) {
                val offer = history[i] as JSONObject
                val o = GrandExchangeOffer()
                o.itemID = offer["itemId"].toString().toInt()
                o.sell = offer["isSell"] as Boolean
                o.totalCoinExchange = (offer["totalCoinExchange"].toString().toInt())
                o.completedAmount = (offer["completedAmount"].toString().toInt())
                getInstance(player).history[i] = o
            }
        }
    }

    override fun savePlayer(player: Player, save: JSONObject) {
        /**
         * Save history to JSON
         */
        val history = JSONArray()
        getInstance(player).history.map {
            if(it != null){
                val historyEntry = JSONObject()
                historyEntry["isSell"] = it.sell
                historyEntry["itemId"] = it.itemID.toString()
                historyEntry["totalCoinExchange"] = it.totalCoinExchange.toString()
                historyEntry["completedAmount"] = it.completedAmount.toString()
                history.add(historyEntry)
                SystemLogger.logInfo("Adding history entry for ${it.itemID}")
            }
        }
        save["ge-history"] = history
    }

    /**
     * Opens the collection box.
     */
    fun openCollectionBox() {
        if (!player!!.bankPinManager.isUnlocked) {
            player.bankPinManager.openType(3)
            return
        }
        player.interfaceManager.openComponent(Components.STOCKCOLLECT_109)
        player.packetDispatch.sendAccessMask(6, 18, 109, 0, 2)
        player.packetDispatch.sendAccessMask(6, 23, 109, 0, 2)
        player.packetDispatch.sendAccessMask(6, 28, 109, 0, 2)
        player.packetDispatch.sendAccessMask(6, 36, 109, 0, 2)
        player.packetDispatch.sendAccessMask(6, 44, 109, 0, 2)
        player.packetDispatch.sendAccessMask(6, 52, 109, 0, 2)

        visualizeRecords()
    }

    fun visualizeRecords()
    {
        val conn = GEDB.connect()
        val stmt = conn.createStatement()

        val sb = StringBuilder()

        for (record in offerRecords) {
            if (record != null) {
                val offer_raw = stmt.executeQuery("select * from player_offers where uid = ${record.uid}")
                if(offer_raw.next())
                {
                    val offer = GrandExchangeOffer.fromQuery(offer_raw)
                    if(offer.offerState == OfferState.REMOVED) continue
                    offer.index = record.slot
                    offer.visualize(player)
                }
            }
        }
        stmt.close()
    }

    fun getOffer(index: Int) : GrandExchangeOffer?
    {
        if(index == -1) return getOffer(null)
        return getOffer(offerRecords[index])
    }

    fun getOffer(record: OfferRecord?) : GrandExchangeOffer?
    {
        record ?: return null
        val conn = GEDB.connect()
        val stmt = conn.createStatement()
        val offer_raw = stmt.executeQuery("select * from player_offers where uid = ${record.uid}")
        if(offer_raw.next())
        {
            val offer = GrandExchangeOffer.fromQuery(offer_raw)
            offer.index = record.slot
            stmt.close()
            return offer
        }
        stmt.close()
        return null
    }

    /**
     * Opens the history log.
     *
     * @param p The player to open it for.
     */
    fun openHistoryLog(p: Player) {
        p.interfaceManager.open(Component(643))
        for (i in history.indices) {
            val o: GrandExchangeOffer? = history[i]
            if (o == null) {
                for (j in 0..3) {
                    p.packetDispatch.sendString("-", 643, 25 + i + j * 5)
                }
                continue
            }
            p.packetDispatch.sendString(if (o.sell) "You sold" else "You bought", 643, 25 + i)
            p.packetDispatch.sendString(
                    NumberFormat.getNumberInstance(Locale.US).format(o.completedAmount.toLong()),
                    643,
                    30 + i
            )
            p.packetDispatch.sendString(ItemDefinition.forId(o.itemID).name, 643, 35 + i)
            p.packetDispatch.sendString(
                    NumberFormat.getNumberInstance(Locale.US).format(o.totalCoinExchange.toLong()) + " gp", 643, 40 + i
            )
        }
    }

    fun parse(geData: JSONObject) {
        /**
         * Read offers from the database
         */
        val conn = GEDB.connect()
        val stmt = conn.createStatement()
        val offer_records = stmt.executeQuery("SELECT * from player_offers where player_uid = ${player!!.details.uid} AND offer_state < 6")

        val needsIndex = ArrayDeque<GrandExchangeOffer>()

        while (offer_records.next()) {
            val offer = GrandExchangeOffer.fromQuery(offer_records)
            if (offer.index == -1) //used to index old (converted from JSON) offers
                needsIndex.push(offer)
            else
                offerRecords[offer.index] = OfferRecord(offer.uid, offer.index)
        }
        stmt.close()

        if (needsIndex.isNotEmpty()) {
            for ((index, offer) in offerRecords.withIndex()) {
                if (offer == null) {
                    val o = needsIndex.pop()
                    o.index = index
                    offerRecords[o.index] = OfferRecord(o.uid, o.index)
                    o.update() //write the new index to the database
                }
            }

            while(needsIndex.isNotEmpty()) //If we enter this loop, there were more offers than can fit inside the 6 slots. This should never happen - at least, not anymore. Can't speak for JSON offers.
            {
                val o = needsIndex.pop()
                SystemLogger.logGE("[WARN] PLAYER HAD EXTRA OFFER - RECOMMEND IMMEDIATE REFUND OF CONTENTS -> OFFER UID: ${o.uid}")
                SystemLogger.logGE("[WARN] AS PER ABOVE MESSAGE, REFUND CONTENTS OF OFFER AND MANUALLY SET offer_state = 6")
            }
        }

        visualizeRecords()
    }

    /**
     * Initializes the grand exchange.
     */
    fun init() {
        // Were trades made while gone?
        var updated = false
        for (record in offerRecords) {
            if (record != null) {
                val offer = getOffer(record) ?: continue
                if (!updated && (offer.withdraw[0] != null || offer.withdraw[1] != null)) {
                    updated = true
                }
                offer.visualize(player)
            }
        }
        if (updated) {
            sendMessage(player!!, "You have items from the Grand Exchange waiting in your collection box.")
        }
    }

    fun hasActiveOffer(): Boolean {
        for (i in offerRecords) {
            if (i != null)
                return true
        }
        return false
    }

    /**
     * Formats the grand exchange.
     *
     * @return the formatted offer for the SQL database.
     */
    fun format(): String? {
        var log = ""
        for (record in offerRecords) {
            if (record != null) {
                val offer = getOffer(record) ?: continue
                log += offer.itemID.toString() + "," + offer.amount + "," + offer.sell + "|"
            }
        }
        if (log.isNotEmpty() && log[log.length - 1] == '|') {
            log = log.substring(0, log.length - 1)
        }
        return log
    }

    data class OfferRecord(val uid: Long, val slot: Int)

    companion object {
        @JvmStatic fun getInstance(player: Player? = null): GrandExchangeRecords
        {
            return player?.getAttribute("ge-records", GrandExchangeRecords()) ?: GrandExchangeRecords()
        }
    }
}