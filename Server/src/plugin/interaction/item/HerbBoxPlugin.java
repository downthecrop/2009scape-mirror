package plugin.interaction.item;

import java.util.ArrayList;
import java.util.List;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.ChanceItem;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Handles the herb box item.
 * @author Vexia
 *
 */
@InitializablePlugin
public class HerbBoxPlugin extends OptionHandler {

	/**
	 * The herbs to recieve.
	 */
	private static final ChanceItem[] HERBS = new ChanceItem[] {new ChanceItem(199, 1, 25), new ChanceItem(201, 1, 18), new ChanceItem(203, 1, 13), new ChanceItem(205, 1, 11), new ChanceItem(207, 1, 8), new ChanceItem(209, 1, 6), new ChanceItem(211, 1, 4), new ChanceItem(213, 1, 5), new ChanceItem(215, 1, 3), new ChanceItem(2485, 1, 3), new ChanceItem(217, 1, 3)};

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.forId(14810).getConfigurations().put("option:bank-all", this);
		ItemDefinition.forId(14810).getConfigurations().put("option:take-one", this);
		ItemDefinition.forId(14810).getConfigurations().put("option:check", this);
		return null;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		Item box = node.asItem();
		int herbs = 10 - (1000 - box.getCharge());
		if (herbs < 0 || herbs > 10) {
			player.getInventory().remove(box, box.getSlot(), true);
			return true;
		}
		player.lock(1);
		switch (option) {
		case "bank-all":
			if (player.getBank().freeSlots() < herbs) {
				player.sendMessage("You don't have enough bank space to bank all those herbs.");
				break;
			}
			List<ChanceItem> herbList = new ArrayList<>();
			for (int i = 0; i < herbs; i++) {
				herbList.add(RandomFunction.getChanceItem(HERBS));
			}
			if (player.getInventory().remove(box, box.getSlot(), true)) {
				player.getBank().add(herbList.toArray(new ChanceItem[] {}));
				player.sendMessage(herbs + " herbs have been deposited into your bank.");
			}
			break;
		case "take-one":
			if (player.getInventory().freeSlots() < 1) {
				player.sendMessage("You don't have enough space in your inventory.");
				break;
			}
			ChanceItem herb = RandomFunction.getChanceItem(HERBS);
			player.getInventory().add(herb.getRandomItem());
			box.setCharge(box.getCharge() - 1);
			if (box.getCharge() <= 990) {
				player.getInventory().remove(box, box.getSlot(), true);
			}
			player.sendMessage("You take a herb from the box.");
			break;
		case "check":
			player.sendMessage("The herb box contains " + herbs + " herbs.");
			break;
		}
		return true;
	}

	@Override
	public boolean isWalk() {
		return false;
	}
}
