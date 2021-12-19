package core.game.interaction.inter;

import static api.ContentAPIKt.*;
import core.cache.def.impl.ItemDefinition;
import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.node.entity.skill.crafting.spinning.SpinningItem;
import core.game.node.entity.skill.crafting.spinning.SpinningPulse;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.RunScript;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;
import kotlin.Unit;

@Initializable
public class SpinningInterface extends ComponentPlugin {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.put(459, this);
		return this;
	}

	@Override
	public boolean handle(final Player p, Component component, int opcode, int button, int slot, int itemId) {
		final SpinningItem spin = SpinningItem.forId(button);
		if (spin == null) {
			return true;
		}
		if (!p.getInventory().contains(spin.getNeed(), 1)) {
			p.getPacketDispatch().sendMessage("You need "+ ItemDefinition.forId(spin.getNeed()).getName().toLowerCase() + " to make this.");
			return true;
		}
		int amt = -1;
		switch (opcode) {
		case 155:
			amt = 1;
			break;
		case 196:
			amt = 5;
			break;
		case 124:
			amt = p.getInventory().getAmount(new Item(spin.getNeed()));
			break;
		case 199:
			sendInputDialogue(p, true, "Enter the amount:", (value) -> {
				submitIndividualPulse(p, new SpinningPulse(p, new Item(spin.getNeed(), 1), (int) value, spin));
				return Unit.INSTANCE;
			});
			break;
		}
		if (opcode == 199) {
			return true;
		}
		p.getPulseManager().run(new SpinningPulse(p, new Item(spin.getNeed(), 1), amt, spin));
		return true;
	}
}
