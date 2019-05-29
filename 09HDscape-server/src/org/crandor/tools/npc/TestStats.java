package org.crandor.tools.npc;

import org.crandor.game.container.impl.EquipmentContainer;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;

/**
 * Tests an NPC's combat stats.
 * @author Emperor
 */
public final class TestStats {

	/*
	 * Best player magic attack: +139 Best player magic defence: +202
	 */

	/**
	 * Sets the player's armour to maximize melee accuracy.
	 * @param player The player.
	 */
	public static void setMaxedMeleeAcc(Player player) {
		player.getEquipment().replace(new Item(6570), EquipmentContainer.SLOT_CAPE);
		player.getEquipment().replace(new Item(6585), EquipmentContainer.SLOT_AMULET);
		player.getEquipment().replace(new Item(11694), EquipmentContainer.SLOT_WEAPON);
		player.getEquipment().replace(new Item(7462), EquipmentContainer.SLOT_HANDS);
		player.getEquipment().replace(new Item(6735), EquipmentContainer.SLOT_RING);
	}

	/**
	 * Sets the player's armour to maximize melee strength.
	 * @param player The player.
	 */
	public static void setMaxedMeleeStr(Player player) {
		player.getEquipment().replace(new Item(10828), EquipmentContainer.SLOT_HAT);
		player.getEquipment().replace(new Item(6570), EquipmentContainer.SLOT_CAPE);
		player.getEquipment().replace(new Item(1725), EquipmentContainer.SLOT_AMULET);
		player.getEquipment().replace(new Item(11694), EquipmentContainer.SLOT_WEAPON);
		player.getEquipment().replace(null, EquipmentContainer.SLOT_SHIELD);
		player.getEquipment().replace(new Item(11724), EquipmentContainer.SLOT_CHEST);
		player.getEquipment().replace(new Item(11726), EquipmentContainer.SLOT_LEGS);
		player.getEquipment().replace(new Item(7462), EquipmentContainer.SLOT_HANDS);
		player.getEquipment().replace(new Item(6737), EquipmentContainer.SLOT_RING);
		player.getEquipment().replace(new Item(11732), EquipmentContainer.SLOT_FEET);
	}

	/**
	 * Sets the player's armour to maximize magic accuracy.
	 * @param player The player.
	 */
	public static void setMaxedMagicAcc(Player player) {
		player.getEquipment().replace(new Item(10342), EquipmentContainer.SLOT_HAT);
		player.getEquipment().replace(new Item(2414), EquipmentContainer.SLOT_CAPE);
		player.getEquipment().replace(new Item(10344), EquipmentContainer.SLOT_AMULET);
		player.getEquipment().replace(new Item(6914), EquipmentContainer.SLOT_WEAPON);
		player.getEquipment().replace(new Item(4712), EquipmentContainer.SLOT_CHEST);
		player.getEquipment().replace(new Item(6889), EquipmentContainer.SLOT_SHIELD);
		player.getEquipment().replace(new Item(4714), EquipmentContainer.SLOT_LEGS);
		player.getEquipment().replace(new Item(7462), EquipmentContainer.SLOT_HANDS);
		player.getEquipment().replace(new Item(6920), EquipmentContainer.SLOT_FEET);
		player.getEquipment().replace(new Item(6731), EquipmentContainer.SLOT_RING);
	}

}