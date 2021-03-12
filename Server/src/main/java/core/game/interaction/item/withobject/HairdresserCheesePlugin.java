package core.game.interaction.item.withobject;

import core.plugin.Initializable;
import core.tools.Items;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.node.item.Item;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.plugin.Plugin;

/**
 * Fills an ectophial.
 * @author afaroutdude
 */
@Initializable
public class HairdresserCheesePlugin extends UseWithHandler {

    /**
     * Constructs a new {@code EctophialFillPlugin} {@code Object}
     */
    public HairdresserCheesePlugin() {
        super(Items.CHEESE_1985);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(11677, OBJECT_TYPE, this);
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        player.lock(3);
        //player.animate(Animation.create(1652));
        GameWorld.getPulser().submit(new Pulse(3, player) {
            @Override
            public boolean pulse() {
                if (player.getInventory().remove(new Item(Items.CHEESE_1985))) {
                    player.sendMessage("You throw the cheese to Ridgeley, for which he appears grateful.");
                    player.getAchievementDiaryManager().finishTask(player,DiaryType.FALADOR,0, 6);
                }
                return true;
            }
        });
        return true;
    }

}
