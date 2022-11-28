package core.net.packet.in;

import core.cache.def.impl.ItemDefinition;
import core.cache.def.impl.NPCDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.cache.def.impl.VarbitDefinition;
import core.game.node.entity.player.Player;
import core.net.packet.IncomingPacket;
import core.net.packet.IoBuffer;
import rs09.cache.Cache;
import rs09.cache.CacheIndex;

import java.util.Arrays;

/**
 * Handles the incoming examine packets.
 * @author Emperor
 */
public final class ExaminePacket implements IncomingPacket {

	@Override
	public void decode(Player player, int opcode, IoBuffer buffer) {
		String name;
		switch (buffer.opcode()) {
		case 94: // Object examine
			int id = buffer.getLEShortA();
			if (id < 0 || id > Cache.getIndexCapacity(CacheIndex.SCENERY_CONFIGURATION)) {
				break;
			}
			SceneryDefinition d = SceneryDefinition.forId(id);
			name = d.getExamine();
			//String coords = id + ", " + player.getLocation().getX() + ", " + player.getLocation().getY() + ", " + player.getLocation().getZ();
			player.debug("Object id: " + id + ", models: " + (d.getModelIds() != null ? Arrays.toString(d.getModelIds()) : null) + ", anim: " + d.animationId + ", config: " + (d.getVarbitID() != -1 ? d.getVarbitID() + " (file)" : d.getConfigId()) + ".");
			player.debug("Varp config index: " + VarbitDefinition.forObjectID(d.getVarbitID()).getVarpId());
			player.getPacketDispatch().sendMessage(""+name+"");
			/*if {
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection("LandscapeParser.removeGameObject(new GameObject("+coords+"));//"+ d.getName() ), null);
			}*/
			break;
		case 235:
		case 92: // Item examine
			id = buffer.getLEShortA();
			if (id < 0 || id > Cache.getIndexCapacity(CacheIndex.ITEM_CONFIGURATION)) {
				break;
			}
			ItemDefinition itemDef = ItemDefinition.forId(id);
			player.getPacketDispatch().sendMessage(itemDef.getExamine());
			break;
		case 72: // NPC examine
			id = buffer.getShort();
			if (id < 0 || id > Cache.getIndexCapacity(CacheIndex.NPC_CONFIGURATION)) {
				break;
			}
			player.debug("NPC id: " + id + ".");
			NPCDefinition def = NPCDefinition.forId(id);
			if (def == null) {
				break;
			}
			player.getPacketDispatch().sendMessage(def.getExamine());
			break;
		}
	}
}
