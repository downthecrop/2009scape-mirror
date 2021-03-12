package core.game.node.entity.skill.crafting;

import core.game.node.entity.skill.crafting.limestone.ChiselLimestonePulse;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * The plugin that starts the chisel limestone cutting pulse.
 * @author Jamix77
 */
@Initializable
public final class ChiselLimestonePlugin extends UseWithHandler {

	/**
	 * Constructs a new {@code GemCutPlugin} {@Code Object}
	 */
	public ChiselLimestonePlugin() {
		super(3211);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(1755, ITEM_TYPE, this);
		return null;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		player.getPulseManager().run(new ChiselLimestonePulse(player, new Item(3211)));
		return true;
	}

}
