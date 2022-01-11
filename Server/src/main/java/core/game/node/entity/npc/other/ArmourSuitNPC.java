package core.game.node.entity.npc.other;

import core.game.node.entity.npc.AbstractNPC;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.plugin.Initializable;
import core.game.world.map.Location;

/**
 * Represents the Suit of armour NPC used in Taverly dungeon (entering Cauldron
 * of thunder room).
 * @author Emperor
 */
@Initializable
public final class ArmourSuitNPC extends AbstractNPC {

	/**
	 * Constructs a new {@code ArmourSuitNPC} {@code Object}.
	 */
	public ArmourSuitNPC() {
		this(453, null);
	}

	/**
	 * Constructs a new {@code ArmourSuitNPC} {@code Object}.
	 * @param id The NPC id.
	 * @param location The location.
	 */
	public ArmourSuitNPC(int id, Location location) {
		super(id, location);
	}

	@Override
	public void init() {
		super.init();
		super.setRespawn(false);
		GameWorld.getPulser().submit(new Pulse(50, this) {
			@Override
			public boolean pulse() {
				if (!getProperties().getCombatPulse().isAttacking() && !inCombat()) {
					clear();
					return true;
				}
				return false;
			}
		});
	}

	@Override
	public void clear() {
		super.clear();
		SceneryBuilder.add(new Scenery(818, getProperties().getSpawnLocation(), 1));
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new ArmourSuitNPC(id, location);
	}

	@Override
	public int[] getIds() {
		return new int[] { 453 };
	}

}
