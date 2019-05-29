package plugin.activity.gwd;

import org.crandor.game.content.global.action.DoorActionHandler;
import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.world.map.Direction;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Handles the boss room instant-access key. 
 * @author Splinter
 */
@InitializablePlugin
public class EcumenicalKeyHandler extends UseWithHandler {

	/**
	 * Constructs a new {@code EcumenicalKeyHandler} {@code Object}
	 */
	public EcumenicalKeyHandler() {
		super(14674);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(26425, OBJECT_TYPE, this);
		addHandler(26426, OBJECT_TYPE, this);
		addHandler(26427, OBJECT_TYPE, this);
		addHandler(26428, OBJECT_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		final GameObject object = event.getUsedWith().asObject();
		Direction dir = Direction.get((object.getRotation() + 3) % 4);
		if (dir.getStepX() != 0) {
			if (player.getLocation().getX() == object.getLocation().transform(dir.getStepX(), 0, 0).getX()) {
				player.sendMessage("It would be unwise to use the key on this side of the door!");
				return true;
			}
		} else if (player.getLocation().getY() == object.getLocation().transform(0, dir.getStepY(), 0).getY()) {
			player.sendMessage("It would be unwise to use the key on this side of the door!");
			return true;
		}
		player.lock(2);
		player.getInventory().remove(event.getUsedItem());
		player.sendMessage("The key shatters as you insert it into the lock.");
		DoorActionHandler.handleAutowalkDoor(player, event.getUsedWith().asObject());
		return true;
	}

}
