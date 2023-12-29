package content.region.misthalin.wiztower.handlers.rcguild;

import core.ServerConstants;
import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.plugin.Plugin;


/**
 * Map in RC guild/wiz tower
 * @author ceik
 */
public class RCGuildMap extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        SceneryDefinition.forId(38422).getHandlers().put("option:study",this);
        return null;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        if(option.equals("study")){
            player.getPacketDispatch().sendMessage("A map of " + ServerConstants.SERVER_NAME + ".");
        }
        return false;
    }
}
