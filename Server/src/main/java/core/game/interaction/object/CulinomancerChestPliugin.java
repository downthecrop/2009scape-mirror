package core.game.interaction.object;

import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.object.Scenery;
import core.game.node.object.SceneryBuilder;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.plugin.Initializable;
import core.plugin.Plugin;
import rs09.game.system.config.ShopParser;

/**
 * Handles the culinomancer chest.
 * @author Vexia
 */
@Initializable
public final class CulinomancerChestPliugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(12309).getHandlers().put("option:bank", this);
		SceneryDefinition.forId(12309).getHandlers().put("option:buy-items", this);
		SceneryDefinition.forId(12309).getHandlers().put("option:buy-food", this);
		Scenery object = RegionManager.getObject(new Location(3219, 9623, 0));
		SceneryBuilder.replace(object, object.transform(object.getId(), 3));
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		switch (option) {
		case "bank":
			player.getBank().open();
			return true;
		case "buy-items":
			ShopParser.Companion.openUid(player, 178);
			return true;
		case "buy-food":
			ShopParser.Companion.openUid(player, 177);
			return true;
		}
		return true;
	}

}
