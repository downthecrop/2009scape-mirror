package core.game.interaction.item;

import core.cache.def.impl.ItemDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used to handle the "read" option of a book.
 *
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public class BookreadOption extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.forId(292).getHandlers().put("option:read", this);
        ItemDefinition.forId(757).getHandlers().put("option:read", this);
        ItemDefinition.forId(1856).getHandlers().put("option:read", this);
        ItemDefinition.forId(9003).getHandlers().put("option:read", this);
        ItemDefinition.forId(9004).getHandlers().put("option:read", this);
        ItemDefinition.forId(11710).getHandlers().put("option:read", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        final int id = getDialId(((Item) node).getId());
        player.getInterfaceManager().close();
        return player.getDialogueInterpreter().open(id, node);
    }

    @Override
    public boolean isWalk() {
        return false;
    }

    /**
     * Gets the dialogue id from the item id.
     *
     * @param item the item.
     * @return the dial id.
     */
    public int getDialId(int item) {
        switch (item) {
            case 757:
                return 49610758;
            case 9003:
                return 49610759;
	/*case 9004:
	    return 423943;*/
            case 11710:
                return 2739823;
            case 292:
                return 183764;
            case 1856:
                return 387454;
        }
        return -1;
    }
}
