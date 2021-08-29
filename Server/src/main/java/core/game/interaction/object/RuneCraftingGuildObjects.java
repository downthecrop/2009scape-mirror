package core.game.interaction.object;

import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Created for 2009Scape
 * User: Ethan Kyle Millard
 * Date: March 25, 2020
 * Time: 7:19 PM
 */
@Initializable
public class RuneCraftingGuildObjects extends OptionHandler {


    @Override
    public boolean handle(Player player, Node node, String option) {
        Scenery object = ((Scenery) node);

        switch(object.getId()) {
            case 38279:
                if (player.getViewport().getRegion().getRegionId() == 12337) {
                    player.teleport(Location.create(1696, 5460, 2));
                } else {
                    player.teleport(Location.create(3106, 3160, 1));
                }
                break;
        }

        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        SceneryDefinition.forId(38279).getHandlers().put("option:enter", this);
        return this;
    }
}
