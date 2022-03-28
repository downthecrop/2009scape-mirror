package core.game.interaction.item.withitem;
import core.game.content.dialogue.DialogueAction;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Plugin;
import core.plugin.Initializable;
import rs09.plugin.ClassScanner;

/**
 * The plugin used to dye a dark bow into a more <fashionable> one.
 * @author Splinter
 * @version 1.0
 */
@Initializable
public final class DarkBowDyePlugin extends UseWithHandler {


	/**
	 * Constructs a new {@code DarkBowDyePlugin} {@code Object}.
	 */
	public DarkBowDyePlugin() {
		super(11235);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(14795, ITEM_TYPE, this);
		addHandler(14797, ITEM_TYPE, this);
		addHandler(14799, ITEM_TYPE, this);
		addHandler(14801, ITEM_TYPE, this);
		ClassScanner.definePlugin(new DarkBowCleanPlugin());
		return this;
	}

	@Override
	public boolean handle(final NodeUsageEvent event) {
		final Player player = event.getPlayer();
		player.getDialogueInterpreter().sendOptions("Apply "+event.getUsedItem().getName().replace("dark bow paint", "paint?").toLowerCase(), "Yes", "No");
		player.getDialogueInterpreter().addAction(new DialogueAction() {

			@Override
			public void handle(Player player, int buttonId) {
				switch (buttonId) {
				case 2:
					if (player.getInventory().remove(event.getBaseItem()) && player.getInventory().remove(event.getUsedItem())) {
						player.getInventory().add(getResult(event.getUsedItem().getId()));
						player.sendMessage("You dye the bow into a more fashionable version.");
					}
					break;
				}
			}

		});
		return true;
	}
	
	/**
	 * Gets the resulting colored dark bow.
	 * @return the item
	 */
	private Item getResult(int dyeId){
		switch(dyeId){
			case 14797:
				return new Item(14803, 1);
			case 14795:
				return new Item(14804, 1);
			case 14799:
				return new Item(14805, 1);
			case 14801:
				return new Item(14806, 1);
		}
		return null;
	}
	
	/**
	 * Cleans the dark bow and removes the paint.
	 * @author Andrew
	 */
	public final class DarkBowCleanPlugin extends UseWithHandler {

		/**
		 * Constructs a new {@code DarkBowCleanPlugin} {@code Object}.
		 */
		public DarkBowCleanPlugin() {
			super(3188);
		}

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			addHandler(14803, ITEM_TYPE, this);
			addHandler(14804, ITEM_TYPE, this);
			addHandler(14805, ITEM_TYPE, this);
			addHandler(14806, ITEM_TYPE, this);
			return this;
		}

		@Override
		public boolean handle(final NodeUsageEvent event) {
			final Player player = event.getPlayer();
			player.getDialogueInterpreter().sendOptions("Wipe the paint off?", "Yes", "No");
			player.getDialogueInterpreter().addAction(new DialogueAction() {

				@Override
				public void handle(Player player, int buttonId) {
					switch (buttonId) {
					case 2:
						if (player.getInventory().remove(event.getBaseItem()) && player.getInventory().remove(event.getUsedItem())) {
							player.getInventory().add(new Item(11235, 1));
							player.sendMessage("You wipe the paint off with the cleaning cloth, then toss it away.");
						}
						break;
					}
				}

			});
			return true;
		}
	}
}
