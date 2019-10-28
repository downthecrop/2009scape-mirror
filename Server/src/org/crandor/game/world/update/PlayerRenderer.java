package org.crandor.game.world.update;

import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.info.RenderInfo;
import org.crandor.game.world.map.RegionManager;
import org.crandor.net.packet.IoBuffer;
import org.crandor.net.packet.PacketHeader;

import java.nio.ByteBuffer;
import java.util.Iterator;

/**
 * Handles the player rendering.
 * @author Emperor
 */
public final class PlayerRenderer {

	/**
	 * The maximum amount of players to add per cycle.
	 */
	private static final int MAX_ADD_COUNT = 10;

	/**
	 * Handles the player rendering for a player.
	 * @param player The player.
	 */
	public static void render(Player player) {
		IoBuffer buffer = new IoBuffer(225, PacketHeader.SHORT);
		IoBuffer flags = new IoBuffer(-1, PacketHeader.NORMAL);
		RenderInfo info = player.getRenderInfo();
		updateLocalPosition(player, buffer, flags);
		buffer.putBits(8, info.getLocalPlayers().size());
		for (Iterator<Player> it = info.getLocalPlayers().iterator(); it.hasNext();) {
			Player other = it.next();
			if (!other.isActive() || !other.getLocation().withinDistance(player.getLocation()) || other.getProperties().isTeleporting() || other.isInvisible()) {
				buffer.putBits(1, 1);
				buffer.putBits(2, 3);
				it.remove();
				continue;
			}
			renderLocalPlayer(player, other, buffer, flags);
		}
		int count = 0;
		for (Player other : RegionManager.getLocalPlayers(player, 15)) {
			if (other == player || !other.isActive() || info.getLocalPlayers().contains(other) || other.isInvisible()) {
				continue;
			}
			if (info.getLocalPlayers().size() >= 255 || ++count == MAX_ADD_COUNT) {
				break;
			}
			addLocalPlayer(player, other, info, buffer, flags);
		}
		ByteBuffer masks = flags.toByteBuffer();
		masks.flip();
		if (masks.hasRemaining()) {
			buffer.putBits(11, 2047);
			buffer.setByteAccess();
			buffer.put(masks);
		} else {
			buffer.setByteAccess();
		}
		player.getDetails().getSession().write(buffer);
	}

	/**
	 * Renders a local player.
	 * @param player The player we're updating for.
	 * @param other The player.
	 * @param buffer The buffer.
	 * @param flags The update flags buffer.
	 */
	private static void renderLocalPlayer(Player player, Player other, IoBuffer buffer, IoBuffer flags) {
		if (other.getWalkingQueue().getRunDir() != -1) {
			buffer.putBits(1, 1); // Updating
			buffer.putBits(2, 2); // Sub opcode
			buffer.putBits(1, 1);
			buffer.putBits(3, other.getWalkingQueue().getWalkDir());
			buffer.putBits(3, other.getWalkingQueue().getRunDir());
			flagMaskUpdate(player, other, buffer, flags, false, false);
		} else if (other.getWalkingQueue().getWalkDir() != -1) {
			buffer.putBits(1, 1); // Updating
			buffer.putBits(2, 1); // Sub opcode
			buffer.putBits(3, other.getWalkingQueue().getWalkDir());
			flagMaskUpdate(player, other, buffer, flags, false, false);
		} else if (other.getUpdateMasks().isUpdateRequired()) {
			buffer.putBits(1, 1);
			buffer.putBits(2, 0);
			writeMaskUpdates(player, other, flags, false, false);
		} else {
			buffer.putBits(1, 0);
		}
	}

	/**
	 * Adds a local player.
	 * @param player The player.
	 * @param other The player to add.
	 * @param info The render info of the player.
	 * @param buffer The buffer.
	 * @param flags The flag based buffer.
	 */
	private static void addLocalPlayer(Player player, Player other, RenderInfo info, IoBuffer buffer, IoBuffer flags) {
		buffer.putBits(11, other.getIndex());
		int offsetX = (other.getLocation().getX() - player.getLocation().getX());
		int offsetY = (other.getLocation().getY() - player.getLocation().getY());
		if (offsetY < 0) {
			offsetY += 32;
		}
		if (offsetX < 0) {
			offsetX += 32;
		}
		boolean appearance = info.getAppearanceStamps()[other.getIndex() & 0x800] != other.getUpdateMasks().getAppearanceStamp();
		boolean update = appearance || other.getUpdateMasks().isUpdateRequired() || other.getUpdateMasks().hasSynced();
		buffer.putBits(1, update ? 1 : 0);
		buffer.putBits(5, offsetX);
		buffer.putBits(3, other.getDirection().ordinal());
		buffer.putBits(1, other.getProperties().isTeleporting() ? 1 : 0);
		buffer.putBits(5, offsetY);
		info.getLocalPlayers().add(other);
		if (update) {
			if (appearance) {
				info.getAppearanceStamps()[other.getIndex() & 0x800] = other.getUpdateMasks().getAppearanceStamp();
			}
			writeMaskUpdates(player, other, flags, appearance, true);
		}
	}

	/**
	 * Updates the local player's client position.
	 * @param local The local player.
	 * @param buffer The i/o buffer.
	 * @param flags The update flags buffer.
	 */
	private static void updateLocalPosition(Player local, IoBuffer buffer, IoBuffer flags) {
		if (local.getPlayerFlags().isUpdateSceneGraph() || local.getProperties().isTeleporting()) {
			buffer.putBits(1, 1); // Updating
			buffer.putBits(2, 3); // Sub opcode
			buffer.putBits(7, local.getLocation().getSceneY(local.getPlayerFlags().getLastSceneGraph()));
			buffer.putBits(1, local.getProperties().isTeleporting() ? 1 : 0);
			buffer.putBits(2, local.getLocation().getZ());
			flagMaskUpdate(local, local, buffer, flags, false, false);
			buffer.putBits(7, local.getLocation().getSceneX(local.getPlayerFlags().getLastSceneGraph()));
		} else {
			renderLocalPlayer(local, local, buffer, flags);
		}
	}

	/**
	 * Sets the update mask flag.
	 * @param local The local player.
	 * @param player The player to update.
	 * @param buffer The packet buffer.
	 * @param maskBuffer The mask buffer.
	 * @param sync If we should use the synced buffer.
	 * @param appearance If appearance update mask should be used in the synced
	 * buffer.
	 */
	private static void flagMaskUpdate(Player local, Player player, IoBuffer buffer, IoBuffer maskBuffer, boolean sync, boolean appearance) {
		if (player.getUpdateMasks().isUpdateRequired()) {
			buffer.putBits(1, 1);
			writeMaskUpdates(local, player, maskBuffer, appearance, sync);
		} else {
			buffer.putBits(1, 0);
		}
	}

	/**
	 * Updates the player flags.
	 * @param local The local player.
	 * @param player The player to update.
	 * @param flags The flags buffer.
	 * @param appearance If we should force appearance.
	 * @param sync If we should use the synced buffer.
	 */
	private static void writeMaskUpdates(Player local, Player player, IoBuffer flags, boolean appearance, boolean sync) {
		if (sync) {
			player.getUpdateMasks().writeSynced(local, player, flags, appearance);
		} else if (player.getUpdateMasks().isUpdateRequired()) {
			player.getUpdateMasks().write(local, player, flags);
		}
	}
}