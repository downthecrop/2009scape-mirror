package core.game.interaction.object;

import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;

/**
 * Represents the plugin used for the drop party lever.
 * @author 'Vexia
 * @version 1.0
 */
public final class DropPartyLeverOptionPlugin extends OptionHandler {

	/**
	 * Represents the animation to use.
	 */
	private static final Animation ANIMATION = new Animation(6933);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(26194).getHandlers().put("option:pull", this);
		return this;
	}

	@Override
	public boolean handle(final Player player, Node node, String option) {
		final Scenery object = (Scenery) node;
		if (player.getAttribute("delay:lever", -1) > GameWorld.getTicks())
			return true;
		player.setAttribute("delay:picking", GameWorld.getTicks() + 3);
		player.lock(2);
		player.faceLocation(object.getLocation());
		player.getDialogueInterpreter().open(1 << 16 | 2);
		GameWorld.getPulser().submit(new Pulse(1, player) {
			@Override
			public boolean pulse() {
				player.animate(ANIMATION);
				return true;
			}
		});
		return true;
	}

}
