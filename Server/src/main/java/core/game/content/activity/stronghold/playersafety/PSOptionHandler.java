package core.game.content.activity.stronghold.playersafety;

import static core.game.content.activity.stronghold.playersafety.StrongHoldOfPlayerSafetyPlugin.JAIL_DOWN_STAIRS;
import static core.game.content.activity.stronghold.playersafety.StrongHoldOfPlayerSafetyPlugin.JAIL_ENTRANCE_ID_ENTER;
import static core.game.content.activity.stronghold.playersafety.StrongHoldOfPlayerSafetyPlugin.JAIL_ENTRANCE_LEAVE;
import static core.game.content.activity.stronghold.playersafety.StrongHoldOfPlayerSafetyPlugin.JAIL_ENTRANCE_LOCATION_ENTER;
import static core.game.content.activity.stronghold.playersafety.StrongHoldOfPlayerSafetyPlugin.JAIL_ENTRANCE_LOCATION_LEAVE;
import static core.game.content.activity.stronghold.playersafety.StrongHoldOfPlayerSafetyPlugin.JAIL_STAIRS_DOWN;
import static core.game.content.activity.stronghold.playersafety.StrongHoldOfPlayerSafetyPlugin.JAIL_STAIRS_UP;
import static core.game.content.activity.stronghold.playersafety.StrongHoldOfPlayerSafetyPlugin.JAIL_UP_STAIRS;
import static core.game.content.activity.stronghold.playersafety.StrongHoldOfPlayerSafetyPlugin.TEST_PAPER_ITEM_ID;
import static core.game.content.activity.stronghold.playersafety.StrongHoldOfPlayerSafetyPlugin.forId;

import core.cache.def.impl.ItemDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.game.content.dialogue.DialogueAction;
import core.game.content.global.action.DoorActionHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.emote.Emotes;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.world.map.Location;
import core.plugin.Plugin;

import core.game.content.activity.stronghold.playersafety.StrongHoldOfPlayerSafetyPlugin.JailPlaques;

/**
 * @author Tyler Telis
 */
