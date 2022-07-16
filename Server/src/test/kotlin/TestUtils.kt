import core.cache.Cache
import core.cache.crypto.ISAACCipher
import core.cache.crypto.ISAACPair
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.PlayerDetails
import core.game.node.entity.player.link.IronmanMode
import core.game.node.item.Item
import core.net.IoSession
import core.net.packet.IoBuffer
import org.rs09.consts.Items
import rs09.ServerConstants
import rs09.game.content.global.shops.Shop
import rs09.game.content.global.shops.ShopItem
import rs09.game.system.SystemLogger
import rs09.game.system.config.ConfigParser
import rs09.game.system.config.ServerConfigParser
import rs09.game.world.GameWorld
import rs09.game.world.repository.Repository
import rs09.game.world.update.UpdateSequence
import java.nio.ByteBuffer

object TestUtils {
    fun getMockPlayer(name: String, ironman: IronmanMode = IronmanMode.NONE): Player {
        val p = MockPlayer(name)
        p.ironmanManager.mode = ironman
        Repository.addPlayer(p)
        //Update sequence has a separate list of players for some reason...
        UpdateSequence.renderablePlayers.add(p)
        return p
    }

    fun getMockShop(name: String, general: Boolean, highAlch: Boolean, vararg stock: Item) : Shop {
        return Shop(
            name,
            stock.map { ShopItem(it.id, it.amount, 100) }.toTypedArray(),
            general,
            highAlch = highAlch
        )
    }

    fun getMockTokkulShop(name: String, vararg stock: Item) : Shop {
        return Shop(
            name,
            stock.map { ShopItem(it.id, it.amount, 100) }.toTypedArray(),
            currency = Items.TOKKUL_6529
        )
    }

    fun preTestSetup() {
        if(ServerConstants.DATA_PATH == null) {
            ServerConfigParser.parse(this::class.java.getResource("test.conf"))
            Cache.init(this::class.java.getResource("cache").path.toString())
            ConfigParser().prePlugin()
            ConfigParser().postPlugin()
        }
    }

    fun advanceTicks(amount: Int, skipPulseUpdates: Boolean = true) {
        SystemLogger.logInfo("Advancing ticks by $amount.")
        for(i in 0 until amount) {
            GameWorld.majorUpdateWorker.handleTickActions(skipPulseUpdates)
        }
    }
}

class MockPlayer(name: String) : Player(PlayerDetails(name)) {
    init {
        this.details.session = MockSession()
    }

    override fun update() {
        //do nothing. This is for rendering stuff. We don't render a mock player. Not until the spaghetti is less spaghetti.
    }
}

class MockSession : IoSession(null, null) {
    val receivedPackets = ArrayList<Packet>()
    var disconnected = false

    @Suppress("ArrayInDataClass")
    data class Packet(val opcode: Int, val payload: ByteArray)

    override fun getRemoteAddress(): String? {
        return "127.0.0.1"
    }

    override fun write(context: Any, instant: Boolean) {
        if(context is IoBuffer) {
            receivedPackets.add(Packet(context.opcode(), context.array()))
        }
    }

    fun hasPacketReady(opcode: Int) : Boolean {
        for(pkt in receivedPackets) if(pkt.opcode == opcode) return true
        return false
    }

    fun getPacketsWithOpcode(opcode: Int) : ArrayList<ByteArray> {
        val list = ArrayList<ByteArray>()
        for(pkt in receivedPackets) if(pkt.opcode == opcode) list.add(pkt.payload)
        return list
    }

    override fun getIsaacPair(): ISAACPair {
        return ISAACPair(ISAACCipher(intArrayOf(0)), ISAACCipher(intArrayOf(0)))
    }

    override fun queue(buffer: ByteBuffer?) {}

    override fun write() {}

    override fun disconnect() {disconnected = true}

    fun clear() {
        receivedPackets.clear()
    }

}
