package core.game.node.entity.skill.slayer;

import core.plugin.Initializable;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import rs09.game.node.entity.combat.CombatSwingHandler;
import core.game.node.entity.combat.InteractionType;
import rs09.game.node.entity.combat.handlers.MeleeSwingHandler;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import rs09.game.node.entity.skill.slayer.SlayerEquipmentFlags;
import rs09.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.tools.RandomFunction;

/**
 * Handles the banshee npc.
 * @author Vexia
 */
@Initializable
public final class BansheeNPC extends AbstractNPC {

	/**
	 * The skills to drain.
	 */
	private static final int[] SKILLS = new int[] { Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE, Skills.RANGE, Skills.MAGIC, Skills.PRAYER, Skills.AGILITY };

	/**
	 * The combat handler.
	 */
	private static final MeleeSwingHandler COMBAT_HANDLER = new MeleeSwingHandler() {
		@Override
		public void impact(Entity entity, Entity victim, BattleState state) {
			if (victim instanceof Player) {
				final Player player = (Player) victim;
				if (!hasEarMuffs(player)) {
					if (RandomFunction.random(10) < 4 && player.getProperties().getCombatPulse().getNextAttack() <= GameWorld.getTicks()) {
						player.getWalkingQueue().reset();
						player.getLocks().lockMovement(3);
						player.getProperties().getCombatPulse().setNextAttack(3);
						player.animate(new Animation(1572, Priority.HIGH));
					}
					for (int skill : SKILLS) {
						int drain = (int) (player.getSkills().getStaticLevel(skill) * 0.5);
						player.getSkills().updateLevel(skill, -drain, 0);
					}
					state.setEstimatedHit(8);
				}
			}
			super.impact(entity, victim, state);
		}

		@Override
		public InteractionType isAttackable(Entity entity, Entity victim) {
			return CombatStyle.MAGIC.getSwingHandler().isAttackable(entity, victim);
		}
	};

	/**
	 * Constructs a new {@code BansheeNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 */
	public BansheeNPC(int id, Location location) {
		super(id, location);
	}

	/**
	 * Constructs a new {@code BansheeNPC} {@code Object}.
	 */
	public BansheeNPC() {
		super(0, null);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new BansheeNPC(id, location);
	}

	@Override
	public void onImpact(Entity entity, BattleState state) {
		super.onImpact(entity, state);
		if (state.getAttacker() instanceof Player) {
			final Player player = (Player) state.getAttacker();
			if (!hasEarMuffs(player)) {
				state.neutralizeHits();
			}
		}
		if (state.getEstimatedHit() > 0 || state.getSecondaryHit() > 0) {
			getSkills().heal(1);
		}
	}

	@Override
	public CombatSwingHandler getSwingHandler(boolean swing) {
		return COMBAT_HANDLER;
	}

	/**
	 * Checks if the player has ear muffs.
	 * @param player the player.
	 * @return {@code True} if they have it.
	 */
	public static boolean hasEarMuffs(Player player) {
		return SlayerEquipmentFlags.hasEarmuffs(player);
	}

	@Override
	public int[] getIds() {
		return new int[] { 1612 };
	}

}
