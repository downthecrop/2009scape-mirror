package ms.net.packet

import ms.net.IoSession
import ms.system.PunishmentStorage
import ms.system.communication.ClanRank
import ms.system.communication.ClanRepository
import ms.system.communication.CommunicationInfo
import ms.system.util.ManagementConstants.WORLD_HOP_DELAY
import ms.world.GameServer
import ms.world.PlayerSession
import ms.world.WorldDatabase
import ms.world.info.Response
import ms.world.info.UIDInfo
import java.nio.ByteBuffer

/**
 * Repository class for world packets.
 * @author Emperor
 */
object WorldPacketRepository {
    /**
     * Sends the player registry response.
     * @param server The game server.
     * @param player The player session.
     * @param response The registry response.
     */
    @JvmStatic
    fun sendRegistryResponse(server: GameServer, player: PlayerSession, response: Response) {
        val buffer = IoBuffer(0, PacketHeader.BYTE)
        buffer.putString(player.username)
        buffer.put(response.opcode())//byte
        if (response == Response.MOVING_WORLD) {
            val delay: Long = WORLD_HOP_DELAY - (System.currentTimeMillis() - player.disconnectionTime)
            buffer.put((delay / 1000).toInt())
        }
        server.session.write(buffer)
    }

    /**
     * Sends a message to the player.
     * @param player The player.
     * @param message The message to send.
     */
    @JvmStatic
    fun sendPlayerMessage(player: PlayerSession?, message: String) {
        if (player == null) {
            return
        }
        val buffer = IoBuffer(2, PacketHeader.BYTE)
        buffer.putString(player.username)
        buffer.putString(message)
        player.world.session.write(buffer)
    }

    fun sendPlayerMessage(player: PlayerSession?, messages: Array<String>) {
        if (player == null) {
            return
        }
        for (message in messages) sendPlayerMessage(player, message)
    }

    /**
     * Sends the contact information.
     * @param player The player.
     */
    fun sendContactInformation(player: PlayerSession) {
        val info = player.communication
        val buffer = IoBuffer(3, PacketHeader.SHORT)
        buffer.putString(player.username)
        buffer.put(info.contacts.size)
        for (contact in info.contacts.keys) {
            buffer.putString(contact)
            buffer.put(info.getRank(contact).ordinal)
            buffer.put(CommunicationInfo.getWorldId(player, contact))
        }
        buffer.put(info.blocked.size)
        for (contact in info.blocked) {
            buffer.putString(contact)
        }
        if (info.currentClan == null) {
            buffer.put(0)
        } else {
            buffer.put(1)
            buffer.putString(info.currentClan)
        }
        player.world.session.write(buffer)
    }

    /**
     * Sends a contact update.
     * @param player The player who's contacts we're changing.
     * @param contact The contact to update.
     * @param block If we're updating the blocked list.
     * @param remove If the contact should be removed.
     * @param worldId The world id of the contact.
     * @param rank The clan rank.
     */
    @JvmStatic
    fun sendContactUpdate(
        player: PlayerSession,
        contact: String,
        block: Boolean,
        remove: Boolean,
        worldId: Int,
        rank: ClanRank?
    ) {
        val buffer = IoBuffer(4, PacketHeader.BYTE)
        buffer.putString(player.username)
        buffer.putString(contact)
        buffer.put((if (block) 1 else 0))
        if (rank != null) {
            buffer.put((2 + rank.ordinal))
        } else {
            buffer.put((if (remove) 1 else 0))
            if (!block && !remove) {
                buffer.put(worldId)
            }
        }
        player.world.session.write(buffer)
    }

    /**
     * Sends a clan message.
     * @param player The player to send the message to.
     * @param p The player sending the message.
     * @param message The message to send.
     * @param type The message type.
     */
    @JvmStatic
    fun sendMessage(player: PlayerSession, p: PlayerSession, type: Int, message: String) {
        val buffer = IoBuffer(5, PacketHeader.BYTE)
        buffer.putString(player.username)
        buffer.putString(p.username)
        buffer.put(type)
        buffer.put(p.chatIcon)
        buffer.putString(message)
        println("Send Message: ${player.username} -> ${p.username} T: $type ICO: ${p.chatIcon} $message")
        player.world.session.write(buffer)
    }

