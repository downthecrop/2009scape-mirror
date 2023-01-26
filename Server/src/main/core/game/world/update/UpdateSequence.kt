package core.game.world.update

import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.world.map.RegionManager
import core.game.world.repository.InitializingNodeList
import core.net.packet.PacketRepository
import core.net.packet.context.PlayerContext
import core.net.packet.out.ClearMinimapFlag
import core.game.world.repository.Repository

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
        renderablePlayers.forEach(Player::tick)
        npcList!!.forEach(NPC::tick)
    }

    /**
     * Runs the updating part of the sequence.
     */
    fun run() {
        renderablePlayers.forEach(Player::update)
    }

    /**
     * Ends the sequence, calls the [Entity.reset] method..
     */
    fun end() {
        playersList!!.forEach(Player::reset)
        npcList!!.forEach(NPC::reset)
        renderablePlayers.sync()
        RegionManager.pulse()
        GroundItemManager.pulse()
    }

    /**
     * Terminates the update sequence.
     */
    fun terminate() {
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

    }
}