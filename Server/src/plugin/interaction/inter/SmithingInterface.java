package plugin.interaction.inter;

import org.crandor.game.component.Component;
import org.crandor.game.component.ComponentDefinition;
import org.crandor.game.component.ComponentPlugin;
import org.crandor.game.content.skill.free.smithing.BarType;
import org.crandor.game.content.skill.free.smithing.Bars;
import org.crandor.game.content.skill.free.smithing.SmithingPulse;
import org.crandor.game.content.skill.free.smithing.SmithingType;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.RunScript;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * @author 'Vexia
 */
@InitializablePlugin
public class SmithingInterface extends ComponentPlugin {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.put(300, this);
		return this;
	}

	@Override
	public boolean handle(final Player p, Component component, int opcode, int button, int slot, int itemId) {
		final int item = Bars.getItemId(button, (BarType) p.getGameAttributes().getAttribute("smith-type"));
		final Bars bar = Bars.forId(item);
		if (bar == null) {
			return true;
		}
		int amount = SmithingType.forButton(p, bar, button, bar.getBarType().getBarType());
		p.getGameAttributes().setAttribute("smith-bar", bar);
		p.getGameAttributes().setAttribute("smith-item", item);
		if (amount == -1) {
			p.setAttribute("runscript", new RunScript() {
				@Override
				public boolean handle() {
					final int ammount = (int) value;
					p.getPulseManager().run(new SmithingPulse(p, new Item((int) p.getGameAttributes().getAttribute("smith-item"), ammount), (Bars) p.getGameAttributes().getAttribute("smith-bar"), ammount));
					return false;
				}
			});
			p.getDialogueInterpreter().sendInput(false, "Enter the amount.");
			return true;
		}
		p.getPulseManager().run(new SmithingPulse(p, new Item(item, amount), Bars.forId(item), amount));
		return true;
	}
}
