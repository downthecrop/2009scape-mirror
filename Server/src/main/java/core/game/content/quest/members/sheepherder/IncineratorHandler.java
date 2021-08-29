package core.game.content.quest.members.sheepherder;

import core.game.interaction.DestinationFlag;
import core.game.interaction.MovementPulse;
import core.game.interaction.NodeUsageEvent;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.game.content.quest.PluginInteraction;
import core.game.content.quest.PluginInteractionManager;

import java.util.Objects;

@Initializable
public class IncineratorHandler extends PluginInteraction {
    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        setIds(new int[] {SheepHerder.RED_SHEEP_BONES.getId(), SheepHerder.GREEN_SHEEP_BONES.getId(), SheepHerder.BLUE_SHEEP_BONES.getId(),SheepHerder.YELLOW_SHEEP_BONES.getId()});
        PluginInteractionManager.register(this, PluginInteractionManager.InteractionType.USEWITH);
        return this;
    }

    @Override
    public boolean handle(Player player, NodeUsageEvent event) {
        Node n = event.getUsedWith();
        if(n instanceof Scenery){
            Scenery obj = (Scenery) n;
            if (n.getId() == 165) {
                player.getPulseManager().run(new MovementPulse(player, DestinationFlag.OBJECT.getDestination(player,obj)) {
                    @Override
                    public boolean pulse() {
                        player.lock(2);
                        player.getInventory().remove(event.getUsedItem());
                        player.getAnimator().reset();
                        player.getAnimator().animate(new Animation(3243));
                        switch (Objects.requireNonNull(event.getUsedItem()).getId()) {
                            case 280:
                                player.setAttribute("/save:sheep_herder:red_dead", true);
                                break;
                            case 281:
                                player.setAttribute("/save:sheep_herder:green_dead", true);
                                break;
                            case 282:
                                player.setAttribute("/save:sheep_herder:blue_dead", true);
                                break;
                            case 283:
                                player.setAttribute("/save:sheep_herder:yellow_dead", true);
                                break;
                        }
                        if (player.getAttribute("sheep_herder:red_dead", false) && player.getAttribute("sheep_herder:green_dead", false) && player.getAttribute("sheep_herder:blue_dead", false) && player.getAttribute("sheep_herder:yellow_dead", false)) {
                            player.setAttribute("/save:sheep_herder:all_dead", true);
                        }
                        return true;
                    }
                });
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
