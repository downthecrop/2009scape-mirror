package plugin.interaction.item;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.content.global.LightSource;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.map.zone.impl.DarkZone;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the extinguish light plugin.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class ExtinguishLightPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.setOptionHandler("extinguish", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		final LightSource source = LightSource.forProductId(((Item) node).getId());
		if (source == null) {
			System.err.println("Extinguish Light Plugin source not found - " + ((Item) node).getId() + ".");
			return true;
		}
		player.getInventory().replace(source.getRaw(), ((Item) node).getSlot());
		DarkZone.checkDarkArea(player);
		return true;
	}

	@Override
	public boolean isWalk() {
		return false;
	}
}
