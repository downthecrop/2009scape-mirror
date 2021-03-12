package core.game.content.activity.bountyhunter;

import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.game.world.map.zone.ZoneBorders;

/**
 * Represents the crater types.
 * @author Emperor
 */
public enum CraterType {

	/**
	 * The low level crater.
	 */
	LOW_LEVEL(3, Location.create(1548, 5804, 0), Location.create(1548, 5804, 0), Location.create(3152, 3672, 0), new ZoneBorders(2688, 5632, 2879, 5823)),

	/**
	 * The mid level crater.
	 */
	MID_LEVEL(50, Location.create(1558, 5785, 0), Location.create(1548, 5804, 0), Location.create(3158, 3680, 0), new ZoneBorders(2944, 5632, 3135, 5823)),

	/**
	 * The high level crater.
	 */
	HIGH_LEVEL(95, Location.create(1570, 5804, 0), Location.create(1548, 5804, 0), Location.create(3164, 3685, 0), new ZoneBorders(3200, 5632, 3391, 5823));

	/**
	 * The level required to enter the crater.
	 */
	private final int level;

	/**
	 * The waiting room location.
	 */
	private final Location roomLocation;

	/**
	 * The crater location.
	 */
	private final Location craterLocation;

	/**
	 * The exit location.
	 */
	private final Location exitLocation;

	/**
	 * The zone borders.
	 */
	private final ZoneBorders zone;

	/**
	 * Constructs a new {@code CraterType} {@code Object}. The level required to
	 * enter the crater.
	 * @param roomLocation The location of the waiting room.
	 * @param craterLocation The location of the crater.
	 * @param exitLocation The location to go the when exiting.
	 * @param zone The crater zone.
	 */
	private CraterType(int level, Location roomLocation, Location craterLocation, Location exitLocation, ZoneBorders zone) {
		this.level = level;
		this.roomLocation = roomLocation;
		this.craterLocation = craterLocation;
		this.exitLocation = exitLocation;
		this.zone = zone;
	}

	/**
	 * Checks if the player can enter the crater.
	 * @param player The player.
	 * @return {@code True} if so.
	 */
	public boolean canEnter(Player player) {
		int combatLevel = player.getProperties().getCurrentCombatLevel();
		if (player.getIronmanManager().checkRestriction()) {
			return false;
		}
		if (ordinal() < CraterType.values().length - 1) {
			if (combatLevel > CraterType.values()[ordinal() + 1].level + 5) {
				player.getPacketDispatch().sendMessage("Your combat level has to be below " + (CraterType.values()[ordinal() + 1].level + 5) + " to enter this crater.");
				return false;
			}
		}
		if (combatLevel < level) {
			player.getPacketDispatch().sendMessage("You need a combat level of " + level + " to enter this crater.");
			return false;
		}
		return true;
	}

	/**
	 * Gets the level requirement.
	 * @return The level requirement.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Gets the roomLocation.
	 * @return The roomLocation.
	 */
	public Location getRoomLocation() {
		return roomLocation;
	}

	/**
	 * Gets the craterLocation.
	 * @return The craterLocation.
	 */
	public Location getCraterLocation() {
		return craterLocation;
	}

	/**
	 * Gets the exitLocation.
	 * @return The exitLocation.
	 */
	public Location getExitLocation() {
		return exitLocation;
	}

	/**
	 * Gets the zone.
	 * @return The zone.
	 */
	public ZoneBorders getZone() {
		return zone;
	}
}