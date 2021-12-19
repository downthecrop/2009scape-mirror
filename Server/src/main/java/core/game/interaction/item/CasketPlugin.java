package core.game.interaction.item;

import java.util.ArrayList;
import java.util.List;

import static api.ContentAPIKt.*;
import core.cache.def.impl.ItemDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.ChanceItem;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;
import core.tools.StringUtils;
import org.rs09.consts.Items;
import rs09.game.content.global.WeightBasedTable;
import rs09.game.content.global.WeightedItem;

/**
 * Represents the casket handling plugin.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class CasketPlugin extends OptionHandler {

	/**
	 * Represents the casket rewards.
	 */
	private WeightBasedTable table = WeightBasedTable.create(
			new WeightedItem(Items.COINS_995, 20, 640, 55, false),
			new WeightedItem(Items.UNCUT_SAPPHIRE_1623, 1, 1, 32, false),
			new WeightedItem(Items.UNCUT_EMERALD_1621, 1, 1, 16, false),
			new WeightedItem(Items.UNCUT_RUBY_1619, 1, 1, 9, false),
			new WeightedItem(Items.UNCUT_DIAMOND_1617, 1, 1, 2, false),
			new WeightedItem(Items.COSMIC_TALISMAN_1454, 1, 1, 8, false),
			new WeightedItem(Items.LOOP_HALF_OF_A_KEY_987, 1, 1, 1, false),
			new WeightedItem(Items.TOOTH_HALF_OF_A_KEY_985, 1, 1, 1, false)
	);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.forId(405).getHandlers().put("option:open", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		final Item reward = table.roll().get(0);
		player.getInventory().remove((Item) node);
		player.getDialogueInterpreter().sendItemMessage(reward, "You open the casket. Inside you find " + (reward.getAmount() > 1 ? "some" : (StringUtils.isPlusN(reward.getName()) ? "an" : "a")) + " " + reward.getName().toLowerCase() + ".");
		addItemOrDrop(player, reward.getId(), reward.getAmount());
		return true;
	}

	/**
	 * Gets the chance item from the array.
	 * @param items the items.
	 * @return the chance item.
	 */
	private ChanceItem getChanceItem(ChanceItem[] items) {
		final int chance = RandomFunction.random(100);
		final List<ChanceItem> chances = new ArrayList<>(20);
		for (ChanceItem c : items) {
			if (chance > c.getChanceRate()) {
				chances.add(c);
			}
		}
		return chances.size() == 0 ? items[0] : chances.get(RandomFunction.random(chances.size()));
	}

	@Override
	public boolean isWalk() {
		return false;
	}
}
