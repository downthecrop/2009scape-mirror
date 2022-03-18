package core.game.node.entity.skill.slayer;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.world.map.Location;
import core.plugin.Initializable;
import rs09.game.node.entity.combat.CombatSwingHandler;
import rs09.game.node.entity.combat.handlers.MagicSwingHandler;
import rs09.game.node.entity.skill.slayer.SlayerEquipmentFlags;

/**
 * Handles the aberrant spectre npc.
 * @author Vexia
 */
@Initializable
public final class AberrantSpectreNPC extends AbstractNPC {

	/**
	 * The skills to drain.
	 */
	private static final int[] SKILLS = new int[] { Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE, Skills.RANGE, Skills.MAGIC, Skills.PRAYER, Skills.AGILITY };

	/**
	 * The combat handler to use.
	 */
	private static final MagicSwingHandler COMBAT_HANDLER = new MagicSwingHandler() {
		@Override
		public void impact(final Entity entity, final Entity victim, BattleState state) {
			if (victim instanceof Player) {
				Player player = (Player) victim;
				if (!SlayerEquipmentFlags.hasNosePeg(player)) {
					for (int skill : SKILLS) {
						int drain = (int) (player.getSkills().getStaticLevel(skill) * 0.5);
						player.getSkills().updateLevel(skill, -drain, 0);
					}
				}
			}
			super.impact(entity, victim, state);
		}
	};

	/**
	 * Constructs a new {@code AberrantSpectreNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 */
	public AberrantSpectreNPC(int id, Location location) {
		super(id, location);
	}

	/**
	 * Constructs a new {@code AberrantSpectreNPC} {@code Object}.
	 */
	public AberrantSpectreNPC() {
		super(0, null);
	}

	@Override
	public void checkImpact(BattleState state) {
		super.checkImpact(state);
		if (state.getAttacker() instanceof Player) {
			final Player player = (Player) state.getAttacker();
			if (!SlayerEquipmentFlags.hasNosePeg(player)) {
				state.neutralizeHits();
			}
		}
	}

	@Override
	public CombatSwingHandler getSwingHandler(boolean swing) {
		return COMBAT_HANDLER;
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new AberrantSpectreNPC(id, location);
	}

	@Override
	public int[] getIds() {
		return Tasks.ABERRANT_SPECTRES.getNpcs();
	}

}
