package plugin.npc.bosses.wilderness.venenatis;

import org.crandor.game.content.global.BossKillCounter;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.BattleState;
import org.crandor.game.node.entity.combat.CombatStyle;
import org.crandor.game.node.entity.combat.CombatSwingHandler;
import org.crandor.game.node.entity.combat.InteractionType;
import org.crandor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.crandor.game.node.entity.impl.Projectile;
import org.crandor.game.node.entity.npc.AbstractNPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.state.EntityState;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Handles the Venenatis NPC.
 * @author Vexia
 *
 */
@InitializablePlugin
public class VenenatisNPC extends AbstractNPC {

	/**
	 * The venenatis combat swing handler.
	 */
	private final VenenatisCombatSwingHandler handler = new VenenatisCombatSwingHandler();

	/**
	 * Constructs a new @{Code VenenatisNPC} object.
	 */
	public VenenatisNPC() {
		super(-1, null);
	}

	/**
	 * Constructs a new @{Code VenenatisNPC} object.
	 * @param id The id.
	 * @param location The location.
	 */
	public VenenatisNPC(int id, Location location) {
		super(id, location);
	}

	@Override
	public void init() {
		super.init();
		super.setAggressive(true);
		super.setDefaultBehavior();
		configureBossData();
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new VenenatisNPC(id, location);
	}

	@Override
	public void finalizeDeath(Entity killer) {
		super.finalizeDeath(killer);
		if (killer instanceof Player) {
			BossKillCounter.addtoKillcount((Player) killer, this.getId());
		}
	}

	@Override
	public void sendImpact(BattleState state) {
		if (state.getEstimatedHit() > 50) {
			state.setEstimatedHit(RandomFunction.random(40, 50));
		} else if (state.getSecondaryHit() > 50) {
			state.setEstimatedHit(RandomFunction.random(40, 50));
		}
		super.sendImpact(state);
	}
	
	@Override
	public void checkImpact(BattleState state) {
		super.checkImpact(state);
	}
	
	@Override
	public CombatSwingHandler getSwingHandler(boolean swing) {
		return handler;
	}

	@Override
	public int[] getIds() {
		return new int[] {8612};
	}

	/**
	 * Handles the combat for the npc Venenatis.
	 * @author Vexia
	 *
	 */
	public class VenenatisCombatSwingHandler extends CombatSwingHandler {

		/**
		 * The current combat style.
		 */
		private CombatStyle style = CombatStyle.MAGIC;

		/**
		 * If the special attack is launched.
		 */
		private boolean special;

		/**
		 * If the player drain attack is launched.
		 */
		private boolean prayerDrain;

		/**
		 * Constructs a new @{Code VenenatisCombatSwingHandler} object.
		 */
		public VenenatisCombatSwingHandler() {
			super(CombatStyle.MAGIC);
		}

		@Override
		public InteractionType canSwing(Entity entity, Entity victim) {
			return style.getSwingHandler().canSwing(entity, victim);
		}

		@Override
		public int swing(Entity entity, Entity victim, BattleState state) {
			if (style == CombatStyle.MAGIC) {
				return 2 + (int) Math.floor(entity.getLocation().getDistance(victim.getLocation()) * 0.5);
			}
			return style.getSwingHandler().swing(entity, victim, state);
		}

		@Override
		public void impact(Entity entity, Entity victim, BattleState state) {
			if (victim instanceof Player) {
				Player p = (Player) victim;
				if (special) {
					int hit = style.getSwingHandler().calculateHit(entity, victim, 1.0);
					if (hit < 10) {
						hit = RandomFunction.random(10, 30);
					}
					if (hit > 50) {
						hit = 50;
					}
					p.getStateManager().register(EntityState.STUNNED, true, 5, "Venenatis hurls her web at you, sticking you to the ground.");
					p.getImpactHandler().manualHit(entity, hit, HitsplatType.NORMAL);
					special = false;
				} else if (prayerDrain) {
					p.graphics(new Graphics(170, 96));
					p.getSkills().decrementPrayerPoints(p.getSkills().getPrayerPoints() * 0.30);
					p.sendMessage("You prayer has been drained!");
					prayerDrain = false;
				}
			}
			style.getSwingHandler().impact(entity, victim, state);
			if (RandomFunction.random(2) == 1) {
				style = CombatStyle.MAGIC;
			} else {
				style = CombatStyle.MELEE;
			}
			this.setType(style);
		}

		@Override
		public void visualizeImpact(Entity entity, Entity victim, BattleState state) {
			if ((style == CombatStyle.MAGIC || style == CombatStyle.MELEE) && RandomFunction.random(20) == 1) {
				special = true;
			}
			switch (style) {
			case MAGIC:
				Projectile.create(entity, victim, 165).send();
				break;
			case MELEE:
				if (!special && victim.getSkills().getPrayerPoints() > 0 && RandomFunction.random(20) == 1) {
					prayerDrain = true;
				}
				return;
			default:
				break;
			}
			style.getSwingHandler().visualizeImpact(entity, victim, state);
		}

		@Override
		public int calculateAccuracy(Entity entity) {
			return style.getSwingHandler().calculateAccuracy(entity);
		}

		@Override
		public int calculateHit(Entity entity, Entity victim, double modifier) {
			return style.getSwingHandler().calculateHit(entity, victim, modifier);
		}

		@Override
		public int calculateDefence(Entity entity, Entity attacker) {
			return style.getSwingHandler().calculateDefence(entity, attacker);
		}

		@Override
		public double getSetMultiplier(Entity e, int skillId) {
			return style.getSwingHandler().getSetMultiplier(e, skillId);
		}

	}
}
