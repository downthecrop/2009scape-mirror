package plugin.interaction.city;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.repository.Repository;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents a plugin used to handle entrana related objects.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class EntranaObjectPlugin extends OptionHandler {

	/**
	 * Represents the location to teleport to.
	 */
	private static final Location LOCATION = Location.create(3208, 3764, 0);// magic

	// door

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(2408).getConfigurations().put("option:climb-down", this);
		ObjectDefinition.forId(2407).getConfigurations().put("option:open", this);// magic
		// door
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		switch (option) {
		case "climb-down":
			player.getDialogueInterpreter().open(656, Repository.findNPC(656));
			break;
		case "open":
			player.getPacketDispatch().sendMessage("You feel the world around you dissolve...");
			player.getProperties().setTeleportLocation(LOCATION);
			break;
		}
		return true;
	}

}
