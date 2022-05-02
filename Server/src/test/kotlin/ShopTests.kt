import core.game.node.entity.player.link.IronmanMode
import core.game.node.item.Item
import org.junit.Assert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import rs09.game.content.global.shops.Shop
import rs09.game.content.global.shops.Shops
import rs09.game.system.config.ShopParser

class ShopTests {
    val testPlayer = TestUtils.getMockPlayer("test")
    val testIronman = TestUtils.getMockPlayer("test2", IronmanMode.STANDARD)
    val nonGeneral = TestUtils.getMockShop("Not General", false, Item(4151, 1))
    val general = TestUtils.getMockShop("General", true, Item(4151, 1))

    @Test fun shouldSellItemToStore() {
        testPlayer.inventory.add(Item(4151, 1))
        testPlayer.setAttribute("shop-cont", general.getContainer(testPlayer))
        val status = general.sell(testPlayer, 0, 1)
        Assertions.assertEquals(true, status is Shop.TransactionStatus.Success, "Transaction failed: ${if(status is Shop.TransactionStatus.Failure) status.reason else ""}")
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
        Assertions.assertEquals(true, status is Shop.TransactionStatus.Success, "Transaction failure: ${if(status is Shop.TransactionStatus.Failure) status.reason else ""}")
    }

    @Test fun shouldSellUnstockedItemToGeneralStoreAsIronman() {
        testIronman.inventory.add(Item(1, 1))
        testIronman.setAttribute("shop-cont", general.getContainer(testPlayer))
        val status = general.sell(testIronman, 0, 1)
        Assertions.assertEquals(true, status is Shop.TransactionStatus.Success, "Transaction failure: ${if(status is Shop.TransactionStatus.Failure) status.reason else ""}")
    }

    @Test fun shouldSellStackOfUnstockedItemsToPlayerStock() {
        testPlayer.inventory.add(Item(1, 20))
        testPlayer.setAttribute("shop-cont", general.getContainer(testPlayer))
        val status = general.sell(testPlayer, 0, 20)
        Assertions.assertEquals(true, status is Shop.TransactionStatus.Success, "Transaction failure: ${if(status is Shop.TransactionStatus.Failure) status.reason else ""}")
    }

    @Test fun shouldPutSoldUnstockedItemsInPlayerStock() {
        testPlayer.inventory.add(Item(2,1))
        testPlayer.setAttribute("shop-cont", general.getContainer(testPlayer))
        val status = general.sell(testPlayer, 0, 1)
        Assertions.assertEquals(true, status is Shop.TransactionStatus.Success, "Transaction failure: ${if(status is Shop.TransactionStatus.Failure) status.reason else ""}")
        Assertions.assertEquals(1, general.playerStock.getAmount(2))
        Assertions.assertEquals(0, general.getContainer(testPlayer).getAmount(2))
    }

    @Test fun shouldAllowStandardPlayerToBuy() {
        testPlayer.inventory.add(Item(995, 100000))
        testPlayer.setAttribute("shop-cont", general.getContainer(testPlayer))
        testPlayer.setAttribute("shop-main", true)
        val status = general.buy(testPlayer, 0, 1)
        Assertions.assertEquals(true, status is Shop.TransactionStatus.Success, "Transaction failure: ${if(status is Shop.TransactionStatus.Failure) status.reason else ""}")
    }

    @Test fun shouldAllowStandardPlayerToBuyOverstock() {
        testPlayer.inventory.add(Item(995, 100000))
        testPlayer.setAttribute("shop-cont", general.getContainer(testPlayer))
        testPlayer.setAttribute("shop-main", true)
        general.getContainer(testPlayer).add(Item(4151, 100))
        val status = general.buy(testPlayer, 0, 1)
        Assertions.assertEquals(true, status is Shop.TransactionStatus.Success, "Transaction failure: ${if(status is Shop.TransactionStatus.Failure) status.reason else ""}")
    }

    @Test fun shouldAllowStandardPlayerToBuyPlayerStock() {
        testPlayer.inventory.add(Item(995, 100000))
        testPlayer.setAttribute("shop-cont", general.getContainer(testPlayer))
        testPlayer.setAttribute("shop-main", false)
        general.playerStock.add(Item(4151, 100))
        val status = general.buy(testPlayer, 0, 1)
        Assertions.assertEquals(true, status is Shop.TransactionStatus.Success, "Transaction failure: ${if(status is Shop.TransactionStatus.Failure) status.reason else ""}")
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
        var status: Shop.TransactionStatus = Shop.TransactionStatus.Success()
        Assertions.assertDoesNotThrow({
            status = general.buy(testPlayer, 36, 1)
        }, "Error selling to shop: ${general.playerStock}")
        Assertions.assertEquals(true, status is Shop.TransactionStatus.Success)
    }

    @Test fun invalidStockJsonShouldNotCauseItemShift() {
        val invalidJson = "{1277,10,100}-{1277,10,100}-{1279,10,100}"
        val stock = Shops.parseStock(invalidJson, -1)
        Assertions.assertEquals(2, stock.size)
        Assertions.assertEquals(20, stock[0].amount)
    }
}