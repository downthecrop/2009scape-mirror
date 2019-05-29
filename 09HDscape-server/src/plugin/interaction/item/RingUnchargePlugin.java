package plugin.interaction.item;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Handles the uncharging of the four imbued rings.
 * @author Splinter
 */
@InitializablePlugin
public final class RingUnchargePlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.forId(14807).getConfigurations().put("option:uncharge", this);
		ItemDefinition.forId(14808).getConfigurations().put("option:uncharge", this);
		ItemDefinition.forId(14809).getConfigurations().put("option:uncharge", this);
		ItemDefinition.forId(14810).getConfigurations().put("option:uncharge", this);
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
