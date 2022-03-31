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
 * Handles floor spikes trap.
 * @author Emperor
 */
public final class FloorSpikes implements MovementHook {

	@Override
	public boolean handle(Entity e, final Location l) {
		final Direction dir = e.getDirection();
		final Player player = (Player) e;
		final Location start = l.transform(-dir.getStepX(), -dir.getStepY(), 0);
		e.lock(5);
		if(e.isPlayer())
		{
			((Player) e).logoutListeners.put("floor-spikes", p -> {
				p.setLocation(start);
				return Unit.INSTANCE;
			});
		}
		GameWorld.getPulser().submit(new Pulse(3, e) {
			@Override
			public boolean pulse() {
				player.getPacketDispatch().sendSceneryAnimation(RegionManager.getObject(l), Animation.create(1111));
				if (AgilityHandler.hasFailed(player, 20, 0.25)) {
					if (player.getSkills().getLevel(Skills.AGILITY) < 20) {
						player.getPacketDispatch().sendMessage("You need an agility of at least 20 to get past this trap!");
					}
					int hit = player.getSkills().getLifepoints() / 12;
					if (hit < 2) {
						hit = 2;
					}
					AgilityHandler.failWalk(player, 1, player.getLocation(), start, start, Animation.create(1114), 10, hit, "You were hit by some floor spikes!").setDirection(dir);
					;
				} else {
					AgilityHandler.forceWalk(player, -1, l, l.transform(dir.getStepX() << 1, dir.getStepY() << 1, 0), Animation.create(1115), 20, 26, null);
				}
				player.logoutListeners.remove("floor-spikes");
				return true;
			}
		});
		return false;
	}

}