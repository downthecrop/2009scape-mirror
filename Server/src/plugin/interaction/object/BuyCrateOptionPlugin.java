package plugin.interaction.object;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.system.mysql.impl.ShopSQLHandler;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the buy crate option plugin for the seers village city.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class BuyCrateOptionPlugin extends OptionHandler {

	@Override
	public boolean handle(Player player, Node node, String option) {
		ShopSQLHandler.openUid(player, 93);
		return true;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(6839).getConfigurations().put("option:buy", this);
		return this;
	}

}
