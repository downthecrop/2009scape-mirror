package core.game.content.zone.phasmatys;

import core.cache.def.impl.NPCDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

@Initializable
public class TokenCollectOption extends OptionHandler {

    @Override
    public boolean handle(Player player, Node node, String option) {
        if(option.equals("collect")){
            player.getDialogueInterpreter().open(1686,node.asNpc() ,true);
        }
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.forId(1686).getHandlers().put("option:collect",this);
        return this;
    }
}
