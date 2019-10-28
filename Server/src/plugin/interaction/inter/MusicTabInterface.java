package plugin.interaction.inter;

import org.crandor.game.component.Component;
import org.crandor.game.component.ComponentDefinition;
import org.crandor.game.component.ComponentPlugin;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.music.MusicEntry;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Handles the interface tab buttons.
 * @author Emperor
 */
@InitializablePlugin
public final class MusicTabInterface extends ComponentPlugin {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.put(187, this);
		return this;
	}

	@Override
	public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
		switch (opcode) {
		case 155:
			switch (button) {
			case 11:
				player.getMusicPlayer().toggleLooping();
				return true;
			case 1:
				MusicEntry entry = player.getMusicPlayer().getUnlocked().get(slot);
				if (entry == null) {
					player.getPacketDispatch().sendMessage("You have not unlocked this piece of music yet!</col>");
					return true;
				}
				player.getMusicPlayer().play(entry);
				return true;
			}
			break;
		}
		return false;
	}

}
