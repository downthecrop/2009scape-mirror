package core.game.interaction.item.toys;

import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.container.access.InterfaceContainer;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.game.content.holiday.easter.BasketofEggsEvent;
import org.rs09.consts.Items;
import rs09.game.interaction.item.ToyListeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static api.ContentAPIKt.inEquipment;
import static api.ContentAPIKt.inInventory;


/**
 * Handles diango's item reclaiming interface
 * @author ceik, for the interface, and for most of the holiday items/toys
 */
@Initializable
public class DiangoReclaimInterface extends ComponentPlugin {
    private static final int COMPONENT_ID = 468;
    public static final List<Item> ITEMS = new ArrayList<>(20);
    public static final Item[] HOLIDAY_ITEMS = {new Item(Items.YO_YO_4079), new Item(Items.REINDEER_HAT_10507), BasketofEggsEvent.RUBBER_CHICKEN,new Item(Items.ZOMBIE_HEAD_6722), new Item(6857), new Item(6856), new Item(6858), new Item(6859), new Item(6860), new Item(6861), new Item(6862), new Item(6863), new Item(9920), new Item(9921),new Item(9922), new Item(9923), new Item(9924), new Item(9925), new Item(11019), new Item(11020), new Item(11021), new Item(11022), new Item(11789), new Item(11949), new Item(12634), new Item(14076), new Item(14077), new Item(14081),new Item(14595), new Item(14602), new Item(14603), new Item(14605), new Item(14654), new Item(Items.RED_MARIONETTE_6867), new Item(Items.GREEN_MARIONETTE_6866), new Item(Items.BLUE_MARIONETTE_6865)};

    //initialize the plugin, add lists of items to the ITEMS list...
    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(COMPONENT_ID,this);
        ITEMS.addAll(Arrays.asList(HOLIDAY_ITEMS));
        return this;
    }

    public static void open(Player player){
        //close any currently open interfaces
        Component curOpen = player.getInterfaceManager().getOpened();
        if(curOpen != null){
            curOpen.close(player);
        }

        //filter out items the player already has in their bank, inventory, or equipped
        Item[] reclaimables = ITEMS.stream().filter(Objects::nonNull)
                .filter(item -> !player.getEquipment().containsItem(item) && !player.getInventory().containsItem(item) && !player.getBank().containsItem(item)
                        && (item.getId() != 14654
                        || (!(inInventory(player, 14655, 1) || inEquipment(player, 14656, 1)) && player.getAttribute("sotr:purchased",false))
                        ))
                .toArray(Item[]::new);

        //only send items if there are some to send
        if(reclaimables.length > 0) {
            InterfaceContainer.generateItems(player, reclaimables, new String[]{"Examine", "Take"}, 468, 2, 8, 8);
        }
        //open the interface
        player.getInterfaceManager().open(new Component(468));
    }

    //refresh the interface
    public static void refresh(Player player){
        player.getInterfaceManager().close();
        open(player);
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        Item[] reclaimables = ITEMS.stream().filter(Objects::nonNull).filter(item -> !player.getEquipment().containsItem(item) && !player.getInventory().containsItem(item) && !player.getBank().containsItem(item)).toArray(Item[]::new);
        switch(opcode){
            case 155: //interface item option 1 == take
                //add the clicked item to the player's inventory and refresh the interface
                player.getInventory().add(reclaimables[slot]);
                refresh(player);
                break;
            case 196: //interface item option 2 == examine
                //send the examine text for the item to the player
                player.getPacketDispatch().sendMessage(reclaimables[slot].getDefinition().getExamine());
                break;
        }
        return false;
    }
}
