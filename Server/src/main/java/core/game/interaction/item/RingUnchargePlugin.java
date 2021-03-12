package core.game.interaction.item;

import core.cache.def.impl.ItemDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles the uncharging of the four imbued rings.
 * @author Splinter
 */
@Initializable
public final class RingUnchargePlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.forId(14807).getHandlers().put("option:uncharge", this);
		ItemDefinition.forId(14808).getHandlers().put("option:uncharge", this);
		ItemDefinition.forId(14809).getHandlers().put("option:uncharge", this);
		ItemDefinition.forId(14810).getHandlers().put("option:uncharge", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		if(node == null || player == null){
			return true;
		}
		if(player.getInventory().remove(new Item(node.asItem().getId()))){
			player.getInventory().add(new Item(getReward(node.asItem().getId()), 1));
		}
		return true;
	}
	
	/**
	 * The reward to give.
	 * @param node
	 * @return an item ID
	 */
	private final int getReward(int node){
		switch(node){
		case 14810:
			return 6737;
		case 14808:
			return 6733;
		case 14809:
			return 6735;
		case 14807:
			return 6731;
		}
		return -1;
	}
	
}
