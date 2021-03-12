package core.game.interaction.inter;

import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

@Initializable
public class CrystalKeyChestPlugin extends ComponentPlugin {
	
	private final Integer CHEST_INTERFACE = 501;
	
	private Player player;
	
	public CrystalKeyChestPlugin() {
		/*
		 * Empty
		 */
	}
	
	public CrystalKeyChestPlugin(Player player) {
		this.player = player;
	}
	

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.put(CHEST_INTERFACE, this);
		return this;
	}

	@Override
	public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
		return false;
	}
	
	public void constructInterface(Player player) {
		Integer[] hiddenChildren = new Integer[] { 3, 4, 5, 6, 7};
		Component component = new Component(CHEST_INTERFACE);
		for (int i = 3; i < hiddenChildren.length; i++) {
			player.getPacketDispatch().sendInterfaceConfig(CHEST_INTERFACE, i, true);
		}
		player.getPacketDispatch().sendItemOnInterface(989, 1, CHEST_INTERFACE, hiddenChildren[2]);
		player.getInterfaceManager().open(component);
		this.player = player;
	}

}
