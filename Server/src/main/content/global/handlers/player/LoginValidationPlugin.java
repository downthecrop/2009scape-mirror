package content.global.handlers.player;

import core.api.Container;
import core.game.activity.ActivityManager;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.system.SystemManager;
import core.game.world.GameWorld;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.plugin.PluginManifest;
import core.plugin.PluginType;
import org.rs09.consts.Items;

import java.util.concurrent.TimeUnit;

import static core.api.ContentAPIKt.*;

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
	private static final Item[] QUEST_ITEMS = new Item[] { new Item(Items.QUEST_POINT_CAPE_9813), new Item(Items.QUEST_POINT_HOOD_9814)};

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
                        setVarp(player, 177, 1967876); //lol?
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
		if (!player.getQuestRepository().hasCompletedAll() && anyInEquipment(player, Items.QUEST_POINT_CAPE_9813, Items.QUEST_POINT_HOOD_9814)) {
			String location1 = null;
			String location2 = null;
			int item1 = 0;
			int item2 = 0;
			int amt = 0;
			for (Item i : QUEST_ITEMS) {
				if (removeItem(player, i, Container.EQUIPMENT)) {
					amt++;
					String location;
					if (addItem(player, i.getId(), i.getAmount(), Container.INVENTORY)) {
						location = "your inventory";
					} else if (addItem(player, i.getId(), i.getAmount(), Container.BANK)) {
						location = "your bank";
					} else {
						location = "the Wise Old Man";
						if (i.getId() == Items.QUEST_POINT_CAPE_9813) {
							setAttribute(player, "/save:reclaim-qp-cape", true);
						} else {
							setAttribute(player, "/save:reclaim-qp-hood", true);
						}
					}
					if (amt == 1) {
						item1 = i.getId();
						location1 = location;
					}
					if (amt == 2) {
						item2 = i.getId();
						location2 = location;
					}
				}
			}
			if (amt == 2) {
				sendDoubleItemDialogue(player, item1, item2, "As you no longer have completed all the quests, your " + getItemName(item1) + " unequips itself to " + location1 + " and your " + getItemName(item2) + " unequips itself to " + location2 + "!");
			} else {
				sendItemDialogue(player, item1, "As you no longer have completed all the quests, your " + getItemName(item1) + " unequips itself to " + location1 + "!");
			}
		}
	}
}