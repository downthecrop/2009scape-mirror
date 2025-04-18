package content.region.misthalin.lumbridge.quest.therestlessghost;

import content.data.Quests;
import core.api.Container;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.scenery.Scenery;
import core.plugin.Initializable;
import core.plugin.Plugin;
import org.rs09.consts.Items;

import static core.api.ContentAPIKt.removeItem;

/**
 * Represents the plugin used to add the skull to the cofin.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
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
		addHandler(15061, OBJECT_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Scenery object = (Scenery) event.getUsedWith();
		if (object.getId() == 2145) {
			event.getPlayer().getDialogueInterpreter().sendDialogue("Maybe I should open it first.");
			return true;
		}
		if (removeItem(event.getPlayer(), Items.SKULL_964, Container.INVENTORY)) {
			event.getPlayer().getPacketDispatch().sendMessage("You put the skull in the coffin.");
			event.getPlayer().getQuestRepository().getQuest(Quests.THE_RESTLESS_GHOST).finish(event.getPlayer());
		}
		return true;
	}

}
