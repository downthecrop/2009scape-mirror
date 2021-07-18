package core.game.interaction.object;

import core.cache.def.impl.SceneryDefinition;
import core.game.component.Component;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles the barrows boat plugin.
 * @author Vexia
 */
@Initializable
public class BarrowsBoatPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(6970).getHandlers().put("option:board", this);
		SceneryDefinition.forId(6969).getHandlers().put("option:board", this);
		return this;
	}

	@Override
	public boolean handle(final Player player, Node node, String option) {
		switch (option) {
		case "board":
			final Location dest = node.getId() == 6970 ? new Location(3522, 3285, 0) : new Location(3500, 3380, 0);
			final String name = node.getId() == 6970 ? "Mort'ton." : "the swamp";
			player.lock();
			player.getInterfaceManager().open(new Component(321));
			GameWorld.getPulser().submit(new Pulse(7, player) {

				@Override
				public boolean pulse() {
					player.unlock();
					player.teleport(dest);
					player.getInterfaceManager().close();
					player.getDialogueInterpreter().sendDialogue("You arrive at " + name + ".");
					return true;
				}

			});
			break;
		}
		return true;
	}

}
