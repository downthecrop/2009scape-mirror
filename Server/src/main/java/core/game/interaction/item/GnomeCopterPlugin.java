package core.game.interaction.item;

import core.cache.def.impl.ItemDefinition;
import core.game.container.impl.EquipmentContainer;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles the gnome copter plugin.
 * @author Vexia
 */
@Initializable
public final class GnomeCopterPlugin implements Plugin<Object> {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.forId(12842).getHandlers().put("equipment", this);
		return this;
	}

	@Override
	public Object fireEvent(String identifier, Object... args) {
		final Player player = (Player) args[0];
		final Item item = (Item) args[1];
		switch (identifier) {
		case "equip":
			break;
		case "unequip":
			if (item.getId() == 12842) {
				player.getEquipment().remove(item, EquipmentContainer.SLOT_WEAPON, true);
				return false;
			}
			break;
		}
		return false;
	}

}
