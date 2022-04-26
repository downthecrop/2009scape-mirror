import core.cache.Cache
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.PlayerDetails
import core.game.node.entity.player.link.IronmanMode
import core.game.node.item.Item
import rs09.ServerConstants
import rs09.game.ai.ArtificialSession
import rs09.game.content.global.shops.Shop
import rs09.game.content.global.shops.ShopItem
import rs09.game.system.config.ServerConfigParser
import rs09.game.system.config.XteaParser
import rs09.game.world.GameWorld
import rs09.game.world.repository.Repository

object TestUtils {
    fun getMockPlayer(name: String, ironman: IronmanMode = IronmanMode.NONE): Player {
        val p = Player(PlayerDetails(name, name))
        p.details.session = ArtificialSession.getSingleton()
        p.ironmanManager.mode = ironman
        Repository.addPlayer(p)
        return p
    }

    fun getMockShop(name: String, general: Boolean, vararg stock: Item) : Shop {
        return Shop(name, stock.map { ShopItem(it.id, it.amount, 100) }.toTypedArray(), general)
    }

    fun preTestSetup() {
        if(ServerConstants.DATA_PATH == null) {
            ServerConfigParser.parse("worldprops/test.conf")
            XteaParser().load()
            Cache.init(this::class.java.getResource("cache").path.toString())
        }
    }

    fun advanceTicks(amount: Int) {
        for(i in 0 until amount) {
            GameWorld.majorUpdateWorker.handleTickActions()
        }
    }
}