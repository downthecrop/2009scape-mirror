package core.game.node.entity.skill.agility.brimhaven;

import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.agility.AgilityHandler;
import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.system.task.LocationLogoutTask;
import core.game.system.task.LogoutTask;
import core.game.system.task.MovementHook;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;

/**
 * Handles the pressure pad trap.
 * @author Emperor
 */
public final class PressurePad implements MovementHook {

	@Override
	public boolean handle(Entity e, final Location dest) {
		final Direction dir = e.getDirection();
		final Player player = (Player) e;
		final Location start = dest.transform(-dir.getStepX(), -dir.getStepY(), 0);
		e.lock(5);
		e.addExtension(LogoutTask.class, new LocationLogoutTask(5, start));
		GameWorld.getPulser().submit(new Pulse(3, e) {
			@Override
			public boolean pulse() {
				Graphics.send(Graphics.create(271), dest);
				if (AgilityHandler.hasFailed(player, 20, 0.25)) {
					if (player.getSkills().getLevel(Skills.AGILITY) < 20) {
						player.getPacketDispatch().sendMessage("You need an agility of at least 20 to get past this trap!");
					}
					int hit = player.getSkills().getLifepoints() / 12;
					if (hit < 2) {
						hit = 2;
					}
					AgilityHandler.failWalk(player, 1, player.getLocation(), start, start, Animation.create(1114), 10, hit, "You were hit by some falling rocks!").setDirection(dir);
					;
				} else {
					AgilityHandler.forceWalk(player, -1, dest, dest.transform(dir.getStepX() << 1, dir.getStepY() << 1, 0), Animation.create(1115), 20, 26, null);
				}
				return true;
			}
		});
		return false;
	}

}