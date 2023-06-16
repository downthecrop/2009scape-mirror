package content.region.kandarin.quest.fishingcontest;

import core.game.interaction.MovementPulse;
import core.game.node.Node;
import core.game.node.entity.impl.PulseType;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.game.interaction.PluginInteraction;
import core.game.interaction.PluginInteractionManager;
import core.game.world.repository.Repository;

@Initializable
public class StairInteraction extends PluginInteraction {
    @Override
    public boolean handle(Player player, Node node) {
        if(!player.getQuestRepository().isComplete("Fishing Contest")) {
            Scenery object = node.asScenery();
            switch (object.getId()) {
                case 57:
                    handleStairs(player,232,object);
                    return true;
                case 55:
                    handleStairs(player,3679,object);
                    return true;
            }
        }
        return false;
    }

    private void handleStairs(Player player, int npc_id, Scenery object){
        player.getPulseManager().run(new MovementPulse(player,object.getLocation().transform(0,2,0)) {
            @Override
            public boolean pulse() {
                NPC n = Repository.findNPC(npc_id);
                if (n == null) {
                    player.sendMessage("Are you in a world without NPCs? What did you do?");
                    return true;
                }
                player.getDialogueInterpreter().open(npc_id, n);
                return true;
            }
        }, PulseType.STANDARD);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        setIds(new int[]{57,55});
        PluginInteractionManager.register(this, PluginInteractionManager.InteractionType.OBJECT);
        return this;
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }
}
