package core.game.node.entity.npc.other;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.DeathTask;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.npc.agg.AggressiveBehavior;
import core.game.node.entity.npc.agg.AggressiveHandler;
import core.plugin.Initializable;
import core.game.world.map.Location;

/**
 * Handles a Thrower troll NPC.
 * @author Emperor
 */
@Initializable
public final class ThrowerTrollNPC extends AbstractNPC {

	/**
	 * Constructs a new {@code ThrowerTrollNPC} {@code Object}.
	 */
	public ThrowerTrollNPC() {
		super(1130, null);
	}

	/**
	 * Constructs a new {@code ThrowerTrollNPC} {@code Object}.
	 * @param id The NPC id.
	 * @param location The location.
	 */
	public ThrowerTrollNPC(int id, Location location) {
		super(id, location, true);
	}

	@Override
	public void setDefaultBehavior() {
		super.setAggressive(true);
		super.setAggressiveHandler(new AggressiveHandler(this, new AggressiveBehavior() {

			@Override
			public boolean canSelectTarget(Entity entity, Entity target) {
				if (!target.isActive() || DeathTask.isDead(target)) {
					return false;
				}
				if (!target.getProperties().isMultiZone() && target.inCombat()) {
					return false;
				}
				return true;
			}
		}));
		getAggressiveHandler().setChanceRatio(8);
		getAggressiveHandler().setRadius(7);
		getDefinition().setCombatDistance(7);
		super.setWalks(false);
		super.getLocks().lockMovement(1 << 25);
	}

	@Override
	public void init() {
		super.init();
		getProperties().getCombatPulse().setStyle(CombatStyle.RANGE);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new ThrowerTrollNPC(id, location);
	}

	@Override
	public int[] getIds() {
		return new int[] { 1101, 1102, 1103, 1104, 1105, 1130, 1131, 1132, 1133, 1134 };
	}

}
