package core.game.interaction.inter;

import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.node.entity.skill.crafting.TanningProduct;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.RunScript;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * @author Vexia
 * @version 1.2
 */
@Initializable
public class TanningInterface extends ComponentPlugin {

	/**
	 * Method used to create a new instance.
	 * @param arg
	 * @return
	 * @throws Throwable
	 */
	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.put(324, this);
		return this;
	}

	@Override
	public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
		TanningProduct def = null;
		switch (button) {
		case 1:
			def = TanningProduct.SOFT_LEATHER;
			break;
		case 2:
			def = TanningProduct.HARD_LEATHER;
			break;
		case 3:
			def = TanningProduct.SNAKESKIN;
			break;
		case 4:
			def = TanningProduct.SNAKESKIN2;
			break;
		case 5:
			def = TanningProduct.GREEN_DHIDE;
			break;
		case 6:
			def = TanningProduct.BLUEDHIDE;
			break;
		case 7:
			def = TanningProduct.REDDHIDE;
			break;
		case 8:
			def = TanningProduct.BLACKDHIDE;
			break;
		}
		if (def == null) {
			return true;
		}
		int amount = 0;
		final TanningProduct deff = def;
		switch (opcode){
		case 155:
			amount = 1;
			break;
		case 196:
			amount = 5;
			break;
		case 124:
			amount = 10;
		case 199:
			player.setAttribute("runscript", new RunScript() {
				@Override
				public boolean handle() {
					int amt = (int) getValue();
					TanningProduct.tan(player, amt, deff);
					return true;
				}
			});
			player.getDialogueInterpreter().sendInput(false, "Enter amount:");
			break;
		case 234:
			amount = player.getInventory().getAmount(new Item(def.getItem(), 1));
			break;
		}
		TanningProduct.tan(player, amount, def);
		return true;
	}

}
