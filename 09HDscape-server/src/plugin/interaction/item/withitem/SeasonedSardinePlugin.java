package plugin.interaction.item.withitem;

import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin to make seasoned sardines.
 * @author 'Vexia
 * @date Oct 6, 2013
 */
@InitializablePlugin
public class SeasonedSardinePlugin extends UseWithHandler {

	/**
	 * Represents the seasoned sardine.
	 */
	private final Item SEASONED_SARDINE = new Item(1552);

	/**
	 * Constructs a new {@code SeasonedSardinePlugina} {@code Object}.
	 */
	public SeasonedSardinePlugin() {
		super(327);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(1573, ITEM_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		if (player.getInventory().remove(event.getUsedItem()) && player.getInventory().remove(event.getBaseItem())) {
			player.getDialogueInterpreter().sendDialogue("You rub the doogle leaves over the sardine.");
			player.getInventory().add(SEASONED_SARDINE);
		}
		return true;
	}

}
