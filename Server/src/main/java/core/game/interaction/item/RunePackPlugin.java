package core.game.interaction.item;

import core.cache.def.impl.ItemDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles the new OSRS rune packs.
 * @author Splinter
 */
@Initializable
public final class RunePackPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.forId(14730).getHandlers().put("option:open", this);
		ItemDefinition.forId(14732).getHandlers().put("option:open", this);
		ItemDefinition.forId(14734).getHandlers().put("option:open", this);
		ItemDefinition.forId(14736).getHandlers().put("option:open", this);
		ItemDefinition.forId(14738).getHandlers().put("option:open", this);
		ItemDefinition.forId(14740).getHandlers().put("option:open", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		if(node == null || player == null){
			return true;
		}
		player.lock(1);
		if(player.getInventory().remove(new Item(node.asItem().getId()))){
			player.getInventory().add(new Item(getReward(node.asItem().getId()), 100));
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
		case 14730:
			return 556;
		case 14732:
			return 555;
		case 14734:
			return 557;
		case 14736:
			return 554;
		case 14738:
			return 558;
		case 14740:
			return 562;
		}
		return 556;
	}
	
}
