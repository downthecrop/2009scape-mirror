package content.global.skill.construction.decoration.pohstorage;

import core.game.container.Container;
import core.game.container.ContainerType;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;

/**
 * A proxy Container used to indicate an item was found in POH storage.
 * This is not a real container, just a dummy so that hasAnItem() can be easily extended to check POH storage
 */
public class POHStorageProxyContainer extends Container {

    public POHStorageProxyContainer(Player player) {
        super(1, ContainerType.DEFAULT);
    }

    // Prevents external code from trying to fetch items from this container because it doesn't actually store anything
    @Override
    public Item get(int slot) {
        return null;
    }

    @Override
    public String toString() {
        return "POHStorageProxyContainer";
    }
}