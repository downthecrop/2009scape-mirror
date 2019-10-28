package plugin.npc;

import org.crandor.game.content.global.BossKillCounter;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.BattleState;
import org.crandor.game.node.entity.combat.CombatStyle;
import org.crandor.game.node.entity.combat.CombatSwingHandler;
import org.crandor.game.node.entity.combat.equipment.SwitchAttack;
import org.crandor.game.node.entity.combat.handlers.MultiSwingHandler;
import org.crandor.game.node.entity.impl.Projectile;
import org.crandor.game.node.entity.npc.AbstractNPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.RegionManager;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Handles the chaos elemental npc.
 * @author Vexia
 */
@InitializablePlugin
public class ChaosElementalNPC extends AbstractNPC {

	/**
	 * The multi swing handler.
	 */
	private final MultiSwingHandler COMBAT_HANDLER = new ChaosCombatHandler();

	/**
	 * Constructs a new {@Code ChaosElementalNPC} {@Code Object}
	 * @param id the id.
	 * @param location the location.
	 */
	public ChaosElementalNPC(int id, Location location) {
		super(id, location);
	}

	/**
	 * Constructs a new {@Code ChaosElementalNPC} {@Code Object}
	 */
	public ChaosElementalNPC() {
		this(-1, null);
	}

	@Override
	public CombatSwingHandler getSwingHandler(boolean swing) {
		return COMBAT_HANDLER;
	}

	@Override
	public void sendImpact(BattleState state) {
		if (state.getEstimatedHit() > 28) {
			state.setEstimatedHit(RandomFunction.random(20, 28));
		}
		super.sendImpact(state);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new ChaosElementalNPC(id, location);
	}

	@Override
	public int[] getIds() {
		return new int[] { 3200 };
	}

	@Override
	public void finalizeDeath(Entity killer) {
		super.finalizeDeath(killer);
		BossKillCounter.addtoKillcount((Player) killer, this.getId());
	}

	/**
	 * Handles the chaos combat handler.
	 * @author Vexia
	 */
	public static class ChaosCombatHandler extends MultiSwingHandler {

		/**
		 * The projectile animation.
		 */
		private static final Animation PROJECTILE_ANIM = new Animation(3148);

		/**
		 * The primary projectile.
		 */
		private static final Projectile PRIMARY_PROJECTILE = Projectile.create((Entity) null, null, 557, 60, 55, 41, 46, 20, 255);

		/**
		 * The switch attacks.
		 */
		private static final SwitchAttack[] ATTACKS = new SwitchAttack[] { new SwitchAttack(CombatStyle.MELEE.getSwingHandler(), PROJECTILE_ANIM, new Graphics(556), null, PRIMARY_PROJECTILE), // primary
				new SwitchAttack(CombatStyle.RANGE.getSwingHandler(), PROJECTILE_ANIM, new Graphics(556), null, PRIMARY_PROJECTILE),// primary
				new SwitchAttack(CombatStyle.MAGIC.getSwingHandler(), PROJECTILE_ANIM, new Graphics(556), null, PRIMARY_PROJECTILE),// primary
				new SwitchAttack(CombatStyle.MAGIC.getSwingHandler(), PROJECTILE_ANIM, new Graphics(553), null, Projectile.create((Entity) null, null, 554, 60, 55, 41, 46, 20, 255)) {
					@Override
					public boolean canSelect(Entity entity, Entity victim, BattleState state) {
						return true;
					}
				},// tele
				new SwitchAttack(CombatStyle.MAGIC.getSwingHandler(), PROJECTILE_ANIM, new Graphics(550), null, Projectile.create((Entity) null, null, 551, 60, 55, 41, 46, 20, 255)) {
					@Override
					public boolean canSelect(Entity entity, Entity victim, BattleState state) {
						return true;
					}
				} };

		/**
		 * Constructs a new {@Code ChaosCombatHandler} {@Code
		 * Object}
		 */
		public ChaosCombatHandler() {
			super(ATTACKS);
		}

		@Override
		public void impact(Entity entity, Entity victim, BattleState state) {
			super.impact(entity, victim, state);
			SwitchAttack attack = super.current;
			if (victim instanceof Player) {
				Player player = victim.asPlayer();
				if (player == null) {
					return;
				}
				if (attack.getProjectile().getProjectileId() == 554) {
					Location loc = getRandomLoc(entity);
					while (!RegionManager.isTeleportPermitted(loc) || RegionManager.getObject(loc) != null) {
						loc = getRandomLoc(entity);
					}
					if (loc.equals(player.getLocation())) {
						loc = entity.getLocation();
					}
					player.teleport(loc);
				} else if (attack.getProjectile().getProjectileId() == 551) {
					if (player.getInventory().freeSlots() < 1 || player.getEquipment().itemCount() < 1) {
						return;
					}
					Item e = null;
					int tries = 0;
					while (e == null && tries < 30) {
						e = player.getEquipment().toArray()[RandomFunction.random(player.getEquipment().itemCount())];
						tries++;
						if (e != null && player.getInventory().hasSpaceFor(e)) {
							break;
						}
						e = null;
					}
					if (e == null) {
						return;
					}
					player.lock(1);
					if (!player.getEquipment().containsItem(e)) {
						return;
					}
					if (player.getEquipment().remove(e)) {
						player.getInventory().add(e);
					}
				}
			}
		}

		/**
		 * Gets the random loc.
		 * @param entity the entity.
		 * @return the loc.
		 */
		public Location getRandomLoc(Entity entity) {
			Location l = entity.getLocation();
			boolean negative = RandomFunction.random(2) == 1;
			return l.getLocation().transform((negative ? RandomFunction.random(-10, 10) : RandomFunction.random(0, 10)), (negative ? RandomFunction.random(-10, 10) : RandomFunction.random(0, 10)), 0);
		}
	}

}
