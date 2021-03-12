package core.game.content.ttrail;

import core.game.content.global.action.DigAction;
import core.game.content.global.action.DigSpadeHandler;
import core.game.interaction.Option;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.game.world.map.zone.ZoneBorders;

/**
 * A map clue scroll.
 * @author Vexia
 */
public abstract class MapClueScroll extends ClueScrollPlugin {

	/**
	 * The location of the x spot.
	 */
	private final Location location;

	/**
	 * The object id.
	 */
	private final int object;

	/**
	 * Constructs a new {@Code MapClueScroll} {@Code Object}
	 * @param name the name.
	 * @param clueId the clue id.
	 * @param level the level.
	 * @param interfaceId the interface id.
	 * @param location the location.
	 * @param object the object.
	 */
	public MapClueScroll(String name, int clueId, ClueLevel level, int interfaceId, Location location, final int object, ZoneBorders... borders) {
		super(name, clueId, level, interfaceId, borders);
		this.location = location;
		this.object = object;
	}

	/**
	 * Constructs a new {@Code MapClueScroll} {@Code Object}
	 * @param name the name.
	 * @param clueId the clue id.
	 * @param level the level.
	 * @param interfaceId the interface id.
	 * @param location the location.
	 */
	public MapClueScroll(String name, int clueId, ClueLevel level, int interfaceId, Location location) {
		this(name, clueId, level, interfaceId, location, 0);
	}

	@Override
	public boolean interact(Entity e, Node target, Option option) {
		if (e instanceof Player) {
			Player p = e.asPlayer();
			if (target.getId() == object && option.getName().equals("Search")) {
				if (!p.getInventory().contains(clueId, 1) || !target.getLocation().equals(location)) {
					p.sendMessage("Nothing interesting happens.");
					return false;
				}
				reward(p, false);
				level.open(p, null);
				return true;
			}
		}
		return super.interact(e, target, option);
	}

	@Override
	public void configure() {
		DigSpadeHandler.register(getLocation(), new MapDigAction());
		super.configure();
	}

	/**
	 * The dig method called when the player digs.
	 * @param player the player.
	 */
	public void dig(Player player) {
		reward(player);
		player.getDialogueInterpreter().sendItemMessage(405, "You've found a casket!");
	}

	/**
	 * Handles the map digging reward.
	 * @author Vexia
	 */
	public final class MapDigAction implements DigAction {

		@Override
		public void run(Player player) {
			if (!hasRequiredItems(player)) {
				player.sendMessage("Nothing interesting happens.");
				return;
			}
			dig(player);
		}

	}

	/**
	 * Checks if the player has the required items.
	 * @param player the player.
	 * @return {@code True} if so.
	 */
	public boolean hasRequiredItems(Player player) {
		return player.getInventory().contains(clueId, 1);
	}

	/**
	 * Gets the blocation.
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Gets the bobject.
	 * @return the object
	 */
	public int getObject() {
		return object;
	}

}
