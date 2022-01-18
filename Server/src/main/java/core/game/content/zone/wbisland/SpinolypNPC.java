package core.game.content.zone.wbisland;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatSpell;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.InteractionType;
import core.game.node.entity.combat.equipment.SwitchAttack;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.entity.state.EntityState;
import core.game.world.map.Location;
import core.tools.RandomFunction;
import rs09.game.node.entity.combat.CombatSwingHandler;
import rs09.game.node.entity.combat.handlers.MultiSwingHandler;

/**
 * Represents a spinolyp npc.
 * @author Vexia
 */
public final class SpinolypNPC extends AbstractNPC {

	/**
	 * The swing handler.
	 */
	private static final CombatSwingHandler SWING_HANDLER = new SpinolpySwingHandler();

	/**
	 * Constructs a new {@Code SpinolypNPC} {@Code Object}
	 */
	public SpinolypNPC() {
		this(-1, null);
	}

	/**
	 * Constructs a new {@Code SpinolypNPC} {@Code Object}
	 * @param id the id.
	 * @param location the location.
	 */
	public SpinolypNPC(int id, Location location) {
		super(id, location);
		super.setAggressive(true);
		super.setNeverWalks(true);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new SpinolypNPC(id, location);
	}

	@Override
	public CombatSwingHandler getSwingHandler(boolean swing) {
		return SWING_HANDLER;
	}

	@Override
	public void init() {
		super.init();
		super.getLocks().lockMovement(Integer.MAX_VALUE);
		setSpell();
        getAggressiveHandler().setAllowTolerance(false);
	}

	/**
	 * Sets the spell.
	 */
	public void setSpell() {
		CombatSpell spell = (CombatSpell) SpellBook.MODERN.getSpell(14);
		getProperties().setSpell(spell);
		getProperties().setAutocastSpell(spell);
	}

	@Override
	public void sendImpact(BattleState state) {
		if (state.getEstimatedHit() == 0 && RandomFunction.random(15) < 2) {
			state.setEstimatedHit(RandomFunction.random(1, 11));
		}
		if (state.getEstimatedHit() > 11) {
			state.setEstimatedHit(RandomFunction.random(3, 9));
		}
	}

	@Override
	public int[] getIds() {
		return new int[] { 2894, 2896 };
	}

	/**
	 * Handles the spinolpys swings.
	 * @author Vexia
	 */
	public static final class SpinolpySwingHandler extends MultiSwingHandler {

		/**
		 * Constructs a new {@Code SpinolpySwingHandler} {@Code
		 * Object}
		 */
		public SpinolpySwingHandler() {
			super(new SwitchAttack(CombatStyle.MAGIC.getSwingHandler(), null), new SwitchAttack(CombatStyle.RANGE.getSwingHandler(), null));
		}

		@Override
		public InteractionType canSwing(Entity entity, Entity victim) {
			InteractionType type = super.canSwing(entity, victim);
			if (type == InteractionType.MOVE_INTERACT) {
				type = InteractionType.NO_INTERACT;
			}
			return type;
		}

		@Override
		public int swing(Entity entity, Entity victim, BattleState state) {
			int swing = super.swing(entity, victim, state);
			if (getType() == CombatStyle.MAGIC) {
				setSpell(entity);
			}
			return swing;
		}
		
		@Override
		public int getCombatDistance(Entity e, Entity v, int distance) {
			return 12;
		}

        @Override
        public int calculateDefence(Entity v, Entity e) {
            // Spinolyps' attack always targets ranged defence
            return CombatStyle.RANGE.getSwingHandler().calculateDefence(v, e);
        }

		@Override
		public void visualize(Entity entity, Entity victim, BattleState state) {
			super.visualize(entity, victim, state);
			if (state.getStyle() == CombatStyle.MAGIC) {
				setSpell(entity);
				CombatSpell spell = (CombatSpell) SpellBook.MODERN.getSpell(14);
				state.setSpell(spell);
			}
			state.getStyle().getSwingHandler().visualize(entity, victim, state);
			entity.getAnimator().forceAnimation(entity.getProperties().getAttackAnimation());
		}

		@Override
		public void impact(Entity entity, Entity victim, BattleState state) {
			super.impact(entity, victim, state);
			if (super.getType() == CombatStyle.MAGIC && state.getEstimatedHit() > 0) {
				victim.getSkills().decrementPrayerPoints(1);
			} else {
				if (RandomFunction.random(20) == 5) {
					victim.getStateManager().register(EntityState.POISONED, false, 68, entity);
				}
			}
		}

		/**
		 * Sets the spell.
		 */
		public void setSpell(Entity e) {
			CombatSpell spell = (CombatSpell) SpellBook.MODERN.getSpell(14);
			e.getProperties().setSpell(spell);
			e.getProperties().setAutocastSpell(spell);
		}
	}

}
