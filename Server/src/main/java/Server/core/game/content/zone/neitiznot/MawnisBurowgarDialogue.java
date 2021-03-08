package core.game.content.zone.neitiznot;

import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;

/**
 * Handles the mawnis burowgar dialogue.
 * @author Vexia
 */
public class MawnisBurowgarDialogue extends DialoguePlugin {

	/**
	 * The helmet of neitiznot.
	 */
	private static final Item HELM = new Item(10828);

	/**
	 * Constructs a new {@code MawnisBurowgarDialogue} {@code Object}
	 */
	public MawnisBurowgarDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code MawnisBurowgarDialogue} {@code Object}
	 * @param player the player.
	 */
	public MawnisBurowgarDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new MawnisBurowgarDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc("It makes me proud to know that the helm of my", "ancestors will be worn in battle.");
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			npc("I thank you on behalf of all my kinsmen Talvald.");
			stage++;
			break;
		case 1:
			if (player.hasItem(HELM) || player.hasItem(new Item(10843))) {
				end();
				break;
			}
			player("Ah yes, about that beautiful helmet.");
			stage++;
			break;
		case 2:
			npc("You mean the priceless heirloom that I have to you as", "a sign of my trust and gratitude?");
			stage++;
			break;
		case 3:
			player("Err yes, that one. I may have mislaid it.");
			stage++;
			break;
		case 4:
			npc("It's a good job I have alert and loyal men who notice", "when something like this is left lying around and picks it", "up.");
			stage++;
			break;
		case 5:
			npc("I'm afraid I'm going to have to charge you a", "50,000GP handling cost.");
			stage++;
			break;
		case 6:
			if (!player.getInventory().contains(995, 50000)) {
				player("I don't have that much money on me.");
				stage++;
				break;
			}
			if (player.getInventory().remove(new Item(995, 50000))) {
				player.getInventory().add(HELM, player);
				interpreter.sendItemMessage(HELM, "The Burgher hands you his crown.");
				stage = 8;
			}
			break;
		case 7:
			npc("Well, come back when you do.");
			stage++;
			break;
		case 8:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 5504 };
	}

}
