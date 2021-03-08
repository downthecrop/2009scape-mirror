package core.game.component;

import core.game.node.entity.player.Player;
import core.plugin.Plugin;

/**
 * Represents the plugin used to handle a component reward.
 * @author Vexia
 */
public abstract class ComponentPlugin implements Plugin<Object> {

	/**
	 * Handles the interface interaction.
	 * @param player The player.
	 * @param component The component.
	 * @param opcode The opcode.
	 * @param slot The slot.
	 * @param itemId The item id.
	 * @return {@code True} if succesfully handled.
	 */
	public abstract boolean handle(final Player player, Component component, final int opcode, final int button, int slot, int itemId);
	
	/**
	 * Called when this component opens.
	 * @param player The player
	 * @param component The component opening.
	 */
	public void open(Player player, Component component) {}
		
	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

}
