import core.cache.def.impl.ItemDefinition
import core.game.node.entity.player.link.IronmanMode
import core.game.node.item.Item
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.rs09.consts.Items
import rs09.game.content.global.shops.Shop
import rs09.game.content.global.shops.Shops
import kotlin.math.roundToInt

class ShopTests {
    companion object {
        init {TestUtils.preTestSetup()}
    }
    val testPlayer = TestUtils.getMockPlayer("test")
    val testIronman = TestUtils.getMockPlayer("test2", IronmanMode.STANDARD)
    val nonGeneral = TestUtils.getMockShop("Not General", false, false, Item(4151, 1))
    val general = TestUtils.getMockShop("General", true, false, Item(4151, 1))
    val highAlch = TestUtils.getMockShop("High(af) Alch", true, true, Item(4151, 1))

    @BeforeEach fun beforeEach() {
        testPlayer.inventory.clear()
    }

    fun assertTransactionSuccess(status: Shop.TransactionStatus) {
        Assertions.assertEquals(true, status is Shop.TransactionStatus.Success, "Transaction failure: ${if(status is Shop.TransactionStatus.Failure) status.reason else ""}")
    }

    @Test fun shouldSellItemToStore() {
        testPlayer.inventory.add(Item(4151, 1))
        testPlayer.setAttribute("shop-cont", general.getContainer(testPlayer))
        val status = general.sell(testPlayer, 0, 1)
        assertTransactionSuccess(status)
    }

    @Test fun shouldNotSellUnstockedItemToStandardStore() {
        testPlayer.inventory.add(Item(1, 1))
        testPlayer.setAttribute("shop-cont", nonGeneral.getContainer(testPlayer))
        val status = nonGeneral.sell(testPlayer, 0, 1)
        Assertions.assertEquals(true, status is Shop.TransactionStatus.Failure)
    }

    @Test fun shouldSellUnstockedItemToGeneralStore() {
        testPlayer.inventory.add(Item(1, 1))
        testPlayer.setAttribute("shop-cont", general.getContainer(testPlayer))
        val status = general.sell(testPlayer, 0, 1)
        assertTransactionSuccess(status)
    }

    @Test fun shouldSellUnstockedItemToGeneralStoreAt70PercentOfHalfHighAlchValue() {
        val saleItem = Items.RUNE_MED_HELM_1147
        testPlayer.inventory.add(Item(saleItem, 1))
        testPlayer.setAttribute("shop-cont", general.getContainer(testPlayer))
        val status = general.sell(testPlayer, 0, 1)
        assertTransactionSuccess(status)
        Assertions.assertEquals(Items.COINS_995, testPlayer.inventory[0].id)
        Assertions.assertEquals(
            (ItemDefinition.forId(saleItem).getAlchemyValue(true) * 0.5 * 0.7).roundToInt(),
            testPlayer.inventory[0].amount
        )
    }

    @Test fun shouldSellUnstockedItemToHighAlchStoreAt70PercentHighAlchValue() {
        val saleItem = Items.RUNE_MED_HELM_1147
        testPlayer.inventory.add(Item(saleItem, 1))
        testPlayer.setAttribute("shop-cont", general.getContainer(testPlayer))
        val status = highAlch.sell(testPlayer, 0, 1)
        assertTransactionSuccess(status)
        Assertions.assertEquals(Items.COINS_995, testPlayer.inventory[0].id)
        Assertions.assertEquals(
            (ItemDefinition.forId(saleItem).getAlchemyValue(true) * 0.7).roundToInt(),
            testPlayer.inventory[0].amount
        )
    }

    @Test fun shouldSellUnstockedItemToGeneralStoreAsIronman() {
        testIronman.inventory.add(Item(1, 1))
        testIronman.setAttribute("shop-cont", general.getContainer(testPlayer))
        val status = general.sell(testIronman, 0, 1)
        assertTransactionSuccess(status)
    }

    @Test fun shouldSellStackOfUnstockedItemsToPlayerStock() {
        testPlayer.inventory.add(Item(1, 20))
        testPlayer.setAttribute("shop-cont", general.getContainer(testPlayer))
        val status = general.sell(testPlayer, 0, 20)
        assertTransactionSuccess(status)
    }

