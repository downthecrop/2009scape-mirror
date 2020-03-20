package plugin.npc.other;

import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.BattleState;
import org.crandor.game.node.entity.npc.AbstractNPC;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Rerepsents a barbarian npc.
 * @author 'Vexia
 */
@InitializablePlugin
public final class BarbarianNPC extends AbstractNPC {

	/**
	 * The NPC ids of NPCs using this plugin.
	 */
	private static final int[] ID = { 12, 3246, 3247, 3248, 3249, 3250, 3251, 3252, 3253, 3255, 3256, 3257, 3258, 3259, 3260, 3261, 3262, 3263, 5909 };

	/**
	 * Constructs a new {@code BarbarianNPC} {@code Object}.
	 */
	public BarbarianNPC() {
		super(0, null, true);
	}

	/**
	 * Constructs a new {@code BarbarianNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 */
	private BarbarianNPC(int id, Location location) {
		super(id, location, true);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new BarbarianNPC(id, location);
	}

	@Override
	public void onImpact(final Entity entity, final BattleState state) {
		if (RandomFunction.random(8) == 1) {
			sendChat("YEEEEEEEEAARRRGGGGHHHHHHHH");
		}
		super.onImpact(entity, state);
	}

	@Override
	public int[] getIds() {
		return ID;
	}

}
