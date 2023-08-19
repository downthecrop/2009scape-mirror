import content.global.skill.farming.timers.CropGrowth
import core.ServerConstants
import core.api.log
import core.cache.Cache
import core.cache.crypto.ISAACCipher
import core.cache.crypto.ISAACPair
import core.game.interaction.ScriptProcessor
import core.game.node.Node
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.VarpManager
import core.game.node.entity.player.info.PlayerDetails
import core.game.node.entity.player.info.Rights
import core.game.node.entity.player.info.login.PlayerSaveParser
import core.game.node.entity.player.info.login.PlayerSaver
import core.game.node.entity.player.link.IronmanMode
import core.game.node.entity.player.link.SavedData
import core.game.node.entity.player.link.quest.QuestRepository
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItem
import core.game.node.item.Item
import core.game.shops.Shop
import core.game.shops.ShopItem
import core.game.system.config.ConfigParser
import core.game.system.config.ServerConfigParser
import core.game.system.timer.TimerRegistry
import core.game.system.timer.impl.AntiMacro
import core.game.system.timer.impl.Disease
import core.game.system.timer.impl.Poison
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.repository.Repository
import core.game.world.update.UpdateSequence
import core.net.IoSession
import core.net.packet.IoBuffer
import core.net.packet.PacketProcessor
import core.tools.Log
import org.rs09.consts.Items
import java.net.URI
import java.nio.ByteBuffer

object TestUtils {
    var uidCounter = 0
    const val PLAYER_DEATH_TICKS = 14

    fun getMockPlayer(name: String, ironman: IronmanMode = IronmanMode.NONE, rights: Rights = Rights.ADMINISTRATOR, isBot: Boolean = false): MockPlayer {
        val p = MockPlayer(name, isBot)
        p.ironmanManager.mode = ironman
        p.details.accountInfo.uid = uidCounter++
        p.setPlaying(true)
        p.playerFlags.lastSceneGraph = p.location ?: ServerConstants.HOME_LOCATION
        Repository.addPlayer(p)
        //Update sequence has a separate list of players for some reason...
        UpdateSequence.renderablePlayers.add(p)
        p.details.rights = rights
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
            registerTimers()
        }
    }

    fun registerTimers() { //allow timers to be registered for use by tests
        TimerRegistry.registerTimer(Poison())
        TimerRegistry.registerTimer(Disease())
        TimerRegistry.registerTimer(CropGrowth())
        TimerRegistry.registerTimer(AntiMacro())
    }

    fun loadFile(path: String) : URI? {
        return this::class.java.getResource(path)?.toURI()
    }

    fun advanceTicks(amount: Int, skipPulseUpdates: Boolean = true) {
        log(this::class.java, Log.FINE,  "Advancing ticks by $amount.")
        for(i in 0 until amount) {
            GameWorld.majorUpdateWorker.handleTickActions(skipPulseUpdates)
        }
    }

    fun simulateInteraction (player: Player, target: Node, optionIndex: Int, iface: Int = -1, child: Int = -1) {
        when (target) {
            is GroundItem -> PacketProcessor.enqueue(core.net.packet.`in`.Packet.GroundItemAction(player, optionIndex, target.id, target.location.x, target.location.y))
            is Item -> PacketProcessor.enqueue(core.net.packet.`in`.Packet.ItemAction(player, optionIndex, target.id, target.slot, iface, child))
            is NPC -> PacketProcessor.enqueue(core.net.packet.`in`.Packet.NpcAction(player, optionIndex, target.clientIndex))
            is core.game.node.scenery.Scenery -> PacketProcessor.enqueue(core.net.packet.`in`.Packet.SceneryAction(player, optionIndex, target.id, target.location.x, target.location.y))
        }
        advanceTicks(1, true)
    }
}

class MockPlayer(name: String, val isBot: Boolean) : Player(PlayerDetails(name)), AutoCloseable {
    var hasInit = false
    init { configureBasicProperties(); flagTutComplete(false); init(); flagTutComplete(true) }

    fun configureBasicProperties() {
        this.details.session = MockSession()
        this.location = ServerConstants.HOME_LOCATION
        this.properties.attackStyle = WeaponInterface.AttackStyle(0, WeaponInterface.BONUS_CRUSH)
        this.artificial = isBot
    }

    fun flagTutComplete(complete: Boolean) {
        this.setAttribute("/save:tutorial:complete", complete)
        this.setAttribute("/save:rules:confirmed", complete)
    }

    override fun update() {
        //do nothing. This is for rendering stuff. We don't render a mock player. Not until the spaghetti is less spaghetti.
    }

    override fun init() {
        if (hasInit) return
        hasInit = true
        super.init()
    }

    override fun close() {
        Repository.removePlayer(this)
        UpdateSequence.renderablePlayers.remove(this)
        finishClear()
    }

    override fun setLocation(location: Location?) {
        super.setLocation(location)
        RegionManager.move(this)
        this.playerFlags.lastSceneGraph = location
    }

    fun relog(ticksToWait: Int = -1) {
        val json = PlayerSaver(this).populate()
        val parse = PlayerSaveParser(this)
        parse.saveFile = json

        close()
        timers.clearTimers()
        inventory.clear()
        bank.clear()
        equipment.clear()
        skills = Skills(this)
        savedData = SavedData(this)
        questRepository = QuestRepository(this)
        varpManager = VarpManager(this)
        varpMap.clear()
        saveVarp.clear()
        scripts = ScriptProcessor(this)
        clearAttributes()
        hasInit = false

        if (ticksToWait > 0) TestUtils.advanceTicks(ticksToWait, false)

        configureBasicProperties()
        parse.parseData()
        init()
    }

    override fun debug(string: String?) {
        log (this::class.java, Log.DEBUG, "[$name] -> Received Debug: $string")
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
