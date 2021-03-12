package core.game.node.entity.npc.other;

import core.game.node.entity.npc.AbstractNPC;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.tools.RandomFunction;

/**
 * Represents the representation of the benny npc.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class BennyNPC extends AbstractNPC {

	/**
	 * The NPC ids of NPCs using this plugin.
	 */
	private static final int[] ID = { 5925 };

	/**
	 * Represents the random messages for benny to display.
	 */
	private static final String[] MESSAGES = new String[] { "Read all about it!", "Varrock Herald, on sale here!", "Buy your Varrock Herald now!", "Extra! Extra! Read all about it!", "Varrock Herald, now only 50 gold!" };

	/**
	 * Constructs a new {@code BennyNPC} {@code Object}.
	 */
	public BennyNPC() {
		super(0, null);
	}

	/**
	 * Constructs a new {@code BennyNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 */
	private BennyNPC(int id, Location location) {
		super(id, location);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new BennyNPC(id, location);
	}

	@Override
	public void tick() {
		super.tick();
		if (RandomFunction.random(0, 12) == 5) {
			sendChat(MESSAGES[RandomFunction.random(MESSAGES.length)]);
		}
	}

	@Override
	public int[] getIds() {
		return ID;
	}

	@Override
	public int getWalkRadius() {
		return 6;
	}
}
