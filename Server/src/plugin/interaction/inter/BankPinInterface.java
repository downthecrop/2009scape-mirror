package plugin.interaction.inter;

import org.crandor.game.component.Component;
import org.crandor.game.component.ComponentDefinition;
import org.crandor.game.component.ComponentPlugin;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.BankPinManager;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Handles the bank pin interfaces.
 * @author Vexia
 */
@InitializablePlugin
public class BankPinInterface extends ComponentPlugin {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.forId(13).setPlugin(this);
		ComponentDefinition.forId(14).setPlugin(this);
		return this;
	}

	@Override
	public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
		BankPinManager manager = player.getBankPinManager();
		switch (component.getId()) {
		case 14:// setting
			switch (button) {
			case 60:
			case 62:
				if (!manager.hasPin()) {
					manager.toggleConfirmInterface(button == 60);
				} else {
					manager.setChangingState(1);
					manager.openPin();
				}
				break;
			case 63:
				manager.toggleConfirmInterface(true);
				break;
			case 65:
				manager.cancelPin("The PIN has been cancelled", "and will NOT be set.", "", "You still do not have a Bank", "PIN.");
				break;
			case 89:
			case 91:
				manager.handleConfirmInterface(button);
				break;
			case 61:
			case 64:
				manager.switchRecovery();
				break;
			}
			break;
		case 13:// pin
			switch (button) {
			case 30:
				break;
			default:
				player.getBankPinManager().updateTempPin(button - 1);
				break;
			}
			break;
		}
		return true;
	}

}
