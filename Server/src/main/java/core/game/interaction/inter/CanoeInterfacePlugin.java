package core.game.interaction.inter;

import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.content.global.travel.canoe.Canoe;
import core.game.content.global.travel.canoe.CanoeExtension;
import core.game.content.global.travel.canoe.CanoeStation;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the canoe interface plugins.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class CanoeInterfacePlugin extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(52, this);
        ComponentDefinition.put(53, this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        final CanoeExtension extension = CanoeExtension.extension(player);
        switch (component.getId()) {
            case 52:
                final Canoe canoe = Canoe.getCanoeFromChild(button);
                if (canoe == null) {
                    return true;
                }
                extension.craftCanoe(canoe);
                break;
            case 53:
                final CanoeStation station = CanoeStation.getStationFromButton(button);
                if (station == null) {
                    return true;
                }
                if (extension.getStage() < 3) {
                    return true;
                }
                extension.travel(station);
                break;
        }
        return true;
    }

}