package core.game.interaction.object;

import core.cache.def.impl.ObjectDefinition;
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
		ObjectDefinition.forId(2644).getHandlers().put("option:spin", this);
		ObjectDefinition.forId(4309).getHandlers().put("option:spin", this);
		ObjectDefinition.forId(8748).getHandlers().put("option:spin", this);
		ObjectDefinition.forId(20365).getHandlers().put("option:spin", this);
		ObjectDefinition.forId(21304).getHandlers().put("option:spin", this);
		ObjectDefinition.forId(25824).getHandlers().put("option:spin", this);
		ObjectDefinition.forId(26143).getHandlers().put("option:spin", this);
		ObjectDefinition.forId(34497).getHandlers().put("option:spin", this);
		ObjectDefinition.forId(36970).getHandlers().put("option:spin", this);
		ObjectDefinition.forId(37476).getHandlers().put("option:spin", this);
		return this;
	}

	@Override
	public Location getDestination(Node node, Node n) {
		return null;
	}

}
