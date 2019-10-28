package org.crandor.game.node.entity.state.impl;

import org.crandor.game.container.impl.EquipmentContainer;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.npc.drop.NPCDropTables;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.state.StatePulse;
import org.crandor.game.node.item.ChanceItem;
import org.crandor.game.node.item.Item;
import org.crandor.game.system.mysql.impl.ItemConfigSQLHandler;
import org.crandor.tools.RandomFunction;

import java.nio.ByteBuffer;

/**
 * The pulse used when wearing an ava device.
 * @author Vexia
 */
public final class AvaDevicePulse extends StatePulse {

	/**
	 * The ticks this pulse runs on.
	 */
	private static final int TICKS = (int) (180 / 0.6);

	/**
	 * The rewards to generate randomly.
	 */
	private static final ChanceItem[][] REWARDS = new ChanceItem[][] { { new ChanceItem(884, 1, NPCDropTables.DROP_RATES[0] + 100), new ChanceItem(440, 1, NPCDropTables.DROP_RATES[2]), new ChanceItem(1153, 1, NPCDropTables.DROP_RATES[2]), new ChanceItem(807, 1, NPCDropTables.DROP_RATES[1]), new ChanceItem(863, 1, NPCDropTables.DROP_RATES[1]), new ChanceItem(2351, 1, NPCDropTables.DROP_RATES[2]), new ChanceItem(9140, 1, NPCDropTables.DROP_RATES[1]), new ChanceItem(40, 1, NPCDropTables.DROP_RATES[1]), new ChanceItem(7767, 1, NPCDropTables.DROP_RATES[3]) },// regular
			{ new ChanceItem(886, 1, NPCDropTables.DROP_RATES[0] + 100), new ChanceItem(440, 1, NPCDropTables.DROP_RATES[1]), new ChanceItem(41, 1, NPCDropTables.DROP_RATES[1]), new ChanceItem(2353, 1, NPCDropTables.DROP_RATES[2]), new ChanceItem(9141, 1, NPCDropTables.DROP_RATES[2]), new ChanceItem(808, 1, NPCDropTables.DROP_RATES[2]), new ChanceItem(1353, 1, NPCDropTables.DROP_RATES[3]), new ChanceItem(865, 1, NPCDropTables.DROP_RATES[1]), new ChanceItem(1157, 1, NPCDropTables.DROP_RATES[3]), new ChanceItem(1539, 1, NPCDropTables.DROP_RATES[1]), new ChanceItem(1119, 1, NPCDropTables.DROP_RATES[3]), new ChanceItem(7767, 1, NPCDropTables.DROP_RATES[3]), new ChanceItem(687, 1, NPCDropTables.DROP_RATES[1]) },// upgraded.
	};

	/**
	 * The device used.
	 */
	private Item device;

	/**
	 * Constructs a new {@code AvaDevicePulse} {@code Object}.
	 * @param entity the entity.
	 * @param ticks the ticks.
	 */
	public AvaDevicePulse(Entity entity, int ticks) {
		super(entity, TICKS);
	}

	@Override
	public void save(ByteBuffer buffer) {
		buffer.putInt(device.getId());
	}

	@Override
	public boolean canRun(Entity entity) {
		return super.canRun(entity);
	}

	@Override
	public StatePulse parse(Entity entity, ByteBuffer buffer) {
		return create(entity, new Item(buffer.getInt()));
	}

	@Override
	public StatePulse create(Entity entity, Object... args) {
		AvaDevicePulse pulse = new AvaDevicePulse(entity, TICKS);
		pulse.device = (Item) args[0];
		return pulse;
	}

	@Override
	public boolean pulse() {
		Player player = (Player) entity;
		if (!hasDevice(player)) {
			return true;
		}
		int mod = 0;
		if (RandomFunction.random(10 - mod) > 4) {
			return false;
		}
		generate();
		return !hasDevice(player);
	}

	/**
	 * Generates an item.
	 */
	private void generate() {
		Player player = (Player) entity;
		if (isInterfered()) {
			return;
		}
		if(player.getGlobalData().isAvasDisabled()){
			return;
		}
		Item item = getGeneratedItem();
		if (item.getDefinition().getConfiguration(ItemConfigSQLHandler.EQUIP_SLOT, -1) == EquipmentContainer.SLOT_ARROWS) {
			Item arrowSlot = player.getEquipment().get(EquipmentContainer.SLOT_ARROWS);
			if (arrowSlot == null || arrowSlot.getId() == item.getId()) {
				player.getEquipment().add(item, true, EquipmentContainer.SLOT_ARROWS);
				return;
			}
		}
		player.getInventory().add(item, player);
	}

	@Override
	public boolean isSaveRequired() {
		return true;
	}

	/**
	 * Checks if the player has a device.
	 * @param player the player.
	 * @return {@code True} if so.
	 */
	private boolean hasDevice(Player player) {
		return player.getEquipment().containsItem(device);
	}

	/**
	 * Checks if its the upgraded item.
	 * @return {@code True} if so.
	 */
	private boolean isUpgraded() {
		return device.getId() == 10499;
	}

	/**
	 * Gets a generated item.
	 * @return the item.
	 */
	private Item getGeneratedItem() {
		ChanceItem[] items = REWARDS[isUpgraded() ? 1 : 0];
		if (RandomFunction.random(10) < 4) {
			return RandomFunction.getChanceItem(items);
		} else {
			return items[0];
		}
	}

	/**
	 * Checks if the device is being interfered with.
	 * @return {@code True} if so.
	 */
	private boolean isInterfered() {
		Player player = (Player) entity;
		Item cape = player.getEquipment().get(EquipmentContainer.SLOT_CAPE);
		if (cape != null && (cape.getId() == 10498 || cape.getId() == 10499)) {
			Item torso = player.getEquipment().get(EquipmentContainer.SLOT_CHEST);
			int modelId = torso == null ? -1 : torso.getDefinition().getMaleWornModelId1();
			if (modelId == 301 || modelId == 306 || modelId == 3379) {
				player.getPacketDispatch().sendMessage("Your armour interferes with Ava's device.");
				return true;
			}
		}
		return false;
	}
}
