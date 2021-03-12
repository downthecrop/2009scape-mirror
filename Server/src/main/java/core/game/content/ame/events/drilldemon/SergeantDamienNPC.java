package core.game.content.ame.events.drilldemon;

import core.game.node.entity.npc.AbstractNPC;
import core.game.world.map.Location;
import core.plugin.Plugin;

/**
 * Handles the sergeant damien npc.
 * @author Vexia
 */
public final class SergeantDamienNPC extends AbstractNPC {

	/**
	 * The default spawn.
	 */
	private static final Location DEFAULT_SPAWN = Location.create(3163, 4818, 0);

	/**
	 * The movement path.
	 */
	private static final Location[] MOVEMENT_PATH = new Location[] { Location.create(3163, 4818, 0), Location.create(3162, 4818, 0), Location.create(3161, 4818, 0), Location.create(3160, 4818, 0), Location.create(3159, 4818, 0), Location.create(3159, 4819, 0), Location.create(3159, 4820, 0), Location.create(3159, 4821, 0), Location.create(3159, 4822, 0), Location.create(3160, 4822, 0), Location.create(3161, 4822, 0), Location.create(3162, 4822, 0), Location.create(3163, 4822, 0), Location.create(3164, 4822, 0), Location.create(3165, 4822, 0), Location.create(3166, 4822, 0), Location.create(3167, 4822, 0), Location.create(3167, 4821, 0), Location.create(3167, 4820, 0), Location.create(3167, 4819, 0), Location.create(3167, 4818, 0), Location.create(3166, 4818, 0), Location.create(3165, 4818, 0), Location.create(3164, 4818, 0) };

	/**
	 * Constructs a new {@code SergeantDamienNPC} {@code Object}.
	 */
	public SergeantDamienNPC() {
		super(2790, DEFAULT_SPAWN);
	}

	/**
	 * Constructs a new {@code SergeantDamienNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 */
	public SergeantDamienNPC(int id, Location location) {
		super(id, location);
	}

	@Override
	public void configure() {
		super.configure();
		setWalks(true);
		if (isWalks()) {
			configureMovementPath(MOVEMENT_PATH);
		}
	}

	@Override
	public void setNextWalk() {
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new SergeantDamienNPC(id, location);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		init();
		return super.newInstance(arg);
	}

	@Override
	public int[] getIds() {
		return new int[] { 2790 };
	}
}
