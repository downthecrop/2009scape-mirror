package core.game.content.dialogue;

import static api.ContentAPIKt.*;
import core.cache.def.impl.ItemDefinition;
import core.game.component.Component;
import kotlin.Unit;
import rs09.game.system.SystemLogger;
import core.game.node.entity.skill.crafting.armour.DragonCraftPulse;
import core.game.node.entity.skill.crafting.armour.HardCraftPulse;
import core.game.node.entity.skill.crafting.armour.LeatherCrafting;
import core.game.node.entity.skill.crafting.armour.LeatherCrafting.DragonHide;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.RunScript;
import core.plugin.Initializable;
import core.game.node.item.Item;

/**
 * Represents the dialogue plugin used for leather crafting.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class LeatherCraftDialogue extends DialoguePlugin {

	/**
	 * Represents the type.
	 */
	private String type = "";

	/**
	 * Represents the leather.
	 */
	private int leather;

	/**
	 * Constructs a new {@code LeatherCraftDialogue} {@code Object}.
	 */
	public LeatherCraftDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code LeatherCraftDialogue} {@code Object}.
	 * @param player the player.
	 */
	public LeatherCraftDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new LeatherCraftDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		Component component = new Component(304);
		type = (String) args[0];
		if (type.equals("hard")) {
			player.getInterfaceManager().openChatbox(309);
			player.getPacketDispatch().sendItemZoomOnInterface(1131, 150, 309, 2);
			player.getPacketDispatch().sendString("<br><br><br><br>" + "Heardleather body", 309, 6);
		} else {
			/** dragon */
			leather = (int) args[1];
			player.getInterfaceManager().openChatbox(component);
			int index[] = new int[3];
			if (leather == LeatherCrafting.GREEN_LEATHER) {
				index[0] = DragonHide.GREEN_D_HIDE_BODY.getProduct();
				index[1] = DragonHide.GREEN_D_HIDE_VAMBS.getProduct();
				index[2] = DragonHide.GREEN_D_HIDE_CHAPS.getProduct();
			}
			if (leather == LeatherCrafting.BLUE_LEATHER) {
				index[0] = DragonHide.BLUE_D_HIDE_BODY.getProduct();
				index[1] = DragonHide.BLUE_D_HIDE_VAMBS.getProduct();
				index[2] = DragonHide.BLUE_D_HIDE_CHAPS.getProduct();
			}
			if (leather == LeatherCrafting.RED_LEATHER) {
				index[0] = DragonHide.RED_D_HIDE_BODY.getProduct();
				index[1] = DragonHide.RED_D_HIDE_VAMBS.getProduct();
				index[2] = DragonHide.RED_D_HIDE_CHAPS.getProduct();
			}
			if (leather == LeatherCrafting.BLACK_LEATHER) {
				index[0] = DragonHide.BLACK_D_HIDE_BODY.getProduct();
				index[1] = DragonHide.BLACK_D_HIDE_VAMBS.getProduct();
				index[2] = DragonHide.BLACK_D_HIDE_CHAPS.getProduct();
			}
			player.getPacketDispatch().sendItemZoomOnInterface(index[0], 175, 304, 2);
			player.getPacketDispatch().sendItemZoomOnInterface(index[1], 175, 304, 3);
			player.getPacketDispatch().sendItemZoomOnInterface(index[2], 165, 304, 4);
			player.getPacketDispatch().sendString("<br><br><br><br>" + ItemDefinition.forId(index[0]).getName(), 304, 8);
			player.getPacketDispatch().sendString("<br><br><br><br>" + ItemDefinition.forId(index[1]).getName(), 304, 11);
			player.getPacketDispatch().sendString("<br><br><br><br>" + ItemDefinition.forId(index[2]).getName(), 304, 16);
		}
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		player.getInterfaceManager().closeChatbox();
		int amt = 0;
		switch (type) {
		case "hard":
			switch (buttonId) {
			case 6:
				amt = 1;
				break;
			case 4:
				amt = 5;
				break;
			case 3:
				sendInputDialogue(player, true, "Enter the amount:", (value) -> {
					player.getPulseManager().run(new HardCraftPulse(player, null, (int) value));
					return Unit.INSTANCE;
				});
				return true;
			case 2:
				amt = player.getInventory().getAmount(new Item(LeatherCrafting.HARD_LEATHER));
				break;
			}
			player.getPulseManager().run(new HardCraftPulse(player, null, amt));
			break;
		case "dragon":
			int index = 0;
			if (buttonId > 3 && buttonId < 8) {
				index = 1;
			}
			if (buttonId > 7 && buttonId < 12) {
				index = 2;
			}
			if (buttonId > 11 && buttonId < 16) {
				index = 3;
			}
			DragonHide hide = null;
			if (index == 1) {
				switch (leather) {
				case LeatherCrafting.GREEN_LEATHER:
					hide = DragonHide.GREEN_D_HIDE_BODY;
					break;
				case LeatherCrafting.BLUE_LEATHER:
					hide = DragonHide.BLUE_D_HIDE_BODY;
					break;
				case LeatherCrafting.RED_LEATHER:
					hide = DragonHide.RED_D_HIDE_BODY;
					break;
				case LeatherCrafting.BLACK_LEATHER:
					hide = DragonHide.BLACK_D_HIDE_BODY;
					break;
				}
			}
			if (index == 2) {
				switch (leather) {
				case LeatherCrafting.GREEN_LEATHER:
					hide = DragonHide.GREEN_D_HIDE_VAMBS;
					break;
				case LeatherCrafting.BLUE_LEATHER:
					hide = DragonHide.BLUE_D_HIDE_VAMBS;
					break;
				case LeatherCrafting.RED_LEATHER:
					hide = DragonHide.RED_D_HIDE_VAMBS;
					break;
				case LeatherCrafting.BLACK_LEATHER:
					hide = DragonHide.BLACK_D_HIDE_VAMBS;
					break;
				}
			}
			if (index == 3) {
				switch (leather) {
				case LeatherCrafting.GREEN_LEATHER:
					hide = DragonHide.GREEN_D_HIDE_CHAPS;
					break;
				case LeatherCrafting.BLUE_LEATHER:
					hide = DragonHide.BLUE_D_HIDE_CHAPS;
					break;
				case LeatherCrafting.RED_LEATHER:
					hide = DragonHide.RED_D_HIDE_CHAPS;
					break;
				case LeatherCrafting.BLACK_LEATHER:
					hide = DragonHide.BLACK_D_HIDE_CHAPS;
					break;
				}
			}
			switch (buttonId) {
			case 7:
			case 11:
			case 15:
				amt = 1;
				break;
			case 6:
			case 10:
			case 14:
				amt = 5;
				break;
			case 5:
			case 9:
			case 13:
				amt = 10;
				break;
			case 4:
			case 8:
			case 12:
				final DragonHide hidee = hide;
				if (hidee == null) {
					return false;
				}
				sendInputDialogue(player, true, "Enter the amount:", (value) -> {
					player.getPulseManager().run(new DragonCraftPulse(player, null, hidee, (int) value));
					return Unit.INSTANCE;
				});
				return true;
			}
			if (hide == null) {
				return false;
			}
			player.getPulseManager().run(new DragonCraftPulse(player, null, hide, amt));
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 48923 };
	}
}
