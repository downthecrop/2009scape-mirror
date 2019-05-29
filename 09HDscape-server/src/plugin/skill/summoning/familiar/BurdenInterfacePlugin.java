package plugin.skill.summoning.familiar;

import org.crandor.game.component.Component;
import org.crandor.game.component.ComponentDefinition;
import org.crandor.game.component.ComponentPlugin;
import org.crandor.game.container.Container;
import org.crandor.game.content.skill.member.summoning.familiar.BurdenBeast;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.RunScript;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Handles the beast of burden interface.
 * @author Emperor
 */
@InitializablePlugin
public final class BurdenInterfacePlugin extends ComponentPlugin {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.put(665, this);
		ComponentDefinition.put(671, this);
		return this;
	}

	@Override
	public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
		if (!player.getFamiliarManager().hasFamiliar() || !player.getFamiliarManager().getFamiliar().isBurdenBeast()) {
			return false;
		}
		final BurdenBeast beast = (BurdenBeast) player.getFamiliarManager().getFamiliar();
		final boolean withdraw = component.getId() == 671;
		final Container container = withdraw ? beast.getContainer() : player.getInventory();
		final Item item = slot >= 0 && slot < container.capacity() ? container.get(slot) : null;
		if (item == null && button != 29) {
			return true;
		}
		switch (opcode) {
		case 155:
			if (button == 29) {
				beast.withdrawAll();
				return true;
			}
			beast.transfer(item, 1, withdraw);
			return true;
		case 196:
			beast.transfer(item, 5, withdraw);
			return true;
		case 124:
			beast.transfer(item, 10, withdraw);
			return true;
		case 199:
			beast.transfer(item, container.getAmount(item), withdraw);
			return true;
		case 234:
			player.setAttribute("runscript", new RunScript() {
				@Override
				public boolean handle() {
					beast.transfer(item, (int) getValue(), withdraw);
					return true;
				}
			});
			player.getDialogueInterpreter().sendInput(false, "Enter the amount:");
			break;
		case 168:
			player.getPacketDispatch().sendMessage(item.getDefinition().getExamine());
			return true;
		}
		return false;
	}

}
