package plugin.interaction.item;

import java.util.concurrent.TimeUnit;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.TeleportManager.TeleportType;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Handles the explorers ring.
 * @author Vexia
 *
 */
@InitializablePlugin
public class ExplorersRingPlugin extends OptionHandler {
	
	/**
	 * The cabbage port location.
	 */
	private static final Location CABBAGE_PORT = Location.create(3051, 3291, 0);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.setOptionHandler("run-replenish", this);
		ItemDefinition.setOptionHandler("low-alchemy", this);
		ItemDefinition.setOptionHandler("cabbage-port", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		final Item item = (Item) node;
		int level = getLevel(item.getId());
		switch (option) {
		case "run-replenish":
			if (player.getSavedData().getGlobalData().getRunReplenishDelay() < System.currentTimeMillis()) {
				player.getSavedData().getGlobalData().setRunReplenishCharges(0);
				player.getSavedData().getGlobalData().setRunReplenishDelay(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1));
			}
			int charges = player.getSavedData().getGlobalData().getRunReplenishCharges();
			if (charges >= level) {
				player.sendMessage("You have used all the charges you can for one day.");
				return true;
			}
			player.getSettings().updateRunEnergy(-50);
			player.getSavedData().getGlobalData().setRunReplenishCharges(charges + 1);
			player.sendMessage("You replenish 50% of your run energy.");
			break;
		case "low-alchemy":
			if (player.getSkills().getStaticLevel(Skills.MAGIC) < 21) {
				player.sendMessage("You need a Magic level of 21 in order to do that.");
				break;
			}
			if (player.getSavedData().getGlobalData().getLowAlchemyDelay() < System.currentTimeMillis()) {
				player.getSavedData().getGlobalData().setLowAlchemyCharges(0);
				player.getSavedData().getGlobalData().setLowAlchemyDelay(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1));
			}
			if (player.getSavedData().getGlobalData().getLowAlchemyCharges() > 0 || player.getSavedData().getGlobalData().getLowAlchemyCharges() == 0 && player.getSavedData().getGlobalData().getLowAlchemyDelay() > System.currentTimeMillis()) {
				player.sendMessage("You have used all the charges you can for one day.");
				return true;
			}
			player.sendMessage("You grant yourself with 30 free low alchemy charges.");
			player.getSavedData().getGlobalData().setLowAlchemyCharges(30);
			break;
		case "cabbage-port":
			player.getTeleporter().send(CABBAGE_PORT, TeleportType.CABBAGE);
			break;
		}
		return true;
	}
	
	/**
	 * Gets the level of the ring.
	 * @param itemId The item id.
	 * @return The level.
	 */
	private int getLevel(int itemId) {
		switch (itemId) {
		case 13560:
			return 1;
		case 13561:
			return 2;
		case 13562:
			return 3;
		}
		return -1;
	}

}
