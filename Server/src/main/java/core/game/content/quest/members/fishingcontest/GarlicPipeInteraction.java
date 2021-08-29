package core.game.content.quest.members.fishingcontest;

import core.game.interaction.MovementPulse;
import core.game.interaction.NodeUsageEvent;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;
import org.rs09.consts.Items;
import core.game.content.quest.PluginInteraction;
import core.game.content.quest.PluginInteractionManager;

@Initializable
public class GarlicPipeInteraction extends PluginInteraction {
    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        setIds(new int[]{FishingContest.GARLIC.getId(),41});
        PluginInteractionManager.register(this, PluginInteractionManager.InteractionType.USEWITH);
        PluginInteractionManager.register(this, PluginInteractionManager.InteractionType.OBJECT);
        return this;
    }

    @Override
    public boolean handle(Player player, NodeUsageEvent event) {
        System.out.println("Trying to handle it");
        if(event.getUsed() instanceof Item && event.getUsedWith() instanceof Scenery){
            Scenery usedWith = event.getUsedWith().asScenery();
            Item used = event.getUsedItem();

            if(used.getId() == Items.GARLIC_1550 && usedWith.getId() == 41 && usedWith.getLocation().equals(Location.create(2638, 3446, 0)) && player.getQuestRepository().getStage("Fishing Contest") > 0){
                player.getPulseManager().run(new MovementPulse(player, usedWith.getLocation().transform(0, -1, 0)) {
                    @Override
                    public boolean pulse() {
                        player.getDialogueInterpreter().sendDialogue("You stuff the garlic into the pipe.");
                        player.getInventory().remove(new Item(Items.GARLIC_1550));
                        player.setAttribute("fishing_contest:garlic",true);
                        return true;
                    }
                }, "movement");
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean handle(Player player, Node node) {
        if(node instanceof Scenery){
            Scenery object = node.asScenery();
            if(object.getId() == 41 && object.getLocation().equals(Location.create(2638, 3446, 0)) && player.getAttribute("fishing_contest:garlic",false)){
                player.getPulseManager().run(new MovementPulse(player, object.getLocation().transform(0, -1, 0)) {
                    @Override
                    public boolean pulse() {
                        player.getDialogueInterpreter().sendDialogue("This is the pipe I stuffed that garlic into.");
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