    @Test fun shouldPutSoldUnstockedItemsInPlayerStock() {
        testPlayer.inventory.add(Item(2,1))
        testPlayer.setAttribute("shop-cont", general.getContainer(testPlayer))
        val status = general.sell(testPlayer, 0, 1)
        assertTransactionSuccess(status)
        Assertions.assertEquals(1, general.playerStock.getAmount(2))
        Assertions.assertEquals(0, general.getContainer(testPlayer).getAmount(2))
    }

    @Test fun shouldAllowStandardPlayerToBuy() {
        testPlayer.inventory.add(Item(995, 100000))
        testPlayer.setAttribute("shop-cont", general.getContainer(testPlayer))
        testPlayer.setAttribute("shop-main", true)
        val status = general.buy(testPlayer, 0, 1)
        assertTransactionSuccess(status)
    }

    @Test fun shouldAllowStandardPlayerToBuyOverstock() {
        testPlayer.inventory.add(Item(995, 100000))
        testPlayer.setAttribute("shop-cont", general.getContainer(testPlayer))
        testPlayer.setAttribute("shop-main", true)
        general.getContainer(testPlayer).add(Item(4151, 100))
        val status = general.buy(testPlayer, 0, 1)
        assertTransactionSuccess(status)
    }

    @Test fun shouldAllowStandardPlayerToBuyPlayerStock() {
        testPlayer.inventory.add(Item(995, 100000))
        testPlayer.setAttribute("shop-cont", general.getContainer(testPlayer))
        testPlayer.setAttribute("shop-main", false)
        general.playerStock.add(Item(4151, 100))
        val status = general.buy(testPlayer, 0, 1)
        assertTransactionSuccess(status)
    }

    @Test fun shouldNotAllowIronmanToBuyOverstock() {
        testIronman.inventory.add(Item(995, 100000))
        testIronman.setAttribute("shop-cont", general.getContainer(testIronman))
        testIronman.setAttribute("shop-main", true)
        general.getContainer(testIronman).add(Item(4151, 100))
        val status = general.buy(testIronman, 0, 1)
        Assertions.assertEquals(true, status is Shop.TransactionStatus.Failure)
    }

    @Test fun shouldNotAllowIronmanToBuyPlayerStock() {
        testIronman.inventory.add(Item(995, 100000))
        testIronman.setAttribute("shop-cont", general.playerStock)
        testIronman.setAttribute("shop-main", false)
        general.playerStock.add(Item(4151, 1))
        val status = general.buy(testIronman, 0, 1)
        Assertions.assertEquals(true, status is Shop.TransactionStatus.Failure)
    }

    @Test fun openShopShouldNotThrowException() {
        Assertions.assertDoesNotThrow {
            general.openFor(testPlayer)
        }
    }

    @Test fun shouldNotThrowExceptionWhenRestockingStockWithNullSlot() {
        Assertions.assertDoesNotThrow {
            general.getContainer(testPlayer).add(Item(1, 100))
            general.getContainer(testPlayer).add(Item(2, 100))
            general.getContainer(testPlayer).replace(null, 0) //replace item in slot 0 with null
            for ((k,_) in general.stockInstances) general.needsUpdate[k] = true
            general.restock()
        }
    }

    @Test fun playerStockShouldNeverBeNull() {
        Assertions.assertNotNull(general.playerStock)
    }

    @Test fun shouldAllowBuyingFromPlayerStockOnMultipleRows() {
        for(i in 0 until 100) {
            general.playerStock.add(Item(i + 3100, 1)) //make sure we populate several rows of items
        }
        testPlayer.inventory.add(Item(995, 100000))
        testPlayer.setAttribute("shop-cont", general.getContainer(testPlayer))
        testPlayer.setAttribute("shop-main", false)
        var status: Shop.TransactionStatus = Shop.TransactionStatus.Failure("Test did not assign transaction status.")
        Assertions.assertDoesNotThrow({
            status = general.buy(testPlayer, 36, 1)
        }, "Error selling to shop: ${general.playerStock}")
        assertTransactionSuccess(status)
    }

    @Test fun invalidStockJsonShouldNotCauseItemShift() {
        val invalidJson = "{1277,10,100}-{1277,10,100}-{1279,10,100}"
        val stock = Shops.parseStock(invalidJson, -1)
        Assertions.assertEquals(2, stock.size)
        Assertions.assertEquals(20, stock[0].amount)
    }
}