public class PSOptionHandler extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(JAIL_ENTRANCE_ID_ENTER).getHandlers().put("option:use", this);
		SceneryDefinition.forId(JAIL_ENTRANCE_LEAVE).getHandlers().put("option:leave", this);
		SceneryDefinition.forId(JAIL_STAIRS_UP).getHandlers().put("option:climb-up", this);
		SceneryDefinition.forId(JAIL_STAIRS_DOWN).getHandlers().put("option:climb-down", this);
		SceneryDefinition.forId(29732).getHandlers().put("option:open", this);
		SceneryDefinition.forId(29624).getHandlers().put("option:open", this);
		SceneryDefinition.forId(29728).getHandlers().put("option:enter", this);
		SceneryDefinition.forId(29735).getHandlers().put("option:pull-back", this);
		SceneryDefinition.forId(29623).getHandlers().put("option:use", this);
		SceneryDefinition.forId(29730).getHandlers().put("option:pull", this);
		SceneryDefinition.forId(29731).getHandlers().put("option:pull", this);
		SceneryDefinition.forId(29577).getHandlers().put("option:open", this);
		SceneryDefinition.forId(29578).getHandlers().put("option:search", this);
		ItemDefinition.forId(TEST_PAPER_ITEM_ID).getHandlers().put("option:take exam", this);
		SceneryDefinition.forId(29729).getHandlers().put("option:climb", this);
		for (JailPlaques plaque : JailPlaques.values()) {
			SceneryDefinition.forId(plaque.getObjectId()).getHandlers().put("option:read-plaque on", this);
		}
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		int config = player.getConfigManager().get(1203);
		boolean locked = config == (1 << 29) || config == (0 << 26);
		switch (node.getId()) {
		case 29577:// chest
			SceneryBuilder.replace(node.asScenery(), node.asScenery().transform(29578), 60);
			return true;
		case 29578:
			switch (option) {
			case "search":
				int stage = player.getSavedData().getGlobalData().getTestStage();
				if (stage == 3) {
					player.getEmoteManager().unlock(Emotes.SAFETY_FIRST);
					player.getInventory().add(new Item(995, 10000), player);
					player.getInventory().add(new Item(12629), player);
					player.getDialogueInterpreter().sendItemMessage(12629, "You open the chest to find a large pile of gold, along with a pair of safety gloves. Also in the chest is the secret of the 'Safety First' emote.");
					player.getSavedData().getGlobalData().setTestStage(4);
				} else if (stage == 4) {
					if (!player.hasItem(new Item(12629))) {
						player.getInventory().add(new Item(12629), player);
						player.getDialogueInterpreter().sendItemMessage(12629, "You find a pair of safety gloves.");
					} else {
						player.getDialogueInterpreter().sendDialogue("You already have a pair of safety gloves.");
					}
				}
				return true;
			} 
			return true;
		case 29730:// lever
		case 29731:// level down
			if (!locked) {
				player.sendMessage("You hear cogs and gears moving and the sound of heavy locks falling into place.");
				config = (1 << 29);
			} else {
				player.sendMessage("You hear the cogs and gears moving and a distant unlocking sound.");
				config += (1 << 26);
			}
			player.getConfigManager().set(1203, config, true);
			return true;
		case 29624:
			if (locked) {
				player.sendMessage("The door seems to be locked by some kind of mechanism.");
			} else {
				if (player.getLocation().getZ() == 2) {
					player.teleport(new Location(3177, 4266, 0));
				} else {
					player.teleport(new Location(3177, 4269, 2));
				}
			}
			return true;
		case 29729:
			player.teleport(new Location(3078, 3463, 0));
			return true;
		case 29623:
			player.teleport(new Location(3077, 4235, 0));
			return true;
		case 29735:
			player.getDialogueInterpreter().sendDialogue("There appears to be a tunnel behind the poster.");
			player.getDialogueInterpreter().addAction(new DialogueAction() {

				@Override
				public void handle(Player player, int buttonId) {
					player.teleport(new Location(3140, 4230, 2));
				}

			});
			return true;
		case 29732:
			if (!player.getSavedData().getGlobalData().hasReadPlaques()) {
				player.sendMessage("This door is locked.");
			} else {
				DoorActionHandler.handleAutowalkDoor(player, node.asScenery());
			}
			return true;
		case 29728:
			if (player.getSavedData().getGlobalData().getTestStage() < 3) {
				player.sendMessage("You need to complete the player safety test first.");
				return true;
			}
			player.teleport(new Location(3158, 4280, 3));
			return true;
		}
		if (node instanceof Scenery) {
			Scenery object = (Scenery) node;
			JailPlaques plaque = forId(object.getId());
			if (plaque != null) {
				plaque.read(player, object);
				return true;
			} else if (object.getId() == JAIL_ENTRANCE_ID_ENTER || object.getId() == JAIL_ENTRANCE_LEAVE) {
				player.getProperties().setTeleportLocation(object.getId() == JAIL_ENTRANCE_ID_ENTER ? JAIL_ENTRANCE_LOCATION_ENTER : JAIL_ENTRANCE_LOCATION_LEAVE);
				return true;
			} else if (object.getId() == JAIL_STAIRS_UP || object.getId() == JAIL_STAIRS_DOWN) {
				if (!player.getSavedData().getGlobalData().hasReadPlaques() && object.getId() != JAIL_STAIRS_DOWN) {
					player.getPacketDispatch().sendMessage("You need to read the jail plaques before the guard will allow you upstairs.");
					return true;
				}
				player.getProperties().setTeleportLocation(object.getId() == JAIL_STAIRS_UP ? JAIL_UP_STAIRS : JAIL_DOWN_STAIRS);
				return true;
			}
		} else if (node instanceof Item) {
			Item item = (Item) node;
			if (player.getSavedData().getGlobalData().getTestStage() >= 2) {
				player.sendMessage("You have already completed the test!");
				return true;
			}
			if (item.getId() == TEST_PAPER_ITEM_ID) {
				player.removeAttribute("s-stage");
				player.getDialogueInterpreter().open("player_safety", node.getId());
				return true;
			}
		}
		return false;
	}

}
