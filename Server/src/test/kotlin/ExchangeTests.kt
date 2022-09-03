import core.game.ge.OfferState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.fail
import rs09.game.ge.GEDB
import rs09.game.ge.GrandExchange
import rs09.game.ge.GrandExchangeOffer
import rs09.game.ge.PriceIndex
import rs09.game.system.SystemLogger
import java.io.File
import kotlin.random.Random

@TestInstance(TestInstance.Lifecycle.PER_CLASS) class ExchangeTests {
    companion object {
        private const val TEST_DB_PATH = "ge_test.db"
        init {
            TestUtils.preTestSetup()
            GEDB.init(TEST_DB_PATH)
        }

        fun generateOffer(itemId: Int, amount: Int, price: Int, sale: Boolean, username: String = "test ${System.currentTimeMillis()}") : GrandExchangeOffer {
            val offer = GrandExchangeOffer()
            val uid = username.hashCode()  // normally this would be the account's uid but in the test we don't have an account
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

        @AfterAll fun cleanup() {
            File(TEST_DB_PATH).delete()
        }
    }

    @Test fun testPlaceOffer() {
        val offer = generateOffer(4151, 1, 100000, true)
        val uid = offer.playerUID

        GEDB.run { conn ->
            val stmt = conn.createStatement()
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

    @Test fun manyCompletedOffersAboveDefaultPriceShouldInfluencePriceUpwards() {
        val defaultPrice = GrandExchange.getRecommendedPrice(4151)
        val modifiedPrice = defaultPrice * 1.15

        for(i in 0 until 100) {
            val sellOffer = generateOffer(4151, 1, modifiedPrice.toInt(), true, "test1")
            val buyOffer = generateOffer(4151, 1, modifiedPrice.toInt(), false, "test2")
            GrandExchange.exchange(sellOffer,buyOffer)
        }

        val newPrice = GrandExchange.getRecommendedPrice(4151)

        Assertions.assertEquals(true, newPrice > defaultPrice, "Price was not influenced in the expected way! New Price: $newPrice, default: $defaultPrice")
    }

    @Test fun playerTradingWithThemselvesShouldNotBeAbleToInfluencePrices() {
        val defaultPrice = GrandExchange.getRecommendedPrice(4151)
        val modifiedPrice = defaultPrice * 1.15

        for(i in 0 until 100) {
            val sellOffer = generateOffer(4151, 1, modifiedPrice.toInt(), true, "test")
            val buyOffer = generateOffer(4151, 1, modifiedPrice.toInt(), false, "test")
            GrandExchange.exchange(sellOffer,buyOffer)
        }

        Assertions.assertEquals(defaultPrice, GrandExchange.getRecommendedPrice(4151))
    }

    @Test fun concurrentlySubmittedOffersShouldNotThrowExceptions(){
        runBlocking {
            val a = GlobalScope.launch { for(i in 0 until 5) {PriceIndex.allowItem(i); GrandExchange.addBotOffer(i, 1)} }
            val b = GlobalScope.launch { for(i in 0 until 5) {PriceIndex.allowItem(i); GrandExchange.addBotOffer(i, 1)} }
            a.join()
            b.join()
            Assertions.assertEquals(false, a.isCancelled)
            Assertions.assertEquals(false, b.isCancelled)
        }
    }
}
