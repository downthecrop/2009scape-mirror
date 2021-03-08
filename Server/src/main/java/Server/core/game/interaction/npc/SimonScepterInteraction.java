package core.game.interaction.npc;

import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles when a player uses a pharaoh scepter on Simon (necessary to be accurate)
 * @author ceik
 */
@Initializable
public final class SimonScepterInteraction extends UseWithHandler {

    public SimonScepterInteraction() {
        super(9044, 9046, 9048, 9050);
    }
    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(3123, NPC_TYPE, this);
        //PluginManager.definePlugin(new SimonScepterInteraction());
        return this;
    }
    @Override
    public boolean handle(NodeUsageEvent event){
        final Player player = event.getPlayer();
        player.getDialogueInterpreter().open(3123, ((NPC)event.getUsedWith()), true, false, event.getUsedItem().getId());
        return true;
    }
}
