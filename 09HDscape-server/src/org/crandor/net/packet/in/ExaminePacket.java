package org.crandor.net.packet.in;

import org.crandor.cache.Cache;
import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.cache.def.impl.NPCDefinition;
import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.node.entity.player.Player;
import org.crandor.net.packet.IncomingPacket;
import org.crandor.net.packet.IoBuffer;

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
			if (id < 0 || id > Cache.getObjectDefinitionsSize()) {
				break;
			}
			ObjectDefinition d = ObjectDefinition.forId(id);
			name = d.getExamine();
			//String coords = id + ", " + player.getLocation().getX() + ", " + player.getLocation().getY() + ", " + player.getLocation().getZ();
			player.debug("Object id: " + id + ", models: " + (d.getModelIds() != null ? Arrays.toString(d.getModelIds()) : null) + ", anim: " + d.animationId + ", config: " + (d.getConfigFileId() != -1 ? d.getConfigFileId() + " (file)" : d.getConfigId()) + ".");
			player.getPacketDispatch().sendMessage(""+name+"");
			/*if (player.getName().equalsIgnoreCase("ethan") || player.getName().equalsIgnoreCase("austin")) {
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection("LandscapeParser.removeGameObject(new GameObject("+coords+"));//"+ d.getName() ), null);
			}*/
			break;
		case 235:
		case 92: // Item examine
			id = buffer.getLEShortA();
			if (id < 0 || id > Cache.getItemDefinitionsSize()) {
				break;
			}
			player.getPacketDispatch().sendMessage(getItemExamine(id));
			break;
		case 72: // NPC examine
			id = buffer.getShort();
			if (id < 0 || id > Cache.getNPCDefinitionsSize()) {
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

	/**
	 * Gets the item examine.
	 * @param id the id.
	 * @return the name.
	 */
	public static String getItemExamine(int id) {
		if (id == 995) {
			return "Lovely money!";
		}
		if (ItemDefinition.forId(id).getExamine().length() == 255) {
			return "A set of instructions to be followed.";
		}
		return ItemDefinition.forId(id).getExamine();
	}
}