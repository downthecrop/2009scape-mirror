package rs09.game.ge

import api.getItemName
import api.sendMessage
import core.cache.def.impl.ItemDefinition
import core.game.ge.OfferState
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.PlayerDetails
import core.game.node.entity.player.link.audio.Audio
import core.game.world.callback.CallBack
import rs09.game.system.SystemLogger
import rs09.game.world.repository.Repository
import rs09.tools.secondsToTicks

object GrandExchange : CallBack {
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
                SystemLogger.logGE("Updating offers...")
                val conn = GEDB.connect()
                val stmt = conn.createStatement()
                val buy_offer = stmt.executeQuery("SELECT * from player_offers where is_sale = 0")

                while(buy_offer.next())
                {
                    val offer = GrandExchangeOffer.fromQuery(buy_offer)
                    if(offer.isActive)
                    {
                        val sell_offer = stmt.executeQuery("SELECT * from player_offers where is_sale = 1 AND item_id = ${offer.itemID}")
                        while(sell_offer.next())
                        {
                            val otherOffer = GrandExchangeOffer.fromQuery(sell_offer)
                            if(!otherOffer.isActive) continue
                            val before = offer.amountLeft
                            exchange(offer,otherOffer)
                            if(offer.amountLeft != before)
                                SystemLogger.logGE("Purchased ${offer.amountLeft - before}x ${getItemName(offer.itemID)} @ ${offer.offeredValue}/${otherOffer.offeredValue} gp each.")
                        }

                        if(offer.amountLeft > 0)
                        {
                            val bot_offer = stmt.executeQuery("SELECT * from bot_offers where item_id = ${offer.itemID}")
                            if(bot_offer.next())
                            {
                                val botOffer = GrandExchangeOffer.fromBotQuery(bot_offer)
                                val before = offer.amountLeft
                                exchange(offer, botOffer)
                                if(offer.amountLeft != before)
                                    SystemLogger.logGE("Purchased FROM BOT ${offer.amountLeft - before}x ${getItemName(offer.itemID)}")
                            }
                        }
                    }
                }
                Thread.sleep(60_000) //sleep for 60 seconds
            }
        }.start()

        isRunning = true
    }

    fun dispatch(player: Player, offer: GrandExchangeOffer) : Boolean
    {
        if ( offer.amount < 1 )
            sendMessage(player, "You must choose the quantity you wish to buy!").also { return false }

        if ( offer.offeredValue < 1 )
            sendMessage(player, "You must choose the price you wish to buy for!").also { return false }

        if ( offer.offerState != OfferState.PENDING || offer.uid != 0L )
            return false

        if ( player.isArtificial )
            offer.playerUID = PlayerDetails.getDetails("2009scape").uid.also { offer.isBot = true }
        else
            offer.playerUID = player.details.uid

        offer.offerState = OfferState.REGISTERED
        player.playerGrandExchange.update(offer)

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

        //If the buyer is buying for less than the seller is selling for, don't exchange
        if(seller.offeredValue > buyer.offeredValue) return

        seller.completedAmount += amount
        buyer.completedAmount += amount

        if(seller.amountLeft < 1 && seller.player != null)
            seller.player!!.audioManager.send(Audio(4042,1,1))

        seller.addWithdrawItem(995, amount * buyer.offeredValue)
        buyer.addWithdrawItem(seller.itemID, amount)

        if(seller.offeredValue < buyer.offeredValue)
            buyer.addWithdrawItem(995, amount * (buyer.offeredValue - seller.offeredValue))

        if(seller.amountLeft < 1)
            seller.offerState = OfferState.COMPLETED
        if(buyer.amountLeft < 1)
            buyer.offerState = OfferState.COMPLETED

        seller.update()
        buyer.update()
    }

    override fun call(): Boolean {
        GEDB.init()
        boot()
        return true
    }
}