    /**
     * Sends clan information to the server.
     * @param server The server.
     * @param clan The clan.
     */
    @JvmStatic
    fun sendClanInformation(server: GameServer, clan: ClanRepository) {
        val buffer = IoBuffer(6, PacketHeader.SHORT)
        buffer.putString(clan.owner.username)
        buffer.putString(clan.name)
        var length = clan.players.size
        if (length > ClanRepository.MAX_MEMBERS) {
            length = ClanRepository.MAX_MEMBERS
        }
        buffer.put(length)
        for (i in 0 until length) {
            val player = clan.players[i]
            buffer.putString(player.username)
            buffer.put(player.worldId)
            buffer.put(clan.getRank(player).ordinal)
        }
        buffer.put(clan.joinRequirement.ordinal)
        buffer.put(clan.kickRequirement.ordinal)
        buffer.put(clan.messageRequirement.ordinal)
        buffer.put(clan.lootRequirement.ordinal)
        server.session.write(buffer)
    }

    /**
     * Sends the leave clan packet.
     * @param player The player leaving the clan.
     */
    @JvmStatic
    fun sendLeaveClan(player: PlayerSession) {
        val buffer = IoBuffer(7, PacketHeader.BYTE)
        buffer.putString(player.username)
        player.world.session.write(buffer)
    }

    /**
     * Sends the player login notification.
     * @param server The server.
     * @param player The player logging in.
     * @param names The names.
     */
    @JvmStatic
    fun notifyPlayers(server: GameServer, player: PlayerSession, names: List<String>) {
        val buffer = IoBuffer(8, PacketHeader.SHORT)
        buffer.putString(player.username)
        buffer.put(player.worldId)
        buffer.put(names.size)
        for (name in names) {
            buffer.putString(name)
        }
        server.session.write(buffer)
    }

    /**
     * Notifies the game server a player logged out.
     * @param server The game server to notify.
     * @param player The player logging out.
     */
    @JvmStatic
    fun notifyLogout(server: GameServer, player: PlayerSession) {
        val buffer = IoBuffer(9, PacketHeader.BYTE)
        buffer.putString(player.username)
        server.session.write(buffer)
    }

    /**
     * Sends the update countdown to the server.
     * @param server The server.
     * @param ticks The amount of ticks left.
     */
    @JvmStatic
    fun sendUpdate(server: GameServer, ticks: Int) {
        val buffer = IoBuffer(10)
        buffer.putInt(ticks)
        server.session.write(buffer)
    }

    /**
     * Sends the punishment update packet.
     * @param world The world to send the packet to.
     * @param key The punishment key.
     * @param type The punishment type.
     * @param duration The duration of the punishment.
     */
    @JvmStatic
    fun sendPunishUpdate(world: GameServer, key: String, type: Int, duration: Long) {
        val buffer = IoBuffer(11, PacketHeader.BYTE)
        buffer.putString(key)
        buffer.put(type)
        buffer.putLong(duration)
        world.session.write(buffer)
    }

    /**
     * Sends a configuration reload.
     * @param world the world.
     */
    @JvmStatic
    fun sendConfigReload(world: GameServer) {
        world.session.write(IoBuffer(15, PacketHeader.BYTE))
    }

    /**
     * Handles incoming world packets.
     * @param session The I/O session.
     * @param opcode The opcode.
     * @param b The buffer to read from.
     */
    @JvmStatic
    fun handleIncoming(session: IoSession, opcode: Int, b: ByteBuffer) {
        val buffer = IoBuffer(opcode, PacketHeader.NORMAL, b)
        val server = session.gameServer
        when (opcode) {
            0 -> handlePlayerRegistration(server, buffer)
            1 -> handlePlayerRemoval(server, buffer)
            2 -> handlePunishment(server, buffer)
            3 -> handleCommunicationRequest(server, buffer)
            4, 5 -> handleContactUpdate(server, buffer, opcode == 5)
            6 -> handleJoinClan(server, buffer)
            7 -> handleClanRename(server, buffer)
            8 -> handleClanSetting(server, buffer)
            9 -> handleClanKick(server, buffer)
            10 -> handleClanMessage(server, buffer)
            11 -> handlePrivateMessage(server, buffer)
            12 -> handleClanInfoRequest(server, buffer)
            13 -> handleChatSetting(server, buffer)
            14 -> handleInfoUpdate(server, buffer)
            else -> System.err.println("Handling incoming packet [opcode=" + opcode + ", size=" + b.limit() + "].")
        }
    }

