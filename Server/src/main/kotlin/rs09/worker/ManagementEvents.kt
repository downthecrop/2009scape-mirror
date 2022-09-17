package rs09.worker

import api.sendMessage
import com.google.protobuf.Message
import core.game.node.entity.player.Player
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
import proto.management.ClanJoinNotification
import proto.management.ClanLeaveNotification
import proto.management.ClanMessage
import proto.management.FriendUpdate
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
import rs09.ServerConstants
import rs09.auth.UserAccountInfo
import rs09.game.system.SystemLogger
import rs09.game.world.GameWorld
import rs09.game.world.repository.Repository
import java.util.Deque
import java.util.LinkedList
import java.util.concurrent.BlockingDeque
import java.util.concurrent.LinkedBlockingDeque
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Private

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
            try {
                handleEvent(event)
                handleLoggingFor(event)
            } catch (ignored: Exception) {}
        }
    }

    private fun handleLoggingFor(event: Message) {
        when (event) {
            is PlayerStatusUpdate -> SystemLogger.logMS("${event.username} -(WLD)> ${event.world}")
            is RequestContactInfo -> SystemLogger.logMS("${event.username} -> RQ CONTACT INFO")
            is SendContactInfo -> SystemLogger.logMS("${event.username} <- SND CONTACT INFO")
            is PrivateMessage -> SystemLogger.logMS("[PM] ${event.sender}->${event.receiver}: ${event.message}")
            is ClanMessage -> SystemLogger.logMS("[CM:${event.clanName}] ${event.sender}: ${event.message}")
            is JoinClanRequest -> SystemLogger.logMS("${event.username} +CL ${event.clanName}")
            is LeaveClanRequest -> SystemLogger.logMS("${event.username} -CL ${event.clanName}")
            is RequestClanInfo -> SystemLogger.logMS("REQUEST CLAN INFO: ${event.clanOwner}")
            is SendClanInfo -> SystemLogger.logMS("RECEIVE CLAN INFO: ${event.clanOwner}->${event.clanName}")
            is ClanJoinNotification -> SystemLogger.logMS("${event.username} JOINED CLAN ${event.clanName}")
            is ClanLeaveNotification -> SystemLogger.logMS("${event.username} LEFT CLAN ${event.clanName}")
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

                for ((username, contact) in contacts) {
                    val online = Repository.getPlayerByName(username) != null
                    val cbuild = Contact.newBuilder()
                    cbuild.username = username
                    cbuild.world = if (online) GameWorld.settings!!.worldId else 0
                    cbuild.rank = contact.rank.ordinal
                    response.addContacts(cbuild)
                }

                val blocked = info.blocked.split(",")
                for (user in blocked) response.addBlocked(user)

                publish(response.build())
            }

            is SendContactInfo -> {
                val p = Repository.getPlayerByName(event.username) ?: return

                PacketRepository.send(
                    ContactPackets::class.java,
                    ContactContext(p, ContactContext.UPDATE_STATE_TYPE)
                )

                p.communication.contacts.clear()
                p.communication.blocked.clear()

                for (contact in event.contactsList) {
                    val c = core.game.system.communication.Contact(contact.username)
                    p.communication.contacts[contact.username] = c
                    c.worldId = contact.world
                    c.rank = ClanRank.values()[contact.rank]
                    PacketRepository.send(
                        ContactPackets::class.java,
                        ContactContext(p, contact.username, contact.world)
                    )
                }

                for (blocked in event.blockedList) {
                    p.communication.blocked.add(blocked)
                }

                PacketRepository.send(
                    ContactPackets::class.java,
                    ContactContext(p, ContactContext.IGNORE_LIST_TYPE)
                )

            }

            is FriendUpdate -> {
                val remove = event.type == FriendUpdate.Type.REMOVE
                val f = Repository.getPlayerByName(event.friend)
                val p = Repository.getPlayerByName(event.username)
                val world = if (f != null) GameWorld.settings!!.worldId else 0
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
                    p.communication.clan = clan
                }
            }

            is ClanJoinNotification -> {
                if (event.world == GameWorld.settings!!.worldId) return

                if (shouldWaitForClanInfo(event.clanName)) {
                    queueUntilClanInfo(event.clanName, event)
                    return
                }

                val clan = ClanRepository.get(event.clanName)
                val entry = ClanEntry(event.username, event.world)
                clan.players.add(entry)
                clan.update()
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

            is ClanLeaveNotification -> {
                if (shouldWaitForClanInfo(event.clanName)) {
                    queueUntilClanInfo(event.clanName, event)
                    return
                }

                val clan = ClanRepository.get(event.clanName)
                val entry = clan.players.firstOrNull { it.name.equals(event.username) } ?: return
                clan.players.remove(entry)
                clan.update()
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
                        cmBuilder.rank = (clan.ranks[member.name] ?: ClanRank.NONE).ordinal
                        response.addMembers(cmBuilder)
                    }
                }

                publish(response.build())
            }

            is SendClanInfo -> {
                if (event.hasInfo) {
                    initializeClanFrom(event)
                } else {
                    var info = GameWorld.accountStorage.getAccountInfo(event.clanOwner)
                    if (info.clanName.isNotEmpty()) {
                        initializeClanWith(info)
                    } else {
                        SystemLogger.logMS("Creating default server clan")
                        if (GameWorld.settings!!.enable_default_clan && event.clanOwner == ServerConstants.SERVER_NAME) {
                            //Create a user with the default clan and some basic settings and stick them in the account storage
                            if (info == UserAccountInfo.createDefault()) {
                                info.username = ServerConstants.SERVER_NAME
                                info.password = ServerConstants.MS_SECRET_KEY
                                info.rights = 2
                                SystemLogger.logAlert(this::class.java, "Creating default server account: ${info.username}, password is your MS_SECRET_KEY!")
                                GameWorld.authenticator.createAccountWith(info)
                                info = GameWorld.accountStorage.getAccountInfo(event.clanOwner)
                            }

                            info.clanName = "Global"
                            info.clanReqs = "-1,-1,7,7" //Any join, any message, owner kick, owner loot
                            GameWorld.accountStorage.update(info)
                            initializeClanWith(info)
                        }
                    }
                }

                val queuedEvents = waitingOnClanInfo[event.clanOwner] ?: return
                while (queuedEvents.peek() != null) {
                    publish(queuedEvents.pop())
                }
            }

            is ClanMessage -> {
                if (shouldWaitForClanInfo(event.clanName)) {
                    queueUntilClanInfo(event.clanName, event)
                    return
                }

                val clan = ClanRepository.get(event.clanName)

                for (member in clan.players.filter { it.player != null }) {
                    PacketRepository.send(
                        CommunicationMessage::class.java,
                        MessageContext(member.player, event.sender, event.rank, MessageContext.CLAN_MESSAGE, event.message)
                    )
                }
            }

        }
    }

    private fun initializeClanFrom(event: SendClanInfo) {
        val clan = ClanRepository.getClans().getOrPut(event.clanOwner) { ClanRepository(event.clanOwner) }
        clan.name = event.clanName
        clan.joinRequirement = ClanRank.values()[event.joinRequirement]
        clan.kickRequirement = ClanRank.values()[event.kickRequirement]
        clan.messageRequirement = ClanRank.values()[event.messageRequirement]
        clan.lootRequirement = ClanRank.values()[event.lootRequirement]

        for (member in event.membersList) {
            val entry = ClanEntry(member.username, member.world)
            clan.ranks[member.username] = ClanRank.values()[member.rank]
            if (member.world == GameWorld.settings!!.worldId) {
                val p = Repository.getPlayerByName(member.username)
                entry.player = p
                p?.communication?.clan = clan
            }
            clan.players.add(entry)
        }

        clan.update()
    }

    private fun initializeClanWith(info: UserAccountInfo) {
        val reqs = CommunicationInfo.parseClanRequirements(info.clanReqs)
        val c = ClanRepository(info.username)
        val contacts = CommunicationInfo.parseContacts(info.contacts)
        c.name = info.clanName
        c.joinRequirement = reqs[0]
        c.messageRequirement = reqs[1]
        c.kickRequirement = reqs[2]
        c.lootRequirement = reqs[3]
        for ((username, contact) in contacts) {
            c.ranks[username] = contact.rank
        }
        ClanRepository.getClans()[info.username] = c
    }

    private fun queueUntilClanInfo(clanName: String, message: Message) {
        val queue = waitingOnClanInfo.getOrPut(clanName) {LinkedList()}
        queue.offer(message)

        if (hasRequestedClanInfo[clanName] == null) {
            val request = RequestClanInfo.newBuilder()
            request.clanOwner = clanName
            request.world = GameWorld.settings!!.worldId
            publish(request.build())
            hasRequestedClanInfo[clanName] = true
        }
    }

    private fun shouldWaitForClanInfo(clanName: String): Boolean {
        return ClanRepository.get(clanName) == null && hasRequestedClanInfo[clanName] == null
    }
}