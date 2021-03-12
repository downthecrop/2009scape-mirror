package core.game.interaction.inter;

import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.emote.Emotes;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles the emote tab interface.
 * @author Vexia
 * 
 */
@Initializable
public final class EmoteTabInterface extends ComponentPlugin {
	
	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.put(464, this);
		return this;
	}

	@Override
	public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
		Emotes.handle(player, button);
		return true;
	}
}
