package plugin.quest.witchs_house;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.content.global.action.DoorActionHandler;
import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.combat.ImpactHandler;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.quest.Quest;
import org.crandor.game.node.item.Item;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.PluginManager;
import org.crandor.tools.RandomFunction;

/**
 * Created for 2009Scape
 * User: Ethan Kyle Millard
 * Date: March 15, 2020
 * Time: 9:54 AM
 */
@InitializablePlugin
public class WitchsHousePlugin extends OptionHandler {

    private static final Item LEATHER_GLOVES = new Item(1059);

    @Override
    public boolean handle(Player player, Node node, String option) {
        final Quest quest = player.getQuestRepository().getQuest("Witch's House");
        final int id = node instanceof Item ? ((Item) node).getId() : node instanceof GameObject ? ((GameObject) node).getId() : ((NPC) node).getId();
        switch(id) {
            case 24692:
                int[] items = {1733, 1059, 1061, 1965, 1734};
                for (int item : items) {
                    if (!player.getInventory().containsItem(new Item(item))) {
                        switch(item) {
                            case 1733:
                                player.getInventory().add(new Item(item));
                                player.sendMessage("You find a sewing needle in the bottom of one of the boxes!");
                                return true;
                            case 1734:
                                player.getInventory().add(new Item(item));
                                player.sendMessage("You find some sewing thread in the bottom of one of the boxes!");
                                return true;
                            case 1059:
                                player.getInventory().add(new Item(item));
                                player.sendMessage("You find a pair of leather gloves in the bottom of one of the boxes!");
                                return true;
                            case 1061:
                                player.getInventory().add(new Item(item));
                                player.sendMessage("You find a pair of leather boots in the bottom of one of the boxes!");
                                return true;
                            case 1965:
                                player.getInventory().add(new Item(item));
                                player.sendMessage("You find an old cabbage in the bottom of one of the boxes!");
                                return true;

                        }
                    }
                }
                player.sendMessage("You find nothing interesting in the boxes.");
                break;
            case 2869:
                if (player.getInventory().addIfDoesntHave(new Item(2410))) {
                    player.sendMessage("You find a magnet.");
                } else {
                    player.sendMessage("You search the cupboard but find nothing interesting.");
                }
                break;
            case 2867:
                break;
            case 2861:
                DoorActionHandler.handleAutowalkDoor(player, (GameObject) node);
                break;
            case 2862:
                if (player.getAttribute("attached_magnet") != null || player.getLocation().getY() < 3466) {
                    DoorActionHandler.handleAutowalkDoor(player, (GameObject) node);
                    player.removeAttribute("attached_magnet");
                } else {
                    player.getDialogueInterpreter().sendDialogue("This door is locked.");
                }
                break;
            case 2865:
            case 2866:
                if (player.getEquipment().containsItem(LEATHER_GLOVES)) {
                    DoorActionHandler.handleAutowalkDoor(player, (GameObject) node);
                } else {
                    player.getImpactHandler().manualHit(player, RandomFunction.random(2, 3), ImpactHandler.HitsplatType.NORMAL);
                    player.getDialogueInterpreter().sendDialogue("As your bare hands touch the gate you feel a shock.");
                }
                break;
            case 24721:
                player.sendMessage("You decide to not attract the attention of the witch by playing the piano.");
                break;
        }
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        PluginManager.definePlugin(new WitchsHouseUseWithHandler());
        PluginManager.definePlugin(new MouseNPC());
        ObjectDefinition.forId(2867).getConfigurations().put("option:look-under", this);
        ObjectDefinition.forId(2861).getConfigurations().put("option:open", this);
        ObjectDefinition.forId(2865).getConfigurations().put("option:open", this);
        ObjectDefinition.forId(2866).getConfigurations().put("option:open", this);
        ObjectDefinition.forId(2862).getConfigurations().put("option:open", this);
        ObjectDefinition.forId(24724).getConfigurations().put("option:wind-up", this);
        ObjectDefinition.forId(24673).getConfigurations().put("option:walk-down", this);
        ObjectDefinition.forId(24672).getConfigurations().put("option:walk-up", this);
        ObjectDefinition.forId(24721).getConfigurations().put("option:play", this);
        ObjectDefinition.forId(24692).getConfigurations().put("option:search", this);
        ObjectDefinition.forId(2869).getConfigurations().put("option:search", this);
        return this;
    }

    public static class WitchsHouseUseWithHandler extends UseWithHandler {

        /**
         * The object id of the beehives
         */
        private static final int[] OBJECTS = new int[] { 15518 };

        /**
         * Constructs a new {@Code MerlinCrystalItemHandler} {@Code
         *  Object}
         */
        public WitchsHouseUseWithHandler() {
            super(1985);
        }

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            for (int id : OBJECTS) {
                addHandler(id, OBJECT_TYPE, this);
            }
            UseWithHandler.addHandler(901, UseWithHandler.NPC_TYPE, new UseWithHandler(2410) {
                @Override
                public Plugin<Object> newInstance(Object arg) throws Throwable {
                    addHandler(901, UseWithHandler.NPC_TYPE, this);
                    return this;
                }

                @Override
                public boolean handle(NodeUsageEvent event) {
                    Player player = event.getPlayer();
                    Item useditem = event.getUsedItem();
                    final NPC npc = (NPC) event.getUsedWith();
                    if (useditem.getId() == 2410 && npc.getId() == 901 && player.getAttribute("mouse_out") != null) {
                        player.getDialogueInterpreter().sendDialogue("You attach a magnet to the mouse's harness.");
                        player.removeAttribute("mouse_out");
                        player.setAttribute("attached_magnet", true);
                    }
                    return true;
                }
            });
            return this;
        }

        @Override
        public boolean handle(NodeUsageEvent event) {
            Player player = event.getPlayer();
            Item useditem = event.getUsedItem();
            final GameObject object = (GameObject) event.getUsedWith();
            assert useditem != null;
            if (player.getAttribute("mouse_out") != null && useditem.getId() == 1985 && object.getId() == 15518) {
                player.getDialogueInterpreter().sendDialogue("You can't do this right now.");
            }
            if (useditem.getId() == 1985 && object.getId() == 15518 && player.getAttribute("mouse_out") == null) {
                player.getDialogueInterpreter().sendDialogue("A mouse runs out of the hole.");
                MouseNPC mouse = (MouseNPC) MouseNPC.create(901, Location.create(2903,3466,0));
                mouse.setPlayer(player);
                mouse.setRespawn(false);
                mouse.setWalks(false);
                mouse.init();
                mouse.faceLocation(Location.create(2903, 3465, 0));
                player.setAttribute("mouse_out", true);
            }

            return true;
        }
    }
}
