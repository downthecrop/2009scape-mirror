package core.game.node.entity.npc.bosses.wilderness;

import java.util.ArrayList;
import java.util.List;

import core.game.content.global.BossKillCounter;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import rs09.game.node.entity.combat.CombatSwingHandler;
import core.game.node.entity.combat.ImpactHandler.HitsplatType;
import core.game.node.entity.combat.equipment.SwitchAttack;
import rs09.game.node.entity.combat.handlers.MultiSwingHandler;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.tools.RandomFunction;

/**
 * Handles the Crazy Archaeologist NPC.
 * @author Vexia
 *
 */
@Initializable
public class CrazyArchaeologistNPC extends AbstractNPC {

	/**
	 * The quotes for the crazy archaeologist.
	 */
	private static final String[] QUOTES = new String[] {"I'm Bellock - respect me!", "Get off my site!", "No-one messes with Bellock's dig!", "These ruins are mine!", "Taste my knowledge!", "You belong in a museum!"};

	/**
	 * The multi swing handler.
	 */
	private static final MultiSwingHandler COMBAT_HANDLER = new ArchaeologistHandler();

	/**
	 * Constructs a new @{Code CrazyArchaeologistNPC} object.
	 * @param id The id.
	 * @param location The location.
	 */
	public CrazyArchaeologistNPC(int id, Location location) {
		super(id, location);
	}

	/**
	 * Constructs a new @{Code CrazyArchaeologistNPC} object.
	 */
	public CrazyArchaeologistNPC() {
		super(-1, null);
	}
	
	@Override
	public void init() {
		super.init();
		configureBossData();
	}

	@Override
	public void sendImpact(BattleState state) {
		if (state.getEstimatedHit() > 24) {
			state.setEstimatedHit(24);
		}
		super.sendImpact(state);
	}

	@Override
	public void commenceDeath(Entity killer) {
		sendChat("Ow!");
		super.commenceDeath(killer);
	}

	@Override
	public void finalizeDeath(Entity killer) {
		super.finalizeDeath(killer);
		BossKillCounter.addtoKillcount((Player) killer, this.getId());
	}

	@Override
	public CombatSwingHandler getSwingHandler(boolean swing) {
		return COMBAT_HANDLER;
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new CrazyArchaeologistNPC(id, location);
	}

	@Override
	public int[] getIds() {
		return new int[] {997};
	}

	/**
	 * Handles the Crazy Archaeologist combat handler.
	 * @author Vexia
	 */
	public static class ArchaeologistHandler extends MultiSwingHandler {

		/**
		 * The switch attacks.
		 */
		private static final SwitchAttack[] ATTACKS = new SwitchAttack[] {new SwitchAttack(CombatStyle.MELEE), new SwitchAttack(CombatStyle.RANGE.getSwingHandler(), new Animation(1162))};
		
		/**
		 * The exposion locations.
		 */
		private final List<Location> locs = new ArrayList<>();

		/**
		 * If the special attack is used.
		 */
		private boolean special;

		/**
		 * Constructs a new @{Code ChaosCombatHandler} object.
		 */
		public ArchaeologistHandler() {
			super(ATTACKS);
		}

		@Override
		public int swing(Entity entity, Entity victim, BattleState state) {
			if (state.getStyle() == CombatStyle.RANGE && RandomFunction.random(10) == 1) {
				special = true;
			}
			return super.swing(entity, victim, state);
		}

		@Override
		public void impact(Entity entity, Entity victim, BattleState state) {
			if (special) {
				special = false;
				for (Location l : locs) {
					if (l == null) {
						continue;
					}
					for (Player p : RegionManager.getLocalPlayers(l, 3)) {
						p.getImpactHandler().manualHit(entity, RandomFunction.random(1, 24), HitsplatType.NORMAL);
					}
				}
				locs.clear();
				return;
			}
			super.impact(entity, victim, state);
		}

		@Override
		public void visualizeImpact(Entity entity, Entity victim, BattleState state) {
			if (state.getStyle() == CombatStyle.RANGE && special) {
				entity.sendChat("Rain of knowledge!");
				Location loc = null;
				for (int i = 0; i < 3; i++) {
					loc = getRandomLoc(entity);
					if (!RegionManager.isTeleportPermitted(loc)) {
						loc = getRandomLoc(entity);
					}
					if (locs.contains(loc)) {
						loc = getRandomLoc(entity); 
					}
					if (!RegionManager.isTeleportPermitted(loc)) {
						loc = getRandomLoc(entity);
					}
					if (!locs.contains(loc)) {
						locs.add(loc);
					}
				}
				for (Location l : locs) {
					if (l == null || entity == null) {
						continue;
					}
					Projectile p = Projectile.create(entity, null, 156, 40, 10, 30, 90);
					p.setEndLocation(l);
					p.send();
				}
				entity.animate(new Animation(1167));
			} else {
				if (state.getStyle() == CombatStyle.RANGE) {
					Projectile p = Projectile.ranged(entity, victim, 156, 40, 30, 0, 15);
					p.setSpeed(40);
					p.send();
				}
				entity.sendChat(RandomFunction.getRandomElement(QUOTES));
			}
			if (state.getStyle() == CombatStyle.MELEE) {
				super.visualizeImpact(entity, victim, state);
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
			return l.getLocation().transform((negative ? RandomFunction.random(-3, 3) : RandomFunction.random(0, 3)), (negative ? RandomFunction.random(-3, 3) : RandomFunction.random(3, 3)), 0);
		}
		
	}

}
