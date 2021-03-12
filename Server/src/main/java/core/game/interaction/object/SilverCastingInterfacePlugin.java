package core.game.interaction.object;

import core.game.component.Component;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * @author 'Vexia
 */
@Initializable
public class SilverCastingInterfacePlugin extends UseWithHandler {

	public SilverCastingInterfacePlugin() {
		super(2355);
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		event.getPlayer().getInterfaceManager().open(new Component(438));
		player.getPacketDispatch().sendItemOnInterface(1718, 1, 438, 17);
		player.getPacketDispatch().sendItemOnInterface(1724, 1, 438, 24);
		player.getPacketDispatch().sendItemOnInterface(2961, 1, 438, 31);
		player.getPacketDispatch().sendItemOnInterface(4201, 1, 438, 38);
		player.getPacketDispatch().sendItemOnInterface(5525, 1, 438, 45);
		player.getPacketDispatch().sendItemOnInterface(7637, 1, 438, 53);
		player.getPacketDispatch().sendItemOnInterface(6748, 1, 438, 60);
		player.getPacketDispatch().sendItemOnInterface(9382, 10, 438, 67);
		player.getPacketDispatch().sendItemOnInterface(13154, 10, 438, 74);
		return false;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(2966, OBJECT_TYPE, this);
		addHandler(3044, OBJECT_TYPE, this);
		addHandler(3294, OBJECT_TYPE, this);
		addHandler(4304, OBJECT_TYPE, this);
		addHandler(6189, OBJECT_TYPE, this);
		addHandler(11009, OBJECT_TYPE, this);
		addHandler(11010, OBJECT_TYPE, this);
		addHandler(11666, OBJECT_TYPE, this);
		addHandler(12100, OBJECT_TYPE, this);
		addHandler(12809, OBJECT_TYPE, this);
		addHandler(18497, OBJECT_TYPE, this);
		addHandler(18525, OBJECT_TYPE, this);
		addHandler(18526, OBJECT_TYPE, this);
		addHandler(21879, OBJECT_TYPE, this);
		addHandler(22721, OBJECT_TYPE, this);
		addHandler(26814, OBJECT_TYPE, this);
		addHandler(28433, OBJECT_TYPE, this);
		addHandler(28434, OBJECT_TYPE, this);
		addHandler(30021, OBJECT_TYPE, this);
		addHandler(30510, OBJECT_TYPE, this);
		addHandler(36956, OBJECT_TYPE, this);
		addHandler(37651, OBJECT_TYPE, this);

		return null;
	}

}
