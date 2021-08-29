package core.game.content.quest.members.fishingcontest;

import core.game.interaction.MovementPulse;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.game.content.quest.PluginInteraction;
import core.game.content.quest.PluginInteractionManager;

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
                player.getDialogueInterpreter().open(npc_id,new NPC(npc_id));
                return true;
            }
        }, "movement");
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
