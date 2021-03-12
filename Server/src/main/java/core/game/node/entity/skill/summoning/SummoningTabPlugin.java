package core.game.node.entity.skill.summoning;

import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.node.entity.skill.summoning.familiar.BurdenBeast;
import core.game.node.entity.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the component plugin handler for the summoning tab.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class SummoningTabPlugin extends ComponentPlugin {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.put(662, this);
		return this;
	}

	@Override
	public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
		switch (button) {
		case 51:
			if (player.getFamiliarManager().hasFamiliar()) {
				player.getFamiliarManager().getFamiliar().call();
			} else {
				player.getPacketDispatch().sendMessage("You don't have a follower.");
			}
			break;
		case 67:
			if (player.getFamiliarManager().hasFamiliar()) {
				if (!player.getFamiliarManager().getFamiliar().isBurdenBeast()) {
					player.getPacketDispatch().sendMessage("Your familiar is not a beast of burden.");
					break;
				}
				BurdenBeast beast = (BurdenBeast) player.getFamiliarManager().getFamiliar();
				if (beast.getContainer().isEmpty()) {
					player.getPacketDispatch().sendMessage("Your familiar is not carrying any items.");
					break;
				}
				beast.withdrawAll();
				break;
			}
			player.getPacketDispatch().sendMessage("You don't have a follower.");
			break;
		case 53:
			if (player.getFamiliarManager().hasFamiliar()) {
				player.getDialogueInterpreter().open("dismiss_dial");
			} else {
				player.getPacketDispatch().sendMessage("You don't have a follower.");
			}
			break;
		case 113:
		case 101:
		case 117:
		case 163:
		case 151:
		case 83:
		case 75:
		default:
			if (player.getFamiliarManager().hasFamiliar()) {
				player.getFamiliarManager().getFamiliar().executeSpecialMove(new FamiliarSpecial(player));
			} else {
				player.getPacketDispatch().sendMessage("You don't have a follower.");
			}
			break;
		}
		return true;
	}

}
