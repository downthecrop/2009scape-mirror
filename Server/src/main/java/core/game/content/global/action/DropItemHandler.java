package core.game.content.global.action;

import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.info.login.PlayerParser;
import core.game.node.entity.player.link.audio.Audio;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;
import rs09.game.system.SystemLogger;
import rs09.game.system.config.ItemConfigParser;
import rs09.game.world.GameWorld;

/**
 * Handles the dropping of an item.
 * @author Vexia
 */
public final class DropItemHandler {

	/**
	 * Handles the droping of an item.
	 * @param player the player.
	 * @param node the node.
	 * @param option the option.
	 * @return {@code True} if so.
	 */
	public static boolean handle(final Player player, Node node, String option) {
		Item item = (Item) node;
		if (item.getSlot() == -1) {
			player.getPacketDispatch().sendMessage("Invalid slot!");
			return false;
		}
		switch (option) {
		case "drop":
		case "destroy":
		case "dissolve":
			if (!player.getInterfaceManager().close()) {
				return true;
			}
			player.getDialogueInterpreter().close();
			player.getPulseManager().clear();
			if (option.equalsIgnoreCase("destroy") || option.equalsIgnoreCase("dissolve") || (boolean) item.getDefinition().getHandlers().getOrDefault(ItemConfigParser.DESTROY,false)) {
				player.getDialogueInterpreter().open(9878, item);
				return true;
			}
			if (player.getAttribute("equipLock:" + item.getId(), 0) > GameWorld.getTicks()) {
				SystemLogger.logAlert(player + ", tried to do the drop & equip dupe.");
				return true;
			}
			if (player.getInventory().replace(null, item.getSlot()) == item) {
				item = item.getDropItem();
				player.getAudioManager().send(new Audio(item.getId() == 995 ? 10 : 2739, 1, 0));//2739 ACTUAL DROP SOUND
				GroundItemManager.create(item, player.getLocation(), player);
				PlayerParser.save(player);
			}
			player.setAttribute("droppedItem:" + item.getId(), GameWorld.getTicks() + 2);
			return true;
		}
		return false;
	}

	/**
	 * Drops an item.
	 * @param player the player.
	 * @param item the item.
	 * @return
	 */
	public static boolean drop(Player player, Item item) {
		return handle(player, item, item.getDefinition().hasDestroyAction() ? "destroy" : "drop");
	}
}
