package org.crandor.net.packet.in;

import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.system.SystemLogger;
import org.crandor.game.world.map.RegionManager;
import org.crandor.game.world.repository.Repository;
import org.crandor.net.packet.IncomingPacket;
import org.crandor.net.packet.IoBuffer;
import org.crandor.net.packet.PacketRepository;
import org.crandor.net.packet.context.PlayerContext;
import org.crandor.net.packet.out.ClearMinimapFlag;

/**
 * The incoming item reward packet.
 * @author Emperor
 */
public class ItemActionPacket implements IncomingPacket {

	@SuppressWarnings("unused")
	@Override
	public void decode(Player player, int opcode, IoBuffer buffer) {
		if (player.getLocks().isInteractionLocked()) {
			return;
		}
		int usedWithItemId = -1;
		int usedWithSlot = -1;
		int interfaceHash1 = -1;
		int interfaceId1 = -1;
		int childId1 = -1;
		int usedItemId = -1;
		int usedSlot = -1;
		int interfaceHash2 = -1;
		int interfaceId2 = -1;
		int childId2 = -1;
		NodeUsageEvent event = null;
		Item used = null;
		switch (buffer.opcode()) {
		case 115: // Item on NPC
			int interfaceId = buffer.getIntA() >> 16;
			int slotId = buffer.getLEShort();
			int npcIndex = buffer.getLEShort();
			int itemId = buffer.getLEShortA();
			NPC npc = Repository.getNpcs().get(npcIndex);
			Item item = player.getInventory().get(slotId);
			if (item == null || item.getId() != itemId) {
				return;
			}
			event = new NodeUsageEvent(player, interfaceId, item, npc);
			UseWithHandler.run(event);
			return;
		case 248: // Item on Player
			int playerIndex = buffer.getLEShortA();
			itemId = buffer.getShort();
			slotId = buffer.getShort();
			interfaceId = buffer.getInt() >> 16;
			Player target = Repository.getPlayers().get(playerIndex);
			item = player.getInventory().get(slotId);
			if (target == null || item == null || item.getId() != itemId) {
				return;
			}
			event = new NodeUsageEvent(player, interfaceId, item, target);
			UseWithHandler.run(event);
			return;
		case 27://Item on Item
			usedSlot = buffer.getShort();
			interfaceHash1 = buffer.getLEInt();
			usedWithSlot = buffer.getLEShort();
			interfaceHash2 = buffer.getLEInt();
			usedItemId = buffer.getLEShortA();
			usedWithItemId = buffer.getLEShortA();
			interfaceId1 = interfaceHash1 >> 16;
			childId1 = interfaceHash1 & 0xFFFF;
			interfaceId2 = interfaceHash2 >> 16;
			childId2 = interfaceHash2 & 0xFFFF;
			used = player.getInventory().get(usedSlot);
			Item with = player.getInventory().get(usedWithSlot);
			if (used == null || with == null || used.getId() != usedItemId || with.getId() != usedWithItemId) {
				return;
			}
			if (usedItemId < usedWithItemId) {
				item = used;
				used = with;
				with = item;
			}
			event = new NodeUsageEvent(player, interfaceId1, used, with);
			UseWithHandler.run(event);
			break;
		case 134://Item on Object
			int x = buffer.getShortA();
			int id = buffer.getShort();
			int y = buffer.getLEShort();
			int slot = buffer.getShort();
			buffer.getLEShort();
			buffer.getShort();
			int objectId = buffer.getShortA();
			int z = player.getLocation().getZ();
			GameObject object = RegionManager.getObject(z, x, y);
			if (object == null || object.getId() != objectId) {
				PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
				return;
			}
			object = object.getChild(player);
			if (object == null) {
				PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
				break;
			}
			used = player.getInventory().get(slot);
			if (used == null || used.getId() != id) {
				return;
			}
			event = new NodeUsageEvent(player, 0, used, object);
			UseWithHandler.run(event);
			return;
		default:
			SystemLogger.error("Error in Item Action Packet! Unhandled opcode = " + buffer.opcode());
			return;
		}
	}
}