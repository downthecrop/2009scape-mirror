package core.game.interaction.item.withnpc;

import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the needle on a cow plugin.
 * @author 'Vexia
 * @date 18/11/2013
 */
@Initializable
public class NeedleCowPlugin extends UseWithHandler {

	/**
	 * Constructs a new {@code NeedleCowPlugin} {@code Object}.
	 */
	public NeedleCowPlugin() {
		super(1733);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(81, NPC_TYPE, this);
		addHandler(397, NPC_TYPE, this);
		addHandler(955, NPC_TYPE, this);
		addHandler(1691, NPC_TYPE, this);
		addHandler(1766, NPC_TYPE, this);
		addHandler(1767, NPC_TYPE, this);
		addHandler(1768, NPC_TYPE, this);
		addHandler(1886, NPC_TYPE, this);
		addHandler(1998, NPC_TYPE, this);
		addHandler(1999, NPC_TYPE, this);
		addHandler(2000, NPC_TYPE, this);
		addHandler(2310, NPC_TYPE, this);
		addHandler(3309, NPC_TYPE, this);
		addHandler(5210, NPC_TYPE, this);
		addHandler(5211, NPC_TYPE, this);
		addHandler(5603, NPC_TYPE, this);
		addHandler(7484, NPC_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		event.getPlayer().getPacketDispatch().sendMessage("The cow doesn't want that.");
		return true;
	}

}
