package plugin.skill.herblore;


import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the swamp toad plugin.
 * @author Zombiemode
 * @version 1.0
 */
@InitializablePlugin
public final class SwampToadPlugin extends OptionHandler {
    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.setOptionHandler("remove-legs", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (player.getInventory().replace(new Item(2152), ((Item) node).getSlot()) != null) {
            player.getPacketDispatch().sendMessage("You pull the legs off the toad. Poor toad. At least they'll grow back.");
        }
        return true;
    }

    @Override
    public boolean isWalk() { return false; }
}

