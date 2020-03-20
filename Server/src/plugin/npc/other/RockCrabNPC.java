package plugin.npc.other;

import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.BattleState;
import org.crandor.game.node.entity.npc.AbstractNPC;
import org.crandor.game.node.entity.npc.agg.AggressiveBehavior;
import org.crandor.game.node.entity.npc.agg.AggressiveHandler;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Handles the rock crab npc.
 * @author Vexia
 */
@InitializablePlugin
public final class RockCrabNPC extends AbstractNPC {

	/**
	 * The aggresive behavior.
	 */
	private static final AggressiveBehavior AGGRO_BEHAVIOR = new AggressiveBehavior() {
		@Override
		public boolean canSelectTarget(Entity entity, Entity target) {
			RockCrabNPC npc = (RockCrabNPC) entity;
			if (entity.getLocation().withinDistance(target.getLocation(), 3)) {
				npc.aggresor = true;
				npc.target = target;
				npc.transform(npc.getTransformId());
				return true;
			}
			return super.canSelectTarget(entity, target);
		}
	};

	/**
	 * If currently an aggresor.
	 */
	private boolean aggresor;

	/**
	 * The target.
	 */
	private Entity target;

	/**
	 * Constructs a new {@code RockCrabNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 */
	public RockCrabNPC(int id, Location location) {
		super(id, location, false);
		super.setAggressiveHandler(new AggressiveHandler(this, AGGRO_BEHAVIOR));
		this.setAggressive(true);
		this.setWalks(false);
	}

	/**
	 * Constructs a new {@code RockCrabNPC} {@code Object}.
	 */
	public RockCrabNPC() {
		super(-1, null);
	}

	@Override
	public void handleTickActions() {
		super.handleTickActions();
		if (aggresor && !inCombat() && target.getLocation().getDistance(this.getLocation()) > 12) {
			reTransform();
			aggresor = false;
			getWalkingQueue().reset();
			setWalks(false);
		}
	}

	@Override
	public void sendImpact(BattleState state) {
		if (state.getEstimatedHit() >= 3 && RandomFunction.random(30) != 5) {
			state.setEstimatedHit(0);
		}
		if (state.getEstimatedHit() == 2 && RandomFunction.random(30) != 5) {
			state.setEstimatedHit(0);
		}
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new RockCrabNPC(id, location);
	}

	/**
	 * Gets the transform id.
	 * @return the id.
	 */
	private int getTransformId() {
		if (getId() != super.getOriginalId()) {
			return getOriginalId();
		}
		return getId() - 1;
	}

	@Override
	public int[] getIds() {
		return new int[] { 1266, 1268, 2453, 2890 };
	}

}
