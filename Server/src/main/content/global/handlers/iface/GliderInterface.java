package content.global.handlers.iface;

import static core.api.ContentAPIKt.*;
import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import content.global.travel.glider.GliderPulse;
import content.global.travel.glider.Gliders;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the glider interface component.
 * @author Emperor
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class GliderInterface extends ComponentPlugin {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.put(138, this);
		return this;
	}

	@Override
	public boolean handle(final Player player, Component component, int opcode, int button, int slot, int itemId) {
		final Gliders glider = Gliders.forId(button);
		if (glider == null) {
			return true;
		}
		submitWorldPulse(new GliderPulse(1, player, glider));
		return true;
	}
}
