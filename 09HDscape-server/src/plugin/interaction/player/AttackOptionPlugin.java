package plugin.interaction.player;

import org.crandor.cache.def.impl.NPCDefinition;
import org.crandor.game.interaction.Option;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the attack option plugin handler.
 * @author Emperor
 * @version 1.0
 */
@InitializablePlugin
public final class AttackOptionPlugin extends OptionHandler {

	@Override
	public boolean handle(Player player, Node node, String option) {
		player.attack(node);
		return true;
	}

	@Override
	public boolean isWalk() {
		return false;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		Option._P_ATTACK.setHandler(this);
		NPCDefinition.setOptionHandler("attack", this);
		return this;
	}

	@Override
	public boolean isDelayed(Player player) {
		return false;
	}

}
