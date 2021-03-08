package core.game.interaction.item.withobject;

import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.IronmanMode;
import core.game.node.item.Item;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles the unnoting of noted items when used on a bank booth.
 * @author Vexia
 * @author Splinter
 */
@Initializable
public class BankUnnotePlugin extends UseWithHandler {

	/**
	 * The bank booth ids.
	 */
	private static final int[] BOOTHS = new int[] { 2213, 3194, 5276, 6084, 10517, 11338, 11402, 11758, 12309, 12798, 14367, 16700, 18491, 19230, 20325, 20391, 22819, 24914, 25808, 26972, 27663, 28514, 29085, 30016, 34752, 35647, 36786 };

	/**
	 * The banker ids,
	 */
	private static final int[] BANKERS = new int[] { 44, 45, 494, 495, 496, 497, 498, 499, 953, 1036, 1360, 2163, 2164, 2354, 2355, 2568, 2569, 2570, 3198, 3199, 3824, 5258, 5260, 5776, 5777, 5912, 5913, 6200, 6532, 6533, 6534, 6535, 6538, 7445, 7446, 7605 };

	/**
	 * Constructs a new {@Code BankUnnotePlugin} {@Code Object}
	 */
	public BankUnnotePlugin() {
		super();
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (int id : BOOTHS) {
			addHandler(id, OBJECT_TYPE, this);
		}
		for (int id : BANKERS) {
			addHandler(id, NPC_TYPE, this);
		}
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		final Item item = event.getUsedItem();
		if (!player.isAdmin() && (player.getIronmanManager().getMode() == IronmanMode.NONE || item.getDefinition().isUnnoted())) {
			return false;
		}
		if (item.getDefinition().isStackable() && item.getDefinition().isUnnoted()) {
			player.getPacketDispatch().sendMessage("You can't note stackable items.");
			return true;
		}
		int amount = item.getAmount();
		if (amount > player.getInventory().freeSlots()) {
			amount = 28 - player.getInventory().freeSlots();
		}
		if (amount < 1 || player.getInventory().freeSlots() <= 0) {
			player.sendMessage("Not enough inventory space.");
			return true;
		}
		if (player.getInventory().freeSlots() >= amount) {
			if (player.getInventory().remove(new Item(item.getId(), amount))) {
				Item newItem = new Item(item.getNoteChange(), amount);
				player.getInventory().add(newItem);
				player.getDialogueInterpreter().sendItemMessage(newItem, "You exchange your noted items.");
				player.lock(1);
				return true;
			}
		} else {
			int toAdd = player.getInventory().freeSlots();
			if (player.getInventory().remove(new Item(item.getId(), toAdd))) {
				Item newItem = new Item(item.getNoteChange(), toAdd);
				player.getInventory().add(newItem);
				player.getDialogueInterpreter().sendItemMessage(newItem, "You exchange your noted items.");
				player.lock(1);
				return true;
			}
		}
		return false;
	}

	@Override
	public Location getDestination(Player player, Node with) {
		if (with instanceof NPC) {
			NPC npc = (NPC) with;
			if (npc.getAttribute("facing_booth", false)) {
				Direction dir = npc.getDirection();
				return npc.getLocation().transform(dir.getStepX() << 1, dir.getStepY() << 1, 0);
			}
			if (npc.getId() == 6533) {
				return Location.create(3167, 3490, 0);// ge bankers.
			} else if (npc.getId() == 6535) {
				return Location.create(3162, 3489, 0);
			} else if (npc.getId() == 4907) {
				return npc.getLocation().transform(0, -2, 0);
			}
		}
		return null;
	}

	@Override
	public boolean isDynamic() {
		return true;
	}
}
