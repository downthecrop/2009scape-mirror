package plugin.skill.fletching;

import org.crandor.game.content.skill.member.fletching.items.bow.StringBow;
import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin used to string a bow.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class StringbowPlugin extends UseWithHandler {

	/**
	 * Constructs a new {@code StringbowPlugin} {@code Object}.
	 */
	public StringbowPlugin() {
		super(1777);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (StringBow bw : StringBow.values()) {
			addHandler(bw.getItem().getId(), ITEM_TYPE, this);
		}
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		player.getDialogueInterpreter().open(9174399, event.getUsedItem().getId() == 1777 ? event.getBaseItem() : event.getUsedItem());
		return true;
	}
}
