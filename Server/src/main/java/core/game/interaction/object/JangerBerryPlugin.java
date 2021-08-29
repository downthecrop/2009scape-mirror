package core.game.interaction.object;

import core.cache.def.impl.SceneryDefinition;
import core.plugin.Initializable;
import core.game.node.entity.skill.agility.AgilityHandler;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.OptionHandler;
import core.game.interaction.UseWithHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;

/**
 * Handles the janger berry plugin.
 * @author Vexia
 */
@Initializable
public class JangerBerryPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		UseWithHandler.addHandler(2326, UseWithHandler.OBJECT_TYPE, new UseWithHandler(954) {
			@Override
			public Plugin<Object> newInstance(Object arg) throws Throwable {
				return this;
			}

			@Override
			public boolean handle(NodeUsageEvent event) {
				Scenery object = event.getUsedWith().asScenery();
				if (object.isActive())
					SceneryBuilder.replace(object, object.transform(2325));
				event.getPlayer().getInventory().remove(event.getUsedItem());
				return true;
			}

		});
		SceneryDefinition.forId(2325).getHandlers().put("option:swing-on", this);
		SceneryDefinition.forId(2324).getHandlers().put("option:swing-on", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		if (!player.getLocation().withinDistance(node.getLocation(), 2)) {
			player.sendMessage("I can't reach that.");
			return true;
		}
		Location end = node.getId() == 2325 ? new Location(2505, 3087, 0) : new Location(2511, 3096, 0);
		player.getPacketDispatch().sendSceneryAnimation(node.asScenery(), Animation.create(497), true);
		AgilityHandler.forceWalk(player, 0, player.getLocation(), end, Animation.create(751), 50, 22, "You skillfully swing across.", 1);
		return true;
	}

	@Override
	public Location getDestination(Node node, Node n) {
		return n.getId() == 2324 ? new Location(2511, 3092, 0) : new Location(2501, 3087, 0);
	}
}
