package core.game.interaction.city;

import core.cache.def.impl.SceneryDefinition;
import core.game.content.global.action.ClimbActionHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;
import core.plugin.Initializable;
import core.tools.RandomFunction;

/**
 * Represents the ham hide out node interaction plugin.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class HamHideoutPlugin extends OptionHandler {

	/**
	 * Represents the animation to use.
	 */
	private static final Animation ANIMATION = new Animation(827);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(5490).getHandlers().put("option:open", this);
		SceneryDefinition.forId(5490).getHandlers().put("option:pick-lock", this);
		SceneryDefinition.forId(5491).getHandlers().put("option:close", this);
		SceneryDefinition.forId(5491).getHandlers().put("option:climb-down", this);
		SceneryDefinition.forId(5493).getHandlers().put("option:climb-up", this);
		return this;
	}

	@Override
	public boolean handle(final Player player, Node node, String option) {
		final int id = ((Scenery) node).getId();
		switch (id) {
		case 5493:
			if (player.getLocation().withinDistance(Location.create(3149, 9652, 0))) {
				ClimbActionHandler.climb(player, new Animation(828), new Location(3165, 3251, 0));
				return true;
			}
			ClimbActionHandler.climbLadder(player, (Scenery) node, option);
			return true;
		case 5490:
		case 5491:
			switch (option) {
			case "open":
				if (player.getConfigManager().get(174) == 0) {
					player.getPacketDispatch().sendMessage("This trapdoor seems totally locked.");
				} else {
					player.getConfigManager().set(346, 272731282);
					ClimbActionHandler.climb(player, new Animation(827), new Location(3149, 9652, 0));
					GameWorld.getPulser().submit(new Pulse(2, player) {
						@Override
						public boolean pulse() {
							player.getConfigManager().set(174, 0);
							return true;
						}
					});
				}
				break;
			case "close":
				player.getConfigManager().set(174, 0);
				break;
			case "climb-down":
				switch (id) {
				case 5491:
					player.getProperties().setTeleportLocation(Location.create(3149, 9652, 0));
					break;
				}
				break;
			case "pick-lock":
				player.lock(3);
				player.animate(ANIMATION);
				player.getPacketDispatch().sendMessage("You attempt to pick the lock on the trap door.");
				GameWorld.getPulser().submit(new Pulse(2, player) {
					@Override
					public boolean pulse() {
						player.animate(ANIMATION);
						player.getPacketDispatch().sendMessage("You attempt to pick the lock on the trap door.");
						boolean success = RandomFunction.random(3) == 1;
						player.getPacketDispatch().sendMessage(success ? ("You pick the lock on the trap door.") : "You fail to pick the lock - your fingers get numb from fumbling with the lock.");
						player.unlock();
						if (success) {
							player.getConfigManager().set(174, 1 << 14);
							GameWorld.getPulser().submit(new Pulse(40, player) {
								@Override
								public boolean pulse() {
									player.getConfigManager().set(174, 0);
									return true;
								}
							});
						}
						return true;
					}
				});
				break;
			}
			break;
		}
		return true;
	}

}
