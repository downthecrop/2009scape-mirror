package plugin.interaction.object;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.content.global.action.ClimbActionHandler;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.info.Rights;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin used for the moderator objects in the p-mod room.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class ModeratorObject extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(26806).getConfigurations().put("option:climb-up", this);
		ObjectDefinition.forId(26807).getConfigurations().put("option:j-mod options", this);
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
