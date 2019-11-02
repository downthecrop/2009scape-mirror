package plugin.interaction.item.withobject;

import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.item.Item;
import org.crandor.game.node.object.GameObject;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin used to add the skull to the cofin.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class RestlessGhostSkull extends UseWithHandler {

	/**
	 * Constructs a new {@code RestlessGhostSkull} {@code Object}.
	 */
	public RestlessGhostSkull() {
		super(964);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(2145, OBJECT_TYPE, this);
		addHandler(15052, OBJECT_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final GameObject object = (GameObject) event.getUsedWith();
		if (object.getId() == 2145) {
			event.getPlayer().getDialogueInterpreter().sendDialogue("Maybe I should open it first.");
			return true;
		}
		if (event.getPlayer().getInventory().remove(new Item(964, 24))) {
			event.getPlayer().getPacketDispatch().sendMessage("You put the skull in the coffin.");
			event.getPlayer().getQuestRepository().getQuest("The Restless Ghost").finish(event.getPlayer());
		}
		return true;
	}

}
