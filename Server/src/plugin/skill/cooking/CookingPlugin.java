package plugin.skill.cooking;

import org.crandor.game.content.global.consumable.Consumables;
import org.crandor.game.content.global.consumable.Food;
import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.world.map.Direction;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin used to handle the interaction of a cooking consumable
 * with a range/fire.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class CookingPlugin extends UseWithHandler {

	/**
	 * Represents the objects allowed to cook {@code Food} on.
	 */
	private static final int[] OBJECTS = new int[] { 21302, 13528, 13529, 13533, 13531, 13536, 13539, 13542, 2728, 2729, 2730, 2731, 2732, 2859, 3038, 3039, 3769, 3775, 4265, 4266, 5249, 5499, 5631, 5632, 5981, 9682, 10433, 11404, 11405, 11406, 12102, 12796, 13337, 13881, 14169, 14919, 15156, 20000, 20001, 21620, 21792, 22713, 22714, 23046, 24283, 24284, 25155, 25156, 25465, 25730, 27297, 29139, 30017, 32099, 33500, 34495, 34546, 36973, 37597, 37629, 37726, 114, 4172, 5275, 8750, 16893, 22154, 34410, 34565, 114, 9085, 9086, 9087, 12269, 15398, 25440, 25441, 2724, 2725, 2726, 4618, 4650, 5165, 6093, 6094, 6095, 6096, 8712, 9439, 9440, 9441, 10824, 17640, 17641, 17642, 17643, 18039, 21795, 24285, 24329, 27251, 33498, 35449, 36815, 36816, 37426, 40110 };

	/**
	 * Constructs a new {@code CookingPlugin} {@code Object}.
	 */
	public CookingPlugin() {
		super(10816, 2138, 2142, 2134, 3142, 2136, 1859, 3226, 7518, 3150, 327, 321, 345, 353, 335, 341, 349, 331, 3379, 359, 10138, 5001, 377, 363, 371, 2148, 7944, 383, 395, 389, 3363, 3365, 3367, 5986, 401, 1942, 4237, 2001, 7076, 1871, 7080, 2307, 1889, 2132, 2132, 2132, 2132, 2321, 2319, 7168, 2317, 7176, 7186, 7196, 7206, 7216, 2287, 317, 9986, 2876, 7566, 9984, 7224);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (int object : OBJECTS) {
			addHandler(object, OBJECT_TYPE, this);
		}
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final GameObject object = (GameObject) event.getUsedWith();
		final Food food = Consumables.forRaw(event.getUsedItem());
		if (food == null || !food.interact(event.getPlayer(), object)) {
			return true;
		}
		event.getPlayer().getDialogueInterpreter().open(43989, food, event.getUsedWith());
		return true;
	}

	@Override
	public Location getDestination(final Player player, final Node node) {
		if (node.getName().toLowerCase().equals("fire")) {
			return player.getLocation().getY() > node.getLocation().getY() ? node.getLocation().transform(0, 1, 0) : player.getLocation().getX() < node.getLocation().getX() ? node.getLocation().transform(-1, 0, 0) : player.getLocation().getX() > node.getLocation().getX() ? node.getLocation().transform(1, 0, 0) : node.getLocation().transform(0, -1, 0);
		} else {
			Direction direction = node.getDirection();
			if (direction == Direction.NORTH) {
				return node.getLocation().transform(1, 1, 0);
			} else if (direction == Direction.SOUTH) {
				return node.getLocation().transform(-1, 0, 0);
			}
			return null;
		}
	}

}
