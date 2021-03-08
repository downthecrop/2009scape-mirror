package core.game.node.entity.skill.herblore;

import core.game.node.entity.skill.Skills;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the barbarian mixing plugin.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class BarbarianMixPlugin extends UseWithHandler {

	/**
	 * Constructs a new {@code BarbarianMixPlugin} {@Code Object}.
	 */
	public BarbarianMixPlugin() {
		super(123, 177, 4846, 117, 129, 3012, 135, 3036, 9743, 141, 147, 183, 143, 3020, 10002, 159, 3028, 165, 2456, 171, 3044, 191);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(11324, ITEM_TYPE, this);
		addHandler(11326, ITEM_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		if (player == null) {
			return true;
		}
		final BarbarianPotion potion = BarbarianPotion.forId(((Item) event.getUsedWith()).getId());
		if (potion == null) {
			return true;
		}
		if (!potion.isBoth() && event.getUsedItem().getId() == 11324 || ((Item) event.getUsedWith()).getId() == 11324) {
			return true;
		}
		if (player.getSkills().getLevel(Skills.HERBLORE) < potion.getLevel()) {
			player.getPacketDispatch().sendMessage("You need a herblore level of " + potion.getLevel() + " to make this mix.");
			return true;
		}
		if (potion.getItem() == event.getUsedItem().getId() || potion.getItem() == ((Item) event.getUsedWith()).getId()) {
			player.getInventory().remove(new Item(potion.getItem(), 1));
			player.getInventory().add(new Item(potion.getProduct(), 1));
			player.getSkills().addExperience(Skills.HERBLORE, potion.getExp(), true);
			if (potion.isBoth()) {
				player.getInventory().remove(new Item(11324, 1));
			}
			player.getInventory().remove(new Item(11326, 1));
		}
		return true;
	}

}
