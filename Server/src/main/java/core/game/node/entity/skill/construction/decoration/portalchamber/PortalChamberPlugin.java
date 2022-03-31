package core.game.node.entity.skill.construction.decoration.portalchamber;


import core.cache.def.impl.SceneryDefinition;
import core.game.content.dialogue.DialogueInterpreter;
import core.game.content.dialogue.DialoguePlugin;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.construction.Decoration;
import core.game.node.entity.skill.runecrafting.Rune;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;
import rs09.game.node.entity.skill.construction.Hotspot;
import rs09.plugin.ClassScanner;

/**
 * PortalChamberPlugin
 * @author Clayton Williams
 * @date Jan 2, 2016
 */
@Initializable
public class PortalChamberPlugin extends OptionHandler {
	
	/**
	 * Locations
	 * @author Clayton Williams
	 * @date Jan 3, 2016
	 */
	private static enum Locations {
		
		VARROCK(Location.create(3213, 3428, 0), new Item(Rune.FIRE.getRune().getId(), 100), new Item(Rune.AIR.getRune().getId(), 300), new Item(Rune.LAW.getRune().getId(), 100)),
		LUMBRIDGE(Location.create(3222, 3217, 0), new Item(Rune.EARTH.getRune().getId(), 100), new Item(Rune.AIR.getRune().getId(), 300), new Item(Rune.LAW.getRune().getId(), 100)),
		FALADOR(Location.create(2965, 3380, 0), new Item(Rune.WATER.getRune().getId(), 100), new Item(Rune.AIR.getRune().getId(), 300), new Item(Rune.LAW.getRune().getId(), 100)),
		CAMELOT(Location.create(2730, 3485, 0), new Item(Rune.AIR.getRune().getId(), 500), new Item(Rune.LAW.getRune().getId(), 100)),
		ARDOUGNE(Location.create(2663, 3305, 0), new Item(Rune.WATER.getRune().getId(), 200), new Item(Rune.LAW.getRune().getId(), 200)),
		YANILLE(Location.create(2554, 3114, 0), new Item(Rune.EARTH.getRune().getId(), 200), new Item(Rune.LAW.getRune().getId(), 200)),
		KHARYRLL(Location.create(3493, 3474, 0), new Item(Rune.BLOOD.getRune().getId(), 100), new Item(Rune.LAW.getRune().getId(), 200));

		/**
		 * The location to teleport to
		 */
		private Location location;
		
		/**
		 * The rune requirements
		 */
		private Item[] runes;
		
		/**
		 * Locations
		 * @param location
		 * @param runes
		 */
		Locations(Location location, Item... runes) {
			this.location = location;
			this.runes = runes;
		}
	}
	
