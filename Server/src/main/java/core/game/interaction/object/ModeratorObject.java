package core.game.interaction.object;

import core.cache.def.impl.SceneryDefinition;
import core.game.content.global.action.ClimbActionHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.info.Rights;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used for the moderator objects in the p-mod room.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class ModeratorObject extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(26806).getHandlers().put("option:climb-up", this);
		SceneryDefinition.forId(26807).getHandlers().put("option:j-mod options", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		switch (option) {
		case "climb-up":
			ClimbActionHandler.climb(player, new Animation(828), Location.create(3222, 3218, 0));
			break;
		case "j-mod options":
			if (player.getDetails().getRights() == Rights.REGULAR_PLAYER) {
				return true;
			}
			player.sendMessage("Disabled...");
			break;
		}
		return true;
	}

}
