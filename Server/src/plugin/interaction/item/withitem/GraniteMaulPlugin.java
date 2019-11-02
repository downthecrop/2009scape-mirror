package plugin.interaction.item.withitem;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.content.dialogue.DialogueAction;
import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.PluginManager;

/**
 * The plugin used to make the granite maul into the ornamental version.
 * @author Splinter
 * @version 1.0
 */
@InitializablePlugin
public final class GraniteMaulPlugin extends UseWithHandler {


	/**
	 * Constructs a new {@code GraniteMaulPlugin} {@code Object}.
	 */
	public GraniteMaulPlugin() {
		super(4153);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		PluginManager.definePlugin(new GraniteMaulRevertHandler());
		addHandler(14793, ITEM_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(final NodeUsageEvent event) {
		final Player player = event.getPlayer();
		player.getDialogueInterpreter().sendOptions("Attach the clamp?", "Yes", "No");
		player.getDialogueInterpreter().addAction(new DialogueAction() {

			@Override
			public void handle(Player player, int buttonId) {
				switch (buttonId) {
				case 2:
					if (player.getInventory().remove(event.getBaseItem()) && player.getInventory().remove(event.getUsedItem())) {
						player.getInventory().add(new Item(14792, 1));
						player.sendMessage("You attach the clamp to the granite maul, making it slightly more fashionable.");
					}
					break;
				}
			}

		});
		return true;
	}
	
	/**
	 * Handles the removal of the ornamental kit.
	 * @author Splinter
	 */
	public final class GraniteMaulRevertHandler extends OptionHandler {

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			ItemDefinition.forId(14792).getConfigurations().put("option:revert", this);
			return null;
		}

		@Override
		public boolean handle(Player player, Node node, String option) {
			final Item item = (Item) node;
			player.getDialogueInterpreter().sendOptions("Remove the clamp?", "Yes", "No");
			player.getDialogueInterpreter().addAction(new DialogueAction() {

				@Override
				public void handle(Player player, int buttonId) {
					switch (buttonId) {
					case 2:
						if(player.getInventory().remove(item)){
							player.getInventory().add(new Item(4153, 1));
							player.sendMessage("You remove the clamp from your maul, and in the process, it is destroyed.");
						}
						break;
					}
				}

			});
			return true;
		}

		@Override
		public boolean isWalk() {
			return false;
		}
	}

}