    /**
     * Handles the info of a player update.
     * @param server the server.
     * @param buffer the buffer.
     */
    private fun handleInfoUpdate(server: GameServer, buffer: IoBuffer) {
        val username = buffer.string
        val player = server.players[username]
        if (player != null) {
            player.chatIcon = buffer.get()
        }
    }

    /**
     * Handles a player registration.
     * @param server The game server.
     * @param buffer The buffer.
     */
    private fun handlePlayerRegistration(server: GameServer, buffer: IoBuffer) {
        val username = buffer.string
        val password = buffer.string
        val ipAddress = buffer.string
        val macAddress = buffer.string
        val compName = buffer.string
        val serial = buffer.string
        val rights = buffer.int
        val chatIcon = buffer.get()
        val uid = UIDInfo(ipAddress, compName, macAddress, serial)
        val player = PlayerSession(username, password, UIDInfo(ipAddress, compName, macAddress, serial))
        if (WorldDatabase.isActivePlayer(username)) {
            sendRegistryResponse(server, player, Response.ALREADY_ONLINE)
            return
        }
        player.uid = uid
        player.rights = rights
        player.chatIcon = chatIcon
        if (PunishmentStorage.isSystemBanned(uid)) {
            sendRegistryResponse(server, player, Response.BANNED)
            return
        }
        player.parse()
        if (player.isBanned) {
            sendRegistryResponse(server, player, Response.ACCOUNT_DISABLED)
            return
        }
        if (player.lastWorld != server.info.worldId && player.hasMovedWorld()) {
            sendRegistryResponse(server, player, Response.MOVING_WORLD)
            return
        }
        server.register(player)
    }

    /**
     * Handles the removal of a player.
     * @param server The game server.
     * @param buffer The buffer.
     */
    private fun handlePlayerRemoval(server: GameServer, buffer: IoBuffer) {
        val username = buffer.string
        val session = server.players[username]
        if (session != null) {
            session.isActive = false
            val player = server.players.remove(username)
            if (player != null) {
                session.remove()
            }
            session.worldId = 0
        }
    }

    /**
     * Handles a player registration.
     * @param server The game server.
     * @param buffer The buffer.
     */
    private fun handlePunishment(server: GameServer, buffer: IoBuffer) {
        val type = buffer.get() and 0xFF
        val target = buffer.string
        val duration = buffer.long
        val staff = buffer.string
        PunishmentStorage.handlePunishment(staff, target, type, duration)
    }

    /**
     * Handles the communication info request packet.
     * @param server The game server.
     * @param buffer The buffer.
     */
    private fun handleCommunicationRequest(server: GameServer, buffer: IoBuffer) {
        val username = buffer.string
        val player = server.players[username] ?: return
        sendContactInformation(player)
    }

    /**
     * Handles a contact update packet.
     * @param server The server.
     * @param buffer The buffer to read from.
     * @param block If the list is for blocked players.
     */
    private fun handleContactUpdate(server: GameServer, buffer: IoBuffer, block: Boolean) {
        val username = buffer.string
        val player = server.players[username] ?: return
        val contact = buffer.string
        when (buffer.get()) {
            0 -> {
                if (block) {
                    player.communication.block(contact)
                }
                player.communication.add(contact)
            }
            1 -> player.communication.remove(contact, block)
            2 -> player.communication.updateClanRank(contact, ClanRank.values()[buffer.get()])
        }
    }

