package core.game.node.entity.skill.crafting;

import core.game.component.Component;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * The Stitch Crafting Plugin
 * @author SonicForce41
 */
@Initializable
public class StitchCraftPlugin extends UseWithHandler {

	/**
	 * Constructs a new {@code StitchCraftPlugin.java} {@Code Object}
	 */
	public StitchCraftPlugin() {
		super(1741);
	}

	/**
	 * Method handles the actions for this Plugin
	 */
	@Override
	public boolean handle(NodeUsageEvent event) {
		Player player = event.getPlayer();
		int itemId = event.getUsedItem().getId();
		player.getAttributes().put("leatherId", itemId);
		player.getInterfaceManager().open(new Component(154));
		return true;
	}

	/**
	 * Method returns the instance for plugin
	 */
	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(1733, ITEM_TYPE, this);
		return null;
	}

}
