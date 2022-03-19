package core.game.interaction.inter;

import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.plugin.Initializable;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.combat.equipment.WeaponInterface.WeaponInterfaces;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.info.Rights;
import rs09.game.world.GameWorld;
import core.plugin.Plugin;

/**
 * Represents the component plugin used for the game interface.
 * @author Vexia
 *
 */
@Initializable
public final class GameInterface extends ComponentPlugin {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.put(740, this);
		return this;
	}

	@Override
	public boolean handle(final Player player, Component component, int opcode, int button, int slot, int itemId) {
		switch (component.getId()) {
			case 740:
				switch (button){
					case 3:
						player.getInterfaceManager().closeChatbox();
						break;
				}
				return true;
			case 746:
				switch (button){
					case 12:
						player.getPacketDispatch().sendString("When you have finished playing " + GameWorld.getSettings().getName() + ", always use the button below to logout safely. ", 182, 0);
						break;
					case 49:
						player.getPacketDispatch().sendString("Friends List - " + GameWorld.getSettings().getName() + " " + GameWorld.getSettings().getWorldId(), 550, 3);
						break;
					case 110:
						configureWorldMap(player);
						break;
				}
				return true;
			case 548:
				if (button >= 38 && button<= 44 || button >= 20 && button <= 26) {
					player.getInterfaceManager().setCurrentTabIndex(getTabIndex(button));
				}
				//Interface buttons that advance the Tutorial Island stages
				switch (button) {
					case 21://Friends Tab
						player.getPacketDispatch().sendString("Friends List -" + GameWorld.getSettings().getName() + " " + GameWorld.getSettings().getWorldId(), 550, 3);
						break;
					case 22://Ignore Tab
						break;
					case 24://Settings Tab
						break;
					case 25://Emotes Tab
						break;
					case 26://Music Tab
						break;
					case 38://Attack Tab
						if (player.getExtension(WeaponInterface.class) == WeaponInterfaces.STAFF) {
							final Component c = new Component(WeaponInterfaces.STAFF.getInterfaceId());
							player.getInterfaceManager().openTab(0, c);
							final WeaponInterface inter = player.getExtension(WeaponInterface.class);
							inter.updateInterface();
						}
						break;
					case 39://Skill Tab
						break;
					case 40://Quest Tab
				/*if (GameWorld.isEconomyWorld()) {
					player.getQuestRepository().syncronizeTab(player);
				}  else {
					player.getSavedData().getSpawnData().drawStatsTab(player);
				}*/

						player.getQuestRepository().syncronizeTab(player);
						break;
					case 41://Inventory Tab
						player.getInventory().refresh();
						break;
					case 42://Worn Equipment Tab
						break;
					case 43://Prayer Tab
						break;
					case 44://Magic Tab
						break;
					case 66://World map
					case 110:
						configureWorldMap(player);
						break;
					case 69://Logout
						player.getPacketDispatch().sendString("When you have finished playing " + GameWorld.getSettings().getName() + ", always use the button below to logout safely. ", 182, 0);
						break;
				}
				return true;
			case 750:
				switch (opcode) {
					case 155:
						switch (button) {
							case 1:
								player.getSettings().toggleRun();
								break;
						}
						break;
				}
				return true;
			case 751:
				switch (opcode) {
					case 155:
						switch (button) {
							case 27:
								openReport(player);
								break;
						}
						break;
				}
				return true;
		}
		return true;
	}

	/**
	 * World map
	 * Thanks, Snickerize!
	 */
	private void configureWorldMap(Player player) {
		if (player.inCombat()) {
			player.getPacketDispatch().sendMessage("It wouldn't be very wise opening the world map during combat.");
			return;
		}
		if(player.getLocks().isInteractionLocked() || player.getLocks().isMovementLocked()){
			player.getPacketDispatch().sendMessage("You can't do this right now.");
			return;
		}
		player.getInterfaceManager().openWindowsPane(new Component(755));
		int posHash = (player.getLocation().getZ() << 28) | (player.getLocation().getX() << 14) | player.getLocation().getY();
		player.getPacketDispatch().sendScriptConfigs(622, posHash, "", 0);
		player.getPacketDispatch().sendScriptConfigs(674, posHash, "", 0);
	}

	/**
	 * Method used to open the report interface.
	 * @param player the player.
	 */
	public static void openReport(final Player player) {
		player.getInterfaceManager().open(new Component(553)).setCloseEvent((player1, c) -> {
			player1.getPacketDispatch().sendRunScript(80, "");
			player1.getPacketDispatch().sendRunScript(137, "");
			return true;
		});
		player.getPacketDispatch().sendRunScript(508, "");
		if (player.getDetails().getRights() != Rights.REGULAR_PLAYER) {
			for (int i = 0; i < 18; i++) {
				player.getPacketDispatch().sendInterfaceConfig(553, i, false);
			}
		}
	}

	/**
	 * Gets the tab index.
	 * @param button The button id.
	 * @return The tab index.
	 */
	private static int getTabIndex(int button) {
		int tabIndex = button - 38;
		if (button < 27) {
			tabIndex = (button - 20) + 7;
		}
		return tabIndex;
	}

	/**
	 * Configures the world map for a player.
	 * @param player The player.
	 */
}
