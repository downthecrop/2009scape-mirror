package org.crandor.game.content.global.travel.ship;

import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.map.Location;

/**
 * Represents a ship to travel on.
 * @author 'Vexia
 */
public enum Ships {
	PORT_SARIM_TO_ENTRANA(Location.create(2834, 3331, 1), 1, 15, "Entrana"), ENTRANA_TO_PORT_SARIM(Location.create(3048, 3234, 0), 2, 15, "Port Sarim"), PORT_SARIM_TO_CRANDOR(Location.create(2849, 3238, 0), 3, 12, "Crandor"), CRANDOR_TO_PORT_SARIM(Location.create(2834, 3335, 0), 4, 13, "Port Sarim"), PORT_SARIM_TO_KARAMAJA(new Location(2956, 3143, 1), 5, 9, "Karamja"), KARAMJAMA_TO_PORT_SARIM(new Location(3029, 3217, 0), 6, 8, "Port Sarim"), ARDOUGNE_TO_BRIMHAVEN(new Location(2775, 3234, 1), 7, 4, "Brimhaven"), BRIMHAVEN_TO_ARDOUGNE(new Location(2683, 3268, 1), 8, 4, "Ardougne"), PORT_KHAZARD_TO_SHIP_YARD(Location.create(2998, 3043, 0), 11, 23, "the Ship Yard"), SHIP_YARD_TO_PORT_KHAZARD(Location.create(2676, 3170, 0), 12, 23, "Port Khazard"), CAIRN_ISLAND_TO_SHIP_YARD(Location.create(2998, 3043, 0), 13, 17, "the Ship Yard"), PORT_SARIM_TO_PEST_CONTROL(Location.create(2663, 2676, 1), 14, 12, "Pest Control"), PEST_TO_PORT_SARIM(Location.create(3041, 3198, 1), 15, 12, "Port Sarim"), FELDIP_TO_CAIRN(Location.create(2763, 2956, 0), 16, 10, "Cairn");

	/**
	 * Constructs a new {@code Ships} {@code Object}.
	 * @param location the start location.
	 * @param config the config value.
	 */
	Ships(Location location, int config, int delay, final String name) {
		this.location = location;
		this.config = config;
		this.delay = delay;
		this.name = name;
	}

	/**
	 * Represents the start location of the ship.
	 */
	private final Location location;

	/**
	 * The config value.
	 */
	private final int config;

	/**
	 * The delay of the ship.
	 */
	private final int delay;

	/**
	 * Represents the name of returning.
	 */
	private final String name;

	/**
	 * Method used to sail across the sea.
	 * @param player the player.
	 * @param ship the ship.
	 */
	public static void sail(final Player player, final Ships ship) {
		player.getPulseManager().run(new ShipTravellPulse(player, ship));
	}

	/**
	 * Method used to sail.
	 * @param player the player.
	 */
	public void sail(final Player player) {
		player.getPulseManager().run(new ShipTravellPulse(player, this));
	}

	/**
	 * Gets the location.
	 * @return The location.
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Gets the config.
	 * @return The config.
	 */
	public int getConfig() {
		return config;
	}

	/**
	 * Gets the delay.
	 * @return The delay.
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * Gets the name.
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

}