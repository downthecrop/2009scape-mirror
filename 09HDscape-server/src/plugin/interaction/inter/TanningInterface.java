package plugin.interaction.inter;

import org.crandor.game.component.Component;
import org.crandor.game.component.ComponentDefinition;
import org.crandor.game.component.ComponentPlugin;
import org.crandor.game.content.skill.free.crafting.TanningProduct;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.RunScript;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * @author Vexia
 * @version 1.2
 */
@InitializablePlugin
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
