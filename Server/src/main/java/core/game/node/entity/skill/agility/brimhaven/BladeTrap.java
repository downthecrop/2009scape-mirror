package core.game.node.entity.skill.agility.brimhaven;

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
 * Handles the blade trap.
 * @author Emperor
 */
public final class BladeTrap implements MovementHook {

	@Override
	public boolean handle(Entity e, final Location l) {
		if (BrimhavenArena.sawBladeActive) {
			final Direction dir = e.getDirection();
			final Player player = (Player) e;
			final Location start = l.transform(-dir.getStepX(), -dir.getStepY(), 0);
			e.lock(5);
			if(e.isPlayer())
			{
				e.asPlayer().logoutListeners.put("blade-trap", p -> {
					p.setLocation(start);
					return Unit.INSTANCE;
				});
			}
			GameWorld.getPulser().submit(new Pulse(2, e) {
				@Override
				public boolean pulse() {
					Direction direction = dir;
					Direction d = Direction.get(direction.toInteger() + 2 & 3);
					if (RegionManager.getObject(player.getLocation().transform(dir)) != null) {
						Direction s = d;
						d = direction;
						direction = s;
					}
					Location loc = player.getLocation();
					AgilityHandler.failWalk(player, 1, loc, loc.transform(direction), loc.transform(direction), Animation.create(846), 10, 3, "You were hit by the saw blade!").setDirection(d);
					player.logoutListeners.remove("blade-trap");
					return true;
				}
			});
			return false;
		}
		return true;
	}

}