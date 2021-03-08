package core.game.node.entity.npc.other;

import core.game.node.entity.npc.AbstractNPC;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.tools.RandomFunction;

/**
 * Handles the kamfreena NPC.
 * @author 'Vexia
 */
@Initializable
public final class KamfreenaNPC extends AbstractNPC {

	/**
	 * The messages to yell.
	 */
	private static final String[] FORCE_CHAT = { "When you aim for perfection, you discover it's a moving target.", "Patience and persistence can bring down the tallest tree.", "Be master of mind rather than mastered by mind.", "A reflection on a pool of water does not reveal its depth.", "Life isn't fair, that doesn't mean you can't win." };

	/**
	 * Constructs a new {@code KamfreenaNPC} {@code Object}.
	 */
	public KamfreenaNPC() {
		this(4289, null);
	}

	/**
	 * Constructs a new {@code KamfreenaNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 */
	public KamfreenaNPC(int id, Location location) {
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
		return new KamfreenaNPC(id, location);
	}

	@Override
	public int[] getIds() {
		return new int[] { 4289 };
	}

}
