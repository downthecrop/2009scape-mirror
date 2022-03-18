package core.game.node.entity.skill.slayer;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import rs09.game.node.entity.combat.CombatSwingHandler;
import core.game.node.entity.combat.InteractionType;
import rs09.game.node.entity.combat.handlers.MeleeSwingHandler;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.tools.RandomFunction;
import rs09.game.node.entity.skill.slayer.SlayerEquipmentFlags;

/**
 * Handles the Cave Horrors on Mos'le Harmless. You technically do not need a witchwood icon to fight them.
 * "Without the icon, cave horrors can damage the player for 10% of their total hitpoints, although it does not lower stats."
 * @author Splinter
 */
@Initializable
public final class CaveHorrorNPC extends AbstractNPC {
	/**
	 * The Cave Horror combat handler.
	 */
	private static final MeleeSwingHandler COMBAT_HANDLER = new MeleeSwingHandler() {
		@Override
		public void impact(Entity entity, Entity victim, BattleState state) {
			if (victim instanceof Player) {
				final Player player = (Player) victim;
				if (!hasWitchwood(player)) {
					if (RandomFunction.random(10) < 5) {
						state.setEstimatedHit(player.getSkills().getLifepoints() / 10);
					}
				}
			}
			super.impact(entity, victim, state);
		}

		@Override
		public InteractionType isAttackable(Entity entity, Entity victim) {
			return CombatStyle.MELEE.getSwingHandler().isAttackable(entity, victim);
		}
	};

	/**
	 * Constructs a new {@code CaveHorrorNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 */
	public CaveHorrorNPC(int id, Location location) {
		super(id, location);
	}

	/**
	 * Constructs a new {@code CaveHorrorNPC} {@code Object}.
	 */
	public CaveHorrorNPC() {
		super(0, null);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new CaveHorrorNPC(id, location);
	}

	@Override
	public CombatSwingHandler getSwingHandler(boolean swing) {
		return COMBAT_HANDLER;
	}

	/**
	 * Checks if the player has the icon equipped.
	 * @param player the player.
	 * @return {@code true} if it is equipped.
	 */
	public static boolean hasWitchwood(Player player) {
		return SlayerEquipmentFlags.hasWitchwoodIcon(player);
	}

	@Override
	public int[] getIds() {
		return new int[] { 4354, 4355, 4353, 4356, 4357 };
	}

}
