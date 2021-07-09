package core.game.interaction.object;

import core.cache.def.impl.SceneryDefinition;
import core.game.component.Component;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles the spining interface open.
 * @author Adam
 */
@Initializable
public class SpinSpinningWheelPlugin extends OptionHandler {

	@Override
	public boolean handle(Player player, Node node, String option) {
		player.getInterfaceManager().open(new Component(459));
		return true;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(2644).getHandlers().put("option:spin", this);
		SceneryDefinition.forId(4309).getHandlers().put("option:spin", this);
		SceneryDefinition.forId(8748).getHandlers().put("option:spin", this);
		SceneryDefinition.forId(20365).getHandlers().put("option:spin", this);
		SceneryDefinition.forId(21304).getHandlers().put("option:spin", this);
		SceneryDefinition.forId(25824).getHandlers().put("option:spin", this);
		SceneryDefinition.forId(26143).getHandlers().put("option:spin", this);
		SceneryDefinition.forId(34497).getHandlers().put("option:spin", this);
		SceneryDefinition.forId(36970).getHandlers().put("option:spin", this);
		SceneryDefinition.forId(37476).getHandlers().put("option:spin", this);
		return this;
	}

	@Override
	public Location getDestination(Node node, Node n) {
		return null;
	}

}
