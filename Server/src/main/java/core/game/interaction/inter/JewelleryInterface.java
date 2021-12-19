package core.game.interaction.inter;

import static api.ContentAPIKt.*;
import core.cache.def.impl.ItemDefinition;
import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.crafting.jewellery.JewelleryCrafting;
import core.game.node.entity.skill.crafting.jewellery.JewelleryCrafting.JewelleryItem;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.RunScript;
import core.game.node.item.Item;
import core.plugin.Plugin;
import core.plugin.Initializable;
import core.tools.StringUtils;
import kotlin.Unit;

/**
 * Represents the interface plugin used for jewellery crafting.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class JewelleryInterface extends ComponentPlugin {

	/**
	 * Represents constants of useful items.
	 */
	public static final int RING_MOULD = 1592, AMULET_MOULD = 1595, NECKLACE_MOULD = 1597, BRACELET_MOULD = 11065, GOLD_BAR = 2357, SAPPHIRE = 1607, EMERALD = 1605, RUBY = 1603, DIAMOND = 1601, DRAGONSTONE = 1615, ONYX = 6573;

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.put(446, this);
		return this;
	}

	@Override
	public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
		int amount = 0;
		JewelleryItem data = null;
		switch(button){
		case 20:
			data = JewelleryItem.GOLD_RING;
			break;
		case 22:
			data = JewelleryItem.SAPPIRE_RING;
			break;
		case 24:
			data = JewelleryItem.EMERALD_RING;
			break;
		case 26:
			data = JewelleryItem.RUBY_RING;
			break;
		case 28:
			data = JewelleryItem.DIAMOND_RING;
			break;
		case 30:
			data = JewelleryItem.DRAGONSTONE_RING;
			break;
		case 32:
			data = JewelleryItem.ONYX_RING;
			break;
		case 35:
			data = JewelleryItem.SLAYER_RING;
			break;
		}
		switch (button - 3) {
		case 39:
			data = JewelleryItem.GOLD_NECKLACE;
			break;
		case 41:
			data = JewelleryItem.SAPPHIRE_NECKLACE;
			break;
		case 43:
			data = JewelleryItem.EMERALD_NECKLACE;
			break;
		case 45:
			data = JewelleryItem.RUBY_NECKLACE;
			break;
		case 47:
			data = JewelleryItem.DIAMOND_NECKLACE;
			break;
		case 49:
			data = JewelleryItem.DRAGONSTONE_NECKLACE;
			break;
		case 51:
			data = JewelleryItem.ONYX_NECKLACE;
			break;
		case 58:
			data = JewelleryItem.GOLD_AMULET;
			break;
		case 60:
			data = JewelleryItem.SAPPHIRE_AMULET;
			break;
		case 62:
			data = JewelleryItem.EMERALD_AMULET;
			break;
		case 64:
			data = JewelleryItem.RUBY_AMULET;
			break;
		case 66:
			data = JewelleryItem.DIAMOND_AMULET;
			break;
		case 68:
			data = JewelleryItem.DRAGONSTONE_AMULET;
			break;
		case 70:
			data = JewelleryItem.ONYX_AMULET;
			break;
		case 77:
			data = JewelleryItem.GOLD_BRACELET;
			break;
		case 79:
			data = JewelleryItem.SAPPHIRE_BRACELET;
			break;
		case 81:
			data = JewelleryItem.EMERALD_BRACELET;
			break;
		case 83:
			data = JewelleryItem.RUBY_BRACELET;
			break;
		case 85:
			data = JewelleryItem.DIAMOND_BRACELET;
			break;
		case 87:
			data = JewelleryItem.DRAGONSTONE_BRACELET;
			break;
		case 89:
			data = JewelleryItem.ONYX_BRACELET;
			break;
		}
		if (data == null) {
			return true;
		}
		if (player.getSkills().getLevel(Skills.CRAFTING) < data.getLevel()) {
			String an = StringUtils.isPlusN(ItemDefinition.forId(data.getSendItem()).getName().toLowerCase()) ? "an" : "a";
			player.getPacketDispatch().sendMessage("You need a crafting level of " + data.getLevel() + " to craft " + an + " " + ItemDefinition.forId(data.getSendItem()).getName().toLowerCase() + ".");
			return true;
		}
		String name = ItemDefinition.forId(data.getSendItem()).getName().toLowerCase();
		boolean flag = false;
		if (name.contains("ring") && !player.getInventory().contains(RING_MOULD, 1)) {
			flag = true;
		}
		if (name.contains("necklace") && !player.getInventory().contains(NECKLACE_MOULD, 1)) {
			flag = true;
		}
		if (name.contains("amulet") && !player.getInventory().contains(AMULET_MOULD, 1)) {
			flag = true;
		}
		if (name.contains("bracelet") && !player.getInventory().contains(BRACELET_MOULD, 1)) {
			flag = true;
		}
		if (flag) {
			player.getPacketDispatch().sendMessage("You don't have the required mould to make this.");
			return flag;
		}
		switch (opcode) {
		case 155:
			amount = 1;
			break;
		case 196:
			amount = 5;
			break;
		case 124:
			if (data.name().contains("GOLD")) {
				amount = player.getInventory().getAmount(new Item(GOLD_BAR));
			} else {
				int first = player.getInventory().getAmount(new Item(data.getItems()[0]));
				int second = player.getInventory().getAmount(new Item(data.getItems()[1]));
				if (first == second) {
					amount = first;
				} else if (first > second) {
					amount = second;
				} else {
					amount = first;
				}
			}
			break;
		case 199:
			final JewelleryItem d = data;
			sendInputDialogue(player, true, "Enter the amount:", (value) -> {
				JewelleryCrafting.make(player, d, (int) value);
				return Unit.INSTANCE;
			});
			return true;
		}
		if(!player.getSlayer().flags.isRingUnlocked() && data.equals(JewelleryItem.SLAYER_RING)){
			player.sendMessages("You don't know how to make this. Talk to any Slayer master in order to learn the", "ability that creates Slayer rings.");
			return true;
		}
		JewelleryCrafting.make(player, data, amount);
		return true;
	}

}
