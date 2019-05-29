package plugin.interaction.item;

import org.crandor.game.component.Component;
import org.crandor.game.content.global.Lamps;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin used for an experience lamp.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class LampPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (Lamps lamp : Lamps.values()) {
			lamp.getItem().getDefinition().getConfigurations().put("option:rub", this);
		}
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		player.setAttribute("lamp", node);
		player.getInterfaceManager().open(new Component(134));
		return true;
	}

	@Override
	public boolean isWalk() {
		return false;
	}

}
