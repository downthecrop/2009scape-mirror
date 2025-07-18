package content.global.skill.slayer;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.*;
import core.game.node.entity.combat.equipment.SwitchAttack;
import content.global.handlers.item.equipment.special.DragonfireSwingHandler;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.npc.AbstractNPC;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.tools.RandomFunction;

/**
 * Handles a mithril dragon npc.
 * @author Vexia
 */
@Initializable
public final class MithrilDragonNPC extends AbstractNPC {

	/**
	 * Handles the combat.
	 */
	private static final SwitchAttack DRAGONFIRE = DragonfireSwingHandler.get(false, 52, new Animation(81, Priority.HIGH), Graphics.create(1), null, null);
	private static final SwitchAttack MELEE = new SwitchAttack(CombatStyle.MELEE.getSwingHandler(), new Animation(80, Priority.HIGH));
	private static final SwitchAttack MAGIC = new SwitchAttack(CombatStyle.MAGIC.getSwingHandler(), new Animation(81, Priority.HIGH), null, null, Projectile.create((Entity) null, null, 500, 20, 20, 41, 40, 18, 255));
	private static class InRangeSwitchAttack extends SwitchAttack {
		public InRangeSwitchAttack(CombatSwingHandler swingHandler, Animation animation, Graphics startGraphic, Graphics endGraphic, Projectile projectile) {
			super(swingHandler, animation, startGraphic, endGraphic, projectile);
		}
		@Override
		public boolean canSelect(Entity entity, Entity victim, BattleState state) {
			return CombatStyle.MELEE.getSwingHandler().canSwing(entity, victim) == InteractionType.NO_INTERACT; //only select range if not in melee distance
		}
	};
	private static final SwitchAttack RANGE = new InRangeSwitchAttack(CombatStyle.RANGE.getSwingHandler(), new Animation(81, Priority.HIGH), null, null, Projectile.create((Entity) null, null, 16, 20, 20, 41, 40, 18, 255));
	private final CombatSwingHandler combatAction = new MultiSwingHandler(true, MELEE, MAGIC, DRAGONFIRE, RANGE);

	/**
	 * Constructs a new {@code MithrilDragonNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 */
	public MithrilDragonNPC(int id, Location location) {
		super(id, location);
	}

	/**
	 * Constructs a new {@code MithrilDragonNPC} {@code Object}.
	 */
	public MithrilDragonNPC() {
		super(0, null);
	}

	@Override
	public void sendImpact(BattleState state) {
		CombatStyle style = state.getStyle();
		if (style == null) {
			return;
		}
		int maxHit = state.getEstimatedHit();
		if (maxHit < 1) {
			return;
		}
		switch (style) {
		case MELEE:
			maxHit = 28;
			break;
		case MAGIC:
			maxHit = 18;
			break;
		case RANGE:
			maxHit = 18;
			break;
		}
		if (state.getEstimatedHit() > maxHit) {
			state.setEstimatedHit(RandomFunction.random(maxHit - 5, maxHit));
		}
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new MithrilDragonNPC(id, location);
	}

	@Override
	public CombatSwingHandler getSwingHandler(boolean swing) {
		return combatAction;
	}

	@Override
	public int getDragonfireProtection(boolean fire) {
		return 0x2 | 0x4 | 0x8;
	}

	@Override
	public int[] getIds() {
		return Tasks.MITHRIL_DRAGONS.getNpcs();
	}
}
