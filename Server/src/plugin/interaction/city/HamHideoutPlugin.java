package plugin.interaction.city;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.content.global.action.ClimbActionHandler;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.diary.DiaryType;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.system.task.Pulse;
import org.crandor.game.world.GameWorld;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Represents the ham hide out node interaction plugin.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class HamHideoutPlugin extends OptionHandler {

	/**
	 * Represents the animation to use.
	 */
	private static final Animation ANIMATION = new Animation(827);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(5490).getConfigurations().put("option:open", this);
		ObjectDefinition.forId(5490).getConfigurations().put("option:pick-lock", this);
		ObjectDefinition.forId(5491).getConfigurations().put("option:close", this);
		ObjectDefinition.forId(5491).getConfigurations().put("option:climb-down", this);
		ObjectDefinition.forId(5493).getConfigurations().put("option:climb-up", this);
		return this;
	}

	@Override
	public boolean handle(final Player player, Node node, String option) {
		final int id = ((GameObject) node).getId();
		switch (id) {
		case 5493:
			if (player.getLocation().withinDistance(Location.create(3149, 9652, 0))) {
				ClimbActionHandler.climb(player, new Animation(828), new Location(3165, 3251, 0));
				return true;
			}
			ClimbActionHandler.climbLadder(player, (GameObject) node, option);
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
					GameWorld.submit(new Pulse(2, player) {
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
					if (!player.getAchievementDiaryManager().getDiary(DiaryType.LUMBRIDGE).isComplete(0, 9)) {
						player.getAchievementDiaryManager().getDiary(DiaryType.LUMBRIDGE).updateTask(player, 0, 9, true);
					}
					player.getProperties().setTeleportLocation(Location.create(3149, 9652, 0));
					break;
				}
				break;
			case "pick-lock":
				player.lock(3);
				player.animate(ANIMATION);
				player.getPacketDispatch().sendMessage("You attempt to pick the lock on the trap door.");
				GameWorld.submit(new Pulse(2, player) {
					@Override
					public boolean pulse() {
						player.animate(ANIMATION);
						player.getPacketDispatch().sendMessage("You attempt to pick the lock on the trap door.");
						boolean success = RandomFunction.random(3) == 1;
						player.getPacketDispatch().sendMessage(success ? ("You pick the lock on the trap door.") : "You fail to pick the lock - your fingers get numb from fumbling with the lock.");
						player.unlock();
						if (success) {
							player.getConfigManager().set(174, 1 << 14);
							GameWorld.submit(new Pulse(40, player) {
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
