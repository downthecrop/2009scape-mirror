package core.game.content.activity.pyramidplunder;

import static api.ContentAPIKt.*;
import core.cache.def.impl.NPCDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.game.content.global.action.ClimbActionHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.system.task.Pulse;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;
import core.tools.RandomFunction;

/**
 * Handles the pyramid plunder options.
 * @author Emperor
 */
public final class PyramidOptionHandler extends OptionHandler {

	/**
	 * The guardian room location.
	 */
	private static final Location GUARDIAN_ROOM = Location.create(1968, 4420, 2);

	/**
	 * The empty room location.
	 */
	private static final Location EMPTY_ROOM = Location.create(1934, 4450, 2);

	/**
	 * The current entrance.
	 */
	private static int currentEntrance;

	/**
	 * The last entrance switch made.
	 */
	private static long lastEntranceSwitch;

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(16484).getHandlers().put("option:search", this);
		SceneryDefinition.forId(16485).getHandlers().put("option:search", this);
		SceneryDefinition.forId(16487).getHandlers().put("option:search", this);
		SceneryDefinition.forId(16488).getHandlers().put("option:search", this);
		SceneryDefinition.forId(16490).getHandlers().put("option:search", this);
		SceneryDefinition.forId(16491).getHandlers().put("option:search", this);
		SceneryDefinition.forId(16493).getHandlers().put("option:search", this);
		SceneryDefinition.forId(16494).getHandlers().put("option:enter", this);
		SceneryDefinition.forId(16458).getHandlers().put("option:leave tomb", this);
		SceneryDefinition.forId(16459).getHandlers().put("option:leave tomb", this);
		NPCDefinition.forId(4476).getHandlers().put("option:start-minigame", this);
		NPCDefinition.forId(4476).getHandlers().put("option:talk-to",this);
		return null;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		if (node instanceof Scenery) {
			Location destination = EMPTY_ROOM;
			Scenery object = (Scenery) node;
			boolean willBePushed = (RandomFunction.random(10) > 3);
			if (object.getId() == 16458 || object.getId() == 16459) {
				ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_UP, Location.create(3288, 2801, 0));
				player.getPlunderObjectManager().resetObjectsFor(player);
				return true;
			}
			if (System.currentTimeMillis() - lastEntranceSwitch > 15 * 60 * 1000) {
				currentEntrance = RandomFunction.random(4);
				lastEntranceSwitch = System.currentTimeMillis();
			}
			int entrance = (object.getId() - 16483) / 3;
			int value = 0;
			if (entrance == currentEntrance) {
				destination = GUARDIAN_ROOM;
				value = 4;
			}
			player.getConfigManager().set(704, value);
			player.getPacketDispatch().sendMessage("You use your thieving skills to search the stone panel.");
			if(entrance == currentEntrance && willBePushed){
				player.lock();
				player.animate(new Animation(7299));
				submitWorldPulse(new Pulse(4, player){
					@Override
					public boolean pulse() {
						player.unlock();
						return true;
					}
				});
			} else {
				ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_UP, destination, "You find a door! You open it.");
			}
			//ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_UP, destination, "You find a door! You open it.");
		} else if (node instanceof NPC) {
			if (option.equals("talk-to")) {
				player.getDialogueInterpreter().open(node.getId(), node, 0);
			} else {
				player.getDialogueInterpreter().open(node.getId(), node, 1);
			}
		}
		return true;
	}

}