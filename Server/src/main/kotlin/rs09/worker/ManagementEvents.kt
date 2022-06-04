package rs09.worker

import api.sendMessage
import com.google.protobuf.Message
import core.game.system.communication.ClanEntry
import core.game.system.communication.ClanRank
import core.game.system.communication.ClanRepository
import core.game.system.communication.CommunicationInfo
import core.net.packet.PacketRepository
import core.net.packet.context.ContactContext
import core.net.packet.context.MessageContext
import core.net.packet.out.CommunicationMessage
import core.net.packet.out.ContactPackets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import proto.management.JoinClanRequest
import proto.management.LeaveClanRequest
import proto.management.PlayerStatusUpdate
import proto.management.PrivateMessage
import proto.management.RequestClanInfo
import proto.management.RequestContactInfo
import proto.management.SendClanInfo
import proto.management.SendClanInfo.ClanMember
import proto.management.SendContactInfo
import proto.management.SendContactInfo.Contact
import rs09.game.system.SystemLogger
import rs09.game.world.GameWorld
import rs09.game.world.repository.Repository
import java.util.Deque
import java.util.LinkedList
import java.util.concurrent.BlockingDeque
import java.util.concurrent.LinkedBlockingDeque

/**
 * Processes management-related events e.g clan messages, etc.
 */
object ManagementEvents {
    private var isRunning: Boolean = true
    private val eventQueue: BlockingDeque<Message> = LinkedBlockingDeque()
    private val waitingOnClanInfo = HashMap<String, Deque<Message>>()
    private val hasRequestedClanInfo = HashMap<String, Boolean>()

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
                    val online = Repository.getPlayerByName(username) != null
                    val cbuild = Contact.newBuilder()
                    cbuild.username = username
                    cbuild.world = if (online) GameWorld.settings!!.worldId else 0
                    response.addContacts(cbuild)
                }

                publish(response.build())
            }

            is SendContactInfo -> {
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

            is PrivateMessage -> {
                val sender = Repository.getPlayerByName(event.sender)
                val receiver = Repository.getPlayerByName(event.receiver)

                if (sender != null) {
                    PacketRepository.send(
                        CommunicationMessage::class.java,
                        MessageContext(sender, event.receiver, event.rank, MessageContext.SEND_MESSAGE, event.message)
                    )
                }

                if (receiver != null) {
                    PacketRepository.send(
                        CommunicationMessage::class.java,
                        MessageContext(receiver, event.sender, event.rank, MessageContext.RECIEVE_MESSAGE, event.message)
                    )
                }
            }

            is JoinClanRequest -> {
                val p = Repository.getPlayerByName(event.username) ?: return

                if (shouldWaitForClanInfo(event.clanName)) {
                    queueUntilClanInfo(event.clanName, event)
                    return
                }

                val clan = ClanRepository.get(event.clanName)

                if (clan == null) {
                    sendMessage(p, "The channel you tried to join does not exist.:clan:")
                } else {
                    clan.enter(p)
                }
            }

            is LeaveClanRequest -> {
                val p = Repository.getPlayerByName(event.username) ?: return

                if (shouldWaitForClanInfo(event.clanName)) {
                    queueUntilClanInfo(event.clanName, event)
                    return
                }

                val clan = ClanRepository.get(event.clanName)

                if (clan == null) {
                    sendMessage(p, "Error leaving clan. Please relog.")
                } else {
                    clan.leave(p, true)
                }
            }

            is RequestClanInfo -> {
                val clan = ClanRepository.get(event.clanOwner)
                val response = SendClanInfo.newBuilder()

                if (clan == null) {
                    response.hasInfo = false
                    response.clanOwner = event.clanOwner
                } else {
                    response.hasInfo = true
                    response.clanName = clan.name
                    response.clanOwner = event.clanOwner

                    for (member in clan.players) {
                        val cmBuilder = ClanMember.newBuilder()
                        cmBuilder.username = member.name
                        cmBuilder.world = member.worldId
                        response.addMembers(cmBuilder)
                    }
                }

                publish(response.build())
            }

            is SendClanInfo -> {
                if (event.hasInfo) {
                    val clan = ClanRepository.getClans().getOrPut(event.clanOwner) { ClanRepository(event.clanOwner) }
                    clan.name = event.clanName
                    clan.joinRequirement = ClanRank.values()[event.joinRequirement]
                    clan.kickRequirement = ClanRank.values()[event.kickRequirement]
                    clan.messageRequirement = ClanRank.values()[event.messageRequirement]
                    clan.lootRequirement = ClanRank.values()[event.lootRequirement]

                    for (member in event.membersList) {
                        val entry = ClanEntry(member.username, member.world)
                        clan.ranks[member.username] = ClanRank.NONE //TODO: SEND AND SET ACTUAL RANKS
                        if (member.world == GameWorld.settings!!.worldId) {
                            val p = Repository.getPlayerByName(member.username)
                            entry.player = p
                            p?.communication?.clan = clan
                        }
                        clan.players.add(entry)
                    }

                    clan.update()
                } else {
                    val info = GameWorld.accountStorage.getAccountInfo(event.clanOwner)
                    if (info.clanName.isNotEmpty()) {
                        val reqs = CommunicationInfo.parseClanRequirements(info.clanReqs)
                        val c = ClanRepository(event.clanOwner)
                        c.name = info.clanName
                        c.joinRequirement = reqs[0]
                        c.messageRequirement = reqs[1]
                        c.kickRequirement = reqs[2]
                        c.lootRequirement = reqs[3]
                        ClanRepository.getClans()[event.clanOwner] = c
                    }
                }

                val queuedEvents = waitingOnClanInfo[event.clanOwner] ?: return
                while (queuedEvents.peek() != null) {
                    publish(queuedEvents.pop())
                }
            }

        }
    }

    private fun queueUntilClanInfo(clanName: String, message: Message) {
        val queue = waitingOnClanInfo.getOrPut(clanName) {LinkedList()}
        queue.offer(message)

        if (hasRequestedClanInfo[clanName] == false) {
            val request = RequestClanInfo.newBuilder()
            request.clanOwner = clanName
            request.world = GameWorld.settings!!.worldId
            publish(request.build())
            hasRequestedClanInfo[clanName] = true
        }
    }

    private fun shouldWaitForClanInfo(clanName: String): Boolean {
        return ClanRepository.get(clanName) == null && hasRequestedClanInfo[clanName] == false
    }
}