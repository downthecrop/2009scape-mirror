package plugin.npc.other;

import org.crandor.game.node.entity.npc.AbstractNPC;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Handles the shanomi NPC.
 * @author Emperor
 */
@InitializablePlugin
public final class ShanomiNPC extends AbstractNPC {

	/**
	 * The messages to yell.
	 */
	private static final String[] FORCE_CHAT = { "Those things which cannot be seen, perceive them.", "Do nothing which is of no use.", "Think not dishonestly.", "The Way in training is.", "Gain and loss between you must distinguish.", "Trifles pay attention even to.", "Way of the warrior this is.", "Acquainted with every art become.", "Ways of all professions know you.", "Judgment and understanding for everything develop you must." };

	/**
	 * Constructs a new {@code ShanomiNPC} {@code Object}.
	 */
	public ShanomiNPC() {
		this(4290, null);
	}

	/**
	 * Constructs a new {@code ShanomiNPC} {@code Object}.
	 * @param id The NPC id.
	 * @param location The location.
	 */
	public ShanomiNPC(int id, Location location) {
		super(id, location);
	}

	@Override
	public void tick() {
		if (RandomFunction.random(45) == 5) {
			sendChat(RandomFunction.getRandomElement(FORCE_CHAT));
		}
		super.tick();
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new ShanomiNPC(id, location);
	}

	@Override
	public int[] getIds() {
		return new int[] { 4290 };
	}

}
