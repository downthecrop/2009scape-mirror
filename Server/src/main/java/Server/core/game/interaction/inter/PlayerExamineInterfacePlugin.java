package core.game.interaction.inter;

import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.StringUtils;

/**
 * Package -> core.game.interaction.inter
 * Created on -> 9/6/2016 @8:12 PM for 530
 *
 * @author Ethan Kyle Millard
 */
@Initializable
public class PlayerExamineInterfacePlugin extends ComponentPlugin {

    private Player node;

    public PlayerExamineInterfacePlugin() {

    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(31, this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        switch (button) {
            case 2:
                component.close(player);
                break;
        }
        return true;
    }

    public void prepareInterface(Player player, Player examined) {
        Component component = new Component(31);
        player.getPacketDispatch().sendInterfaceConfig(component.getId(), 7, true);
        player.getPacketDispatch().sendInterfaceConfig(component.getId(), 8, true);
        player.getPacketDispatch().sendInterfaceConfig(component.getId(), 15, true);
        player.getPacketDispatch().sendInterfaceConfig(component.getId(), 18, true);
        player.getPacketDispatch().sendInterfaceConfig(component.getId(), 2, true);
        player.getPacketDispatch().sendInterfaceConfig(component.getId(), 14, true);
        player.getPacketDispatch().sendInterfaceConfig(component.getId(), 13, true);
        player.getInterfaceManager().open(component);
        player.getPacketDispatch().sendString("<col=" + examined.getSavedData().getSpawnData().getTitle().getTitleColor() + ">" + examined.getSavedData().getSpawnData().getTitle().getName() + "</col> " + StringUtils.formatDisplayName(examined.getName()) + " - Combat Level: " + player.getProperties().getCurrentCombatLevel(), 31, 3);
        player.getPacketDispatch().sendString("Total Level -> ", 31, 9);
        player.getPacketDispatch().sendString("Inventory items -> ", 31, 10);
        player.getPacketDispatch().sendString("Inventory value -> ", 31, 16);
        player.getPacketDispatch().sendString("Custom state-> ", 31, 19);
        player.getPacketDispatch().sendString("" + examined.getSkills().getTotalLevel(), 31, 11);
        player.getPacketDispatch().sendString("" + examined.getInventory().itemCount(), 31, 12);
        player.getPacketDispatch().sendString("" + examined.getInventory().getWealth(), 31, 17);
        player.getPacketDispatch().sendString("" + examined.getCustomState(), 31, 20);
        player.getPacketDispatch().sendString("Close", 31, 5);

        for (int i = 0; i < 11; i++) {
            Item item = examined.getEquipment().get(i);
            if (item == null) {
                continue;
            }
            System.out.println(item.getName());
        }

    }
}
