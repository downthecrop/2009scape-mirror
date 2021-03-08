package core.game.interaction.item;

import core.cache.def.impl.ItemDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.system.task.Pulse;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the diano item plugin.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class DiangoItemPlugin extends OptionHandler {

	/**
	 * Represents the animations.
	 */
	private static final Animation[] ANIMATIONS = new Animation[] { new Animation(8990), new Animation(1902), new Animation(1904) };

	@Override
	public boolean handle(final Player player, Node node, String option) {
		switch (option) {
		case "fly":
			player.animate(ANIMATIONS[0]);
			break;
		case "spin":
			player.animate(ANIMATIONS[1]);
			player.getPulseManager().run(new Pulse(1) {
				int count = 0;

				@Override
				public boolean pulse() {
					if (count++ >= 9) {
						player.animate(ANIMATIONS[2]);
						return true;
					}
					return false;
				}

				@Override
				public void stop() {
					super.stop();
					player.animate(ANIMATIONS[2]);
				}

			});
			break;
		}
		return true;
	}

	@Override
	public boolean isWalk() {
		return false;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.setOptionHandler("fly", this);
		ItemDefinition.setOptionHandler("spin", this);
		return this;
	}

}