	/**
	 * Directs a portal
	 * @param player
	 * @param identifier
	 */
	public static void direct(Player player, String identifier) {
		player.getInterfaceManager().closeSingleTab();
		int dpId = player.getAttribute("con:dp-id", 1);	
		Hotspot[] hotspots = player.getHouseManager().getRoom(player.getLocation()).getHotspots();
		for (int i = 0; i < hotspots.length; i++) {
			Hotspot h = hotspots[i];
			if (h.getHotspot().name().equalsIgnoreCase("PORTAL" + dpId)) {
				if (h.getDecorationIndex() == -1) {
					player.sendMessage("You must build a portal frame first!");
					return;
				}
				Decoration previous = h.getHotspot().getDecorations()[h.getDecorationIndex()];
				String name = previous.name();
				String prefix = "TEAK";
				if (name.toLowerCase().contains("mahogany")) {
					prefix = "MAHOGANY";
				} else if (name.toLowerCase().contains("marble")) {
					prefix = "MARBLE";
				}
				for (Locations l : Locations.values()) {
					if (l.name().contains(identifier)) {
						Item[] runes = l.runes;
						if (!player.getInventory().containsItems(runes)) {
							player.sendMessage("You do not have the required runes to build this portal");
							return;
						}	
						player.getInventory().remove(runes);
						break;
					}
				}
				h.setDecorationIndex(h.getHotspot().getDecorationIndex(Decoration.forName(prefix + "_" + identifier + "_" + "PORTAL")));
				player.getHouseManager().reload(player, player.getHouseManager().isBuildingMode()); //TODO replace object live instead?
			}
		}	
	}
	

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(13639).getHandlers().put("option:direct-portal", this);
		SceneryDefinition.forId(13639).getHandlers().put("option:scry", this);
		SceneryDefinition.forId(13640).getHandlers().put("option:direct-portal", this);
		SceneryDefinition.forId(13641).getHandlers().put("option:direct-portal", this);
		for (int i = 13615; i <= 13635; i++) {
			SceneryDefinition.forId(i).getHandlers().put("option:enter", this);
		}
		ClassScanner.definePlugin(new DirectPortalDialogue());
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		Scenery object = node.asScenery();
		switch (option) {
			case "direct-portal":
				if (!player.getHouseManager().isBuildingMode()) {
					player.sendMessage("You can currently only do this in building mode.");
					return true;
				}
				player.getDialogueInterpreter().open(DialogueInterpreter.getDialogueKey("con:directportal"));
				return true;
			case "enter":
				String objectName = object.getName();
				for (Locations l : Locations.values()) {
					if (objectName.toLowerCase().contains(l.name().toLowerCase())) {
						player.teleport(l.location);
						if (player.getHouseManager().isInHouse(player) && node.getId() == 13635) {
							//player.getAchievementDiaryManager().getDiary(DiaryType.MORYTANIA).updateTask(player, 2, 0, true);
						}
						break;
					}
				}
				return true;
			case "scry":
				//wtf
				return true;
		}
		return false;
	}
	
	/**
	 * DirectPortalDialogue.java
	 * @author Clayton Williams
	 * @date Jan 2, 2016
	 */
	private static final class DirectPortalDialogue extends DialoguePlugin {
		
		/**
		 * DirectPortalDialogue
		 */
		public DirectPortalDialogue() {
			/**
			 * Empty
			 */
		}

		/**
		 * DirectPortalDialogue
		 * @param player
		 */
		public DirectPortalDialogue(Player player) {
			super(player);
		}

		@Override
		public DialoguePlugin newInstance(Player player) {
			return new DirectPortalDialogue(player);
		}

		@Override
		public boolean open(Object... args) {
			interpreter.sendOptions("Select a portal", "Portal 1", "Portal 2", "Portal 3");
			stage = 0;
			return true;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			player.setAttribute("con:dp-id", buttonId);
			end();
			player.getDialogueInterpreter().open(394857);
			return true;
		}

		@Override
		public int[] getIds() {
			return new int[]{DialogueInterpreter.getDialogueKey("con:directportal")};
		}
		
	}
	
	/**
	 * The direct options build
	 */
	/*private final static TabbedOption DIRECT_OPTIONS = new TabbedOption("Direct Portal", "Select an option",
		new Page( 
			new PageAction("Varrock Portal") {
				@Override
				public boolean run(Player player) {
					PortalChamberPlugin.direct(player, "VARROCK");
					return true;
				}
			},
			new PageAction("Lumbridge Portal") {
				@Override
				public boolean run(Player player) {
					PortalChamberPlugin.direct(player, "LUMBRIDGE");
					return true;
				}
			}, 
			new PageAction("Falador Portal") {
				@Override
				public boolean run(Player player) {
					PortalChamberPlugin.direct(player, "FALADOR");
					return true;
				}
			},
			new PageAction("Camelot Portal") {
				@Override
				public boolean run(Player player) {
					PortalChamberPlugin.direct(player, "CAMELOT");
					return true;
				}
			},
			new PageAction("Ardougne Portal") {
				@Override
				public boolean run(Player player) {
					PortalChamberPlugin.direct(player, "ARDOUGNE");
					return true;
				}
			}
		), new Page(
			new PageAction("Yanille Portal") {
				@Override
				public boolean run(Player player) {
					PortalChamberPlugin.direct(player, "YANILLE");
					return true;
				}
			},
			new PageAction("Kharyrll Portal") {
				@Override
				public boolean run(Player player) {
					PortalChamberPlugin.direct(player, "KHARYRLL");
					return true;
				}
			}
		)
	);*/

}
