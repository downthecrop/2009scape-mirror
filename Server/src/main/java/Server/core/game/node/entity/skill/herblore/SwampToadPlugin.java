package core.game.node.entity.skill.herblore;


import core.cache.def.impl.ItemDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the swamp toad plugin.
 * @author Zombiemode
 * @version 1.0
 */
@Initializable
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

