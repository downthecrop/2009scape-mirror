package core.game.node.entity.npc.bosses.wilderness;

import core.game.content.global.BossKillCounter;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.equipment.SwitchAttack;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.audio.Audio;
import core.game.node.item.Item;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.tools.RandomFunction;
import rs09.game.node.entity.combat.CombatSwingHandler;
import rs09.game.node.entity.combat.handlers.MultiSwingHandler;

/**
 * Handles the chaos elemental npc.
 * @author Vexia
 */
@Initializable
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

	@Override
	public void tick() {
		super.tick();
		if(!isActive()){
			getProperties().getCombatPulse().stop();
		}
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
			state.setEstimatedHit(RandomFunction.random(20, 28)); //possibly absolutely mental "haha random" damage adjustment. not sure. - crash
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
			SwitchAttack attack = super.getCurrent();
			if (victim instanceof Player) {
				Player player = victim.asPlayer();
				if (player == null) {
					return;
				}
				if (attack.getProjectile().getProjectileId() == 557) {
					player.getAudioManager().send(new Audio(350), true); // C. Elemental Discord Impact SFX
				}
				else if (attack.getProjectile().getProjectileId() == 554) {
					player.getAudioManager().send(new Audio(346), true); // C. Elemental Confusion Impact SFX
					Location loc = getRandomLoc(entity);
					while (!RegionManager.isTeleportPermitted(loc) || RegionManager.getObject(loc) != null) {
						loc = getRandomLoc(entity);
					}
					if (loc.equals(player.getLocation())) {
						loc = entity.getLocation();
					}
					player.teleport(loc);
				} else if (attack.getProjectile().getProjectileId() == 551) {
					player.getAudioManager().send(new Audio(353), true); // C. Elemental Madness Impact SFX
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
