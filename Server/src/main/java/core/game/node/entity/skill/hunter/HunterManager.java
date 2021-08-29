package core.game.node.entity.skill.hunter;

import core.game.node.entity.skill.Skills;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Manages the players hunter state.
 * @author Vexia
 */
public final class HunterManager {

	/**
	 * The list of active traps.
	 */
	private final List<TrapWrapper> traps = new ArrayList<>(20);

	/**
	 * The player instance.
	 */
	private final Player player;

	/**
	 * Constructs a new {@code HunterManager} {@code Object}.
	 * @param player the player.
	 */
	public HunterManager(final Player player) {
		this.player = player;
	}

	/**
	 * Calles every game pulse.
	 */
	public void pulse() {
		if (traps.size() == 0) {
			return;
		}
		Iterator<TrapWrapper> iterator = traps.iterator();
		TrapWrapper wrapper = null;
		while (iterator.hasNext()) {
			wrapper = iterator.next();
			if (wrapper.cycle()) {
				iterator.remove();
			}
		}
	}

	/**
	 * Called when the player logs out.
	 */
	public void logout() {
		Iterator<TrapWrapper> iterator = traps.iterator();
		TrapWrapper wrapper = null;
		while (iterator.hasNext()) {
			wrapper = iterator.next();
			if (wrapper.getType().getSettings().clear(wrapper, 0)) {
				iterator.remove();
			}
		}
	}

	/**
	 * Register a hunting trap.
	 * @param trap the trap.
	 * @param node the node.
	 * @param object the object.
	 * @return {@code True} if registered.
	 */
	public boolean register(Traps trap, Node node, final Scenery object) {
		final TrapWrapper wrapper = new TrapWrapper(player, trap, object);
		trap.getSettings().reward(player, node, wrapper);
		wrapper.setHook(trap.addHook(wrapper));
		return traps.add(wrapper);
	}

	/**
	 * Removes the wrapper from the registar.
	 * @param wrapper the wrapper.
	 * @return {@code True} if so.
	 */
	public boolean deregister(final TrapWrapper wrapper) {
		return traps.remove(wrapper);
	}

	/**
	 * Checks if they're the owner of the trap.
	 * @param object the object.
	 * @return {@code True} if they're the owner.
	 */
	public boolean isOwner(Scenery object) {
		return getUid(object) == getUid();
	}

	/**
	 * Gets the wrapper.
	 * @param object the object.
	 * @return the wrapper.
	 */
	public TrapWrapper getWrapper(Scenery object) {
		for (TrapWrapper wrapper : traps) {
			if (wrapper.getObject() == object || (wrapper.getSecondary() != null && wrapper.getSecondary() == object)) {
				return wrapper;
			}
		}
		return null;
	}

	/**
	 * Checks if the player exceeds the trap limit.
	 * @return {@code True} if so.
	 */
	public boolean exceedsTrapLimit(Traps trap) {
		if (trap.getSettings().exceedsLimit(player)) {
			return true;
		}
		return traps.size() + 1 > getMaximumTraps();
	}

	/**
	 * Gets the trap amount.
	 * @return the trap amount.
	 */
	public int getTrapAmount() {
		return traps.size();
	}

	/**
	 * Gets the maximum amount of traps.
	 * @return the traps.
	 */
	public int getMaximumTraps() {
		final int level = getStaticLevel();
		return level >= 80 ? 5 : level >= 60 ? 4 : level >= 40 ? 3 : level >= 20 ? 2 : 1;
	}

	/**
	 * Gets the uid of a trap.
	 * @param object the object.
	 * @return the uid.
	 */
	public int getUid(Scenery object) {
		return object.getAttributes().getAttribute("trap-uid", 0);
	}

	/**
	 * Gets the uid of this manager.
	 * @return the manager.
	 */
	public int getUid() {
		return player.getName().hashCode();
	}

	/**
	 * Gets the static level.
	 * @return the level.
	 */
	public int getStaticLevel() {
		return player.getSkills().getStaticLevel(Skills.HUNTER);
	}

	/**
	 * Gets the player.
	 * @return The player.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the traps.
	 * @return The traps.
	 */
	public List<TrapWrapper> getTraps() {
		return traps;
	}

}
