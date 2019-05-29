package plugin.skill.firemaking;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.content.skill.free.firemaking.FireMakingPulse;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.GroundItem;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin used to light a log.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class LightLogPlugin extends OptionHandler {

	@Override
	public boolean handle(Player player, Node node, String option) {
		player.getPulseManager().run(new FireMakingPulse(player, ((Item) node), ((GroundItem) node)));
		return true;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.setOptionHandler("light", this);
		return this;
	}

}
