import core.game.node.entity.player.Player
import core.game.node.entity.player.info.PlayerDetails
import core.game.node.entity.player.link.IronmanMode
import core.game.node.item.Item
import rs09.game.ai.ArtificialSession
import rs09.game.content.global.shops.Shop
import rs09.game.content.global.shops.ShopItem

object TestUtils {
    fun getMockPlayer(name: String, ironman: IronmanMode = IronmanMode.NONE): Player {
        val p = Player(PlayerDetails(name, name))
        p.details.session = ArtificialSession.getSingleton()
        p.ironmanManager.mode = ironman
        return p
    }

    fun getMockShop(name: String, general: Boolean, vararg stock: Item) : Shop {
        return Shop(name, stock.map { ShopItem(it.id, it.amount, 100) }.toTypedArray(), general)
    }
}