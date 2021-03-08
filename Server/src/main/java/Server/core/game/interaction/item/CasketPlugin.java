package core.game.interaction.item;

import java.util.ArrayList;
import java.util.List;

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
	private static final ChanceItem[] CASKET_REWARD = new ChanceItem[] { new ChanceItem(995, 8, 3000, 30), new ChanceItem(1623, 1, 30), new ChanceItem(1621, 1, 70), new ChanceItem(1619, 1, 70), new ChanceItem(1617, 1, 97), new ChanceItem(987, 1, 97), new ChanceItem(985, 1, 97), new ChanceItem(1454, 1, 30), new ChanceItem(1452, 1, 70), new ChanceItem(1462, 1, 97) };

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.forId(405).getHandlers().put("option:open", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		final ChanceItem reward = getChanceItem(CASKET_REWARD);
		player.getInventory().remove((Item) node);
		player.getDialogueInterpreter().sendItemMessage(reward, "You open the casket. Inside you find " + (reward.getAmount() > 1 ? "some" : (StringUtils.isPlusN(reward.getName()) ? "an" : "a")) + " " + reward.getName().toLowerCase() + ".");
		player.getInventory().add(reward.getAmount() == 1 ? reward : new Item(reward.getId(), RandomFunction.random(reward.getMinimumAmount(), reward.getMaximumAmount())));
		return true;
	}

	/**
	 * Gets the chance item from the array.
	 * @param items the items.
	 * @return the chance item.
	 */
	private ChanceItem getChanceItem(ChanceItem[] items) {
		final int chance = RandomFunction.random(100);
		final List<ChanceItem> chances = new ArrayList<>();
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
