package core.game.node.entity.skill.construction;


import core.cache.def.impl.ItemDefinition;
import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.scenery.Scenery;
import core.plugin.Initializable;
import core.plugin.Plugin;
import rs09.game.node.entity.skill.construction.Hotspot;
import rs09.game.system.SystemLogger;

/**
 * Handles the creating of a decoration object.
 * @author Emperor
 *
 */
@Initializable
public final class ConstructionInterface extends ComponentPlugin {

	@Override
	public Plugin<Object> newInstance(Object arg) {
		ComponentDefinition.put(396, this);
		ComponentDefinition.put(398, this);
		ComponentDefinition.put(402, this);
		return this;
	}

	@Override
	public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
		switch (component.getId()) {
			case 396:
				switch (button) {
					case 132:
						player.getInterfaceManager().close();
						Hotspot hotspot = player.getAttribute("con:hotspot");
						Scenery object = player.getAttribute("con:hsobject");
						if (hotspot == null || object == null) {
							SystemLogger.logErr("Failed building decoration " + hotspot + " : " + object);
							break;
						}
						slot = ((slot % 2 != 0) ? 4 : 0) + (slot >> 1);
						if (slot >= hotspot.getHotspot().getDecorations().length) {
							SystemLogger.logErr("Failed building decoration " + slot + "/" + hotspot.getHotspot().getDecorations().length);
							break;
						}
						boolean debug = player.isStaff();
						Decoration deco = hotspot.getHotspot().getDecorations()[slot];
						if (!debug) {
							if (player.getSkills().getLevel(Skills.CONSTRUCTION) < deco.getLevel()) {
								player.sendMessage("You need to have a Construction level of " + deco.getLevel() + " to build that.");
								return true;
							}
							if (!player.getInventory().containsItems(deco.getItems())) {
								player.sendMessage("You don't have the right materials.");
								return true;
							}
							for (int tool : deco.getTools()) {
								if (tool == BuildingUtils.WATERING_CAN) {
									boolean hasWateringCan = false;
									for (int i = 0; i < 8; i++) {
										if (player.getInventory().contains(tool - i, 1)) {
											hasWateringCan = true;
											break;
										}
									}
									if (!hasWateringCan) {
										player.sendMessage("You need a watering can to plant this.");
										return true;
									}
									continue;
								}
								if (!player.getInventory().contains(tool, 1)) {
									player.sendMessage("You need a " + ItemDefinition.forId(tool).getName() + " to build this.");
									return true;
								}
							}
						}
						BuildingUtils.buildDecoration(player, hotspot, deco, object);
						return true;
				}
				break;
			case 398:
				switch (button) {
					case 14:
						player.getHouseManager().toggleBuildingMode(player, true);
						return true;
					case 1:
						player.getHouseManager().toggleBuildingMode(player, false);
						return true;
					case 15:
						player.getHouseManager().expelGuests(player);
						return true;
					case 13:
						if (!player.getHouseManager().isInHouse(player)) {
							player.sendMessage("You can't do this outside of your house.");
							return true;
						}
						HouseManager.leave(player);
						return true;
				}
				break;
			case 402:
				int index = button - 160;
				SystemLogger.logErr("BuildRoom Interface Index: " + index);
				if (index > -1 && index < RoomProperties.values().length) {
					player.getDialogueInterpreter().open("con:room", RoomProperties.values()[index]);
					return true;
				}
				break;
		}
		return false;
	}

}