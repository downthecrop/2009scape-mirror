package rs09.worker

import com.google.protobuf.Message
import core.net.packet.PacketRepository
import core.net.packet.context.ContactContext
import core.net.packet.out.ContactPackets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import proto.management.PlayerStatusUpdate
import rs09.game.world.GameWorld
import rs09.game.world.repository.Repository
import java.util.concurrent.BlockingDeque
import java.util.concurrent.LinkedBlockingDeque

/**
 * Processes management-related events e.g clan messages, etc.
 */
object ManagementEvents {
    private var isRunning: Boolean = true
    private val eventQueue: BlockingDeque<Message> = LinkedBlockingDeque()

    val job = GlobalScope.launch {
        while (isRunning) {
            val event = withContext(Dispatchers.IO) { eventQueue.take() }
            handleEvent(event)
        }
    }

    @JvmStatic fun publish(event: Message) {
        eventQueue.offer(event)
    }

    private fun handleEvent(event: Message) {
        when (event) {
            is PlayerStatusUpdate -> {
                val notifiablePlayers = if (event.notifyFriendsOnly) {
                    GameWorld.accountStorage.getOnlineFriends(event.username)
                } else {
                    Repository.playerNames.keys.toList()
                }.filter { Repository.getPlayerByName(it)?.communication?.contacts?.containsKey(event.username) == true }

                for (playerName in notifiablePlayers) {
                    val p = Repository.getPlayerByName(playerName) ?: continue
                    PacketRepository.send(ContactPackets::class.java, ContactContext(p, event.username, event.world))
                }
            }
        }
    }
}