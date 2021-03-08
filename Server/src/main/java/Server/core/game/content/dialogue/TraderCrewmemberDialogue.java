package core.game.content.dialogue;

import core.game.content.global.travel.ship.ShipCharter;
import core.game.content.global.travel.ship.ShipCharter.Destination;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.tools.StringUtils;

/**
 * Handles the TraderCrewmemberDialogue dialogue.
 * @author 'Vexia
 */
@Initializable
public class TraderCrewmemberDialogue extends DialoguePlugin {

	/**
	 * Represents the ship charter destination.
	 */
	private Destination destination;

	/**
	 * Represents the cost of charting the ship.
	 */
	private int cost;

	/**
	 * Constructs a new {@code TraderCrewmemberDialogue} {@code Object}.
	 */
	public TraderCrewmemberDialogue() {
	}

	/**
	 * Constructs a new {@code TraderCrewmemberDialogue} {@code Object}.
	 * @param player the player.
	 */
	public TraderCrewmemberDialogue(Player player) {
		super(player);
	}

	@Override
	public int[] getIds() {
		return new int[] { 4651, 4652, 4653, 4654, 4655, 4656, 4650 };
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			interpreter.sendOptions("Choose an option:", "Yes, who are you?", "Yes, I would like to charter a ship.");
			stage = 1;
			break;
		case 1:
			switch (buttonId) {
			case 1:
				player("Yes, who are you?");
				stage = 100;
				break;
			case 2:
				player("Yes, I would like to charter a ship.");
				stage = 2000;
				break;
			}
			break;
		case 100:
			npc("I'm one of the Trader Stan's crew; we are all part of the", "largest fleet of trading and sailing vessels to ever sail the", "seven seas.");
			stage = 101;
			break;
		case 101:
			npc("If you want to get to a port in a hurry then you can", "charter one of our ships to take you there - if the price", "is right...");
			stage = 102;
			break;
		case 102:
			player("So, where exactly can I go with your ships?");
			stage = 103;
			break;
		case 103:
			npc("We run ships from Port Phasmatys over to Port Tyras,", "stopping at Port Sarim, Catherby, Jaramja,", "the Shipyard and Port Khazard.");
			stage = 104;
			break;
		case 104:
			player("Wow, that's a lot of ports. I take it you have some exotic", "stuff to trade?");
			stage = 105;
			break;
		case 105:
			npc("We certainly do! We have access to items", "bought and sold from around the world.");
			stage = 106;
			break;
		case 106:
			npc("Would you like to take a look?");
			stage = 107;
			break;
		case 107:
			options("Yes.", "No.");
			stage = 108;
			break;
		case 108:
			switch (buttonId) {
			case 1:
				player("Yes.");
				stage = 1000;
				break;
			case 2:
				end();
				break;

			}
			break;
		case 1000:
			end();
			npc.openShop(player);
			break;
		case 2000:
			npc("Certainly sir, where would you like to go?");
			stage = 2001;
			break;
		case 2001:
			end();
			ShipCharter.open(player);
			break;
		case 3000:
			options("Ok", "Choose again", "No");
			stage = 30001;
			break;
		case 30001:
			switch (buttonId) {
			case 1:
				end();
				if (cost == 0) {
					destination.sail(player);
					break;
				}
				if (!player.getInventory().containsItem(new Item(995, cost))) {
					end();
					return true;
				}
				if (!player.getInventory().remove(new Item(995, cost))) {
					interpreter.sendDialogues(player, null, "I don't have the money for that.");
					stage = 30002;
					return true;
				}
				destination.sail(player);
				break;
			case 2:
				end();
				ShipCharter.open(player);
				break;
			case 3:
				end();
				break;
			}
			break;
		case 30002:
			end();
			break;
		}
		return true;
	}

	@Override
	public DialoguePlugin newInstance(Player player) {

		return new TraderCrewmemberDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		if (args.length > 1) {
			destination = ((Destination) args[1]);
			cost = (int) args[2];
			interpreter.sendDialogue("To sail to " + StringUtils.formatDisplayName(destination.name()) + " from here will cost you " + cost + " gold.", "Are you sure you want to pay that?");
			stage = 3000;
			return true;
		}
		npc("Can I help you?");
		stage = 0;
		return true;
	}
}
