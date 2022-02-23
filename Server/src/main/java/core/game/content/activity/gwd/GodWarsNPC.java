package core.game.content.activity.gwd;

import java.util.ArrayList;
import java.util.List;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.DeathTask;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.npc.agg.AggressiveBehavior;
import core.game.node.entity.npc.agg.AggressiveHandler;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.game.world.map.RegionManager;

/**
 * Handles a god wars NPC.
 * @author Emperor
 */
@Initializable
public final class GodWarsNPC extends AbstractNPC {

	/**
	 * The aggressive behavior.
	 */
	private static final AggressiveBehavior AGGRO_BEHAVIOR = new AggressiveBehavior() {

		@Override
		public boolean canSelectTarget(Entity entity, Entity target) {
			if (!target.isActive() || DeathTask.isDead(target) || DeathTask.isDead(entity)) {
				return false;
			}
			if (!target.getProperties().isMultiZone() && target.inCombat()) {
				return false;
			}
			if (target instanceof GodWarsNPC) {
				if (((GodWarsNPC) target).faction != ((GodWarsNPC) entity).faction) {
					return true;
				}
			} else if (target instanceof Player) {
				if (!((GodWarsNPC) entity).faction.isProtected((Player) target)) {
					return true;
				}
			}
			return false;
		}

		@Override
		public List<Entity> getPossibleTargets(Entity entity, int radius) {
			List<Entity> targets = new ArrayList<>(20);
			for (Player player : RegionManager.getLocalPlayers(entity, radius)) {
				if (canSelectTarget(entity, player)) {
					targets.add(player);
				}
			}
			if (!targets.isEmpty()) {
				return targets;
			}
			for (NPC npc : RegionManager.getLocalNpcs(entity, radius)) {
				if (canSelectTarget(entity, npc)) {
					targets.add(npc);
				}
			}
			return targets;
		}
	};

	/**
	 * The god wars faction (0=armadyl, 1=bandos, 2=saradomin, 3=zamorak).
	 */
	private GodWarsFaction faction;

	/**
	 * Constructs a new {@code GodWarsNPC} {@code Object}.
	 */
	public GodWarsNPC() {
		super(-1, null);
	}

	/**
	 * Constructs a new {@code GodWarsNPC} {@code Object}.
	 * @param id The NPC id.
	 * @param location The location.
	 */
	public GodWarsNPC(int id, Location location) {
		super(id, location);
	}

	@Override
	public void init() {
		super.init();
		setWalks(true);
		faction = GodWarsFaction.forId(getId());
	}

	@Override
	public void setDefaultBehavior() {
		setAggressive(true);
		aggressiveHandler = new AggressiveHandler(this, AGGRO_BEHAVIOR);
	}

	@Override
	public boolean isAttackable(Entity entity, CombatStyle style, boolean message) {
		if (style == CombatStyle.MELEE && faction == GodWarsFaction.ARMADYL && entity instanceof Player) {
            if(message) {
                ((Player) entity).getPacketDispatch().sendMessage("The aviansie is flying too high for you to attack using melee.");
            }
			return false;
		}
		return super.isAttackable(entity, style, message);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new GodWarsNPC(id, location);
	}

	@Override
	public int[] getIds() {
		return new int[] { 6210, 6211, 6212, 6213, 6214, 6215, 6216, 6217, 6218, 6219, 6220, 6221, 6229, 6230, 6231, 6232, 6233, 6234, 6235, 6236, 6237, 6238, 6239, 6240, 6241, 6242, 6243, 6244, 6245, 6246, 6254, 6255, 6256, 6257, 6258, 6259, 6267, 6268, 6269, 6270, 6271, 6272, 6273, 6274, 6275, 6276, 6277, 6278, 6279, 6280, 6281, 6282, 6283 };
	}

}
