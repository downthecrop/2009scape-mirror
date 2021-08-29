package core.game.content.quest.members.fishingcontest;

import core.game.interaction.MovementPulse;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.game.content.quest.PluginInteraction;
import core.game.content.quest.PluginInteractionManager;

@Initializable
public class VineInteraction extends PluginInteraction {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        setIds(new int[]{58,2989,2990,2991,2992,2993,2994,2013});
        PluginInteractionManager.register(this, PluginInteractionManager.InteractionType.OBJECT);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node) {
        if(node instanceof Scenery){
            if(player.getQuestRepository().getStage("Fishing Contest") > 0 && player.getQuestRepository().getStage("Fishing Contest") < 100){
                player.getPulseManager().run(new MovementPulse(player, node.asScenery().getLocation().transform(0, 0, 0)) {
                    @Override
                    public boolean pulse() {
                        if(player.getInventory().containsItem(FishingContest.SPADE)) {
                            player.getAnimator().animate(new Animation(830));
                            player.getDialogueInterpreter().sendDialogue("You find some worms.");
                            player.getInventory().add(FishingContest.RED_VINE_WORM);
                        } else {
                            player.getDialogueInterpreter().sendDialogue("The ground looks promising around these vines.","Perhaps you should dig.");
                        }
                        return true;
                    }
                }, "movement");
                return true;
            }
        }
        return false;
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }
}
