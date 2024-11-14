package content.region.wilderness.handlers;

import content.data.BossKillCounter;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.*;
import core.game.node.entity.combat.ImpactHandler.HitsplatType;
import core.game.node.entity.combat.equipment.SwitchAttack;
import core.game.node.entity.combat.equipment.Weapon;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.npc.NPCBehavior;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.tools.RandomFunction;
import org.rs09.consts.NPCs;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the Corporeal beast NPC.
 * @author Emperor
 */
@Initializable
public final class CorporealBeastNPC extends NPCBehavior {

	/**
	 * The combat handler.
	 */
	private final MultiSwingHandler combatHandler = new CombatHandler();

	/**
	 * The dark energy core NPC.
	 */
	public NPC darkEnergyCore;

	/**
	 * Whether to force a dark core spawn roll on our next swing (only done if we just got hit >= 32 damage).
	 */
	public boolean forceCoreRoll = false;

	/**
	 * Constructs a new {@code CorporealBeastNPC} {@code Object}.
	 */
	public CorporealBeastNPC() {
		super(new int[] { NPCs.CORPOREAL_BEAST_8133 });
	}

	@Override
	public void onCreation(NPC self) {
		self.configureBossData();
	}

	@Override
	public CombatSwingHandler getSwingHandlerOverride(NPC self, CombatSwingHandler original) {
		return combatHandler;
	}

	@Override
	public void beforeDamageReceived(NPC self, Entity attacker, BattleState state) {
		if (state.getStyle() == CombatStyle.MELEE || state.getStyle() == CombatStyle.RANGE) {
			Weapon w = state.getWeapon();
			String name = w != null ? w.getName() : "";
			if (w == null || name.toLowerCase().indexOf("spear") == -1) {
				if (state.getEstimatedHit() > 0) {
					state.setEstimatedHit(state.getEstimatedHit() / 2);
				}
				if (state.getSecondaryHit() > 0) {
					state.setSecondaryHit(state.getSecondaryHit() / 2);
				}
			}
		}
		if (state.getEstimatedHit() >= 32) {
			CorporealBeastNPC corp = (CorporealBeastNPC) self.behavior;
			corp.forceCoreRoll = true;
		}
		if (state.getEstimatedHit() > 100) {
			state.setEstimatedHit(100);
		}
		if (state.getSecondaryHit() > 100) {
			state.setSecondaryHit(100);
		}
	}

	@Override
	public void onDeathFinished(NPC self, Entity killer) {
		BossKillCounter.addtoKillcount((Player) killer, NPCs.CORPOREAL_BEAST_8133);
		if (darkEnergyCore != null) {
			darkEnergyCore.clear();
			darkEnergyCore = null;
		}
	}

	/**
	 * Handles the Corporeal beast's combat.
	 * @author Emperor
	 */
	static class CombatHandler extends MultiSwingHandler {

		/**
		 * Constructs a new {@code CombatHandler} {@code Object}.
		 */
		public CombatHandler() {
			super(
				//Melee (crush)
				new SwitchAttack(CombatStyle.MELEE.getSwingHandler(), Animation.create(10057)).setMaximumHit(51),
				//Melee (slash)
				new SwitchAttack(CombatStyle.MELEE.getSwingHandler(), Animation.create(10058)).setMaximumHit(51),
				//Magic (drain skill, blocked by prayer)
				new SwitchAttack(CombatStyle.MAGIC.getSwingHandler(), Animation.create(10410), null, null, Projectile.create(null, null, 1823, 60, 36, 41, 46)).setMaximumHit(55),
				//Magic (location-based, hits through prayer)
				new SwitchAttack(CombatStyle.MAGIC.getSwingHandler(), Animation.create(10410), null, null, Projectile.create(null, null, 1824, 60, 36, 41, 46)).setMaximumHit(42),
				//Magic (hits through prayer)
				new SwitchAttack(CombatStyle.MAGIC.getSwingHandler(), Animation.create(10410), null, null, Projectile.create(null, null, 1825, 60, 36, 41, 46)).setMaximumHit(65)
			);
		}

		@Override
		public int swing(Entity entity, Entity victim, BattleState state) {
			// If we're below the right HP threshold, roll a chance to spawn the dark core
			CorporealBeastNPC corp = (CorporealBeastNPC) ((NPC) entity).behavior;
			double thresh = entity.getSkills().getMaximumLifepoints() * (0.3 + (entity.getViewport().getCurrentPlane().getPlayers().size() * 0.05));
			if (corp.forceCoreRoll || entity.getSkills().getLifepoints() < thresh) {
				rollDarkCore(entity, corp, victim);
				corp.forceCoreRoll = false;
			}
			// If we can stomp, do that for our turn
			if (doStompAttack(entity)) {
				entity.getProperties().getCombatPulse().setNextAttack(entity.getProperties().getAttackSpeed());
				return -1;
			}
			// Location-based attack.
			if (super.getNext().getProjectile() != null && super.getNext().getProjectile().getProjectileId() == 1824) {
				setCurrent(getNext());
				CombatStyle style = getCurrent().getStyle();
				setType(style);
				int index = RandomFunction.randomize(super.getAttacks().length);
				SwitchAttack pick = getNext(entity, victim, state, index);
				setNext(pick);
				fireLocationBasedAttack(entity, victim.getLocation());
				entity.getProperties().getCombatPulse().setNextAttack(entity.getProperties().getAttackSpeed());
				return -1;
			}
			return super.swing(entity, victim, state);
		}

