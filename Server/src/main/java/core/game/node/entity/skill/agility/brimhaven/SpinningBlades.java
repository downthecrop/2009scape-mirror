package core.game.node.entity.skill.agility.brimhaven;

import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.agility.AgilityHandler;
import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.system.task.MovementHook;
import core.game.system.task.Pulse;
import kotlin.Unit;
import rs09.game.world.GameWorld;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;

/**
 * Handles the spinning blades trap.
 * @author Emperor
 */
public final class SpinningBlades implements MovementHook {

	@Override
	public boolean handle(Entity e, final Location l) {
		final Direction dir = e.getDirection();
		final Player player = (Player) e;
		final Location start = l.transform(-dir.getStepX(), -dir.getStepY(), 0);
		e.lock(5);
		if(e.isPlayer())
		{
			((Player) e).logoutListeners.put("spin-blades", p -> {
				p.setLocation(start);
				return Unit.INSTANCE;
			});
		}
		GameWorld.getPulser().submit(new Pulse(3, e) {
			@Override
			public boolean pulse() {
				sendObjectAnimation(player, l);
				if (AgilityHandler.hasFailed(player, 40, 0.15)) {
					if (player.getSkills().getLevel(Skills.AGILITY) < 40) {
						player.getPacketDispatch().sendMessage("You need an agility of at least 40 to get past this trap!");
					}
					int hit = player.getSkills().getLifepoints() / 12;
					if (hit < 2) {
						hit = 2;
					}
					AgilityHandler.failWalk(player, 1, player.getLocation(), start, start, Animation.create(1114), 10, hit, "You were hit by the spinning blades.").setDirection(dir);
				} else {
					AgilityHandler.forceWalk(player, -1, l, l.transform(dir.getStepX() << 1, dir.getStepY() << 1, 0), Animation.create(1115), 20, 26, null);
				}
				player.logoutListeners.remove("spin-blades");
				return true;
			}
		});
		return false;
	}

	/**
	 * Sends the object animation for the spinning blades.
	 * @param player The player.
	 * @param l The location.
	 */
	private static void sendObjectAnimation(Player player, Location l) {
		if (l.equals(Location.create(2778, 9579, 3))) {
			l = Location.create(2777, 9580, 3);
		} else if (l.equals(Location.create(2783, 9574, 3))) {
			l = Location.create(2782, 9573, 3);
		} else if (l.equals(Location.create(2778, 9557, 3))) {
			l = Location.create(2777, 9556, 3);
		}
		player.getPacketDispatch().sendSceneryAnimation(RegionManager.getObject(l), Animation.create(1107));
	}
}