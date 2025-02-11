package content.region.fremennik.dialogue;

import core.game.dialogue.DialoguePlugin;
import content.data.GodBook;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;

import java.util.ArrayList;
import java.util.List;

import static core.api.ContentAPIKt.addItemOrDrop;
import static core.api.ContentAPIKt.hasAnItem;

/**
 * Handles the dialogue for Jossik.
 * @author Vexia
 */
@Initializable
public class JossikDialogue extends DialoguePlugin {

	/**
	 * The uncompleted list of books.
	 */
	private List<GodBook> uncompleted;

	/**
	 * Constructs a new {@code JossikDialogue} {@code Object}
	 */
	public JossikDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code JossikDialogue} {@code Object}
	 * @param player the player.
	 */
	public JossikDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new JossikDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		npc("Hello again, adventurer.", "What brings you this way?");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			options("Can I see your wares?", "Have you found any prayerbooks?");
			stage++;
			break;
		case 1:
			if (buttonId == 1) {
				player("Can I see your wares?");
				stage = 10;
			} else {
				player("Have you found any prayerbooks?");
				stage = 20;
			}
			break;
		case 20:
			boolean missing = false;
			for (GodBook book : GodBook.values()) {
				if (player.getSavedData().getGlobalData().hasCompletedGodBook(book) && hasAnItem(player, book.getBook().getId(), true).getContainer() == null) {
					// i.e.: if you have a completed book on file but you lost it
					missing = true;
					addItemOrDrop(player, book.getBook().getId(), 1);
				}
			}
			int damaged = player.getSavedData().getGlobalData().getGodBook();
			if (damaged != -1 && hasAnItem(player, GodBook.values()[damaged].getDamagedBook().getId(), true).getContainer() == null) {
				// i.e.: if you have an uncompleted book on file but you lost it
				missing = true;
				addItemOrDrop(player, GodBook.values()[damaged].getDamagedBook().getId(), 1);
			}
			if (missing) {
				npc("As a matter of fact, I did! This book washed up on the", "beach, and I recognised it as yours!");
				stage = 23;
				return true;
			}
			uncompleted = new ArrayList<>(3);
			for (GodBook book : GodBook.values()) {
				if (!player.getSavedData().getGlobalData().hasCompletedGodBook(book)) {
					uncompleted.add(book);
				}
			}
			boolean hasUncompleted = false;
			for (GodBook book : GodBook.values()) {
				if (hasAnItem(player,book.getDamagedBook().getId(), true).getContainer() != null) {
					// i.e.: you have an uncompleted book on file and you still have it -> do not allow the player to get a new one, GL #2035
					hasUncompleted = true;
				}
			}
			if (uncompleted.isEmpty() || hasUncompleted) {// all completed.
				npc("No, sorry adventurer, I haven't.");
				stage = 23;
				return true;
			}
			npc("Funnily enough I have! I found some books in caskets", "just the other day! I'll sell one to you for 5000 coins;", "what do you say?");
			stage++;
			break;
		case 21:
			String[] names = new String[uncompleted.size() + 1];
			for (int i = 0; i < uncompleted.size(); i++) {
				names[i] = uncompleted.get(i).getName();
			}
			names[names.length - 1] = "Don't buy anything.";
			options(names);
			stage++;
			break;
		case 22:
			if (buttonId - 1 > uncompleted.size() - 1) {
				player("Don't buy anything.");
				stage = 23;
				break;
			}
			if (!player.getInventory().contains(995, 5000)) {
				player("Sorry, I don't seem to have enough coins.");
				stage = 23;
				break;
			}
			if (player.getInventory().freeSlots() == 0) {
				player("Sorry, I don't have enough inventory space.");
				stage = 23;
				break;
			}
			GodBook purchase = uncompleted.get(buttonId - 1);
			if (purchase != null && player.getInventory().remove(new Item(995, 5000))) {
				npc("Here you go!");
				player.getSavedData().getGlobalData().setGodBook(purchase.ordinal());
				player.getInventory().add(purchase.getDamagedBook(), player);
				stage = 23;
			} else {
				end();
			}
			break;
		case 23:
			end();
			break;
		case 10:
			npc("Sure thing!", "I think you'll agree, my prices are remarkable!");
			stage++;
			break;
		case 11:
			npc.openShop(player);
			end();
			break;
		}
		return true;
	}

	/**
	 * Checks if the player has a god book.
	 * @return {@code True} if so.
	 */
	public boolean hasGodBook() {
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 1334, 1335 };
	}

}
