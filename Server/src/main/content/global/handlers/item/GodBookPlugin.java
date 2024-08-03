package content.global.handlers.item;

import core.game.dialogue.DialogueAction;
import content.data.GodBook;
import core.game.node.entity.skill.Skills;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.OptionHandler;
import core.game.interaction.UseWithHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.GroundItem;
import core.game.node.item.Item;
import core.game.node.item.ItemPlugin;
import core.plugin.Plugin;
import core.plugin.Initializable;
import core.plugin.ClassScanner;

/**
 * Handles the god books.
 * @author Vexia
 */
@Initializable
public class GodBookPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (GodBook book : GodBook.values()) {
			book.getDamagedBook().getDefinition().getHandlers().put("option:check", this);
		}
		ClassScanner.definePlugins(new PageHandler(), new GodBookItem());
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		GodBook book = GodBook.forItem(node.asItem(), option.equalsIgnoreCase("check"));
		if (book != null) {
			switch (option) {
			case "check":
				String[] messages = new String[4];
				for (int i = 0; i < messages.length; i++) {
					messages[i] = book.hasPage(player, node.asItem(), i + 1) ? "The " + getNumberName(i + 1) + " page is in the book." : "The " + getNumberName(i + 1) + " page is missing.";
				}
				player.getDialogueInterpreter().sendDialogue(messages);
				return true;
			case "preach":
				player.getDialogueInterpreter().open("god-book", book);
				return true;
			}
		}
		return true;
	}

	/**
	 * A god book item.
	 * @author Vexia
	 */
	public class GodBookItem extends ItemPlugin {

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			for (GodBook book : GodBook.values()) {
				register(book.getDamagedBook().getId());
			}
			return this;
		}

		@Override
		public boolean canPickUp(Player player, GroundItem item, int type) {
			if (player.hasItem(item.asItem())) {
				player.sendMessage("You do not need more than one incomplete book.");
				return false;
			}
			return true;
		}

	}

	/**
	 * The page handler.
	 * @author Vexia
	 */
	public class PageHandler extends UseWithHandler {

		/**
		 * Constructs a new {@code PageHandler} {@code Object}
		 */
		public PageHandler() {
			super(3839, 3841, 3843);
		}

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			for (GodBook book : GodBook.values()) {
				for (Item i : book.getPages()) {
					addHandler(i.getId(), ITEM_TYPE, this);
				}
			}
			return this;
		}

		@Override
		public boolean handle(NodeUsageEvent event) {
			GodBook book = GodBook.forItem(event.getUsedItem(), true);
			Player player = event.getPlayer();
			if (book != null && book.isPage(event.getUsedWith().asItem())) {
				book.insertPage(player, event.getUsedItem(), event.getUsedWith().asItem());
				return true;
			}
			return false;
		}

	}

	/**
	 * Gets the number name.
	 * @param i the integer to check.
	 * @return the number name.
	 */
	private String getNumberName(int i) {
		return i == 1 ? "first" : i == 2 ? "second" : i == 3 ? "third" : "fourth";
	}

}
