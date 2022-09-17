package core.net.packet.in;

import core.game.content.quest.PluginInteractionManager;
import core.game.interaction.DestinationFlag;
import core.game.interaction.MovementPulse;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.world.map.RegionManager;
import core.net.packet.IncomingPacket;
import core.net.packet.IoBuffer;
import core.net.packet.PacketRepository;
import core.net.packet.context.PlayerContext;
import core.net.packet.out.ClearMinimapFlag;
import org.rs09.consts.Items;
import rs09.game.interaction.IntType;
import rs09.game.interaction.InteractionListeners;
import rs09.game.node.entity.skill.farming.CompostBins;
import rs09.game.node.entity.skill.farming.FarmingPatch;
import rs09.game.node.entity.skill.farming.UseWithBinHandler;
import rs09.game.node.entity.skill.farming.UseWithPatchHandler;
import rs09.game.system.SystemLogger;
import rs09.game.system.command.rottenpotato.RottenPotatoUseWithHandler;
import rs09.game.world.repository.Repository;

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
        player.debug(String.format("ItemActionPacket.decode(%d)", opcode));
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
			if(itemId == Items.ROTTEN_POTATO_5733){
				RottenPotatoUseWithHandler.handle(npc,player);
				return;
			}
			event = new NodeUsageEvent(player, interfaceId, item, npc);
			if(PluginInteractionManager.handle(player,event)){
				return;
			}
			if(InteractionListeners.run(item,npc,IntType.NPC,player)){
				return;
			}
			if(player.getZoneMonitor().useWith(used,npc)){
				return;
			}
			event = new NodeUsageEvent(player, interfaceId, item, npc);
			try {
				UseWithHandler.run(event);
			} catch (Exception e){
				e.printStackTrace();
			}
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
			if(itemId == Items.ROTTEN_POTATO_5733){
				RottenPotatoUseWithHandler.handle(target,player);
				return;
			}
			event = new NodeUsageEvent(player, interfaceId, item, target);
			if(PluginInteractionManager.handle(player,event)){
				return;
			}
			if(InteractionListeners.run(item, target, IntType.PLAYER, player)){
				return;
			}
			if(player.getZoneMonitor().useWith(used,player)){
				return;
			}
			event = new NodeUsageEvent(player, interfaceId, item, target);
			try {
				UseWithHandler.run(event);
			} catch (Exception e){
				e.printStackTrace();
			}
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
			if(used.getId() == Items.ROTTEN_POTATO_5733){
				RottenPotatoUseWithHandler.handle(with,player);
				return;
			}
			if(with.getId() == Items.ROTTEN_POTATO_5733){
				RottenPotatoUseWithHandler.handle(used,player);
				return;
			}
			if(InteractionListeners.run(used,with,IntType.ITEM,player)){
				return;
			}
			if(player.getZoneMonitor().useWith(used,with)){
				return;
			}
			if (usedItemId < usedWithItemId) {
				item = used;
				used = with;
				with = item;
			}
			player.debug("USED: " + used + " WITH: " + with);
			event = new NodeUsageEvent(player, interfaceId1, used, with);
			if(PluginInteractionManager.handle(player,event)){
				return;
			}
			event = new NodeUsageEvent(player, interfaceId1, used, with);
			try {
				UseWithHandler.run(event);
			} catch (Exception e){
				e.printStackTrace();
			}
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
			Scenery object = RegionManager.getObject(z, x, y);
            //player.debug(String.format("opcode 134: oid %d iid %d", objectId, id));
            //player.debug(String.format("opcode 134: %d %d", object != null ? object.getId() : -1, object != null && object.getChild(player) != null ? object.getChild(player).getId() : -1));
            Scenery child = object != null ? object.getChild(player) : null;
            if(object == null || (object.getId() != objectId && (child == null || child.getId() != objectId))) {
                PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
                return;
            }
			used = player.getInventory().get(slot);
			if (used == null || used.getId() != id) {
				return;
			}
			if(used.getId() == Items.ROTTEN_POTATO_5733){
				RottenPotatoUseWithHandler.handle(object,player);
				return;
			}
			if(InteractionListeners.run(used,child,IntType.SCENERY,player)){
				return;
			}
			if(InteractionListeners.run(used,object,IntType.SCENERY,player)){
				return;
			}
			if(player.getZoneMonitor().useWith(used,object)){
				return;
			}
			event = new NodeUsageEvent(player, 0, used, object);

			/**
			 * Farming-specific handler for item -> patch
			 */
			if(FarmingPatch.forObject(object) != null && UseWithPatchHandler.allowedNodes.contains(used.getId())){
				NodeUsageEvent finalEvent = event;
				player.getPulseManager().run(new MovementPulse(player,object, DestinationFlag.OBJECT) {
					@Override
					public boolean pulse() {
						player.faceLocation(destination.getLocation());
						UseWithPatchHandler.handle(finalEvent);
						return true;
					}
				});
				return;
			}

			/**
			 * Farming-specific handler for item -> compost bin
			 */
			if(CompostBins.forObject(object) != null && UseWithBinHandler.allowedNodes.contains(used.getId())){
				NodeUsageEvent finalEvent = event;
				player.getPulseManager().run(new MovementPulse(player,object,DestinationFlag.OBJECT){
					@Override
					public boolean pulse() {
						player.faceLocation(destination.getLocation());
						UseWithBinHandler.handle(finalEvent);
						return true;
					}
				});
				return;
			}

			if(PluginInteractionManager.handle(player,event)){
				return;
			}

			try {
				UseWithHandler.run(event);
			} catch (Exception e){
				e.printStackTrace();
			}
			return;
		default:
			SystemLogger.logErr(this.getClass(), "Error in Item Action Packet! Unhandled opcode = " + buffer.opcode());
			return;
		}
	}
}
