package core.game.content.global.worldevents.shootingstar;

import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.node.entity.player.Player;
import core.plugin.Plugin;

public class ShootingStarScoreboard extends ComponentPlugin {
    public static Component iface = new Component(787);

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(iface.getId(),this);
        return null;
    }
}
