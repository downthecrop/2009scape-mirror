package core.game.node.entity.skill.hunter;

import core.game.node.entity.Entity;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import rs09.game.world.GameWorld;
import core.game.world.map.Location;
import core.tools.RandomFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles a hunter npc.
 * @author Vexia
 */
public final class HunterNPC extends AbstractNPC {

	/**
	 * The trap type.
	 */
	private final Traps trap;

	/**
	 * The trap type node.
	 */
	private final TrapNode type;

	/**
	 * Constructs a new {@code HunterNPC} {@code Object}.
	 */
	public HunterNPC() {
		this(0, null, null, null);
		this.setWalks(true);
	}

	/**
	 * Constructs a new {@code HunterNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 * @param trap the trap.
	 * @param type the type.
	 */
	public HunterNPC(int id, Location location, Traps trap, TrapNode type) {
		super(id, location);
		this.trap = trap;
		this.type = type;
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		Object[] data = Traps.getNode(id);
		return new HunterNPC(id, location, (Traps) data[0], (TrapNode) data[1]);
	}

	@Override
	public void updateLocation(Location last) {
		final TrapWrapper wrapper = trap.getByHook(getLocation());
		if (wrapper != null) {
			wrapper.getType().catchNpc(wrapper, this);
		}
	}

	@Override
	protected Location getMovementDestination() {
		if (trap.getHooks().size() == 0 || RandomFunction.random(170) > 5) {
			return super.getMovementDestination();
		}
		TrapHook hook = trap.getHooks().get(RandomFunction.random(trap.getHooks().size()));
		if (hook == null || !type.canCatch(hook.getWrapper(), this)) {
			return super.getMovementDestination();
		}
		Location destination = hook.getChanceLocation();
		return destination != null && destination.getDistance(getLocation()) <= 24 ? destination : super.getMovementDestination();
	}

	@Override
	public void handleDrops(Player p, Entity killer) {
		int ticks = getAttribute("hunter", 0);
		if (ticks < GameWorld.getTicks()) {
			super.handleDrops(p, killer);
		}
	}

	@Override
	public int[] getIds() {
		List<Integer> ids = new ArrayList<>(10);
		for (Traps t : Traps.values()) {
			for (TrapNode node : t.getNodes()) {
				for (int id : node.getNpcIds()) {
					ids.add(id);
				}
			}
		}
		int[] array = new int[ids.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = ids.get(i);
		}
		return array;
	}

	/**
	 * Gets the type.
	 * @return The type.
	 */
	public TrapNode getType() {
		return type;
	}

	/**
	 * Gets the trap.
	 * @return The trap.
	 */
	public Traps getTrap() {
		return trap;
	}

}
