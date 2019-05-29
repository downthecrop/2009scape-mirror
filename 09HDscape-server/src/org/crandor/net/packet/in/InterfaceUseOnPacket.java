package org.crandor.net.packet.in;

import org.crandor.ServerConstants;
import org.crandor.game.content.skill.free.magic.MagicSpell;
import org.crandor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.crandor.game.interaction.MovementPulse;
import org.crandor.game.node.entity.combat.CombatSwingHandler;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.SpellBookManager;
import org.crandor.game.node.item.GroundItemManager;
import org.crandor.game.node.item.Item;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.world.GameWorld;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.RegionManager;
import org.crandor.game.world.repository.Repository;
import org.crandor.net.packet.IncomingPacket;
import org.crandor.net.packet.IoBuffer;
import org.crandor.net.packet.PacketRepository;
import org.crandor.net.packet.context.PlayerContext;
import org.crandor.net.packet.out.ClearMinimapFlag;

/**
 * Handles the Interface "Use" on packets.
 * @author Stacx
 */
public class InterfaceUseOnPacket implements IncomingPacket {

	@SuppressWarnings("unused")
	@Override
	public void decode(final Player player, int opcode, IoBuffer buffer) {
		int payload;
		int interfaceId;
		int componentId;
		int itemId;
		int x;
		int y;
		switch (buffer.opcode()) {
		case 73: // Interface On GroundItem
			componentId = buffer.getShort();
			interfaceId = buffer.getShort();
			y = buffer.getShort();
			itemId = buffer.getLEShortA();
			x = buffer.getLEShortA();
			final int spell = buffer.getLEShort();
			final Item groundItem = GroundItemManager.get(itemId, Location.create(x, y, player.getLocation().getZ()), player);
			if (groundItem == null || !player.getLocation().withinDistance(groundItem.getLocation())) {
				break;
			}
			if (player.getAttribute("magic:delay", -1) > GameWorld.getTicks()) {
				break;
			}
			if (player.getZoneMonitor().clickButton(interfaceId, componentId, spell, itemId, opcode)) {
				break;
			}
			if (CombatSwingHandler.isProjectileClipped(player, groundItem, false)) {
				MagicSpell.castSpell(player, SpellBookManager.SpellBook.MODERN, spell, groundItem);
			} else {
				player.getPulseManager().run(new MovementPulse(player, groundItem) {
					@Override
					public boolean update() {
						if (CombatSwingHandler.isProjectileClipped(player, groundItem, false)) {
							super.destination = player.getLocation();
						}
						boolean finished = super.update();
						if (finished) {
							player.getWalkingQueue().reset();
						}
						return finished;

					}

					@Override
					public boolean pulse() {
						MagicSpell.castSpell(player, SpellBookManager.SpellBook.MODERN, spell, groundItem);
						return true;
					}
				}, "movement");
			}
			break;
		case 195: // Interface On Player
			payload = buffer.getShortA();
			componentId = buffer.getLEShort();
			interfaceId = buffer.getLEShort();
			int targetIndex = buffer.getLEShortA();
			// Logger.log("Interface:" + interfaceId+ " Component:" +
			// componentId + " Target Index:"+ targetIndex);
			//System.out.println("magic on player component = "+componentId+"!");
			final Player target = Repository.getPlayers().get(targetIndex);
			if (target == null || !player.getLocation().withinDistance(target.getLocation())) {
				PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
				break;
			}
			switch (interfaceId) {
			case 192:
				MagicSpell.castSpell(player, SpellBookManager.SpellBook.MODERN, componentId, target);
				break;
			case 193:
				MagicSpell.castSpell(player, SpellBookManager.SpellBook.ANCIENT, componentId, target);
				break;
			case 430:
				MagicSpell.castSpell(player, SpellBookManager.SpellBook.LUNAR, componentId, target);
				break;
			case 662:
				switch (componentId) {
				case 67:
				case 69:
				case 119:
				case 121:
				default:
					if (!player.getFamiliarManager().hasFamiliar()) {
						player.getPacketDispatch().sendMessage("You don't have a familiar.");
					} else {
						player.getFamiliarManager().getFamiliar().executeSpecialMove(new FamiliarSpecial(target));
					}
					break;
				}
				break;
			default:
				player.debug("Option usage [inter=" + interfaceId + ", child=" + componentId + ", target=" + target + "].");
			}
			break;
		case 233: // Interface On Object
			y = buffer.getLEShortA();
			x = buffer.getShortA();
			itemId = buffer.getLEShortA();
			if (itemId == 65535) {
				itemId = -1;
			}
			payload = buffer.getIntA();
			interfaceId = payload >> 16;
			componentId = payload & 0xFFFF;
			int objectId = buffer.getShortA();
			player.debug("Option usage [inter=" + interfaceId + ", child=" + componentId + ", target=(" + objectId + "," + x + "," + y + "), item=" + itemId + "].");
			GameObject object = RegionManager.getObject(player.getLocation().getZ(), x, y);
			if (object == null) {
				object = RegionManager.getObject(Location.create(x, y, 0));
			}
			if (object != null) {
				object = object.getChild(player);
			}
			if (object == null || (object.getId() != objectId && object.getWrapper().getId() != objectId) || !player.getLocation().withinDistance(object.getLocation())) {
				PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
				break;
			}
			switch (interfaceId) {
			case 430:
				MagicSpell.castSpell(player, SpellBookManager.SpellBook.LUNAR, componentId, object);
				break;
			case 192:
				MagicSpell.castSpell(player, SpellBookManager.SpellBook.MODERN, componentId, object);
				break;
			case 662:
				switch (componentId) {
				case 137:
				default:
					player.getFamiliarManager().getFamiliar().executeSpecialMove(new FamiliarSpecial(object));
					break;
				}
				break;
			}
			break;
		case 239: // Interface On NPC
			componentId = buffer.getLEShort();
			interfaceId = buffer.getLEShort();
			int unknown = buffer.getShortA();
		    int index = buffer.getLEShortA();
			if (index < 1 || index > ServerConstants.MAX_NPCS) {
				PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
				break;
			}

			NPC npc = Repository.getNpcs().get(index);
			if (npc == null || !player.getLocation().withinDistance(npc.getLocation())) {
				PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
				break;
			}
			if (player.getAttribute("magic:delay", -1) > GameWorld.getTicks()) {
				break;
			}
			switch (interfaceId) {
			case 430:
				MagicSpell.castSpell(player, SpellBookManager.SpellBook.LUNAR, componentId, npc);
				break;
			case 192:
				MagicSpell.castSpell(player, SpellBookManager.SpellBook.MODERN, componentId, npc);
				break;
			case 193:
				MagicSpell.castSpell(player, SpellBookManager.SpellBook.ANCIENT, componentId, npc);
				break;
			case 662:
				switch (componentId) {
				case 67:
				case 69:
				case 177:
				case 121:
				case 119:
				default:
					if (!player.getFamiliarManager().hasFamiliar()) {
						player.getPacketDispatch().sendMessage("You don't have a familiar.");
					} else {
						player.getFamiliarManager().getFamiliar().executeSpecialMove(new FamiliarSpecial(npc));
					}
					break;
				}
				break;
			default:
				player.debug("Option usage [inter=" + interfaceId + ", child=" + componentId + ", target=" + npc + "].");
			}
			break;
		case 253: // Interface On Item
			componentId = buffer.getLEShort();
			interfaceId = buffer.getLEShort();
			int itemSlot = buffer.getLEShortA();
			itemId = buffer.getShortA();
			unknown = buffer.getShortA();
			if (itemSlot < 0 || itemSlot > 27) {
				break;
			}
			Item item = player.getInventory().get(itemSlot);
			if (item == null) {
				break;
			}
			switch (interfaceId) {
			case 430:
				if (player.getAttribute("magic:delay", -1) > GameWorld.getTicks()) {
					break;
				}
				MagicSpell.castSpell(player, SpellBookManager.SpellBook.LUNAR, componentId, item);
				break;
			case 192:
				if (player.getAttribute("magic:delay", -1) > GameWorld.getTicks()) {
					break;
				}
				MagicSpell.castSpell(player, SpellBookManager.SpellBook.MODERN, componentId, item);
				break;
			case 662:
				if (player.getFamiliarManager().hasFamiliar()) {
					player.getFamiliarManager().getFamiliar().executeSpecialMove(new FamiliarSpecial(item, interfaceId, componentId, item));
				} else {
					player.getPacketDispatch().sendMessage("You don't have a follower.");
				}
				break;
			default:
				player.debug("Option usage [inter=" + interfaceId + ", child=" + componentId + ", target=" + item + "].");
			}
			break;
		}
	}
}