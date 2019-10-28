package plugin.skill.crafting;

import org.crandor.game.content.skill.free.crafting.jewellery.JewelleryCrafting;
import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin used to craft jewellery.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class JewelleryCraftPlugin extends UseWithHandler {

	/**
	 * Represents the ids to use for this plugin.
	 */
	private static final int[] IDS = new int[] { 4304, 6189, 11010, 11666, 12100, 12809, 18497, 26814, 30021, 30510, 36956, 37651 };

	/**
	 * Constructs a new {@code JewelleryCraftPlugin} {@code Object}.
	 */
	public JewelleryCraftPlugin() {
		super(2357);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (int i : IDS) {
			addHandler(i, OBJECT_TYPE, this);
		}
		return this;
	}

	@Override
	public boolean handle(final NodeUsageEvent event) {
		JewelleryCrafting.open(event.getPlayer());
		return true;
	}

}
