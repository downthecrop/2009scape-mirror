package plugin.npc.other;

import org.crandor.game.node.entity.npc.AbstractNPC;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Handles the anton NPC.
 * @author 'Vexia
 */
@InitializablePlugin
public final class AntonNPC extends AbstractNPC {

	/**
	 * The messages to yell.
	 */
	private static final String[] FORCE_CHAT = { "Armour and axes to suit your needs.", "Imported weapons from the finest smithys around the lands!", "Ow, my toe! That armour is heavy." };

	/**
	 * Constructs a new {@code AntonNPC} {@code Object}.
	 */
	public AntonNPC() {
		this(4295, null);
	}

	/**
	 * Constructs a new {@code AntonNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 */
	public AntonNPC(int id, Location location) {
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
		return new AntonNPC(id, location);
	}

	@Override
	public int[] getIds() {
		return new int[] { 4295 };
	}

}
