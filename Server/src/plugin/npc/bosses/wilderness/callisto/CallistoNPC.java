package plugin.npc.bosses.wilderness.callisto;

import org.crandor.game.content.global.BossKillCounter;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.BattleState;
import org.crandor.game.node.entity.combat.CombatStyle;
import org.crandor.game.node.entity.combat.CombatSwingHandler;
import org.crandor.game.node.entity.combat.InteractionType;
import org.crandor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.crandor.game.node.entity.impl.Projectile;
import org.crandor.game.node.entity.npc.AbstractNPC;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.prayer.PrayerType;
import org.crandor.game.node.entity.state.EntityState;
import org.crandor.game.world.map.Direction;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.RegionManager;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Handles the Callisto NPC.
 * @author Vexia
 *
 */
@InitializablePlugin
public class CallistoNPC extends AbstractNPC {

	/**
	 * The callisto combat swing handler.
	 */
	private final CallistoCombatSwingHandler handler = new CallistoCombatSwingHandler();

	/**
	 * Constructs a new @{Code CallistoNPC} object.
	 */
	public CallistoNPC() {
		super(-1, null);
	}

	/**
	 * Constructs a new @{Code CallistoNPC} object.
	 * @param id The id.
	 * @param location The location.
	 */
	public CallistoNPC(int id, Location location) {
		super(id, location);
	}
	
	@Override
	public void init() {
		super.init();
		configureBossData();
	}

	@Override
	public void finalizeDeath(Entity killer) {
		super.finalizeDeath(killer);
		if (killer instanceof Player) {
			BossKillCounter.addtoKillcount((Player) killer, this.getId());
		}
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new CallistoNPC(id, location);
	}

	@Override
	public void sendImpact(BattleState state) {
		if (state.getEstimatedHit() > 60) {
			state.setEstimatedHit(RandomFunction.random(40, 60));
		} else if (state.getSecondaryHit() > 60) {
			state.setEstimatedHit(RandomFunction.random(40, 60));
		}
		super.sendImpact(state);
	}

	@Override
	public void checkImpact(BattleState state) {
		if (state.getEstimatedHit() > 0 && handler.isHeal()) {
			handler.setHeal(false);
			int dif = (int) (state.getEstimatedHit() * 0.20);
			if (state.getEstimatedHit() - dif > 0) {
				state.setEstimatedHit(state.getEstimatedHit() - dif);
			}
			getSkills().heal(dif);
		}
		super.checkImpact(state);
	}

	@Override
	public CombatSwingHandler getSwingHandler(boolean swing) {
		return handler;
	}

	@Override
	public int[] getIds() {
		return new int[] {8610};
	}

	/**
	 * Handles the combat for the npc Callisto.
	 * @author Vexia
	 *
	 */
	public class CallistoCombatSwingHandler extends CombatSwingHandler {

		/**
		 * The current combat style.
		 */
		private CombatStyle style = CombatStyle.MAGIC;

		/**
		 * If the special attack is launched.
		 */
		private boolean special;

		/**
		 * If the heal attack is launched.
		 */
		private boolean heal;

		/**
		 * Constructs a new @{Code CallistoCombatSwingHandler} object.
		 */
		public CallistoCombatSwingHandler() {
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
					for (Player o : RegionManager.getLocalPlayers(entity, 6)) {
						Direction dir = Direction.getLogicalDirection(entity.getLocation(), o.getLocation());
						Location loc = o.getLocation().transform(dir.getStepX() * 7, dir.getStepY() * 7, 0);
						if (!RegionManager.isTeleportPermitted(loc) || RegionManager.getObject(loc) != null) {
							continue;
						}
						o.teleport(loc);
						o.getStateManager().register(EntityState.STUNNED, true, 3, "Callisto's roar throws you backwards."); 
						o.getImpactHandler().manualHit(entity, 3, HitsplatType.NORMAL);
					};
				}
				if (!special && state.getStyle() == CombatStyle.MAGIC) {
					p.sendMessage("Callisto's fury sends an almighty shockwave through you.");
				}
			}
			if (style == CombatStyle.MAGIC) {
				state.setMaximumHit(60);
			}
			if (!special) {
				style.getSwingHandler().impact(entity, victim, state);
			}
			if (RandomFunction.random(2) == 1) {
				style = CombatStyle.MAGIC;
			} else {
				style = CombatStyle.MELEE;
			}
			this.setType(style);
			if (special) {
				special = false;
			}
		}

		@Override
		public void visualizeImpact(Entity entity, Entity victim, BattleState state) {
			if (RandomFunction.random(20) == 1) {
				special = true;
			}
			if (special) {
				return;
			}
			if (entity.getSkills().getLifepoints() < entity.getSkills().getStaticLevel(Skills.HITPOINTS) && RandomFunction.random(20) == 1) {
				setHeal(true);
				entity.graphics(Graphics.create(481));
			}
			switch (style) {
			case MAGIC:
				if (victim instanceof Player && !victim.asPlayer().getPrayer().get(PrayerType.PROTECT_FROM_MAGIC) || victim instanceof NPC) {
					state.setEstimatedHit(RandomFunction.random(1, 60));
				} 
				Projectile.create(entity, victim, 395).send();
				break;
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

		/**
		 * @return the heal
		 */
		public boolean isHeal() {
			return heal;
		}

		/**
		 * @param heal the heal to set
		 */
		public void setHeal(boolean heal) {
			this.heal = heal;
		}
	}
}
