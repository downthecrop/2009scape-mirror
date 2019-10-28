package plugin.interaction.inter;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.component.Component;
import org.crandor.game.component.ComponentDefinition;
import org.crandor.game.component.ComponentPlugin;
import org.crandor.game.content.skill.free.crafting.spinning.SpinningItem;
import org.crandor.game.content.skill.free.crafting.spinning.SpinningPulse;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.RunScript;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

@InitializablePlugin
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
			p.setAttribute("runscript", new RunScript() {
				@Override
				public boolean handle() {
					int ammount = (int) value;
					p.getPulseManager().run(new SpinningPulse(p, new Item(spin.getNeed(), 1), ammount, spin));
					return true;
				}
			});
			p.getDialogueInterpreter().sendInput(false, "Enter the amount.");
			break;
		}
		if (opcode == 199) {
			return true;
		}
		p.getPulseManager().run(new SpinningPulse(p, new Item(spin.getNeed(), 1), amt, spin));
		return true;
	}
}
