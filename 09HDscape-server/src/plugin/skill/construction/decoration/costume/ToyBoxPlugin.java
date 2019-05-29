package plugin.skill.construction.decoration.costume;


import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Handles the Toy Box POH
 * ToyBoxPlugin.java
 * @author Lee
 * @date 10/2/2017
 */
@InitializablePlugin
public class ToyBoxPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(18802).getConfigurations().put("option:open", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		// TODO Auto-generated method stub
		return true;
	}
	
	
}
