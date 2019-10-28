package plugin.interaction.npc;

import org.crandor.cache.def.impl.NPCDefinition;
import org.crandor.game.component.Component;
import org.crandor.game.content.global.travel.glider.Gliders;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin used for gliders.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class GliderPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		NPCDefinition.forId(3809).getConfigurations().put("option:glider", this);
		NPCDefinition.forId(3810).getConfigurations().put("option:glider", this);
		NPCDefinition.forId(3811).getConfigurations().put("option:glider", this);
		NPCDefinition.forId(3812).getConfigurations().put("option:glider", this);
		NPCDefinition.forId(3813).getConfigurations().put("option:glider", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		player.getInterfaceManager().open(new Component(138));
		Gliders.sendConfig(node.asNpc(), player);
		return true;
	}

}
