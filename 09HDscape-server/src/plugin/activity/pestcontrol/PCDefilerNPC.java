package plugin.activity.pestcontrol;

import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.BattleState;
import org.crandor.game.node.entity.combat.CombatStyle;
import org.crandor.game.node.entity.combat.CombatSwingHandler;
import org.crandor.game.node.entity.combat.InteractionType;
import org.crandor.game.node.entity.combat.handlers.RangeSwingHandler;
import org.crandor.game.node.entity.npc.AbstractNPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.MapDistance;

/**
 * Handles the Defiler NPCs.
 * @author Emperor
 */
public final class PCDefilerNPC extends AbstractNPC {

	/**
	 * The pest control session.
	 */
	private PestControlSession session;

	/**
	 * The combat swing handler.
	 */
	private static final CombatSwingHandler SWING_HANDLER = new RangeSwingHandler() {

		@Override
		public InteractionType canSwing(Entity entity, Entity victim) {
			if (!isProjectileClipped(entity, victim, false)) {
				return InteractionType.NO_INTERACT;
			}
			if (victim.getCenterLocation().withinDistance(entity.getCenterLocation(), 15) && isAttackable(entity, victim) != InteractionType.NO_INTERACT) {
				if (victim.getLocation().withinDistance(entity.getLocation(), MapDistance.RENDERING.getDistance() / 2)) {
					entity.getWalkingQueue().reset();
				}
				return InteractionType.STILL_INTERACT;
			}
			return InteractionType.NO_INTERACT;
		}
	};

	/**
	 * Constructs a new {@code PCDefilerNPC} {@code Object}.
	 */
	public PCDefilerNPC() {
		super(3762, null);
	}

	/**
	 * Constructs a new {@code PCDefilerNPC} {@code Object}.
	 * @param id The NPC id.
	 * @param location The location.
	 */
	public PCDefilerNPC(int id, Location location) {
		super(id, location);
	}

	@Override
	public void init() {
		super.setAggressive(true);
		super.init();
		super.getDefinition().setCombatDistance(15);
		super.walkRadius = 64;
		getProperties().getCombatPulse().setStyle(CombatStyle.RANGE);
		session = getExtension(PestControlSession.class);
	}

	@Override
	public void tick() {
		super.tick();
		if (session != null && !inCombat() && !getProperties().getCombatPulse().isAttacking()) {
			getProperties().getCombatPulse().attack(session.getSquire());
		}
	}

	@Override
	public void onImpact(final Entity entity, BattleState state) {
		super.onImpact(entity, state);
		if (session != null && state != null && entity instanceof Player) {
			int total = 0;
			if (state.getEstimatedHit() > 0) {
				total += state.getEstimatedHit();
			}
			if (state.getSecondaryHit() > 0) {
				total += state.getSecondaryHit();
			}
			session.addZealGained((Player) entity, total);
		}
	}

	@Override
	public CombatSwingHandler getSwingHandler(boolean swing) {
		return SWING_HANDLER;
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new PCDefilerNPC(id, location);
	}

	@Override
	public int[] getIds() {
		return new int[] { 3762, 3763, 3764, 3765, 3766, 3767, 3768, 3769, 3770, 3771 };
	}

}