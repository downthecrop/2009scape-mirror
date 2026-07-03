package content.global.skill.crafting;

import core.plugin.Initializable;
import content.global.skill.crafting.jewellery.JewelleryCrafting;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.plugin.Plugin;
import org.rs09.consts.Scenery;

/**
 * Represents the plugin used to craft jewellery.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class JewelleryCraftPlugin extends UseWithHandler {

	/**
	 * Represents the ids to use for this plugin.
	 */
	private static final int[] IDS = new int[]{
			Scenery.FURNACE_4304, Scenery.FURNACE_6189, Scenery.FURNACE_11010, Scenery.FURNACE_11666,
			Scenery.FURNACE_12100, Scenery.FURNACE_12809, Scenery.FURNACE_18497, Scenery.CLAY_FORGE_21303,
			Scenery.FURNACE_26814, Scenery.FURNACE_30021, Scenery.FURNACE_30510, Scenery.FURNACE_36956,
			Scenery.FURNACE_37651
	};

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
