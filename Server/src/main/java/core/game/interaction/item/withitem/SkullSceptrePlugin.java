package core.game.interaction.item.withitem;

import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * @author 'Vexia
 */
@Initializable
public class SkullSceptrePlugin extends UseWithHandler {

	public SkullSceptrePlugin() {
		super(9008, 9009, 9011);
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		if (event.getUsedItem().getId() == 9008 && ((Item) event.getUsedWith()).getId() == 9007) {
			event.getPlayer().getInventory().remove(new Item(9008, 1));
			event.getPlayer().getInventory().remove(new Item(9007, 1));
			event.getPlayer().getInventory().add(new Item(9009, 1));
			event.getPlayer().getDialogueInterpreter().open(78489);
			return true;
		}
		if (event.getUsedItem().getId() == 9011 && ((Item) event.getUsedWith()).getId() == 9010) {
			event.getPlayer().getInventory().remove(new Item(9010, 1));
			event.getPlayer().getInventory().remove(new Item(9011, 1));
			event.getPlayer().getInventory().add(new Item(9012, 1));
			event.getPlayer().getDialogueInterpreter().open(78489, true);
			return true;
		}
		if (event.getUsedItem().getId() == 9012 && ((Item) event.getUsedWith()).getId() == 9009) {
			event.getPlayer().getInventory().remove(new Item(9009, 1));
			event.getPlayer().getInventory().remove(new Item(9012, 1));
			event.getPlayer().getInventory().add(new Item(9013, 1));
			event.getPlayer().getDialogueInterpreter().open(78489, true, true);
			return true;
		}
		return true;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(9007, ITEM_TYPE, this);
		addHandler(9010, ITEM_TYPE, this);
		addHandler(9012, ITEM_TYPE, this);
		return this;
	}

}
