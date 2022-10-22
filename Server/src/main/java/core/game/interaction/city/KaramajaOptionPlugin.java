package core.game.interaction.city;

import core.cache.def.impl.NPCDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.game.component.Component;
import core.plugin.Initializable;
import org.rs09.consts.Items;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.game.content.dialogue.FacialExpression;
import core.game.content.global.action.ClimbActionHandler;
import core.game.content.global.action.DoorActionHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.world.map.Location;
import rs09.game.world.repository.Repository;
import core.plugin.Plugin;

/**
 * Represents the option plugin used to handle karamaja options.
 *
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class KaramajaOptionPlugin extends OptionHandler {

    /**
     * Represents the pineapple item.
     */
    private static final Item PINEAPPLE = new Item(2114);

    /**
     * Represents the pine apple objects.
     */
    private static final int[] PINEAPPLE_OBJECTS = new int[]{ 1408, 1409, 1410, 1411, 1412, 1413 };

    /**
     * Represents the musa point dungeon.
     */
    private static final Location MUSA_POINT_DUNGEON = Location.create(2856, 9567, 0);

    /**
     * Represents the volacno rim location.
     */
    private static final Location VOLCANO_RIM = new Location(2856, 3167, 0);

    /**
     * Represents the agility brimhaven location.
     */
    private static final Location AGILITY_LOCATION = new Location(2805, 9589, 3);

    /**
     * Represents the main base asgility location (ground floor)
     */
    private static final Location AGILITY_MAIN = new Location(2809, 3193, 0);

    /**
     * Represents the component.
     */
    private static final Component TICKET_EXCHANGE = new Component(6);

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        SceneryDefinition.forId(2078).getHandlers().put("option:search", this);// random crate.
        SceneryDefinition.forId(492).getHandlers().put("option:climb-down", this);// musa point rock
        SceneryDefinition.forId(1764).getHandlers().put("option:climb", this);// musa dungeon rope
        SceneryDefinition.forId(3617).getHandlers().put("option:climb-down", this);// agility ladder
        SceneryDefinition.forId(3618).getHandlers().put("option:climb-up", this);// agility ladder
        NPCDefinition.forId(437).getHandlers().put("option:pay", this);// capn izzy
        NPCDefinition.forId(1055).getHandlers().put("option:trade", this);// capn izzy trader (tickets)
        SceneryDefinition.forId(2626).getHandlers().put("option:open", this);// grubors locked door
        SceneryDefinition.forId(2628).getHandlers().put("option:open", this);// the shrimp and parrot door (chef)
        SceneryDefinition.forId(2627).getHandlers().put("option:open", this);// garv door
        SceneryDefinition.forId(1591).getHandlers().put("option:open", this);// garv door
        NPCDefinition.forId(1178).getHandlers().put("option:fish", this);// lubufu fishing spot
        SceneryDefinition.forId(5083).getHandlers().put("option:enter", this);// sanibock dungeon entrance
        SceneryDefinition.forId(2439).getHandlers().put("option:open", this);
        for (int pineapple : PINEAPPLE_OBJECTS) {
            SceneryDefinition.forId(pineapple).getHandlers().put("option:pick", this);// pineapple picking
        }
        SceneryDefinition.forId(2975).getHandlers().put("option:shake", this); // leafy palm tree
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        switch (option) {
            case "pick":// pineapples.
                if (player.getInventory().freeSlots() == 0) {
                    player.getPacketDispatch().sendMessage("Not enough inventory space.");
                    return true;
                }
                final Scenery pineApple = ((Scenery) node);
                if (pineApple.getId() == 1413) {
                    player.getPacketDispatch().sendMessage("There are no pineapples left on this plant.");
                    return true;
                }
                boolean last = pineApple.getId() == 14012;
                if (player.getInventory().add(PINEAPPLE)) {
                    SceneryBuilder.replace(pineApple, pineApple.transform(pineApple.getId() + 1), last ? 270 : 40);
                    player.getPacketDispatch().sendMessage("You pick a pineapple.");
                }
                break;
            case "enter":
                switch (((Scenery) node).getId()) {
                    case 5083:
                        if (player.getAttribute("saniboch:paid", false) || player.getAchievementDiaryManager().getDiary(DiaryType.KARAMJA).isComplete()) {
                            ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_UP, Location.create(2713, 9564, 0));
                            player.removeAttribute("saniboch:paid");
                            break;
                        }
                        player.getDialogueInterpreter().sendDialogues(1595, null, "You can't go in there without paying!");
                        break;
                }
                break;
            case "trade":
                switch (((NPC) node).getId()) {
                    case 1055:
                        player.getInterfaceManager().open(TICKET_EXCHANGE);
                        break;
                }
                break;
            case "fish":
                player.getDialogueInterpreter().sendDialogues(1171, FacialExpression.FURIOUS, "Keep off my fishing spot, whippersnapper!");
                break;
            case "pay":
                if (!player.getAttribute("capn_izzy", false)) {
                    if (player.getInventory().contains(995, 200) && player.getInventory().remove(new Item(995, 200))) {
                        player.getDialogueInterpreter().sendItemMessage(new Item(995, 200), "You give Cap'n Izzy the 200 coin entrance fee.");
                        player.getPacketDispatch().sendMessage("You give Cap'n Izzy the 200 coin entrance fee.");
                        player.setAttribute("/save:capn_izzy", true);
                        return true;
                    } else {
                        player.getPacketDispatch().sendMessage("You don't have the 200 coin entrance fee.");
                    }
                } else {
                    player.getDialogueInterpreter().sendDialogues(437, null, "Avast there, ye've already paid!");
                }
                break;
            case "pay-fare":
                switch (((Scenery) node).getId()) {
                    case 2230:
                        player.getPacketDispatch().sendMessage("This cart appears to have been out of reward for some time.");
                        break;
                }
                break;
            case "climb-up":
                switch (((Scenery) node).getId()) {
                    case 3618:
                        player.getProperties().setTeleportLocation(AGILITY_MAIN);
                        break;
                }
                break;
            case "climb-down":
                switch (((Scenery) node).getId()) {
                    case 492:
                        player.getPacketDispatch().sendMessage("You climb down through the pot hole.");
                        player.getProperties().setTeleportLocation(MUSA_POINT_DUNGEON);
                        break;
                    case 3617:
                        if (!player.getAttribute("capn_izzy", false)) {
                            player.getDialogueInterpreter().open(437, Repository.findNPC(437), true);
                            return true;
                        }
                        player.removeAttribute("capn_izzy");
                        player.getProperties().setTeleportLocation(AGILITY_LOCATION);
                        break;
                }
                break;
            case "climb":
                switch (((Scenery) node).getId()) {
                    case 1764:
                        player.getProperties().setTeleportLocation(VOLCANO_RIM);
                        player.getPacketDispatch().sendMessage("You climb up the hanging rope...");
                        player.getPacketDispatch().sendMessage("You appear on the volcano rim.");
                        break;
                }
                break;
            case "open":
                switch (((Scenery) node).getId()) {
                    case 2626:
                        player.getDialogueInterpreter().open(789, Repository.findNPC(789));
                        break;
                    case 2628:
                        player.getPacketDispatch().sendMessage("The door is securely closed.");
                        break;
                    case 2627:
                        player.getDialogueInterpreter().open(788, Repository.findNPC(788), true);
                        break;
                    case 1591:
                        player.getPacketDispatch().sendMessage("You try and open the door...");
                        player.getPacketDispatch().sendMessage("The door is locked tight, I can't open it.");
                        break;
                    case 2439:
                        player.getPacketDispatch().sendMessage("The gate doesn't open.");
                        break;
                }
                break;
            case "search":
                switch (((Scenery) node).getId()) {
                    case 2078:
                        player.getPacketDispatch().sendMessage("There are no bananas left on the tree.");
                        break;
                }
                break;
            case "shake":
                if (player.getInventory().hasSpaceFor(new Item(Items.PALM_LEAF_2339))) {
                    player.getPacketDispatch().sendMessage("You shake the tree...");
                    SceneryBuilder.replace(node.asScenery(), node.asScenery().transform(2976), 60); // 35 second cool-down
                    player.lock();
                    final Pulse palmPulse = new Pulse(2) {
                        @Override
                        public boolean pulse() {
                            player.getPacketDispatch().sendMessage("You pick up a fallen palm leaf off of the ground.");
                            player.getInventory().add(new Item(Items.PALM_LEAF_2339));
                            // Collect five palm leaves
                            if (!player.getAchievementDiaryManager().hasCompletedTask(DiaryType.KARAMJA, 2, 7)) {
                            	int palms = player.getAttribute("palms", 0);
                            	palms++;
                            	player.setAttribute("palms", palms);
                            	player.getAchievementDiaryManager().updateTask(player, DiaryType.KARAMJA, 2, 7, palms >= 5);
							}
                            player.unlock();
                            return true;
                        }
                    };
                    GameWorld.getPulser().submit(palmPulse);
                } else {
                    player.getPacketDispatch().sendMessage("You don't have enough inventory space.");
                }
                break;
        }

        return true;
    }

    @Override
    public Location getDestination(Node node, Node n) {
        if (n instanceof Scenery) {
            if (((Scenery) n).getDefinition().hasAction("open")) {
                return DoorActionHandler.getDestination((Player) node, (Scenery) n);
            }
        }
        return null;
    }

}