    /**
     * Handles a clan related packet.
     * @param server The game server.
     * @param buffer The buffer.
     */
    private fun handleJoinClan(server: GameServer, buffer: IoBuffer) {
        val name = buffer.string
        val clanName = buffer.string
        val player = server.players[name]
        if (player == null || !player.isActive) {
            System.err.println("Invalid player specified in clan packet!")
            return
        }
        if (player.clan != null) {
            player.clan.leave(player, true)
            return
        }
        if (clanName.length < 1) {
            sendLeaveClan(player)
            return
        }
        val clan = ClanRepository.get(server, clanName)
        if (clan == null) {
            sendPlayerMessage(
                player,
                arrayOf(
                    "The channel you tried to join does not exist.",
                    "Try joining the main clan named '2009Scape'.:clan:"
                )
            )
            return
        }
        clan.enter(player)
    }

    /**
     * Handles renaming a clan.
     * @param server The server.
     * @param buffer The buffer.
     */
    private fun handleClanRename(server: GameServer, buffer: IoBuffer) {
        val username = buffer.string
        val name = buffer.string
        val player = server.players[username]
        if (player == null || !player.isActive) {
            return
        }
        val clan = ClanRepository.getClans()[username]
        player.communication.clanName = name
        if (clan != null) {
            if (name.isEmpty()) {
                clan.clean(true)
            } else {
                clan.rename(name)
            }
        }
    }

    /**
     * Handles changing clan settings packet.
     * @param server The game server.
     * @param buffer The buffer.
     */
    private fun handleClanSetting(server: GameServer, buffer: IoBuffer) {
        val username = buffer.string
        val type = buffer.get()
        val rank = if (type < 4) ClanRank.values()[buffer.get() and 0xFF] else null
        val player = server.players[username]
        if (player == null || !player.isActive) {
            return
        }
        val clan = ClanRepository.get(server, username)
        when (type) {
            0 -> {
                player.communication.joinRequirement = rank
                clan?.clean(false)
            }
            1 -> player.communication.messageRequirement = rank
            2 -> {
                player.communication.kickRequirement = rank
                clan?.update()
            }
            3 -> player.communication.lootRequirement = rank
        }
    }

    /**
     * Handles kicking a player from the clan.
     * @param server The game server.
     * @param buffer The buffer.
     */
    private fun handleClanKick(server: GameServer, buffer: IoBuffer) {
        val username = buffer.string
        val playerName = buffer.string
        val player = server.players[username]
        if (player == null || !player.isActive || player.clan == null) {
            return
        }
        val target = WorldDatabase.getPlayer(playerName)
        if (target == null || !target.isActive) {
            return
        }
        player.clan.kick(player, target)
    }

    /**
     * Handles a clan message.
     * @param server The game server.
     * @param buffer The buffer.
     */
    private fun handleClanMessage(server: GameServer, buffer: IoBuffer) {
        val username = buffer.string
        val message = buffer.string
        val player = server.players[username]
        if (player == null || !player.isActive || player.clan == null) {
            return
        }
        player.clan.message(player, message)
    }

    /**
     * Handles a clan message.
     * @param server The game server.
     * @param buffer The buffer.
     */
    private fun handlePrivateMessage(server: GameServer, buffer: IoBuffer) {
        val username = buffer.string
        val receiver = buffer.string
        val message = buffer.string
        val player = server.players[username]
        if (player == null || !player.isActive) {
            return
        }
        player.communication.sendMessage(receiver, message)
    }

    /**
     * Handles a clan information request packet.
     * @param server The server.
     * @param buffer The buffer.
     */
    private fun handleClanInfoRequest(server: GameServer, buffer: IoBuffer) {
        val name = buffer.string
        val clan = ClanRepository.get(server, name) ?: return
        sendClanInformation(server, clan)
    }

    /**
     * Handles a chat setting update packet.
     * @param server The server.
     * @param buffer The buffer.
     */
    private fun handleChatSetting(server: GameServer, buffer: IoBuffer) {
        val name = buffer.string
        val publicSetting = buffer.get()
        val privateSetting = buffer.get()
        val tradeSetting = buffer.get()
        val player = server.players[name]
        if (player == null || !player.isActive) {
            return
        }
        player.communication.updateSettings(publicSetting, privateSetting, tradeSetting)
    }
}