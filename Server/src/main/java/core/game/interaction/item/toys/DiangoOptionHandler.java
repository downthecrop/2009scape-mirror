package core.game.interaction.item.toys;

import core.cache.def.impl.NPCDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

@Initializable
public class DiangoOptionHandler extends OptionHandler {
    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.forId(970).getHandlers().put("option:holiday-items",this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        DiangoReclaimInterface.open(player);
        return true;
    }
}