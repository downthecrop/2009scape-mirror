package core.game.interaction.item.toys;

import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;

@Initializable
public class ReindeerHatPlugin extends OptionHandler {
    public final static Item ReindeerHat = new Item(10507);

    @Override
    public Plugin newInstance(Object arg) throws Throwable {
        ReindeerHat.getDefinition().getHandlers().put("option:operate",this);
        return null;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        player.getLocks().lockInteractions(2);
        player.getAnimator().animate(new Animation(5059));
        return true;
    }
}
