package core.game.content.global.travel;

import core.game.component.Component;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.TeleportManager.TeleportType;
import core.game.world.map.Location;

/**
 * Handles the Minigame Group Finder teleport interface.
 * 
 * @author Torchic
 */
public class MinigameGroupFinder {
	
	/**
	 * Opens the minigame finder for the player.
	 * @param player The player.
	 */
	public static void openFinder(Player player) {
		player.setAttribute("minigameFinder:open", true);
		player.getInterfaceManager().openTab(2, new Component(259));
		player.getPacketDispatch().sendString("Minigame Group Finder", 259, 9);
		player.getPacketDispatch().sendString("Destinations", 259, 1);
		player.getPacketDispatch().sendString("     Shilo Village", 259, 2);
		player.getPacketDispatch().sendString("Warrior's Guild", 259, 3);
		player.getPacketDispatch().sendString("Dagganoth Kings", 259, 4);
		player.getPacketDispatch().sendString("God Wars Dungeon", 259, 5);
		player.getPacketDispatch().sendString("Minigames", 259, 6);	
		player.getPacketDispatch().sendString("     Barbarian Assault", 259, 23);
		player.getPacketDispatch().sendString("Blast Furnace", 259, 24);
		player.getPacketDispatch().sendString("Burthorpe Games Room", 259, 25);	
		player.getPacketDispatch().sendString("Castle Wars", 259, 26);	
		player.getPacketDispatch().sendString("     Clan Wars", 259, 19);
		player.getPacketDispatch().sendString("Duel Arena", 259, 20);	
		player.getPacketDispatch().sendString("Fishing Trawler", 259, 21);
		player.getPacketDispatch().sendString("Fist of Guthix", 259, 22);
		player.getPacketDispatch().sendString("Nightmare Zone", 259, 12);
		player.getPacketDispatch().sendString("     Pest Control", 259, 11);		
		player.getPacketDispatch().sendString("Pyramid Plunder", 259, 13);
		player.getPacketDispatch().sendString("Shades of Mort'ton", 259, 14);
		player.getPacketDispatch().sendString("Stealing Creation", 259, 28);
		player.getPacketDispatch().sendString("     Trouble Brewing", 259, 27);
		player.getPacketDispatch().sendString("TzHaar Fight Pit", 259, 29);
		player.getPacketDispatch().sendString("", 259, 30);
		player.getPacketDispatch().sendString("", 259, 16);
		player.getPacketDispatch().sendString("", 259, 15);
		player.getPacketDispatch().sendString("", 259, 17);
		player.getPacketDispatch().sendString("", 259, 18);
		for (int i = 15; i < 19; i ++) {
			player.getPacketDispatch().sendString("", 259, i);
		}
	}

	/**
	 * Holds all the optiosn for the minigame group finder.
	 * @author Torchic
	 *
	 */
	public enum MinigameOptions {

		/**
		 * Destinations
		 */
		SHILO_VILLAGE(2, Location.create(2876, 2954, 0)),
		WARRIORS_GUILD(3, Location.create(2878, 3546, 0)),
		DAGGANOTH_KINGS(4, Location.create(2524, 3739, 0)),
		GOD_WARS_DUNGEON(5, Location.create(2898, 3712, 0)),
		
		/**
		 * Minigames
		 */
		BARBARIAN_ASSAULT(23, Location.create(2520, 3570, 0)),
		BLAST_FURNACE(24, Location.create(1950, 4968, 0)),
		BURTHORPE_GAMES_ROOM(25, Location.create(2899, 3563, 0)),
		CASTLE_WARS(26, Location.create(2442, 3089, 0)),
		CLAN_WARS(19, Location.create(3273, 3686, 0)),
		DUEL_ARENA(20, Location.create(3365, 3265, 0)),
		FISHING_TRAWLER(21, Location.create(2658, 3158, 0)),
		FIST_OF_GUTHIX(22, Location.create(1675, 5599, 0)),
		NIGHTMARE_ZONE(12, Location.create(3184, 3945, 0)),
		PEST_CONTROL(11, Location.create(2659, 2676, 0)),
		PYRAMID_PLUNDER(13, Location.create(3289, 2787, 0)),
		SHADES_OF_MORTTON(14, Location.create(3504, 3284, 0)),
		TROUBLE_BREWING(27, Location.create(3222, 3222, 0)),
		STEALING_CREATION(28, Location.create(3135, 3627, 0)),
		FIGHT_PITS(29, Location.create(2400, 5178, 0));

		/**
		 * The option (button) id of the teleport.
		 */
		private int optionId;
		
		/**
		 * The location to send the player.
		 */
		private Location tile;

		/**
		 * Constructs a new {@Code MinigameOptions} {@Code Object}
		 * @param optionId The option id.
		 * @param tile The location tile.
		 */
		MinigameOptions(int optionId, Location tile) {
			this.optionId = optionId;
			this.tile = tile;
		}

		/**
		 * Gets the location.
		 * @return
		 */
		public Location getLocation() {
			return tile;

		}

		/**
		 * Gets the telport option id.
		 * @return
		 */
		public int getTeleportId() {
			return optionId;
		}

		/**
		 * Iterates through the options to find the correct teleport based on option id.
		 * @param optionId The option id.
		 * @return
		 */
		public static MinigameOptions getTeleportOptions(int optionId) {
			for (MinigameOptions tele : MinigameOptions.values()) {
				if (tele.getTeleportId() == optionId) {
					return tele;
				}
			}
			return null;
		}
	}

	/**
	 * Teleports the player to the location based on the componentId
	 * 
	 * @param player
	 * @param buttonId
	 * @return true if teleport exists
	 */
	public static boolean teleport(final Player player, int buttonId) {
		MinigameOptions option = MinigameOptions.getTeleportOptions(buttonId);
		if (option == null || option.getLocation() == new Location(3222, 3222, 0)) {
			player.sendMessage("Location has not been added!");
			return false;
		}
		player.getTeleporter().send(option.getLocation(), TeleportType.NORMAL, 1);
		player.removeAttribute("minigameFinder:open");
		player.getInterfaceManager().openTab(2, new Component(274));
		player.getPacketDispatch().sendString("Minigame Group Finder", 274, 5);
		return true;			
	}
}