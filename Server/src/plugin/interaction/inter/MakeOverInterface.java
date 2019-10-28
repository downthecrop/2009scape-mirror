package plugin.interaction.inter;

import org.crandor.game.component.Component;
import org.crandor.game.component.ComponentDefinition;
import org.crandor.game.component.ComponentPlugin;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.appearance.Gender;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.repository.Repository;
import org.crandor.game.world.update.flag.player.AppearanceFlag;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the interface used to handle the buttons related to the make over
 * mage interface.
 * @author 'Vexia
 * @date 26/12/2013
 */
@InitializablePlugin
public class MakeOverInterface extends ComponentPlugin {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.put(205, this);
		return this;
	}

	@Override
	public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
		handle(player, button);
		return true;
	}

	/**
	 * Method used to confirm the order.
	 * @param player the player.
	 */
	public static void confirm(Player player) {
		boolean male = player.getAttribute("design:male", player.getAppearance().isMale());
		int skin = player.getAttribute("design:skin", player.getAppearance().getSkin().getColor());
		if (!player.getInventory().containsItem(new Item(995, 3000))) {
			return;
		}
		if (skin != -1 && player.getInventory().remove(new Item(995, 3000))) {
			Gender g = male ? Gender.MALE : Gender.FEMALE;
			if (g != player.getAppearance().getGender()) {
				player.getAppearance().changeGender(g);
			}
			player.getAppearance().getSkin().changeColor(skin);
			player.getUpdateMasks().register(new AppearanceFlag(player));
			player.getInterfaceManager().close();
			player.removeAttribute("design:male");
			player.removeAttribute("design:skin");
			player.getDialogueInterpreter().open(2676, Repository.findNPC(2676), 1);
		}
	}

	/**
	 * Method used to handle the buttons.
	 * @param player the player.
	 * @param button the button.
	 */
	public static void handle(Player player, int button) {
		int skin = 0;
		switch (button) {
		case 113:
			player.setAttribute("design:male", false);
			break;
		case 112:
			player.setAttribute("design:male", true);
			break;
		case 100:
			skin = 7;
			break;
		case 93:
			skin = 1;
			break;
		case 94:
			skin = 2;
			break;
		case 95:
			break;
		case 96:
			skin = 3;
			break;
		case 97:
			skin = 4;
			break;
		case 98:
			skin = 5;
			break;
		case 99:
			skin = 6;
			break;
		case 88:
			confirm(player);
			break;
		}
		if (button != 88 && button != 112 && button != 113) {
			player.setAttribute("design:skin", skin);
		}
	}
}
