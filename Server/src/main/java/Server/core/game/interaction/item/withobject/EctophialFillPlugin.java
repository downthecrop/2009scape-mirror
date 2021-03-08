package core.game.interaction.item.withobject;

import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Fills an ectophial.
 * @author Vexia
 */
@Initializable
public class EctophialFillPlugin extends UseWithHandler {

	/**
	 * Constructs a new {@code EctophialFillPlugin} {@code Object}
	 */
	public EctophialFillPlugin() {
		super(4252);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(5282, OBJECT_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		player.lock(3);
		player.animate(Animation.create(1652));
		GameWorld.getPulser().submit(new Pulse(3, player) {
			@Override
			public boolean pulse() {
				if (player.getInventory().remove(new Item(4252))) {
					player.getInventory().add(new Item(4251));
				}
				player.sendMessage("You refill the ectophial.");
				return true;
			}
		});
		return true;
	}

}
