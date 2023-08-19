package core.game.world.repository

import core.api.log
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.login.PlayerParser
import core.game.system.task.TaskExecutor
import core.tools.SystemLogger
import core.game.world.GameWorld
import core.tools.Log
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Handles disconnecting players queuing.
 * @author Emperor
 */
class DisconnectionQueue {
    /**A
     * The pending disconnections queue.
     */
    private val queue = HashMap<String, DisconnectionEntry?>()
    private val queueTimers = HashMap<String, Int>()

    /**
     * Updates all entries.
     */
    fun update() {
        if (queue.isEmpty() || GameWorld.ticks % 3 != 0) {
            return
        }
        //make a copy of current entries as to avoid concurrency exceptions
        val entries = ArrayList(queue.entries)

        //loop through entries and disconnect each
        entries.forEach {
            if(finish(it.value,false)) queue.remove(it.key)
            else {
                //Make sure there's no room for the disconnection queue to stroke out and leave someone logged in for 10 years.
                queueTimers[it.key] = (queueTimers[it.key] ?: 0) + 3
                if ((queueTimers[it.key] ?: Int.MAX_VALUE) >= 1500) {
                    it.value?.player?.let { player ->
                        player.finishClear()
                        Repository.removePlayer(player)
                        remove(it.key)
                        log(this::class.java, Log.WARN, "Force-clearing ${it.key} after 15 minutes of being in the disconnection queue!")
                    }
                }
            }
        }
    }


    fun isEmpty(): Boolean{
        return queue.isEmpty()
    }

    /**
     * Finishes a disconnection.
     * @param entry The entry.
     * @param force If finalization should be forced.
     */
    private fun finish(entry: DisconnectionEntry?, force: Boolean): Boolean {
        val player = entry!!.player
        if (!force && !player.allowRemoval()) {
            return false
        }
        player.packetDispatch.sendLogout()
        player.finishClear()
        Repository.removePlayer(player)
        try {
            if(player.communication.clan != null)
                player.communication.clan.leave(player, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (player.isArtificial) {
            return true
        }
        if (!force) {
            TaskExecutor.executeSQL {
                Thread.currentThread().name = "PlayerSave SQL"
                save(player, true)
            }
            log(this::class.java, Log.INFO, "Player cleared. Removed ${player.details.username}.")
            return true
        }
        save(player, false)
        log(this::class.java, Log.INFO, "Player cleared. Removed ${player.details.username}.")
        return true
    }

    /**
     * Gets a queued player.
     * @param name The player name.
     * @return The player instance.
     */
    operator fun get(name: String?): Player? {
        val entry = queue[name]
        return entry?.player
    }

    /**
     * Clears the queue.
     */
    fun clear() {
        for (entry in queue.values.toTypedArray()) {
            finish(entry, true)
        }
        queue.clear()
    }

    @JvmOverloads
    fun add(player: Player, clear: Boolean = false) {
        if(queue[player.name] != null) return
        queue[player.name] = DisconnectionEntry(player, clear)
        log(this::class.java, Log.INFO, "Queueing ${player.name} for disconnection.")
    }

    operator fun contains(name: String?): Boolean {
        return queue.containsKey(name)
    }

    fun remove(name: String?) {
        queue.remove(name)
        queueTimers.remove(name)
    }

    internal data class DisconnectionEntry(val player: Player, var isClear: Boolean) {}

    /**
     * Saves the player.
     * @param player The player to be saved.
     * @param sql If the sql database should be updated.
     */
    fun save(player: Player, sql: Boolean): Boolean {
        try {
            PlayerParser.saveImmediately(player)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        return false
    }
}
