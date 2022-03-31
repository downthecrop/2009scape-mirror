package core.game.interaction.object;

import core.cache.def.impl.SceneryDefinition;
import org.rs09.consts.Items;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.OptionHandler;
import core.game.interaction.UseWithHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.plugin.Initializable;
import core.plugin.Plugin;
import rs09.plugin.ClassScanner;


/**
 * Plugin that enables getting flour from barrel in Sinclair mansion kitchen.
 * @author afaroutdude
 */
@Initializable
public final class SinclairFlourBarrelPlugin extends OptionHandler {
    private final static Item EMPTY_POT = new Item(Items.EMPTY_POT_1931);
    private static final Item FLOUR = new Item(Items.POT_OF_FLOUR_1933);

    @Override
    public boolean handle(Player player, Node node, String option) {
        return getFlour(player, (Scenery) node);
    }

    public boolean getFlour(final Player player, final Scenery object) {
        if (!player.getInventory().containsItem(EMPTY_POT)) {
            player.getPacketDispatch().sendMessage("I need an empty pot to hold the flour in.");
            return true;
        }
        if (player.getInventory().remove(EMPTY_POT)) {
            player.lock(3);
            player.getInventory().add(FLOUR);
            player.getPacketDispatch().sendMessage("You take some flour from the barrel.");

            // Seers achievement diary
            if (!player.getAchievementDiaryManager().getDiary(DiaryType.SEERS_VILLAGE).isComplete(0,5)) {
                if (player.getAttribute("diary:seers:sinclair-flour", 0) >= 4) {
                    player.setAttribute("/save:diary:seers:sinclair-flour", 5);
                    player.getAchievementDiaryManager().getDiary(DiaryType.SEERS_VILLAGE).updateTask(player,0,5,true);
                } else {
                    player.setAttribute("/save:diary:seers:sinclair-flour", player.getAttribute("diary:seers:sinclair-flour", 0) + 1);
                }
            }

            player.getPacketDispatch().sendMessage("There's still plenty of flour left.");

            return true;
        }
        return false;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        SceneryDefinition.forId(26122).getHandlers().put("option:take from", this);
        ClassScanner.definePlugin(new FlourHandler());
        return this;
    }

    private class FlourHandler extends UseWithHandler {

        public FlourHandler() {
            super(EMPTY_POT.getId());
        }

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            addHandler(26122, OBJECT_TYPE, this);
            return this;
        }

        @Override
        public boolean handle(NodeUsageEvent event) {
            return getFlour(event.getPlayer(), event.getUsedWith().asScenery());
        }
    }
}