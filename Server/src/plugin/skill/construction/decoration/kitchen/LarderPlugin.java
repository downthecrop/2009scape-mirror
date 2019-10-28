package plugin.skill.construction.decoration.kitchen;


import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.PluginManager;

/**
 * Handles the interactions for the three Larders.
 * @author Splinter
 */
@InitializablePlugin
public final class LarderPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		PluginManager.definePlugin(new LarderDialogue());
		ObjectDefinition.forId(13565).getConfigurations().put("option:search", this);
		ObjectDefinition.forId(13566).getConfigurations().put("option:search", this);
		ObjectDefinition.forId(13567).getConfigurations().put("option:search", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		player.getDialogueInterpreter().open(42048, node.getId());
		return true;
	}

	/**
	 * Dialogue options for the Larders.
	 * @author Splinter
	 * @version 1.0
	 */
	public final class LarderDialogue extends DialoguePlugin {

		/**
		 * Constructs a new {@code LarderDialogue} {@code Object}.
		 */
		public LarderDialogue() {
			/**
			 * empty.
			 */
		}

		/**
		 * Constructs a new {@code LarderDialogue} {@code Object}.
		 * 
		 * @param player
		 *            the player.
		 */
		public LarderDialogue(Player player) {
			super(player);
		}

		@Override
		public DialoguePlugin newInstance(Player player) {
			return new LarderDialogue(player);
		}

		@Override
		public boolean open(Object... args) {
			int id = (int) args[0];
			switch (id) {
			case 13565:
				interpreter.sendOptions("Select an Option", "Tea Leaves", "Bucket of Milk");
				stage = 1;
				break;
			case 13566:
				interpreter.sendOptions("Select an Option", "Tea Leaves", "Bucket of Milk", "Eggs", "Pot of Flour");
				stage = 1;
				break;
			case 13567:
				interpreter.sendOptions("Select an Option", "Tea Leaves", "Bucket of Milk", "Eggs", "Pot of Flour", "More Options");
				stage = 1;
				break;
			}
			return true;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			if (player.getInventory().freeSlots() < 1) {
				player.sendMessage("You need at least one free inventory space to take from the larder.");
				end();
				return true;
			}
			switch (stage) {
			case 1:
				switch (buttonId) {
				case 1:
					player.getInventory().add(new Item(7738, 1));
					end();
					break;
				case 2:
					player.getInventory().add(new Item(1927, 1));
					end();
					break;
				case 3:
					player.getInventory().add(new Item(1944, 1));
					end();
					break;
				case 4:
					player.getInventory().add(new Item(1933, 1));
					end();
					break;
				case 5:
					player.getDialogueInterpreter().sendOptions(
							"Select an Option", "Potatoes", "Garlic", "Onions", "Cheese");
					stage = 2;
					break;
				}
				break;
			case 2:
				switch (buttonId) {
				case 1:
					player.getInventory().add(new Item(1942, 1));
					end();
					break;
				case 2:
					player.getInventory().add(new Item(1550, 1));
					end();
					break;
				case 3:
					player.getInventory().add(new Item(1957, 1));
					end();
					break;
				case 4:
					player.getInventory().add(new Item(1985, 1));
					end();
					break;
				}
			}
			return true;
		}

		@Override
		public int[] getIds() {
			return new int[] { 42048 };
		}
	}
}