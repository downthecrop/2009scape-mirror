package core.game.interaction.object;

import core.cache.def.impl.SceneryDefinition;
import core.plugin.Initializable;
import core.game.content.dialogue.FacialExpression;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;
import core.tools.RandomFunction;

/**
 * Represents the haystack plugin.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class HaystackPlugin extends OptionHandler {

	/**
	 * Represents the needle to give.
	 */
	private static final Item NEEDLE = new Item(1733);

	/**
	 * Represents the animation.
	 */
	private static final Animation ANIMATION = new Animation(827);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(298).getHandlers().put("option:search", this);
		SceneryDefinition.forId(299).getHandlers().put("option:search", this);
		SceneryDefinition.forId(300).getHandlers().put("option:search", this);
		SceneryDefinition.forId(304).getHandlers().put("option:search", this);
		SceneryDefinition.forId(36892).getHandlers().put("option:search", this);
		SceneryDefinition.forId(36893).getHandlers().put("option:search", this);
		SceneryDefinition.forId(36894).getHandlers().put("option:search", this);
		SceneryDefinition.forId(36895).getHandlers().put("option:search", this);
		SceneryDefinition.forId(36896).getHandlers().put("option:search", this);
		SceneryDefinition.forId(36897).getHandlers().put("option:search", this);
		SceneryDefinition.forId(36898).getHandlers().put("option:search", this);
		SceneryDefinition.forId(36899).getHandlers().put("option:search", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		final Scenery object = ((Scenery) node);
		final int rand = RandomFunction.random(50);
		player.lock(2);
		player.animate(ANIMATION);
		player.getPacketDispatch().sendMessage("You search the " + object.getName().toLowerCase() + "...");
		if (rand == 1 && player.getInventory().freeSlots() > 0 || player.getInventory().containsItem(NEEDLE)) {
			player.getInventory().add(NEEDLE);
			player.getDialogueInterpreter().sendDialogues(player, FacialExpression.HALF_GUILTY, "Wow! A needle!", "Now what are the chances of finding that?");
			return true;
		}
		player.getPacketDispatch().sendMessage("You find nothing of interest.");
		return true;
	}

}
