package core.game.interaction.item;

import core.game.content.dialogue.DialogueAction;
import core.game.content.dialogue.DialogueInterpreter;
import core.game.content.dialogue.DialoguePlugin;
import core.game.content.global.GodBook;
import core.game.node.entity.skill.Skills;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.OptionHandler;
import core.game.interaction.UseWithHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.GroundItem;
import core.game.node.item.Item;
import core.game.node.item.ItemPlugin;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;
import core.plugin.Initializable;
import rs09.plugin.PluginManager;

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
		PluginManager.definePlugins(new PageHandler(), new GodBookItem(), new SymbolBlessHandler());
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
	 * Handles the blessing of a symbol with a god book.
	 * @author Vexia
	 */
	public class SymbolBlessHandler extends UseWithHandler {

		/**
		 * Constructs a new {@code SymbolBlessHandler} {@code Object}
		 */
		public SymbolBlessHandler() {
			super(1716);
		}

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			for (GodBook book : GodBook.values()) {
				addHandler(book.getBook().getId(), ITEM_TYPE, this);
			}
			return this;
		}

		@Override
		public boolean handle(NodeUsageEvent event) {
			Player player = event.getPlayer();
			GodBook book = GodBook.forItem(event.getUsedItem(), false);
			if (book == null) {
				return false;
			}
			final Item symbol = event.getUsedWith().asItem();
			if (player.getSkills().getLevel(Skills.PRAYER) < 50) {
				player.sendMessage("You need a Prayer level of at least 50 in order to do this.");
				return true;
			}
			if (player.getSkills().getPrayerPoints() < 4) {
				player.sendMessage("You need at least 4 prayer points in order to do this.");
				return true;
			}
			if (book == GodBook.BOOK_OF_BALANCE) {
				player.getDialogueInterpreter().sendOptions("Select an Option", "Unholy symbol", "Holy symbol");
				player.getDialogueInterpreter().addAction(new DialogueAction() {

					@Override
					public void handle(Player player, int buttonId) {
						bless(player, symbol, buttonId == 1 ? GodBook.UNHOLY_BOOK : GodBook.HOLY_BOOK);
					}

				});
				return true;
			}
			bless(player, symbol, book);
			return true;
		}

		/**
		 * Blesses a symbol.
		 * @param player the player.
		 * @param book the book.
		 */
		private void bless(Player player, Item symbol, GodBook book) {
			if (!player.getInventory().containsItem(symbol)) {
				return;
			}
			if (player.getInventory().get(symbol.getSlot()) == null) {
				return;
			}
			player.getInventory().replace(book.getBlessItem()[0], symbol.getSlot());
			player.getSkills().decrementPrayerPoints(4);
		}

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
