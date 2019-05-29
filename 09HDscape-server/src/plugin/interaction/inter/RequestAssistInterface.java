package plugin.interaction.inter;

import org.crandor.game.component.Component;
import org.crandor.game.component.ComponentDefinition;
import org.crandor.game.component.ComponentPlugin;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.request.assist.AssistSession;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * @author 'Vexia
 */
@InitializablePlugin
public class RequestAssistInterface extends ComponentPlugin {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.put(301, this);
		return this;
	}

	@Override
	public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
		final AssistSession session = AssistSession.getExtension(player);
		if (session == null) {
			return true;
		}
		if (player != session.getPlayer()) {
			return true;
		}
		switch (button) {
		case 15:
			session.toggleButton((byte) 0);
			break;
		case 20:
			session.toggleButton((byte) 1);
			break;
		case 25:
			session.toggleButton((byte) 2);
			break;
		case 30:
			session.toggleButton((byte) 3);
			break;
		case 35:
			session.toggleButton((byte) 4);
			break;
		case 40:
			session.toggleButton((byte) 5);
			break;
		case 45:
			session.toggleButton((byte) 6);
			break;
		case 50:
			session.toggleButton((byte) 7);
			break;
		case 55:
			session.toggleButton((byte) 8);
			break;
		}
		session.refresh();
		return true;
	}

}
