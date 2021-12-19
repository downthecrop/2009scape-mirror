package core.game.interaction.inter;

import static api.ContentAPIKt.*;
import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.RunScript;
import core.game.node.entity.player.link.request.trade.TradeModule;
import core.plugin.Initializable;
import core.plugin.Plugin;
import kotlin.Unit;

/**
 * Represents the interface plugin used to handle all trade related functions.
 * @author 'Vexia
 */
@Initializable
public final class TradeInterfacePlugin extends ComponentPlugin {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.put(334, this);
		ComponentDefinition.put(335, this);
		ComponentDefinition.put(336, this);
		return this;
	}

	@Override
	public boolean handle(Player player, Component component, int opcode, int button, final int slot, int itemId) {
		final TradeModule module = TradeModule.getExtension(player);
		if (module == null) {
			return true;
		}
		switch (component.getId()) {
		case 334:// second accept screen
			switch (button) {
			case 20:// acept
				module.setAccepted(true, true);
				break;
			case 21:// decline
				module.decline();
				break;
			}
			break;
		case 335:// main accept interface
			switch (opcode) {
			case 155:
				switch (button) {
				case 16:// accept
					module.setAccepted(true, true);
					break;
				case 18:// decline
					module.decline();
					break;
				case 30:// withdraw one
					module.getContainer().withdraw(slot, 1);
					break;
				}
				break;
			case 196:
				module.getContainer().withdraw(slot, 5);
				break;
			case 124:
				module.getContainer().withdraw(slot, 10);
				break;
			case 199:
				module.getContainer().withdraw(slot, module.getContainer().getAmount(module.getContainer().get(slot)));
				break;
			case 234:
				sendInputDialogue(player, false, "Enter the amount:", (value) -> {
					String s = value.toString();
					s = s.replace("k","000");
					s = s.replace("K","000");
					s = s.replace("m","000000");
					s = s.replace("M","000000");
					int val = Integer.parseInt(s);
					module.getContainer().withdraw(slot, val);
					return Unit.INSTANCE;
				});
				break;
			case 9:// examine.
				if (TradeModule.getExtension(button == 32 ? module.getTarget() : player) == null) {
					return true;
				}
				player.getPacketDispatch().sendMessage(TradeModule.getExtension(button == 32 ? module.getTarget() : player).getContainer().get(slot).getDefinition().getExamine());
				break;
			}
			break;
		case 336:// overlay interface
			switch (opcode) {
			case 155:
				module.getContainer().offer(slot, 1);
				break;
			case 196:
				module.getContainer().offer(slot, 5);
				break;
			case 124:
				module.getContainer().offer(slot, 10);
				break;
			case 199:
				module.getContainer().offer(slot, player.getInventory().getAmount(player.getInventory().get(slot)));
				break;
			case 234:
				sendInputDialogue(player, false, "Enter the amount:", (value) -> {
					String s = value.toString();
					s = s.replace("k","000");
					s = s.replace("K","000");
					int val = Integer.parseInt(s);
					module.getContainer().offer(slot, val);
					return Unit.INSTANCE;
				});
				break;
			case 9:
				player.getPacketDispatch().sendMessage(player.getInventory().get(slot).getDefinition().getExamine());
				break;
			}
			break;
		}
		return true;
	}

}
