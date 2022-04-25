import core.game.ge.OfferState
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.fail
import rs09.game.ge.GEDB
import rs09.game.ge.GrandExchange
import rs09.game.ge.GrandExchangeOffer
import rs09.game.system.SystemLogger
import kotlin.random.Random

@TestInstance(TestInstance.Lifecycle.PER_CLASS) class ExchangeTests {
    companion object {
        private const val TEST_DB_PATH = "ge_test.db"
        init {
            GEDB.init(TEST_DB_PATH)
        }

        fun generateOffer(itemId: Int, amount: Int, price: Int, sale: Boolean) : GrandExchangeOffer {
            val offer = GrandExchangeOffer()
            val uid = "test ${System.currentTimeMillis()}".hashCode()
            offer.offerState = OfferState.REGISTERED
            offer.itemID = itemId
            offer.offeredValue = price
            offer.amount = amount
            offer.timeStamp = System.currentTimeMillis()
            offer.index = 0
            offer.isBot = false
            offer.playerUID = uid
            offer.sell = sale
            offer.writeNew()
            return offer
        }
    }

    @Test fun testPlaceOffer() {
        val offer = generateOffer(4151, 1, 100000, true)
        val uid = offer.playerUID

        with (GEDB.connect()) {
            val stmt = this.createStatement()
            val result = stmt.executeQuery("select * from player_offers where player_uid = $uid")
            val thisOffer = if(result.next()) GrandExchangeOffer.fromQuery(result) else fail("Offer did not exist!")
            Assertions.assertEquals(offer.itemID, thisOffer.itemID)
        }
    }

    @Test fun testBuyFirstSellAfterFavorsSell() {
        val buyOffer = generateOffer(4151, 1, 100000, false)
        val sellOffer = generateOffer(4151, 1, 85000, true)
        GrandExchange.exchange(buyOffer, sellOffer)
        Assertions.assertEquals(null, buyOffer.withdraw[1], "Buyer got coins back on sell bias") //should get no coins back
    }

    @Test fun testSellFirstBuyAfterFavorsBuy() {
        val sellOffer = generateOffer(4151, 1, 85000, true)
        val buyOffer = generateOffer(4151, 1, 100000, false)
        GrandExchange.exchange(sellOffer,buyOffer)
        Assertions.assertNotEquals(null, buyOffer.withdraw[1], "Buyer did not get coins back on buy bias") //should get coins back
    }

    @Test fun buyerCannotBuyHigherSellOffer() {
        val sellOffer = generateOffer(4151, 1, 125000, true)
        val buyOffer = generateOffer(4151, 1, 100000, false)
        GrandExchange.exchange(sellOffer,buyOffer)
        Assertions.assertEquals(null, buyOffer.withdraw[0]) //Buyer should not get any items, sell offer is higher
    }
}