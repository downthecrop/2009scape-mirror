package core.net.amsc;

import core.game.node.entity.player.Player;
import core.game.node.entity.player.info.PlayerDetails;
import core.game.node.entity.player.info.Rights;
import core.game.node.entity.player.info.login.Response;
import core.game.system.SystemManager;
import core.game.system.SystemState;
import core.game.system.communication.*;
import core.net.packet.IoBuffer;
import core.net.packet.PacketHeader;
import core.net.packet.PacketRepository;
import core.net.packet.context.ClanContext;
import core.net.packet.context.ContactContext;
import core.net.packet.context.MessageContext;
import core.net.packet.out.CommunicationMessage;
import core.net.packet.out.ContactPackets;
import core.net.packet.out.UpdateClanChat;
import rs09.auth.AuthResponse;
import rs09.game.node.entity.player.info.login.LoginParser;
import rs09.game.system.SystemLogger;
import rs09.game.world.GameWorld;
import rs09.game.world.repository.Repository;

import java.nio.ByteBuffer;

/**
 * The Management server packet repository.
 * @author Emperor
 */
public final class MSPacketRepository {
	/**
	 * Sends a contact update.
	 *
	 * @param username The username.
	 * @param contact  The contact's username.
	 * @param remove   If we're removing the contact from the list.
	 * @param block    If the contact list is for the blocked players.
	 * @param rank     The new clan rank (or null when not updating clan rank!).
	 */
	public static void sendContactUpdate(String username, String contact, boolean remove, boolean block, ClanRank rank) {
		IoBuffer buffer = new IoBuffer(block ? 5 : 4, PacketHeader.BYTE);
		buffer.putString(username);
		buffer.putString(contact);
		if (rank != null) {
			buffer.put((byte) 2);
			buffer.put((byte) rank.ordinal());
		} else {
			buffer.put((byte) (remove ? 1 : 0));
		}
		WorldCommunicator.getSession().write(buffer);
	}

	/**
	 * Sends the clan rename packet.
	 *
	 * @param player   The player.
	 * @param clanName The clan name.
	 */
	public static void sendClanRename(Player player, String clanName) {
		IoBuffer buffer = new IoBuffer(7, PacketHeader.BYTE);
		buffer.putString(player.getName());
		buffer.putString(clanName);
		WorldCommunicator.getSession().write(buffer);
	}

	/**
	 * Sets a clan setting.
	 *
	 * @param player The player.
	 * @param type   The setting type.
	 * @param rank   The rank to set.
	 */
	public static void setClanSetting(Player player, int type, ClanRank rank) {
		if (!WorldCommunicator.isEnabled()) {
			return;
		}
		IoBuffer buffer = new IoBuffer(8, PacketHeader.BYTE);
		buffer.putString(player.getName());
		buffer.put((byte) type);
		if (rank != null) {
			buffer.put((byte) rank.ordinal());
		}
		WorldCommunicator.getSession().write(buffer);
	}

	/**
	 * Sends the kicking a clan member packet.
	 *
	 * @param username The player's username.
	 * @param name     The name.
	 */
	public static void sendClanKick(String username, String name) {
		IoBuffer buffer = new IoBuffer(9, PacketHeader.BYTE);
		buffer.putString(username);
		buffer.putString(name);
		WorldCommunicator.getSession().write(buffer);
	}

	/**
	 * Sends the chat settings.
	 *
	 * @param player         The player.
	 * @param publicSetting  The public chat setting.
	 * @param privateSetting The private chat setting.
	 * @param tradeSetting   The trade setting.
	 */
	public static void sendChatSetting(Player player, int publicSetting, int privateSetting, int tradeSetting) {
		IoBuffer buffer = new IoBuffer(13, PacketHeader.BYTE);
		buffer.putString(player.getName());
		buffer.put(publicSetting);
		buffer.put(privateSetting);
		buffer.put(tradeSetting);
		WorldCommunicator.getSession().write(buffer);
	}
}