package content.region.desert.sophanem.handlers;

import core.cache.def.impl.SceneryDefinition;
import core.game.global.action.ClimbActionHandler;
import core.game.global.action.DoorActionHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;

import static core.api.ContentAPIKt.hasRequirement;
import content.data.Quests;

/**
 * The plugin for handling stuff in Sophanem.
 * @author jamix77, Player Name
 *
 */
@Initializable
public class SophanemPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(20277).getHandlers().put("option:climb-up", this);
		SceneryDefinition.forId(20275).getHandlers().put("option:climb-down", this);
		SceneryDefinition.forId(20391).getHandlers().put("option:open", this);
		SceneryDefinition.forId(28514).getHandlers().put("option:open", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		final int id = node instanceof Scenery ? ((Scenery) node).getId() : ((Item) node).getId();
		switch (id) {
		case 20275:
			if (!hasRequirement(player, Quests.CONTACT)) {
				break;
			}
			ClimbActionHandler.climb(player, new Animation(827), Location.create(2799, 5160, 0));
			break;
		case 20277:
			ClimbActionHandler.climb(player, new Animation(828), Location.create(3315,2796,0));
			break;
		case 20391:
		case 28514:
			if (!hasRequirement(player, Quests.ICTHLARINS_LITTLE_HELPER)) {
				break;
			}
			DoorActionHandler.handleDoor(player, (Scenery) node);
			break;
		}
		return true;
	}
}
