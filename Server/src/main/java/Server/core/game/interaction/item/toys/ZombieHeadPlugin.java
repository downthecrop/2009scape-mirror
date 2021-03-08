package core.game.interaction.item.toys;

import core.cache.def.impl.ItemDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;

@Initializable
public class ZombieHeadPlugin extends OptionHandler {
    static final Item ZOMBIE_HEAD = new Item(6722);
    static final Animation TALK_AT = new Animation(2840);
    static final Animation DISPLAY = new Animation(2844);
    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition def = ItemDefinition.forId(ZOMBIE_HEAD.getId());
        def.getHandlers().put("option:talk-at",this);
        def.getHandlers().put("option:display",this);
        def.getHandlers().put("option:question",this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        switch(option){
            case "talk-at":
                player.getLocks().lockInteractions(2);
                player.getAnimator().animate(TALK_AT);
                player.sendChat("Alas!");
                break;
            case "display":
                player.getLocks().lockInteractions(2);
                player.getAnimator().animate(DISPLAY);
                player.sendChat("MWAHHAHAHAHAHAHA");
                break;
            case "question":
                //player.getDialogueInterpreter().open()
        }
        return true;
    }
}
