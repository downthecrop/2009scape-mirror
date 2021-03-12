package core.game.world.map.zone.impl;

import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.ZoneBorders;

/**
 * Represents a bank zone.
 * @author 'Vexia
 */
public final class BankZone extends MapZone {

	/**
	 * Represents the instance.
	 */
	private static final BankZone INSTANCE = new BankZone();

	/**
	 * Represents the varrock west bank.
	 */
	public static final ZoneBorders VARROCK_WEST = new ZoneBorders(3179, 3432, 3194, 3446);

	/**
	 * Represents the varrock east bank.
	 */
	public static final ZoneBorders VARROCK_EAST = new ZoneBorders(3250, 3416, 3257, 3423);
	

	/**
	 * Constructs a new {@code BankZone} {@code Object}.
	 */
	public BankZone() {
		super("bank", true);
	}

	@Override
	public void configure() {
		register(VARROCK_WEST);
		register(VARROCK_EAST);
	}

	/**
	 * Gets the instance.
	 * @return The instance.
	 */
	public static BankZone getInstance() {
		return INSTANCE;
	}

}