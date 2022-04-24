package rs09.game.ge

import api.StartupListener
import api.getItemName
import api.itemDefinition
import api.sendMessage
import core.game.ge.GrandExchangeDatabase
import core.game.ge.OfferState
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.PlayerDetails
import core.game.node.entity.player.link.audio.Audio
import rs09.game.system.SystemLogger
import rs09.game.system.config.ItemConfigParser
import rs09.game.world.repository.Repository
import java.lang.Integer.max

/**
 * Handles the exchanging of offers, offer update thread, etc.
 * @author Ceikry
 */
class GrandExchange : StartupListener {
    /**
     * Fallback safety check to make sure we don't start the GE twice under any circumstance
     */
    var isRunning = false

    /**
     * Initializes the offer manager and spawns an update thread.
     * @param local whether or not the GE should be the local in-code server rather than some hypothetical remote implementation.
     */
    fun boot(){
        if(isRunning) return


        SystemLogger.logGE("Initializing GE...")
        //OfferManager.init()
        SystemLogger.logGE("GE Initialized.")

        SystemLogger.logGE("Initializing GE Update Worker")

        val t = Thread {
            Thread.currentThread().name = "GE Update Worker"
            while(true) {
                with(GEDB.connect()) {
                    val conn = this
                    val stmt = conn.createStatement()
                    val activeOffer = stmt.executeQuery("SELECT * from player_offers where offer_state < 4 AND NOT offer_state = 2")
                    val activeOffers = ArrayList<GrandExchangeOffer>()

                    while(activeOffer.next())
                    {
                        val offer = GrandExchangeOffer.fromQuery(activeOffer)
                        if(!offer.isActive) continue
                        activeOffers.add(offer)
                    }

                    for(offer in activeOffers)
                        processOffer(offer)

                    stmt.close()
                }
                Thread.sleep(15_000) //sleep for 15 seconds
            }
        }.start()

        isRunning = true
    }

