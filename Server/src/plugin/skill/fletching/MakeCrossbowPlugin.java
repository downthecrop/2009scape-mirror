package plugin.skill.fletching;

import org.crandor.game.content.skill.member.fletching.items.crossbow.Limb;
import org.crandor.game.content.skill.member.fletching.items.crossbow.StringCross;
import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin used to make a crossbow.
 * @author 'Vexia
 */
@InitializablePlugin
public class MakeCrossbowPlugin extends UseWithHandler {

	/**
	 * Constructs a new {@code StringcrossbowPlugin} {@code Object}.
	 */
	public MakeCrossbowPlugin() {
		super(9420, 9423, 9422, 9425, 9427, 9429, 9431, /** crossbow string */
		9438);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (Limb l : Limb.values()) {
			addHandler(l.getStock().getId(), ITEM_TYPE, this);
		}
		for (StringCross c : StringCross.values()) {
			addHandler(c.getItem().getId(), ITEM_TYPE, this);
		}
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		if (event.getUsedItem().getName().toLowerCase().equals("string") || event.getBaseItem().getName().toLowerCase().contains("string")) {
			player.getDialogueInterpreter().open(92747, event.getUsedItem().getId() == 9438 ? event.getBaseItem() : event.getUsedItem());
			return true;
		}
		final Limb limb = Limb.forItems(event.getUsedItem(), event.getBaseItem());
		if (limb == null) {
			player.getDialogueInterpreter().sendDialogue("That's not the correct limb to attach.");
			return true;
		}
		player.getDialogueInterpreter().open(84923, event.getUsedItem(), event.getBaseItem());
		return true;
	}

}
