package core.game.node.entity.npc.agg;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.DeathTask;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.world.map.zone.impl.WildernessZone;
import core.game.world.map.RegionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles an NPC's aggressive behaviour.
 * @author Emperor
 */
public class AggressiveBehavior {

	/**
	 * The default aggressive behavior.
	 */
	public static final AggressiveBehavior DEFAULT = new AggressiveBehavior();

	/**
	 * The wilderness aggressive behavior.
	 */
	public static final AggressiveBehavior WILDERNESS = new AggressiveBehavior() {

		@Override
		public boolean canSelectTarget(Entity entity, Entity target) {
			if(target.isInvisible()){
				return false;
			}
			if (!target.isActive() || DeathTask.isDead(target)) {
				return false;
			}
			if (!entity.canSelectTarget(target)) {
				return false;
			}
			if (!target.getProperties().isMultiZone() && target.inCombat()) {
				return false;
			}
			if (target instanceof Player) {
				return ((Player) target).getSkullManager().isWilderness();
			}
			return true;
		}
	};

	/**
	 * Constructs a new {@code AggressiveBehaviour} {@code Object}.
	 */
	public AggressiveBehavior() {
		/*
		 * empty.
		 */
	}

	/**
	 * Checks if the NPC is aggressive towards the entity.
	 * @param entity The timed entry.
	 * @return {@code True} if the NPC can select the entity as a target.
	 */
	public boolean canSelectTarget(Entity entity, Entity target) {
		int regionId = target.getLocation().getRegionId();
		if (!target.isActive() || DeathTask.isDead(target)) {
			return false;
		}
		if (!target.getProperties().isMultiZone() && target.inCombat()) {
			return false;
		}
		if (entity instanceof NPC && target instanceof Player) {
			NPC npc = (NPC) entity;
			if (npc.getAggressiveHandler() != null && npc.getAggressiveHandler().isAllowTolerance() && !WildernessZone.isInZone(npc)) {
				if (RegionManager.forId(regionId).isTolerated(target.asPlayer())) {
					return false;
				}
			}
		}
		int level = target.getProperties().getCurrentCombatLevel();
		if (level > entity.getProperties().getCurrentCombatLevel() << 1 && !ignoreCombatLevelDifference()) {
			return false;
		}
		return true;
	}

	public boolean ignoreCombatLevelDifference() {
		return false;
	}

	/**
	 * Gets the priority flag.
	 * @param target The target.
	 * @return The priority flag.
	 */
	public int getPriorityFlag(Entity target) {
		int flag = 0;
		if (target.inCombat()) {
			flag++;
		}
		if (target.getLocks().isInteractionLocked()) {
			flag++;
		}
		Entity e = target.getAttribute("aggressor");
		if (e != null && e.getProperties().getCombatPulse().getVictim() == target) {
			flag++;
		}
		return flag;
	}

	/**
	 * Gets the list of possible targets.
	 * @param entity The entity.
	 * @param radius The aggressive radius.
	 * @return The list of possible targets.
	 */
	public List<Entity> getPossibleTargets(Entity entity, int radius) {
		List<Entity> targets = new ArrayList<>(20);
		for (Player player : RegionManager.getLocalPlayers(entity, radius)) {
			if (canSelectTarget(entity, player)) {
				targets.add(player);
			}
		}
		return targets;
	}

	/**
	 * Gets the most logical target from the targets list.
	 * @param entity The entity.
	 * @param possibleTargets The possible targets list.
	 * @return The target.
	 */
	public Entity getLogicalTarget(Entity entity, List<Entity> possibleTargets) {
		Entity target = null;
		double comparingFlag = Double.MAX_VALUE;
		for (Entity e : possibleTargets) {
			double flag = (double)getPriorityFlag(e) + Math.random();
			if (flag <= comparingFlag) {
				comparingFlag = flag;
				target = e;
			}
		}
		return target;
	}
}
