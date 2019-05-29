package org.crandor.game.world.update;

import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.info.RenderInfo;
import org.crandor.game.world.GameWorld;
import org.crandor.game.world.map.RegionManager;
import org.crandor.net.packet.IoBuffer;
import org.crandor.net.packet.PacketHeader;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;

/**
 * The NPC renderer.
 * @author Emperor
 */
public final class NPCRenderer {

	/**
	 * Handles the NPC rendering for a player.
	 * @param player The player.
	 */
	public static void render(Player player) {
		IoBuffer buffer = new IoBuffer(32, PacketHeader.SHORT);
		RenderInfo info = player.getRenderInfo();
		List<NPC> localNPCs = info.getLocalNpcs();
		IoBuffer maskBuffer = new IoBuffer(-1, PacketHeader.NORMAL, ByteBuffer.allocate(1 << 16));
		buffer.setBitAccess();
		buffer.putBits(8, localNPCs.size());
		for (Iterator<NPC> it = localNPCs.iterator(); it.hasNext();) {
			NPC npc = it.next();
			boolean withinDistance = player.getLocation().withinDistance(npc.getLocation());
			if (npc.isHidden(player) || !withinDistance || npc.getProperties().isTeleporting()) {
				buffer.putBits(1, 1).putBits(2, 3);
				it.remove();
				if (!withinDistance && npc.getAggressiveHandler() != null) {
					npc.getAggressiveHandler().removeTolerance(player.getIndex());
				}
			} else if (npc.getWalkingQueue().getRunDir() != -1) {
				buffer.putBits(1, 1).putBits(2, 2).putBits(3, npc.getWalkingQueue().getWalkDir()).putBits(3, npc.getWalkingQueue().getRunDir());
				flagMaskUpdate(player, buffer, maskBuffer, npc, false);
			} else if (npc.getWalkingQueue().getWalkDir() != -1) {
				buffer.putBits(1, 1).putBits(2, 1).putBits(3, npc.getWalkingQueue().getWalkDir());
				flagMaskUpdate(player, buffer, maskBuffer, npc, false);
			} else if (npc.getUpdateMasks().isUpdateRequired()) {
				buffer.putBits(1, 1).putBits(2, 0);
				writeMaskUpdates(player, maskBuffer, npc, false);
			} else {
				buffer.putBits(1, 0);
			}
		}
		for (NPC npc : RegionManager.getLocalNpcs(player)) {
			if (localNPCs.size() >= 255) {
				break;
			}
			if (localNPCs.contains(npc) || npc.isHidden(player)) {
				continue;
			}
			buffer.putBits(15, npc.getIndex()).putBits(1, npc.getProperties().isTeleporting() ? 1 : 0).putBits(3, npc.getDirection().ordinal());
			flagMaskUpdate(player, buffer, maskBuffer, npc, true);
			int offsetX = npc.getLocation().getX() - player.getLocation().getX();
			int offsetY = npc.getLocation().getY() - player.getLocation().getY();
			if (offsetX < 0) {
				offsetX += 32;
			}
			if (offsetY < 0) {
				offsetY += 32;
			}
			buffer.putBits(5, offsetY);
			buffer.putBits(14, npc.getId());
			buffer.putBits(5, offsetX);
			if (npc.getAggressiveHandler() != null) {
				npc.getAggressiveHandler().getPlayerTolerance()[player.getIndex()] = GameWorld.getTicks();
			}
			localNPCs.add(npc);
		}
		ByteBuffer masks = maskBuffer.toByteBuffer();
		masks.flip();
		if (masks.hasRemaining()) {
			buffer.putBits(15, 32767);
			buffer.setByteAccess();
			buffer.put(masks);
		} else {
			buffer.setByteAccess();
		}
		player.getSession().write(buffer);
	}

	/**
	 * Sets the mask update flag.
	 * @param buffer The buffer to write on.
	 * @param npc The NPC.
	 */
	private static void flagMaskUpdate(Player player, IoBuffer buffer, IoBuffer maskBuffer, NPC npc, boolean sync) {
		if (npc.getUpdateMasks().isUpdateRequired()) {
			buffer.putBits(1, 1);
			writeMaskUpdates(player, maskBuffer, npc, sync);
		} else {
			buffer.putBits(1, 0);
		}
	}

	/**
	 * Writes the mask updates.
	 * @param maskBuffer The mask buffer to write on.
	 * @param npc The NPC to update.
	 * @param sync If called upon synchronization.
	 */
	public static void writeMaskUpdates(Player player, IoBuffer maskBuffer, NPC npc, boolean sync) {
		if (sync) {
			npc.getUpdateMasks().writeSynced(player, npc, maskBuffer, true);
		} else {
			npc.getUpdateMasks().write(player, npc, maskBuffer);
		}
	}
}