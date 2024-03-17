package content.global.handlers.scenery

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import org.rs09.consts.Components
import org.rs09.consts.Scenery

class SignpostListener : InteractionListener {
    override fun defineListeners() {
        on(Scenery.SIGNPOST_18493, IntType.SCENERY, "read") { player, node ->
            if (node.asScenery().location.equals(Location(3235, 3228))) {
                setInterfaceText(player, "Head north towards Fred's farm, and the windmill.", 135, 3) // North
                setInterfaceText(player, "South to the swamps of Lumbridge.", 135, 9) // South
                setInterfaceText(player, "Cross the bridge and head east to Al Kharid or north to Varrock.", 135, 8) // East
                setInterfaceText(player, "West to the Lumbridge Castle and Draynor Village. Beware the goblins!", 135, 12) // West
                openInterface(player, Components.AIDE_COMPASS_135)
            } else if (node.asScenery().location.equals(Location(3261, 3230))) {
                setInterfaceText(player, "North to farms and Varrock.", 135, 3) // North
                setInterfaceText(player, "The River Lum lies to the south.", 135, 9) // South
                setInterfaceText(player, "East to Al Kharid - toll gate; bring some money.", 135, 8) // East
                setInterfaceText(player, "West to Lumbridge.", 135, 12) // West
                openInterface(player, Components.AIDE_COMPASS_135)
            } else if (node.asScenery().location.equals(Location(2983, 3278))) {
                setInterfaceText(player, "North to the glorious White Knights' city of Falador.", 135, 3) // North
                setInterfaceText(player, "South to Rimmington.", 135, 9) // South
                setInterfaceText(player, "East to Port Sarim and Draynor Village.", 135, 8) // East
                setInterfaceText(player, "West to the Crafting Guild.", 135, 12) // West
                openInterface(player, Components.AIDE_COMPASS_135)
            } else if (node.asScenery().location.equals(Location(3107, 3296))) {
                setInterfaceText(player, "North to Draynor Manor.", 135, 3) // North
                setInterfaceText(player, "South to Draynor Village and the Wizards' Tower.", 135, 9) // South
                setInterfaceText(player, "East to Lumbridge.", 135, 8) // East
                setInterfaceText(player, "West to Port Sarim.", 135, 12) // West
                openInterface(player, Components.AIDE_COMPASS_135)
            } else {
                setInterfaceText(player, "North to unknown.", 135, 3) // North
                setInterfaceText(player, "South to unknown.", 135, 9) // South
                setInterfaceText(player, "East to unknown.", 135, 8) // East
                setInterfaceText(player, "West to unknown.", 135, 12) // West
                openInterface(player, Components.AIDE_COMPASS_135)
            }
            return@on true
        }
        on(Scenery.SIGNPOST_24263, IntType.SCENERY, "read") { player, node ->
            if (node.asScenery().location.equals(Location(3268, 3332))) {
                setInterfaceText(player, "Sheep lay this way.", 135, 3) // North
                setInterfaceText(player, "South through farms to Al Kharid and Lumbridge.", 135, 9) // South
                setInterfaceText(player, "East to Al Kharid mine and follow the path north to Varrock east gate.", 135, 8) // East
                setInterfaceText(player, "West to Champion's Guild and Varrock south gate.", 135, 12) // West
                openInterface(player, Components.AIDE_COMPASS_135)
            } else if (node.asScenery().location.equals(Location(3283, 3333))) {
                setInterfaceText(player, "North to Varrock mine and Varrock east gate.", 135, 3) // North
                setInterfaceText(player, "South to large Mining area and Al Kharid.", 135, 9) // South
                setInterfaceText(player, "Follow the path east to the Dig Site.", 135, 8) // East
                setInterfaceText(player, "West to Champion's Guild and Varrock south gate.", 135, 12) // West
                openInterface(player, Components.AIDE_COMPASS_135)
            } else {
                setInterfaceText(player, "North to unknown.", 135, 3) // North
                setInterfaceText(player, "South to unknown.", 135, 9) // South
                setInterfaceText(player, "East to unknown.", 135, 8) // East
                setInterfaceText(player, "West to unknown.", 135, 12) // West
                openInterface(player, Components.AIDE_COMPASS_135)
            }
            return@on true
        }
        on(Scenery.SIGNPOST_4132, IntType.SCENERY, "read") { player, node ->
             if (node.asScenery().location.equals(Location(3166, 3286))) {
                setInterfaceText(player, "North to the windmill.", 135, 3) // North
                setInterfaceText(player, "South to a fishing pond next to Fred's farm.", 135, 9) // South
                setInterfaceText(player, "East to Lumbridge.", 135, 8) // East
                setInterfaceText(player, "West to Port Sarim and Draynor Village.", 135, 12) // West
                openInterface(player, Components.AIDE_COMPASS_135)
            } else if (node.asScenery().location.equals(Location(3285, 3430))) {
                setInterfaceText(player, "North to the Lumber Yard.", 135, 3) // North
                setInterfaceText(player, "South to Al Kharid and Lumbridge.", 135, 9) // South
                setInterfaceText(player, "East to the Dig Site.", 135, 8) // East
                setInterfaceText(player, "West to Varrock.", 135, 12) // West
                openInterface(player, Components.AIDE_COMPASS_135)
            } else if (node.asScenery().location.equals(Location(2734, 3485))) {
                setInterfaceText(player, "North to Sinclair Mansion.", 135, 3) // North
                setInterfaceText(player, "South to the Courthouse.", 135, 9) // South
                setInterfaceText(player, "East to Camelot Castle and Catherby.", 135, 8) // East
                setInterfaceText(player, "Follow the path west to Ardougne.", 135, 12) // West
                openInterface(player, Components.AIDE_COMPASS_135)
            } else if (node.asScenery().location.equals(Location(2604, 3240))) {
                 setInterfaceText(player, "North to the Ardougne City Zoo.", 135, 3) // North
                 setInterfaceText(player, "South to the Monastery.", 135, 9) // South
                 setInterfaceText(player, "East to the Tower of Life.", 135, 8) // East
                 setInterfaceText(player, "West to the Clocktower.", 135, 12) // West
                 openInterface(player, Components.AIDE_COMPASS_135)
             } else if (node.asScenery().location.equals(Location(2605, 3298))) {
                 setInterfaceText(player, "North to the Fishing Guild and Hemenster.", 135, 3) // North
                 setInterfaceText(player, "South to the Ardougne City Zoo.", 135, 9) // South
                 setInterfaceText(player, "East to Ardougne Market.", 135, 8) // East
                 setInterfaceText(player, "West to Ardougne Castle and West Ardougne.", 135, 12) // West
                 openInterface(player, Components.AIDE_COMPASS_135)
             } else if (node.asScenery().location.equals(Location(2646, 3404))) {
                 setInterfaceText(player, "North to the Ranging Guild and Seer's Village.", 135, 3) // North
                 setInterfaceText(player, "South to the Ardougne City.", 135, 9) // South
                 setInterfaceText(player, "East to the Sorcerer's Tower.", 135, 8) // East
                 setInterfaceText(player, "West to the Fishing Guild.", 135, 12) // West
                 openInterface(player, Components.AIDE_COMPASS_135)
             } else {
                setInterfaceText(player, "North to unknown.", 135, 3) // North
                setInterfaceText(player, "South to unknown.", 135, 9) // South
                setInterfaceText(player, "East to unknown.", 135, 8) // East
                setInterfaceText(player, "West to unknown.", 135, 12) // West
                openInterface(player, Components.AIDE_COMPASS_135)
            }
            return@on true
        }
        on(Scenery.SIGNPOST_4134, IntType.SCENERY, "read") { player, node ->
            if (node.asScenery().location.equals(Location(2651, 3606))) {
                setInterfaceText(player, "North to Rellekka.", 135, 3) // North
                setInterfaceText(player, "South to Seers' Village.", 135, 9) // South
                setInterfaceText(player, "East to Death Plateau.", 135, 8) // East
                setInterfaceText(player, "West to the Lighthouse.", 135, 12) // West
                openInterface(player, Components.AIDE_COMPASS_135)
            } else if (node.asScenery().location.equals(Location(3100, 3418))) {
                setInterfaceText(player, "North to Edgeville.", 135, 3) // North
                setInterfaceText(player, "South to Draynor Manor.", 135, 9) // South
                setInterfaceText(player, "East to Varrock west gate.", 135, 8) // East
                setInterfaceText(player, "West to Barbarian Village.", 135, 12) // West
                openInterface(player, Components.AIDE_COMPASS_135)
            } else {
                setInterfaceText(player, "North to unknown.", 135, 3) // North
                setInterfaceText(player, "South to unknown.", 135, 9) // South
                setInterfaceText(player, "East to unknown.", 135, 8) // East
                setInterfaceText(player, "West to unknown.", 135, 12) // West
                openInterface(player, Components.AIDE_COMPASS_135)
            }
            return@on true
        }
    }
    /**
     * Old handlers of Signpost. Not all should be opening the AIDE_COMPASS.
     *
     * 	@Override
     * 	public Plugin<Object> newInstance(Object arg) throws Throwable {
     * 		SceneryDefinition.forId(4132).getHandlers().put("option:read", this);
     * 		SceneryDefinition.forId(4133).getHandlers().put("option:read", this);
     * 		SceneryDefinition.forId(4134).getHandlers().put("option:read", this);
     * 		SceneryDefinition.forId(4135).getHandlers().put("option:read", this);
     * 		SceneryDefinition.forId(5164).getHandlers().put("option:read", this);
     * 		SceneryDefinition.forId(10090).getHandlers().put("option:read", this);
     * 		SceneryDefinition.forId(13873).getHandlers().put("option:read", this);
     * 		SceneryDefinition.forId(15522).getHandlers().put("option:read", this);
     * 		SceneryDefinition.forId(25397).getHandlers().put("option:read", this);
     * 		SceneryDefinition.forId(30039).getHandlers().put("option:read", this);
     * 		SceneryDefinition.forId(30040).getHandlers().put("option:read", this);
     * 		SceneryDefinition.forId(31296).getHandlers().put("option:read", this);
     * 		SceneryDefinition.forId(31298).getHandlers().put("option:read", this);
     * 		SceneryDefinition.forId(31299).getHandlers().put("option:read", this);
     * 		SceneryDefinition.forId(31300).getHandlers().put("option:read", this);
     * //		ObjectDefinition.forId(31301).getConfigurations().put("option:read", this);//goblin village
     * 		return this;
     * 	}
     *
     *  Was technically supposed to hide the map
     * 	@Override
     * 	public boolean handle(Player player, Node node, String option) {
     * 		PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 2));
     * 		player.getInterfaceManager().open(new Component(135)).setCloseEvent(new CloseEvent() {
     * 			@Override
     * 			public boolean close(Player player, Component c) {
     * 				PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
     * 				return true;
     * 			}
     * 		});
     * 		return true;
     * 	}
     */
}