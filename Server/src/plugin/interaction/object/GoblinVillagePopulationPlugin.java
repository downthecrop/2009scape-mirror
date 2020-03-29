package plugin.interaction.object;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

@InitializablePlugin
public class GoblinVillagePopulationPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(31301).getConfigurations().put("option:read", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		int population = 26; //TODO: Find out how to get all goblins in the area.
		player.getDialogueInterpreter().sendPlainMessage(false, "Welcome to Goblin Village.", "Current population: " + population);
		return true;
	}

}
