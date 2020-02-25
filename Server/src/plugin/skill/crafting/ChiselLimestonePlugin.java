package plugin.skill.crafting;

import org.crandor.game.content.skill.free.crafting.limestone.ChiselLimestonePulse;
import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * The plugin that starts the chisel limestone cutting pulse.
 * @author Jamix77
 */
@InitializablePlugin
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
