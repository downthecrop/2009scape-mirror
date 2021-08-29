package core.game.content.dialogue;

import core.cache.def.impl.ItemDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.player.FaceLocationFlag;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;

/**
 * Represents the plugin used for mithril seeds.
 * @author 'Vexia
 * @version 1.2
 */
@Initializable
public final class MithrilSeedsPlugin extends OptionHandler {

	/**
	 * Represents the item to use.
	 */
	private static final Item ITEM = new Item(299, 1);

	/**
	 * Represents the common flower ids.
	 */
	private static final int FLOWERS[] = { 2980, 2981, 2982, 2983, 2984, 2985, 2986 };

	/**
	 * Represents the rare flower ids.
	 */
	private static final int RARE[] = new int[] { 2987, 2988 };

	/**
	 * Represents the animation.
	 */
	private static final Animation ANIMATION = new Animation(827);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.forId(299).getHandlers().put("option:plant", this);
		return this;
	}

	@Override
	public boolean handle(final Player player, Node node, String option) {
		if (player.getAttribute("delay:plant", -1) > GameWorld.getTicks()) {
			return true;
		}
		if (RegionManager.getObject(player.getLocation()) != null) {
			player.getPacketDispatch().sendMessage("You can't plant a flower here.");
			return true;
		}
		player.animate(ANIMATION);
		player.getInventory().remove(ITEM);
		final Scenery object = SceneryBuilder.add(new Scenery(getFlower(RandomFunction.random(100) == 1 ? RARE : FLOWERS), player.getLocation()), 100);
		player.moveStep();
		player.lock(3);
		player.getPulseManager().run(new Pulse(1, player) {
			@Override
			public boolean pulse() {
				player.faceLocation(FaceLocationFlag.getFaceLocation(player, object));
				player.getDialogueInterpreter().open(1 << 16 | 1, object);
				return true;
			}
		});
		player.setAttribute("delay:plant", GameWorld.getTicks() + 3);
		player.getPacketDispatch().sendMessage("You open the small mithril case and drop a seed by your feet.");
		return true;
	}

	@Override
	public boolean isWalk() {
		return false;
	}

	/**
	 * Gets a random flower from an array.
	 * @return the flower.
	 */
	public static int getFlower(int[] array) {
		return array[RandomFunction.random(array.length)];
	}

}
