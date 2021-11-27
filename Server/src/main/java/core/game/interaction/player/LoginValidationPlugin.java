package core.game.interaction.player;

import core.game.content.activity.ActivityManager;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.system.SystemManager;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.plugin.PluginManifest;
import core.plugin.PluginType;
import rs09.game.world.GameWorld;

import java.util.concurrent.TimeUnit;

/**
 * Validates a player login.
 * @author Emperor
 * @author Vexia
 * 
 */
@PluginManifest(type = PluginType.LOGIN)
@Initializable
public final class LoginValidationPlugin implements Plugin<Player> {
	
	/**
	 * Represents the quest point items to remove.
	 */
	private static final Item[] QUEST_ITEMS = new Item[] { new Item(9813), new Item(9814)};

	/**
	 * Constructs a new {@Code LoginValidationPlugin} {@Code Object}
	 */
	public LoginValidationPlugin() {
		/*
		 * empty.
		 */
	}

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	@Override
	public Plugin<Player> newInstance(final Player player) throws Throwable {
		player.unlock();
		if (player.isArtificial()) {
			return this;
		}
		if (!SystemManager.getSystemConfig().validLogin(player)) {
			return this;
		}
		if (GameWorld.getSettings().isDevMode()) {
			player.toggleDebug();
		}
		if (player.getAttribute("fc_wave", -1) > -1) {
			ActivityManager.start(player, "fight caves", true);
		}
		if (player.getAttribute("falconry", false)) {
			ActivityManager.start(player, "falconry", true);
		}
		if (player.getSavedData().getQuestData().getDragonSlayerAttribute("repaired")) {
			player.getConfigManager().set(177, 1967876);
		}
		if (player.getSavedData().getGlobalData().getLootShareDelay() < System.currentTimeMillis() && player.getSavedData().getGlobalData().getLootShareDelay() != 0L) {
			player.getGlobalData().setLootSharePoints((int) (player.getGlobalData().getLootSharePoints() - player.getGlobalData().getLootSharePoints() * 0.10));
		} else {
			player.getSavedData().getGlobalData().setLootShareDelay(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1));
		}
		checkQuestPointsItems(player);
		return this;
	}

	/**
	 * Method used to check for the quest point cape items.
	 * @param player the player.
	 */
	private static void checkQuestPointsItems(final Player player) {
		if (!player.getQuestRepository().hasCompletedAll() &&
			(player.getEquipment().contains(9813, 1) || player.getEquipment().contains(9814, 1))
		) {
			for (Item i : QUEST_ITEMS) {
				if (player.getEquipment().remove(i)) {
					player.getDialogueInterpreter().sendItemMessage(i, "As you no longer have completed all the quests, your " + i.getName() + " unequips itself to your " + (player.getInventory().freeSlots() < 1 ? "bank" : "inventory") + "!");
					if (player.getInventory().freeSlots() < 1) {
						player.getBank().add(i);
					} else {
						player.getInventory().add(i);
					}
				}
			}
		}
	}
}
