package core.game.node.entity.skill.construction.decoration.kitchen;


import core.cache.def.impl.SceneryDefinition;
import core.game.content.dialogue.DialoguePlugin;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;
import rs09.plugin.ClassScanner;

/**
 * Handles the shelves in the kitchen room.
 * @author Splinter
 */
@Initializable
public final class ShelfPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ClassScanner.definePlugin(new ShelfDialogue());
		for (int i = 13545; i < 13552; i++) {
			SceneryDefinition.forId(i).getHandlers().put("option:search", this);
		}
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		player.getDialogueInterpreter().open(778341, node.getId());
		return true;
	}

	/**
	 * Dialogue options for the shelves, what a mess!
	 * @author Splinter
	 * @version 1.0
	 */
	public final class ShelfDialogue extends DialoguePlugin {

		/**
		 * Constructs a new {@code ShelfDialogue} {@code Object}.
		 */
		public ShelfDialogue() {
			/**
			 * empty.
			 */
		}
		
		/**
		 * Constructs a new {@code ShelfDialogue} {@code Object}.
		 * @param player the player.
		 */
		public ShelfDialogue(Player player) {
			super(player);
		}

		@Override
		public DialoguePlugin newInstance(Player player) {
			return new ShelfDialogue(player);
		}

		@Override
		public boolean open(Object... args) {
			int id = (int) args[0];
			switch (id) {
			case 13545:// wood 1
				interpreter.sendOptions("Select an Option", "Kettle", "Teapot", "Clay cup");
				stage = 1;
				break;
			case 13546:// wood 2
				interpreter.sendOptions("Select an Option", "Kettle", "Teapot", "Clay cup", "Empty beer glass");
				stage = 1;
				break;
			case 13547:// wood 3
				interpreter.sendOptions("Select an Option", "Kettle", "Teapot", "Clay cup", "Empty beer glass", "Cake tin");
				stage = 1;
				break;
			case 13548:// oak 1
				interpreter.sendOptions("Select an Option", "Kettle", "Teapot", "Clay cup", "Empty beer glass", "Bowl");
				stage = 2;
				break;
			case 13549:// oak 2
				interpreter.sendOptions("Select an Option", "Kettle", "Teapot", "Porcelain cup", "Empty beer glass", "More Options");
				stage = 3;
				break;
			case 13550:// teak 1
			case 13551:
				interpreter.sendOptions("Select an Option", "Kettle", "Teapot", "Porcelain cup", "Empty beer glass", "More Options");
				stage = 5;
				break;
			}
			return true;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			if (player.getInventory().freeSlots() < 1) {
				player.sendMessage("You need at least one free inventory space to take from the shelves.");
				end();
				return true;
			}
			switch (stage) {
			case 1:// all wood shelves
				switch (buttonId) {
				case 1:
					end();
					player.getInventory().add(new Item(7688, 1));
					break;
				case 2:
					end();
					player.getInventory().add(new Item(7702, 1));
					break;
				case 3:
					end();
					player.getInventory().add(new Item(7728, 1));
					break;
				case 4:
					end();
					player.getInventory().add(new Item(1919, 1));
					break;
				case 5:
					end();
					player.getInventory().add(new Item(1887, 1));
					break;
				}
				break;
			case 2:// Oak shelf #1
				switch (buttonId) {
				case 1:
					end();
					player.getInventory().add(new Item(7688, 1));
					break;
				case 2:
					end();
					player.getInventory().add(new Item(7702, 1));
					break;
				case 3:
					end();
					player.getInventory().add(new Item(7728, 1));
					break;
				case 4:
					end();
					player.getInventory().add(new Item(1919, 1));
					break;
				case 5:
					end();
					player.getInventory().add(new Item(1923, 1));
					break;
				}
				break;
			case 3:// Oak shelves #2 only
				switch (buttonId) {
				case 1:
					end();
					player.getInventory().add(new Item(7688, 1));
					break;
				case 2:
					end();
					player.getInventory().add(new Item(7702, 1));
					break;
				case 3:
					end();
					player.getInventory().add(new Item(4244, 1));
					break;
				case 4:
					end();
					player.getInventory().add(new Item(1919, 1));
					break;
				case 5:
					interpreter.sendOptions("Select an Option", "Bowl", "Cake tin");
					stage = 4;
					break;
				}
				break;
			case 4:// Oak shelves #2 only
				switch (buttonId) {
				case 1:
					end();
					player.getInventory().add(new Item(1923, 1));
					break;
				case 2:
					end();
					player.getInventory().add(new Item(1887, 1));
					break;
				}
			case 5:// teak shelves
				switch (buttonId) {
				case 1:
					end();
					player.getInventory().add(new Item(7688, 1));
					break;
				case 2:
					end();
					player.getInventory().add(new Item(7702, 1));
					break;
				case 3:
					end();
					player.getInventory().add(new Item(7735, 1));
					break;
				case 4:
					end();
					player.getInventory().add(new Item(1919, 1));
					break;
				case 5:
					interpreter.sendOptions("Select an Option", "Bowl", "Pie dish", "Empty pot");
					stage = 6;
					break;
				}
				break;
			case 6:// teak shelves
				switch (buttonId) {
				case 1:
					end();
					player.getInventory().add(new Item(1923, 1));
					break;
				case 2:
					end();
					player.getInventory().add(new Item(2313, 1));
					break;
				case 3:
					end();
					player.getInventory().add(new Item(1931, 1));
					break;
				}
				break;
			}
			return true;
		}

		@Override
		public int[] getIds() {
			return new int[] { 778341 };
		}
	}
}