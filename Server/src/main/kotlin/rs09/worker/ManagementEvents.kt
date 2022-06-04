package rs09.worker

import com.google.protobuf.Message
import core.game.system.communication.CommunicationInfo
import core.net.packet.PacketRepository
import core.net.packet.context.ContactContext
import core.net.packet.out.ContactPackets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import proto.management.PlayerStatusUpdate
import proto.management.RequestContactInfo
import proto.management.SendContactInfo
import proto.management.SendContactInfo.Contact
import rs09.game.system.SystemLogger
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
        SystemLogger.logInfo("Handling ${event.toString()}")
        when (event) {

            is PlayerStatusUpdate -> {
                val notifiablePlayers = if (event.notifyFriendsOnly) {
                    GameWorld.accountStorage.getOnlineFriends(event.username)
                } else {
                    Repository.playerNames.keys.toList()
                }.filter { Repository.getPlayerByName(it)?.communication?.contacts?.containsKey(event.username) == true }

                for (playerName in notifiablePlayers) {
                    val p = Repository.getPlayerByName(playerName) ?: continue
                    p.communication.contacts[event.username]?.worldId = event.world
                    PacketRepository.send(ContactPackets::class.java, ContactContext(p, event.username, event.world))
                }
            }

            is RequestContactInfo -> {
                val response = SendContactInfo.newBuilder()
                response.username = event.username
                val info = GameWorld.accountStorage.getAccountInfo(event.username)
                val contacts = CommunicationInfo.parseContacts(info.contacts)

                for ((username, _) in contacts) {
                    Repository.getPlayerByName(username) ?: continue
                    val cbuild = Contact.newBuilder()
                    cbuild.username = username
                    cbuild.world = GameWorld.settings!!.worldId
                    response.addContacts(cbuild)
                }

                publish(response.build())
            }

            is SendContactInfo -> {
                SystemLogger.logInfo("Got Request__>__>__>__>_>")

                val p = Repository.getPlayerByName(event.username) ?: return

                PacketRepository.send(
                    ContactPackets::class.java,
                    ContactContext(p, ContactContext.UPDATE_STATE_TYPE)
                )

                for (contact in event.contactsList) {
                    val c = core.game.system.communication.Contact(contact.username)
                    p.communication.contacts[contact.username] = c
                    c.worldId = contact.world
                    PacketRepository.send(
                        ContactPackets::class.java,
                        ContactContext(p, contact.username, contact.world)
                    )
                }

                PacketRepository.send(
                    ContactPackets::class.java,
                    ContactContext(p, ContactContext.IGNORE_LIST_TYPE)
                )

            }
        }
    }
}