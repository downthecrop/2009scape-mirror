package core.game.interaction;

import core.cache.def.impl.SceneryDefinition;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import rs09.game.system.SystemLogger;
import core.game.world.map.Location;
import core.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles an interaction option.
 * @author Emperor
 */
public abstract class OptionHandler implements Plugin<Object> {

	/**
	 * Handles the interaction option.
	 * @param player The player who used the option.
	 * @param node The node the player selected an option on.
	 * @param option The option selected.
	 * @return {@code True} if successful.
	 */
	public abstract boolean handle(Player player, Node node, String option);

	/**
	 * Checks if the option should be handled after 1 game tick.
	 * @param player The player.
	 * @return {@code True} if so.
	 */
	public boolean isDelayed(Player player) {
		return true;
	}

	/**
	 * Checks if it needs a walk..
	 * @param node the node.
	 * @return true if so.
	 */
	public boolean isWalk(final Player player, final Node node) {
		return false;
	}

	/**
	 * Gets the walk.
	 * @return if a walk is required.
	 */
	public boolean isWalk() {
		return true;
	}

	/**
	 * Gets the custom destination for the node.
	 * @param n The moving node.
	 * @param node The node to walk to.
	 * @return The custom destination, or {@code null} if we should use the
	 * default destination.
	 */
	public Location getDestination(Node n, Node node) {
		return null;
	}

	/**
	 * Gets the valid children for the wrapper id.
	 * @param wrapper the wrapper id.
	 * @return the valid children.
	 */
	public int[] getValidChildren(int wrapper) {
		final SceneryDefinition definition = SceneryDefinition.forId(wrapper);
		final List<Integer> list = new ArrayList<>(20);
		if (definition.getChildrenIds() == null) {
			SystemLogger.logErr("Null child wrapper in option handler wrapperId=" + wrapper);
			return new int[] { wrapper };
		}
		for (int child : definition.getChildrenIds()) {
			if (child != -1 && !list.contains(child)) {
				list.add(child);
			}
		}
		int[] array = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i] = list.get(i);
		}
		return array;
	}

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}
}