    companion object {
        fun processOffer(offer: GrandExchangeOffer)
        {
            with (GEDB.connect()) {
                val conn = this
                if(offer.isActive) {
                    val stmt = conn.createStatement()
                    val olderOffers = stmt.executeQuery("SELECT * FROM player_offers WHERE item_id = ${offer.itemID} AND is_sale = ${!offer.sell} AND offer_state < 4 AND NOT offer_state = 2 AND time_stamp < ${offer.timeStamp}")
                    var bestOffer: GrandExchangeOffer? = null

                    while(olderOffers.next()) {
                        val otherOffer = GrandExchangeOffer.fromQuery(olderOffers)
                        if (
                            (offer.sell && otherOffer.offeredValue < offer.offeredValue)
                            || (!offer.sell && otherOffer.offeredValue > offer.offeredValue)
                        ) continue
                        //We favor the newer offers
                        if(bestOffer == null ||
                            if(offer.sell) otherOffer.offeredValue > bestOffer.offeredValue
                            else otherOffer.offeredValue < bestOffer.offeredValue
                        ) bestOffer = otherOffer
                    }
                    olderOffers.close()

                    if(bestOffer != null) exchange(offer, bestOffer)

                    if(offer.amountLeft > 0) {
                        bestOffer = null
                        val newerOffers = stmt.executeQuery("SELECT * FROM player_offers WHERE item_id = ${offer.itemID} AND is_sale = ${!offer.sell} AND offer_state < 4 AND NOT offer_state = 2 AND time_stamp >= ${offer.timeStamp}")
                        while(newerOffers.next()) {
                            val otherOffer = GrandExchangeOffer.fromQuery(newerOffers)
                            if (
                                (offer.sell && otherOffer.offeredValue < offer.offeredValue)
                                || (!offer.sell && otherOffer.offeredValue > offer.offeredValue)
                            ) continue
                            //We continue favoring the newest offers
                            if(bestOffer == null ||
                                    if(offer.sell) otherOffer.offeredValue < bestOffer.offeredValue
                                    else otherOffer.offeredValue > bestOffer.offeredValue
                              ) bestOffer = otherOffer
                        }
                        newerOffers.close()
                        if(bestOffer != null) exchange(offer, bestOffer)
                    }

                    if(!offer.sell && offer.amountLeft > 0) {
                        val botStmt = conn.createStatement()
                        val bot_offer = botStmt.executeQuery("SELECT * from bot_offers where item_id = ${offer.itemID}")
                        if(bot_offer.next())
                        {
                            val botOffer = GrandExchangeOffer.fromBotQuery(bot_offer)
                            val before = offer.amountLeft
                            exchange(offer, botOffer)
                            if(offer.amountLeft != before)
                                SystemLogger.logGE("Purchased FROM BOT ${offer.amountLeft - before}x ${getItemName(offer.itemID)}")
                        }
                        botStmt.close()
                    }
                }
            }
        }

        @JvmStatic
        fun getRecommendedPrice(itemID: Int, from_bot: Boolean = false): Int {
            var base = max(GrandExchangeDatabase.getDatabase()[itemID]?.value ?: 0, getItemDefPrice(itemID))
            if (from_bot) base = (max(BotPrices.getPrice(itemID), base) * 1.10).toInt()
            return base
        }

        private fun getItemDefPrice(itemID: Int): Int {
            return max(itemDefinition(itemID).getConfiguration(ItemConfigParser.GE_PRICE) ?: 0, itemDefinition(itemID).value)
        }

        @JvmStatic
        fun getOfferStats(itemID: Int, sale: Boolean) : String
        {
            val conn = GEDB.connect()

            var foundOffers = 0
            var totalAmount = 0
            var bestPrice = 0
            val sb = StringBuilder()
            var stmt = conn.createStatement()

            if(!sale)
            {
                var botAmt = 0
                var botPrice = 0
                val player_offers = stmt.executeQuery("SELECT * from player_offers where item_id = $itemID AND is_sale = 1 AND offer_state < 4 AND NOT offer_state = 2")

                while(player_offers.next())
                {
                    val o = GrandExchangeOffer.fromQuery(player_offers)
                    ++foundOffers
                    totalAmount += o.amountLeft
                    if(o.offeredValue < bestPrice || bestPrice == 0)
                        bestPrice = o.offeredValue
                }

                stmt.close()
                stmt = conn.createStatement()
                val bot_offers = stmt.executeQuery("SELECT * from bot_offers where item_id = $itemID")
                if(bot_offers.next())
                {
                    val o = GrandExchangeOffer.fromBotQuery(bot_offers)
                    botAmt = o.amount
                    botPrice = getRecommendedPrice(itemID, true)
                }

                sb.append("Player Stock: <col=FFFFFF>$totalAmount  ")
                sb.append("</col>  Lowest Price: <col=FFFFFF>$bestPrice<br>")
                sb.append("-".repeat(50))
                sb.append("</col><br>Bot Stock: <col=FFFFFF>$botAmt  ")
                sb.append("</col>  Bot Price: <col=FFFFFF>$botPrice")
            }
            else
            {
                val buy_offers = stmt.executeQuery("SELECT * from player_offers where item_id = $itemID AND is_sale = 0 AND offer_state < 4 AND NOT offer_state = 2")

                while(buy_offers.next())
                {
                    val o = GrandExchangeOffer.fromQuery(buy_offers)
                    ++foundOffers
                    totalAmount += o.amountLeft
                    if(o.offeredValue > bestPrice)
                        bestPrice = o.offeredValue
                }

                sb.append("Buy Offers: <col=FFFFFF>$totalAmount    ")
                sb.append("</col>Highest Offer: <col=FFFFFF>$bestPrice</col>")
            }

            stmt.close()
            return sb.toString()
        }

        fun addBotOffer(itemID: Int, amount: Int): Boolean
        {
            if (GrandExchangeDatabase.getDatabase()[itemID] == null)
                return false

            val offer = GrandExchangeOffer.createBotOffer(itemID, amount)
            offer.writeNew()

            return true
        }

        fun dispatch(player: Player, offer: GrandExchangeOffer) : Boolean
        {
            if ( offer.amount < 1 )
                sendMessage(player, "You must choose the quantity you wish to buy!").also { return false }

            if ( offer.offeredValue < 1 )
                sendMessage(player, "You must choose the price you wish to buy for!").also { return false }

            if ( offer.offerState != OfferState.PENDING || offer.uid != 0L )
            {
                SystemLogger.logWarn("[GE] DISPATCH FAILURE: ${offer.offerState.name}, UID: ${offer.uid}")
                return false
            }

            if ( player.isArtificial )
                offer.playerUID = PlayerDetails.getDetails("2009scape").uid.also { offer.isBot = true }
            else
                offer.playerUID = player.details.uid

            offer.offerState = OfferState.REGISTERED
            //GrandExchangeRecords.getInstance(player).update(offer)

            if (offer.sell) {
                Repository.sendNews(player.username + " just offered " + offer.amount + " " + getItemName(offer.itemID) + " on the GE.")
            }

            offer.writeNew()
            return true
        }

        fun exchange(offer: GrandExchangeOffer, other: GrandExchangeOffer)
        {
            if(offer.sell && other.sell) return //Don't exchange if they are both sell offers
            val amount = Integer.min(offer.amount - offer.completedAmount, other.amount - other.completedAmount)

            val seller = if(offer.sell) offer else other
            val buyer = if(offer == seller) other else offer

            val sellerBias = seller.timeStamp > buyer.timeStamp

            //If the buyer is buying for less than the seller is selling for, don't exchange
            if(seller.offeredValue > buyer.offeredValue) return

            seller.completedAmount += amount
            buyer.completedAmount += amount

            if(seller.amountLeft < 1 && seller.player != null)
                seller.player!!.audioManager.send(Audio(4042,1,1))

            seller.addWithdrawItem(995, amount * if(sellerBias) buyer.offeredValue else seller.offeredValue)
            buyer.addWithdrawItem(seller.itemID, amount)

            if(!sellerBias)
                buyer.addWithdrawItem(995, amount * (buyer.offeredValue - seller.offeredValue))

            if(seller.amountLeft < 1)
                seller.offerState = OfferState.COMPLETED
            if(buyer.amountLeft < 1)
                buyer.offerState = OfferState.COMPLETED

            seller.totalCoinExchange += if(sellerBias) buyer.offeredValue else seller.offeredValue * amount
            buyer.totalCoinExchange += if(sellerBias) buyer.offeredValue else seller.offeredValue * amount

            seller.update()
            val sellerPlayer = Repository.uid_map[seller.playerUID]
            GrandExchangeRecords.getInstance(sellerPlayer).visualizeRecords()
            buyer.update()
            val buyerPlayer = Repository.uid_map[buyer.playerUID]
            GrandExchangeRecords.getInstance(buyerPlayer).visualizeRecords()
        }

        fun getValidOffers(): List<GrandExchangeOffer>
        {
            val conn = GEDB.connect()
            val stmt = conn.createStatement()
            val offers = ArrayList<GrandExchangeOffer>()

            val results = stmt.executeQuery("SELECT * FROM player_offers WHERE offer_state < 4 AND NOT offer_state = 2")
            while(results.next())
            {
                val o = GrandExchangeOffer.fromQuery(results)
                offers.add(o)
            }
            stmt.close()
            return offers
        }

        fun getBotOffers(): List<GrandExchangeOffer>
        {
            val conn = GEDB.connect()
            val stmt = conn.createStatement()
            val offers = ArrayList<GrandExchangeOffer>()

            val results = stmt.executeQuery("SELECT item_id,amount FROM bot_offers WHERE amount > 0")
            while(results.next())
            {
                val o = GrandExchangeOffer.fromBotQuery(results)
                offers.add(o)
            }
            stmt.close()
            return offers
        }

    }

    override fun startup(){
        GEDB.init()
        boot()
    }
}