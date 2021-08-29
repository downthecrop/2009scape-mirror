package core.game.interaction.item.withobject;

import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.scenery.Scenery;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles using a rope on the God wars entrance.
 * @author Emperor
 */
@Initializable
public final class GWDEntranceRopeUsage extends UseWithHandler {

	/**
	 * Constructs a new {@code GWDEntranceRopeUsage} {@code Object}.
	 */
	public GWDEntranceRopeUsage() {
		super(954);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(26340, OBJECT_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		Scenery object = (Scenery) event.getUsedWith();
		return object.getInteraction().get(0).getHandler().handle(event.getPlayer(), object, "tie-rope");
	}

}
