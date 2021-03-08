package core.game.interaction.item;

import core.cache.def.impl.ItemDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;
import core.plugin.Initializable;
import core.tools.RandomFunction;

/**
 * Handles the option for the toy horse.
 * @author Vexia
 */
@Initializable
public class ToyHorsePlugin extends OptionHandler {

	/**
	 * The force chat's you can say.
	 */
	private static final String CHATS[] = { "Come-on Dobbin, we can win the race!", "Hi-ho Silver, and away!", "Neaahhhyyy!" };

	@Override
	public boolean handle(Player player, Node node, String option) {
		player.lock(2);
		int id = ((Item) node).getId();
		int anim = id == 2524 ? 920 : id == 2526 ? 921 : id == 2522 ? 919 : 918;
		player.animate(new Animation(anim));
		player.sendChat(CHATS[RandomFunction.random(CHATS.length)]);
		return true;
	}

	@Override
	public boolean isWalk() {
		return false;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		int[] ids = new int[] { 2524, 2526, 2522 };
		for (int id : ids) {
			ItemDefinition.forId(id).getHandlers().put("play-with", this);
		}
		return this;
	}
}
