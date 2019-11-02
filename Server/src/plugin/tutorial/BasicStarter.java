package plugin.tutorial;

import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.PluginManifest;
import org.crandor.plugin.PluginType;

/**
 * Basic starter until tutorial is completed.
 *
 * @author Michael Sasse (https://github.com/mikeysasse/)
 */
@InitializablePlugin
@PluginManifest(type = PluginType.LOGIN)
public class BasicStarter implements Plugin<Player> {

    private static final int[][] STARTER_ITEMS = { { 1351, 1 }, { 590, 1 },
            { 303, 1 }, { 315, 1 }, { 1925, 1 }, { 1931, 1 }, { 2309, 1 },
            { 1265, 1 }, { 1205, 1 }, { 1277, 1 }, { 1171, 1 }, { 841, 1 },
            { 882, 25 }, { 556, 25 }, { 558, 15 }, { 555, 6 }, { 557, 4 },
            { 559, 2 } };

    public BasicStarter() { }

    @Override
    public Plugin<Player> newInstance(Player player) throws Throwable {
        System.out.println(player.getDetails().getLastLogin());
        if (player.getDetails().getLastLogin() == 0) {
            for (int[] item : STARTER_ITEMS) {
                player.getInventory().add(new Item(item[0], item[1]));
            }
        }
        return this;
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }
}
