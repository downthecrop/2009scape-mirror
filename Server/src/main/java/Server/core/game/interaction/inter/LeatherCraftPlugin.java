package core.game.interaction.inter;

import core.plugin.Initializable;
import core.game.node.entity.skill.crafting.armour.LeatherCrafting;
import core.game.node.entity.skill.crafting.armour.LeatherCrafting.SoftLeather;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.plugin.Plugin;

/**
 * Represents the plugin used for crafting leather.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class LeatherCraftPlugin extends UseWithHandler {

	/**
	 * Constructs a new {@code LeatherCraftPlugin} {@code Object}.
	 */
	public LeatherCraftPlugin() {
		super(LeatherCrafting.LEATHER, LeatherCrafting.HARD_LEATHER, LeatherCrafting.GREEN_LEATHER, LeatherCrafting.BLUE_LEATHER, LeatherCrafting.RED_LEATHER, LeatherCrafting.BLACK_LEATHER);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(LeatherCrafting.NEEDLE, ITEM_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		if (event.getBaseItem().getId() == LeatherCrafting.LEATHER || event.getUsedItem().getId() == LeatherCrafting.LEATHER) {
			SoftLeather.open(event.getPlayer());
		} else if (event.getBaseItem().getId() == LeatherCrafting.HARD_LEATHER || event.getUsedItem().getId() == LeatherCrafting.HARD_LEATHER) {
			event.getPlayer().getDialogueInterpreter().open(48923, "hard");
		} else {
			event.getPlayer().getDialogueInterpreter().open(48923, "dragon", event.getUsedItem().getId());
		}
		return true;
	}
}
