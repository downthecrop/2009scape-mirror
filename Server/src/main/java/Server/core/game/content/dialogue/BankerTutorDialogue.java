package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Represents the banker tutor dialogue.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class BankerTutorDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code BankerTutorDialogue} {@code Object}.
	 * @param player the player.
	 */
	public BankerTutorDialogue(final Player player) {
		super(player);
	}

	/**
	 * Constructs a new {@code BankerTutorDialogue} {@code Object}.
	 */
	public BankerTutorDialogue() {
		/**
		 * empty.
		 */
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new BankerTutorDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = args[0] instanceof NPC ? (NPC) args[0] : null;
		npc("Good day, how may I help you?");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			options("How do I use the bank?", "I'd like to access my bank account please.", "I'd like to check my PIN settings.");
			stage = 1;
			break;
		case 1:
			switch (buttonId) {
			case 1:
				options("Using the bank itself.", "Using Bank deposit boxes.", "What's this PIN thing that people keep talking about?", "Goodbye.");
				stage = 9;
				break;
			case 2:
				end();
				player.getBank().open();
				break;
			case 3:
				end();
				player.getBankPinManager().openSettings();
				break;
			}
			break;
		case 9:
			switch (buttonId) {
			case 1:
				player("Using the bank itself. I'm not sure how....?");
				stage = 10;
				break;
			case 2:
				player("Using Bank deposit boxes.... what are they?");
				stage = 20;
				break;
			case 3:
				player("What's this PIN thing that people keep talking about?");
				stage = 30;
				break;
			case 4:
				player("Goodbye.");
				stage = 99;
				break;
			}
			break;
		case 10:
			npc("Speak to any banker and ask to see your bank", "account. If you have set a PIN you will be asked for", "it, then all the belongings you have placed in the bank", "will appear in the window. To withdraw one item, left-");
			stage = 11;
			break;
		case 11:
			npc("click on it once.");
			stage = 12;
			break;
		case 12:
			npc("To withdraw many, right-click on the item and select", "from the menu. The same for depositing, left-click on", "the item in your inventory to deposit it in the bank.", "Right-click on it to deposit many of the same items.");
			stage = 13;
			break;
		case 13:
			npc("To move things around in your bank: firstly select", "Swap or Insert as your default moving mode, you can", "find these buttons on the bank window itself. Then click", "and drag an item to where you want it to appear.");
			stage = 14;
			break;
		case 14:
			npc("You may withdraw 'notes' or 'certificates' when the", "items you are trying to withdraw do not stack in your", "inventory. This will only work for items which are", "tradeable.");
			stage = 15;
			break;
		case 15:
			npc("For instance, if you wanted to sell 100 logs to another", "player, they would not fit in one inventory and you", "would need to do multiple trades. Instead, click the", "Note button to do withdraw the logs as 'certs' or 'notes',");
			stage = 16;
			break;
		case 16:
			npc("then withdraw the items you need.");
			stage = 99;
			break;
		case 20:
			npc("They look like grey pillars, there's one just over there,", "near the desk. Bank deposit boxes save so much time.", "If you're simply wanting to deposit a single item, 'Use'", "it on the deposit box.");
			stage = 21;
			break;
		case 21:
			npc("Otherwise, simply click once on the box and it will give", "you a choice of what to deposit in an interface very", "similar to the bank itself. Very quick for when you're", "simply fishing or mining etc.");
			stage = 22;
			break;
		case 22:
			end();
			break;
		case 30:
			npc("The PIN - Personal Identification Number - can be", "set on your bank account to protect the items there in", "case someone finds out your account password. It", "consists of four numbers that you remember and tell");
			stage = 31;
			break;
		case 31:
			npc("no one.");
			stage = 32;
			break;
		case 32:
			npc("So if someone did manage to get your password they", "couldn't steal your items if they were in the bank.");
			stage = 33;
			break;
		case 33:
			end();
			break;
		case 99:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 4907 };
	}

}
