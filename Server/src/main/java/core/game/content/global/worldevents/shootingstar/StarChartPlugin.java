package core.game.content.global.worldevents.shootingstar;

import core.cache.def.impl.SceneryDefinition;
import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.plugin.Plugin;
import rs09.plugin.ClassScanner;
import core.plugin.PluginManifest;

@PluginManifest(name="ShootingStars")
public class StarChartPlugin extends ComponentPlugin {
    private Component iface = new Component(104);
    private int index= 19;

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {

        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ClassScanner.definePlugin(new StarChartOptions());
        ComponentDefinition.forId(iface.getId()).setPlugin(this);
        return this;
    }

    public class StarChartOptions extends OptionHandler{

        @Override
        public boolean handle(Player player, Node node, String option) {
            player.getInterfaceManager().open(iface);
            return true;
        }

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            SceneryDefinition.forId(25582).getHandlers().put("option:look-at",this);
            SceneryDefinition.forId(25581).getHandlers().put("option:look-at",this);
            SceneryDefinition.forId(25580).getHandlers().put("option:look-at",this);
            SceneryDefinition.forId(25583).getHandlers().put("option:look-at",this);
            SceneryDefinition.forId(25579).getHandlers().put("option:look-at",this);
            SceneryDefinition.forId(25578).getHandlers().put("option:look-at",this);
            return this;
        }
    }
}