		/**
		 * Rolls a 1/8 chance to spawn a dark core.
		 * @param npc The corporeal beast NPC.
		 * @param victim The victim.
		 */
		private void rollDarkCore(Entity corp, final CorporealBeastNPC npc, Entity victim) {
			if (npc.darkEnergyCore != null && npc.darkEnergyCore.isActive() && !DeathTask.isDead(npc.darkEnergyCore)) {
				return;
			}
			if (!RandomFunction.roll(8)) {
				return;
			}
			Location l = RegionManager.getTeleportLocation(victim.getLocation(), 3);
			npc.darkEnergyCore = NPC.create(8127, l, corp);
			npc.darkEnergyCore.setActive(true);
			Projectile.create(corp.getLocation().transform(2, 2, 0), l, 1828, 60, 0, 0, 60, 20, 0).send();
			GameWorld.getPulser().submit(new Pulse(2, corp) {
				@Override
				public boolean pulse() {
					if (npc.darkEnergyCore == null)
						return true;
					npc.darkEnergyCore.init();
					return true;
				}
			});
		}

		/**
		 * Fires the location based magic attack.
		 * @param entity The corporeal beast.
		 * @param location The location.
		 */
		private void fireLocationBasedAttack(final Entity entity, final Location location) {
			entity.animate(getCurrent().getAnimation());
			Projectile.create(entity, null, 1824, 60, 0, 41, 0).transform(entity, location, true, 46, 10).send();
			int ticks = 1 + (int) Math.ceil(entity.getLocation().getDistance(location) * 0.5);
			GameWorld.getPulser().submit(new Pulse(ticks) {
				boolean secondStage = false;
				List<Player> players = RegionManager.getLocalPlayers(entity);
				Location[] locations = null;

				@Override
				public boolean pulse() {
					if (!secondStage) {
						for (Player p : players) {
							if (p.getLocation().equals(location)) {
								hit(p);
							}
						}
						locations = new Location[4 + RandomFunction.random(3)];
						for (int i = 0; i < locations.length; i++) {
							locations[i] = location.transform(-2 + RandomFunction.random(5), -2 + RandomFunction.random(5), 0);
							Projectile.create(location, locations[i], 1824, 60, 0, 25, 56, 0, 0).send();
						}
						setDelay(2);
						secondStage = true;
						return false;
					}
					for (int i = 0; i < locations.length; i++) {
						Location l = locations[i];
						Graphics.send(Graphics.create(1806), l.transform(-1, -1, 0));
						for (Player p : players) {
							if (p.getLocation().equals(l)) {
								hit(p);
							}
						}
					}
					players.clear();
					players = null;
					locations = null;
					return true;
				}

				private void hit(Player p) {
					int hit = 0;
					if (isAccurateImpact(entity, p)) {
						hit = RandomFunction.random(42);
						if (p.hasProtectionPrayer(CombatStyle.MAGIC)) {
							hit = (int) (hit * 0.6);
						}
					}
					p.getImpactHandler().handleImpact(entity, hit, CombatStyle.MAGIC);
				}
			});
		}

		/**
		 * Attempts to do a stomp attack if needed.
		 * @param entity The corporeal beast.
		 * @return {@code True} if a stomp attack is being done.
		 */
		public boolean doStompAttack(Entity entity) {
			Location l = entity.getLocation();
			List<Player> stompTargets = null;
			for (Player player : RegionManager.getLocalPlayers(entity, 5)) {
				Location p = player.getLocation();
				if (p.getX() >= l.getX() && p.getY() >= l.getY() && p.getX() < l.getX() + entity.size() && p.getY() < l.getY() + entity.size()) {
					if (stompTargets == null) {
						stompTargets = new ArrayList<>(20);
					}
					stompTargets.add(player);
				}
			}
			if (stompTargets != null) {
				entity.visualize(Animation.create(10496), Graphics.create(1834));
				for (Player p : stompTargets) {
					p.getImpactHandler().manualHit(entity, RandomFunction.random(52), HitsplatType.NORMAL, 1);
				}
				return true;
			}
			return false;
		}

		@Override
		public void adjustBattleState(Entity entity, Entity victim, BattleState state) {
			super.adjustBattleState(entity, victim, state);
			if (getCurrent().getProjectile() != null && getCurrent().getProjectile().getProjectileId() == 1823) {
				if (state.getEstimatedHit() > 0) {
					int random = RandomFunction.random(3);
					int skill = random == 0 ? Skills.PRAYER : random == 1 ? Skills.MAGIC : Skills.SUMMONING;
					int drain = 1 + RandomFunction.random(6);
					if ((skill == Skills.PRAYER ? victim.getSkills().getPrayerPoints() : victim.getSkills().getLevel(skill)) < 1) {
						victim.getImpactHandler().manualHit(entity, drain, HitsplatType.NORMAL, 2);
						((Player) victim).getPacketDispatch().sendMessage("Your Hitpoints have been slightly drained!");
					} else {
						if (skill == Skills.PRAYER) {
							victim.getSkills().decrementPrayerPoints(drain);
						} else {
							victim.getSkills().updateLevel(skill, -drain, 0);
						}
						if (victim instanceof Player) {
							((Player) victim).getPacketDispatch().sendMessage("Your " + Skills.SKILL_NAME[skill] + " has been slightly drained!");
						}
					}
				}
			}
		}

		@Override
		protected int getFormattedHit(Entity entity, Entity victim, BattleState state, int hit) {
			if (getCurrent().getProjectile() == null || getCurrent().getProjectile().getProjectileId() != 1825) {
				hit = (int) entity.getFormattedHit(state, hit);
			} else if (victim.hasProtectionPrayer(CombatStyle.MAGIC)) {
				hit = (int) (hit * 0.6);
			}
			return formatHit(victim, hit);
		}
	}
}
