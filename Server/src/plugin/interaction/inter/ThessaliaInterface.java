package plugin.interaction.inter;

import org.crandor.game.component.Component;
import org.crandor.game.component.ComponentDefinition;
import org.crandor.game.component.ComponentPlugin;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the interface plugin to handle thessalia interfaces.
 * @author Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class ThessaliaInterface extends ComponentPlugin {

	/**
	 * Represents the coins item.
	 */
	private static final Item COINS = new Item(995, 500);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.put(201, this);
		ComponentDefinition.put(202, this);
		ComponentDefinition.put(206, this);
		ComponentDefinition.put(207, this);
		return this;
	}

	@Override
	public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
		switch (component.getId()) {
		case 207:
			switch (button) {
			case 157:
			case 158:
				player.setAttribute("newBody", button - 139);
				break;
			case 159:
			case 161:
			case 163:
				player.setAttribute("newBody", button - 138);
				break;
			case 160:
			case 162:
				player.setAttribute("newBody", button - 140);
				break;
			case 164:
			case 165:
			case 166:
			case 167:
			case 168:
			case 169:
				player.setAttribute("newArms", button - 138);
				break;
			case 90:
			case 91:
			case 92:
			case 93:
			case 94:
			case 95:
			case 96:
			case 97:
			case 98:
			case 99:
			case 100:
			case 101:
			case 102:
			case 103:
			case 104:
			case 105:
				player.setAttribute("newBodyColour", button - 90);
				break;
			case 106:
				if (!player.getInventory().containsItem(COINS)) {
					return true;
				}
				if (player.getInventory().remove(COINS)) {
					if (player.getAttribute("newBody") != null) {
						player.getAppearance().getTorso().changeLook((Integer) player.getAttribute("newBody"));
					}
					if (player.getAttribute("newArms") != null) {
						player.getAppearance().getArms().changeLook((Integer) player.getAttribute("newArms"));
					}
					if (player.getAttribute("newBodyColour") != null) {
						player.getAppearance().getTorso().changeColor((Integer) player.getAttribute("newBodyColour"));
					}
					player.getAppearance().sync();
					player.getInterfaceManager().close();
					player.getDialogueInterpreter().open(548, true, true);
				}
				break;
			}
			break;
		case 206:// male leg
			switch (button) {
			case 128:
			case 129:
			case 130:
			case 131:
				player.setAttribute("newLegs", button - 92);
				break;
			case 105:
				player.setAttribute("newLegsColour", button - 105);
				break;
			case 90:
			case 91:
			case 92:
			case 93:
			case 94:
			case 95:
			case 96:
			case 97:
			case 98:
			case 99:
			case 100:
			case 101:
			case 102:
			case 103:
			case 104:
				player.setAttribute("newLegsColour", button - 89);
				break;
			case 106:
				if (player.getInventory().remove(COINS)) {
					if (player.getAttribute("newLegs") != null) {
						player.getAppearance().getLegs().changeLook((Integer) player.getAttribute("newLegs"));
					}
					if (player.getAttribute("newLegsColour") != null) {
						player.getAppearance().getLegs().changeColor((Integer) player.getAttribute("newLegsColour"));
					}
					player.getAppearance().sync();
					player.getInterfaceManager().close();
					player.getDialogueInterpreter().open(548, true, true);
				}
				break;
			}
			break;
		case 202:
			switch (button) {
			case 146:
			case 147:
			case 148:
			case 149:
			case 150:
				player.setAttribute("newBody", button - 90);
				break;
			case 151:
			case 152:
			case 153:
			case 154:
			case 155:
				player.setAttribute("newArms", button - 90);
				break;
			case 90:
			case 91:
			case 92:
			case 93:
			case 94:
			case 95:
			case 96:
			case 97:
			case 98:
			case 99:
			case 100:
			case 101:
			case 102:
			case 103:
			case 104:
			case 105:
				player.setAttribute("newBodyColour", button - 90);
				break;
			case 106:
				if (player.getInventory().remove(COINS)) {
					if (player.getAttribute("newBody") != null) {
						player.getAppearance().getTorso().changeLook((Integer) player.getAttribute("newBody"));
					}
					if (player.getAttribute("newArms") != null) {
						player.getAppearance().getArms().changeLook((Integer) player.getAttribute("newArms"));
					}
					if (player.getAttribute("newBodyColour") != null) {
						player.getAppearance().getTorso().changeColor((Integer) player.getAttribute("newBodyColour"));
					}
					player.getAppearance().sync();
					player.getInterfaceManager().close();
					player.getDialogueInterpreter().open(548, true, true);
				}
				break;
			}
			break;
		case 201:
			switch (button) {
			case 201:
			case 131:
			case 132:
			case 133:
			case 134:
			case 135:
				player.setAttribute("newLegs", button - 61);
				break;
			case 136:
			case 137:
				player.setAttribute("newLegs", button - 60);
				break;
			case 105:
				player.setAttribute("newLegsColour", button - 105);
				break;
			case 90:
			case 91:
			case 92:
			case 93:
			case 94:
			case 95:
			case 96:
			case 97:
			case 98:
			case 99:
			case 100:
			case 101:
			case 102:
			case 103:
			case 104:
				player.setAttribute("newLegsColour", button - 89);
				break;
			case 106:
				if (player.getInventory().remove(COINS)) {
					if (player.getAttribute("newLegs") != null) {
						player.getAppearance().getLegs().changeLook((Integer) player.getAttribute("newLegs"));
					}
					if (player.getAttribute("newLegsColour") != null) {
						player.getAppearance().getLegs().changeColor((Integer) player.getAttribute("newLegsColour"));
					}
					player.getAppearance().sync();
					player.getInterfaceManager().close();
					player.getDialogueInterpreter().open(548, true, true);
				}
				break;
			}
			break;
		}
		return true;
	}
}
