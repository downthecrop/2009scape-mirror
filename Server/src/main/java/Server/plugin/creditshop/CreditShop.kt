package plugin.creditshop

import core.cache.def.impl.ItemDefinition
import core.game.content.global.shop.Shop
import core.game.content.global.shop.ShopViewer
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.Items


/**
 * Shop that sells various things for credits, which can be obtained by voting or contributing code
 * @author Ceikry
 */
class CreditShop : Shop("Credit Shop <3", listOf(
        Item(Items.CHRISTMAS_CRACKER_962,100),
        Item(Items.RED_PARTYHAT_1038,100),
        Item(Items.BLUE_PARTYHAT_1042, 100),
        Item(Items.GREEN_PARTYHAT_1044, 100),
        Item(Items.YELLOW_PARTYHAT_1040, 100),
        Item(Items.PURPLE_PARTYHAT_1046,100),
        Item(Items.WHITE_PARTYHAT_1048, 100),
        Item(Items.RED_HWEEN_MASK_1057,100),
        Item(Items.BLUE_HWEEN_MASK_1055, 100),
        Item(Items.GREEN_HWEEN_MASK_1053,100),
        Item(Items.SANTA_HAT_1050, 100),
        Item(Items.SCYTHE_10735,100),
        Item(Items.EASTER_RING_7927,100),
        Item(Items.BASKET_OF_EGGS_4565,100)
).toTypedArray(),false){
    val prices = hashMapOf(
            Items.SANTA_HAT_1050 to 35,
            Items.CHRISTMAS_CRACKER_962 to 40,
            Items.RED_PARTYHAT_1038 to 50,
            Items.BLUE_PARTYHAT_1042 to 50,
            Items.GREEN_PARTYHAT_1044 to 50,
            Items.YELLOW_PARTYHAT_1040 to 50,
            Items.PURPLE_PARTYHAT_1046 to 50,
            Items.WHITE_PARTYHAT_1048 to 50,
            Items.RED_HWEEN_MASK_1057 to 60,
            Items.BLUE_HWEEN_MASK_1055 to 60,
            Items.GREEN_HWEEN_MASK_1053 to 60,
            13887 to 100,
            13893 to 100,
            13899 to 100,
            13858 to 100,
            13861 to 100,
            13864 to 100,
            13867 to 100,
            10735 to 200,
            14643 to 125,
            Items.EASTER_RING_7927 to 100,
            Items.BASKET_OF_EGGS_4565 to 100
    )

    init {
        isPointShop = true
    }

    override fun open(player: Player?) {
        player ?: return
        super.open(player)
        val amt = player.details.credits
        player.sendMessage("You have $amt credits to spend.")
    }


    override fun getBuyPrice(item: Item?, player: Player?): Int {
        return prices.get(item?.id) ?: Integer.MAX_VALUE.also { player?.sendMessage("This item's price hasn't been defined. Please contact us.") }
    }

    override fun getPoints(player: Player?): Int {
        player ?: return 0
        return player.details.credits
    }

    override fun canSell(player: Player?, item: Item?, def: ItemDefinition?): Boolean {
        player ?: return false
        player.sendMessage("This shop cannot be sold to.")
        return false
    }

    override fun getPointsName(): String {
        return "credit"
    }

    override fun value(player: Player?, viewer: ShopViewer?, item: Item?, sell: Boolean) {
        item ?: return
        player ?: return
        var multiple: Boolean
        if(sell){
            player.sendMessage("This shop cannot be sold to.").also { return }
        }
        player.sendMessage("This item costs " + (prices[item.id] ?: Integer.MAX_VALUE).also { multiple = it > 1 } + " " + pointsName + if(multiple) "s." else ".")
    }

    override fun decrementPoints(player: Player?, decrement: Int) {
        player ?: return
        player.details.credits -= decrement
    }

}