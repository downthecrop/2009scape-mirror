package plugin.activity.barrows;

import org.crandor.game.container.impl.EquipmentContainer;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.equipment.DegradableEquipment;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.game.node.item.ItemPlugin;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.PluginManager;

/**
 * Handles the barrows equipment.
 * @author Emperor
 */
public final class BarrowsEquipment extends DegradableEquipment {

	/**
	 * Constructs a new {@code BarrowsEquipment} {@code Object}.
	 * @param slot The slot.
	 * @param itemIds The item ids.
	 */
	public BarrowsEquipment(int slot, int... itemIds) {
		super(slot, itemIds);
	}

	/**
	 * Initializes the barrows equipment degrading.
	 */
	public static void init() {
		PluginManager.definePlugin(new BarrowsEquipment(EquipmentContainer.SLOT_HAT, 4708, 4856, 4857, 4858, 4859, 4716, 4880, 4881, 4882, 4883, 4724, 4904, 4905, 4906, 4907, 4732, 4928, 4929, 4930, 4931, 4745, 4952, 4953, 4954, 4955, 4753, 4976, 4977, 4978, 4979));
		PluginManager.definePlugin(new BarrowsEquipment(EquipmentContainer.SLOT_WEAPON, 4710, 4862, 4863, 4864, 4865, 4718, 4886, 4887, 4888, 4889, 4726, 4910, 4911, 4912, 4913, 4734, 4934, 4935, 4936, 4937, 4747, 4958, 4959, 4960, 4961, 4755, 4982, 4983, 4984, 4985));
		PluginManager.definePlugin(new BarrowsEquipment(EquipmentContainer.SLOT_CHEST, 4712, 4868, 4869, 4870, 4871, 4720, 4892, 4893, 4894, 4895, 4728, 4916, 4917, 4918, 4919, 4736, 4940, 4941, 4942, 4943, 4749, 4964, 4965, 4966, 4967, 4757, 4988, 4989, 4990, 4991));
		PluginManager.definePlugin(new BarrowsEquipment(EquipmentContainer.SLOT_LEGS, 4714, 4874, 4875, 4876, 4877, 4722, 4898, 4899, 4900, 4901, 4730, 4922, 4923, 4924, 4925, 4738, 4946, 4947, 4948, 4949, 4751, 4970, 4971, 4972, 4973, 4759, 4994, 4995, 4996, 4997));
		PluginManager.definePlugin(new BarrowsWrapper());
	}

	@Override
	public void degrade(Player player, Entity entity, Item item) {
		int index = -1;
		for (int i = 0; i < getItemIds().length; i++) {
			if (item.getId() == getItemIds()[i]) {
				index = i;
				break;
			}
		}
		if (index < 0) {
			return;
		}
		if (item.getCharge() > 0) {
			item.setCharge(item.getCharge() - 1);
		}
		int replaceId = item.getId();
		if (index % 5 == 0 || item.getCharge() < 1) {
			String name = item.getName();
			int ch = -1;
			if ((ch = name.indexOf("1")) > -1) {
				name = name.substring(0, ch - 1);
			} else if ((ch = name.indexOf("2")) > -1) {
				name = name.substring(0, ch - 1);
			} else if ((ch = name.indexOf("5")) > -1) {
				name = name.substring(0, ch - 1);
			} else if ((ch = name.indexOf("7")) > -1) {
				name = name.substring(0, ch - 1);
			}
			player.getPacketDispatch().sendMessage("Your " + name + " has degraded.");
			if (index % 5 == 4) {
				if (player.getInventory().add(new Item(item.getId() + 1))) {
					player.getEquipment().replace(null, getSlot());
					return;
				}
				replaceId = item.getId() + 1;
			} else {
				replaceId = getItemIds()[index + 1];
			}
		}
		if (replaceId != item.getId()) {
			player.getEquipment().replace(new Item(replaceId), getSlot());
		}
	}

	@Override
	public int getDropItem(int itemId) {
		int index = -1;
		for (int i = 0; i < getItemIds().length; i++) {
			if (itemId == getItemIds()[i]) {
				index = i;
				break;
			}
		}
		if (index % 5 < 1) {
			return itemId;
		}
		return getItemIds()[index + (4 - (index % 5))] + 1;
	}

	/**
	 * A barrows wrapper for an item.
	 * @author Vexia
	 */
	public static class BarrowsWrapper extends ItemPlugin {

		/**
		 * The ids.
		 */
		private static final int[] IDS = new int[] { 4708, 4856, 4857, 4858, 4859, 4716, 4880, 4881, 4882, 4883, 4724, 4904, 4905, 4906, 4907, 4732, 4928, 4929, 4930, 4931, 4745, 4952, 4953, 4954, 4955, 4753, 4976, 4977, 4978, 4979, 4710, 4862, 4863, 4864, 4865, 4718, 4886, 4887, 4888, 4889, 4726, 4910, 4911, 4912, 4913, 4734, 4934, 4935, 4936, 4937, 4747, 4958, 4959, 4960, 4961, 4755, 4982, 4983, 4984, 4985, 4712, 4868, 4869, 4870, 4871, 4720, 4892, 4893, 4894, 4895, 4728, 4916, 4917, 4918, 4919, 4736, 4940, 4941, 4942, 4943, 4749, 4964, 4965, 4966, 4967, 4757, 4988, 4989, 4990, 4991, 4714, 4874, 4875, 4876, 4877, 4722, 4898, 4899, 4900, 4901, 4730, 4922, 4923, 4924, 4925, 4738, 4946, 4947, 4948, 4949, 4751, 4970, 4971, 4972, 4973, 4759, 4994, 4995, 4996, 4997 };

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			register(IDS);
			return this;
		}

		@Override
		public Item getDeathItem(Item item) {
			return new Item(DegradableEquipment.getDropReplacement(item.getId()), 1);
		}
	}
}