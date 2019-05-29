package plugin.interaction.item.withitem;

import org.crandor.game.content.global.LightSource;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.map.zone.impl.DarkZone;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin used to ignite a light source.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public class LightSourcePlugin extends UseWithHandler {

	/**
	 * Constructs a new {@code LightSourcePlugin.java} {@code Object}.
	 */
	public LightSourcePlugin() {
		super(LightSource.getRawIds());
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(590, ITEM_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		Item item = (Item) event.getUsedWith();
		int box = event.getUsedItem().getId();
		int light = item.getId();
		if (box != 590) {
			box = 590;
		}
		if (light == 590) {
			light = event.getUsedItem().getId();
		}
		final LightSource source = LightSource.forId(light);
		if (source == null) {
			System.err.println("[Light Source Plugin] error, source " + light + " unhandled.");
			return true;
		}
		if (source.getLevel() > player.getSkills().getLevel(Skills.FIREMAKING)) {
			player.getPacketDispatch().sendMessage("You need a firemaking level of at least " + source.getLevel() + " to light this.");
			return true;
		}
		player.getInventory().replace(source.getProduct(), (event.getUsedItem().getId() == 590 ? ((Item) event.getUsedWith()).getSlot() : event.getUsedItem().getSlot()));
		DarkZone.checkDarkArea(player);
		return true;
	}
}
