package plugin.interaction.inter;

import org.crandor.game.component.Component;
import org.crandor.game.component.ComponentDefinition;
import org.crandor.game.component.ComponentPlugin;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.repository.Repository;
import org.crandor.game.world.update.flag.player.AppearanceFlag;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin used to handle the interface of changing hairs.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class HairInterfacePlugin extends ComponentPlugin {

	/**
	 * Represents the coins item.
	 */
	private static final Item COINS = new Item(995, 1000);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.put(204, this);
		ComponentDefinition.put(203, this);
		ComponentDefinition.put(199, this);
		return this;
	}

	@Override
	public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
		handle(player, button, component.getId() == 204 ? true : false, component.getId() == 199 ? new int[] { 1, 2 } : new int[] { 1 });
		return true;
	}

	/**
	 * Represents the method used to handle the buttons.
	 * @param player the player.
	 * @param button the button,
	 * @param male the male.
	 */
	public static void handle(final Player player, int button, boolean male, int... inter) {
		if (inter != null && inter.length == 2) {
			int style = -1;
			int col = -1;
			switch (button) {
			case 139:
				style = 11;
				break;
			case 140:
				style = 10;
				break;
			case 141:
				style = 13;
				break;
			case 142:
				style = 15;
				break;
			case 143:
				style = 17;
				break;
			case 144:
				style = 12;
				break;
			case 145:
				style = 14;
				break;
			case 146:
				style = 16;
				break;
			case 127:
				col = 0;
				break;
			case 128:
				col = 1;
				break;
			case 129:
				col = 2;
				break;
			case 130:
				col = 3;
				break;
			case 131:
				col = 4;
				break;
			case 132:
				col = 5;
				break;
			case 133:
				col = 6;
				break;
			case 134:
				col = 7;
				break;
			case 135:
				col = 8;
				break;
			case 136:
				col = 9;
				break;
			case 137:
				col = 10;
				break;
			case 138:
				col = 11;
				break;
			case 104:
				confirm(player);
				/** confirms the players choice on design. */
				break;
			}
			if (style != -1) {
				player.setAttribute("newBeard", style);
			}
			if (col != -1) {
				player.setAttribute("newHairColour", col);
			}
			return;
		}
		if (male) {
			switch (button) {
			case 134:
			case 135:
			case 136:
			case 137:
			case 138:
			case 139:
			case 140:
			case 141:
			case 142:
				player.setAttribute("newHair", button - 134);
				break;
			case 122:
			case 123:
			case 124:
			case 125:
			case 126:
			case 127:
			case 128:
			case 129:
			case 130:
			case 131:
			case 132:
			case 133:
				player.setAttribute("newHairColour", button - 122);
				break;
			case 98:
				confirm(player);
				break;
			}
		} else {
			switch (button) {
			case 136:
			case 137:
			case 138:
			case 139:
			case 140:
			case 141:
			case 142:
			case 143:
			case 144:
			case 145:
				player.setAttribute("newHair", button - 91);
				break;
			case 124:
			case 125:
			case 126:
			case 127:
			case 128:
			case 129:
			case 130:
			case 131:
			case 132:
			case 133:
			case 134:
			case 135:
				player.setAttribute("newHairColour", button - 124);
				break;
			case 99:
				confirm(player);
				break;
			}
		}
	}

	/**
	 * The accepting of your new style.
	 * @param player the player.
	 */
	public static void confirm(Player player) {
		if (!player.getInventory().containsItem(COINS)) {
			return;
		}
		if (player.getInventory().remove(COINS)) {
			if (player.getAttribute("newHair") != null) {
				player.getAppearance().getHair().changeLook((Integer) player.getAttribute("newHair"));
			}
			if (player.getAttribute("newHairColour") != null) {
				player.getAppearance().getHair().changeColor((Integer) player.getAttribute("newHairColour"));
			}
			if (player.getAttribute("newBeard") != null) {
				player.getAppearance().getBeard().changeLook((Integer) player.getAttribute("newBeard"));
			}
			player.getUpdateMasks().register(new AppearanceFlag(player));
			player.getInterfaceManager().close();
			player.getDialogueInterpreter().sendDialogues(Repository.findNPC(598), null, "Hope you like the new do!");
		}
	}
}
