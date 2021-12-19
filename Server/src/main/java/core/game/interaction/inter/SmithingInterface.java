package core.game.interaction.inter;

import static api.ContentAPIKt.*;
import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.plugin.Initializable;
import core.game.node.entity.skill.smithing.BarType;
import core.game.node.entity.skill.smithing.Bars;
import core.game.node.entity.skill.smithing.SmithingPulse;
import core.game.node.entity.skill.smithing.SmithingType;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.RunScript;
import core.game.node.item.Item;
import core.plugin.Plugin;
import kotlin.Unit;

/**
 * @author 'Vexia
 */
@Initializable
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
			sendInputDialogue(p, true, "Enter the amount:", (value) -> {
				p.getPulseManager().run(new SmithingPulse(p, new Item((int) p.getGameAttributes().getAttribute("smith-item"), (int) value), (Bars) p.getGameAttributes().getAttribute("smith-bar"), (int) value));
				return Unit.INSTANCE;
			});
			return true;
		}
		p.getPulseManager().run(new SmithingPulse(p, new Item(item, amount), Bars.forId(item), amount));
		return true;
	}
}
