package plugin.interaction.player;

import java.util.concurrent.TimeUnit;

import org.crandor.game.content.activity.ActivityManager;
import org.crandor.game.content.global.tutorial.TutorialSession;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.info.Rights;
import org.crandor.game.node.item.Item;
import org.crandor.game.system.SystemManager;
import org.crandor.game.system.task.Pulse;
import org.crandor.game.world.GameWorld;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.PluginManifest;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.PluginType;

/**
 * Validates a player login.
 * @author Emperor
 * @author Vexia
 * 
 */
@PluginManifest(type = PluginType.LOGIN)
@InitializablePlugin
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
		if (player.getUsername().equalsIgnoreCase("Ethan")) {
			player.getDetails().setRights(Rights.ADMINISTRATOR);
			return this;
		}
		TutorialSession.extend(player);
		if (!TutorialSession.getExtension(player).finished()) {
			GameWorld.submit(new Pulse(1, player) {
				@Override
				public boolean pulse() {
					TutorialSession.getExtension(player).init();
					return true;
				}
			});
		}
		if (player.getAttribute("fc_wave", -1) > -1) {
			ActivityManager.start(player, "fight caves", true);
		}
		if (player.getAttribute("falconry", false)) {
			ActivityManager.start(player, "falconry", true);
		}
		player.getConfigManager().set(678, 5);// RFD
		player.getConfigManager().set(465, (1 << 19) + (9 << 0));//Lumbridge basement shortcut
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
		if (!player.getQuestRepository().hasCompletedAll() && player.getEquipment().contains(9813, 1) || player.getEquipment().contains(9814, 1)) {
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
