package content.global.handlers.iface;

import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.prayer.PrayerType;
import core.plugin.Initializable;
import core.plugin.Plugin;

import static core.api.ContentAPIKt.hasRequirement;
import content.data.Quests;

/**
 * Represents the prayer interface.
 * @author 'Vexia
 */
@Initializable
public final class PrayerTabInterface extends ComponentPlugin {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.put(271, this);
		return this;
	}

	@Override
	public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
		final PrayerType type = PrayerType.get(button);
                if (type == PrayerType.CHIVALRY || type == PrayerType.PIETY)
                    if (!hasRequirement(player, Quests.KINGS_RANSOM))
                        return true;
		if (type == null) {
			return true;
		}
		player.getPrayer().toggle(type);
		return true;
	}
}
