package plugin.interaction.object;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.object.GameObject;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the champions arena plugin.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class ChampionsArenaPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(10556).getConfigurations().put("option:open", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		int id = node instanceof GameObject ? ((GameObject) node).getId() : ((NPC) node).getId();
		switch (id) {
		case 10556:
			player.getDialogueInterpreter().open(3050, true, true);
			break;
		}
		return true;
	}

}
