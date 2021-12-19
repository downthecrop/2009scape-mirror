package core.game.content.zone.phasmatys;

import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.system.task.Pulse;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;

/**
 * Handles the ectoplasm filling.
 * @author Vexia
 */
public class EctoplasmFillPlugin extends UseWithHandler {

	/**
	 * The object ids of the slime.
	 */
	private static final int[] OBJECTS = new int[] { 5461, 17116, 17117, 17118, 17119 };

	/**
	 * Constructs a new {@code EctoplasmFillPlugin} {@code Object}.
	 */
	public EctoplasmFillPlugin() {
		super(1925);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (int id : OBJECTS) {
			addHandler(id, OBJECT_TYPE, this);
		}
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		player.animate(Animation.create(4471));
        player.getAudioManager().send(1132);
		player.getPacketDispatch().sendMessage("You fill the bucket with ectoplasm.");
		player.getPulseManager().run(new Pulse(3, player) {
			@Override
			public boolean pulse() {
				if (player.getInventory().remove(new Item(1925, 1))) {
					player.getInventory().add(new Item(4286));
					if (player.getInventory().contains(1925, 1)) {
						player.animate(Animation.create(4471), 1);
                        player.getAudioManager().send(1132);
						return false;
					}
				}
				return !player.getInventory().contains(1925, 1);
			}
		});
		return true;
	}

}
