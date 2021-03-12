package rs09.game.world.update

import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.world.map.RegionManager
import core.game.world.repository.InitializingNodeList
import core.net.packet.PacketRepository
import core.net.packet.context.PlayerContext
import core.net.packet.out.ClearMinimapFlag
import rs09.game.world.repository.Repository
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

/**
 * The entity update sequence.
 * @author Emperor
 */
class UpdateSequence
/**
 * Constructs a new `ParallelUpdatingSequence` `Object`.
 */
{
    var lobbyList: List<Player>? = null
    var playersList: List<Player>? = null
    var npcList: List<NPC>? = null

    /**
     * Starts the update sequence.
     * @return `True` if we should continue.
     */
    fun start() {
        lobbyList = Repository.lobbyPlayers
        playersList = renderablePlayers
        npcList = Repository.renderableNpcs
        lobbyList!!.map{ PacketRepository.send(ClearMinimapFlag::class.java, PlayerContext(it)) }
        playersList!!.map{player ->
            try {
                player.tick()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        npcList!!.map{npc ->
            try {
                npc.tick()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Runs the updating part of the sequence.
     */
    fun run() {
        val latch = CountDownLatch(renderablePlayers.size)
        playersList!!.forEach(Consumer { player: Player ->
            EXECUTOR.execute {
                try {
                    player.update()
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
                latch.countDown()
            }
        })
        try {
            latch.await(1000L, TimeUnit.MILLISECONDS)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    /**
     * Ends the sequence, calls the [Entity.reset] method..
     */
    fun end() {
        playersList!!.forEach(Consumer { obj: Player -> obj.reset() })
        npcList!!.forEach(Consumer { obj: NPC -> obj.reset() })
        renderablePlayers.sync()
        RegionManager.pulse()
        GroundItemManager.pulse()
    }

    /**
     * Terminates the update sequence.
     */
    fun terminate() {
        EXECUTOR.shutdown()
    }

    companion object {
        /**
         * Gets the renderablePlayers.
         * @return The renderablePlayers.
         */
        /**
         * The list of active players.
         */
        @JvmStatic
        val renderablePlayers = InitializingNodeList<Player>()

        /**
         * The executor used.
         */
        @JvmStatic
        private val EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

    }
}