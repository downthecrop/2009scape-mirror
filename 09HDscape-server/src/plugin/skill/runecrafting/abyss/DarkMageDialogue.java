package plugin.skill.runecrafting.abyss;

import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.content.skill.free.runecrafting.RunePouch;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the dark mages dialogue.
 * @author Vexia
 */
public final class DarkMageDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code DarkMageDialogue} {@code Object}.
	 */
	public DarkMageDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code DarkMageDialogue} {@code Object}.
	 * @param player the player.
	 */
	public DarkMageDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new DarkMageDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		if (args.length >= 2) {
			if (repair()) {
				npc("There, I have repaired your pouches.", "Now leave me alone. I'm concentrating.");
				stage = 30;
				return true;
			} else {
				npc("You don't seem to have any pouches in need of repair.", "Leave me alone.");
				stage = 30;
				return true;
			}
		}
		player("Hello there.");
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			npc("Quiet!", "You must not break my concentration!");
			stage++;
			break;
		case 1:
			options("Why not?", "What are you doing here?", "Ok, Sorry");
			stage++;
			break;
		case 2:
			switch (buttonId) {
			case 1:
				player("Why not?");
				stage = 10;
				break;
			case 2:
				player("What are you doing here?");
				stage = 20;
				break;
			case 3:
				player("Ok, sorry.");
				stage = 30;
				break;
			}
			break;
		case 10:
			npc("Well, if my concentration is broken while keeping this", "gate open, then if we are lucky, everyone within a one", "mile radius will either have their heads explode, or will be", "consumed internally by the creatures of the Abyss.");
			stage++;
			break;
		case 11:
			player("Erm...", "And if we are unlucky?");
			stage++;
			break;
		case 12:
			npc("If we are unlucky, then the entire universe will begin", "to fold in upon itself, and all reality as we know it will", "be annihilated in a single stroke.");
			stage++;
			break;
		case 13:
			npc("So leave me alone!");
			stage = 30;
			break;
		case 20:
			npc("Do you mean what am I doing here in Abyssal space,", "Or are you asking me what I consider my ultimate role", "to be in this voyage that we call life?");
			stage++;
			break;
		case 21:
			player("Um... the first one.");
			stage++;
			break;
		case 22:
			npc("By remaining here and holding this portal open, I am", "providing a permanent link between normal space and", "this strange dimension that we call Abyssal space.");
			stage++;
			break;
		case 23:
			npc("As long as this spell remains in effect, we have the", "capability to teleport into abyssal space at will.");
			stage++;
			break;
		case 24:
			npc("Now leave me be!", "I can afford no distraction in my task!");
			stage = 30;
			break;
		case 30:
			end();
			break;
		}
		return true;
	}

	/**
	 * Repairs pouches.
	 */
	private boolean repair() {
		List<Item> pouches = getPouchRepairs();
		if (pouches.size() == 0) {
			return false;
		}
		RunePouch rune = null;
		for (Item pouch : pouches) {
			rune = RunePouch.forItem(pouch);
			rune.repair(player, pouch);
		}
		return true;
	}

	/**
	 * Gets the pouches to repair.
	 * @return the pouches.
	 */
	public List<Item> getPouchRepairs() {
		List<Item> items = new ArrayList<>();
		for (RunePouch pouch : RunePouch.values()) {
			if (pouch == RunePouch.SMALL) {
				continue;
			}
			Item item = null;
			if (player.getInventory().containsItem(pouch.getPouch())) {
				item = player.getInventory().getItem(pouch.getPouch());
			} else if (player.getInventory().containsItem(pouch.getDecayedPouch())) {
				item = player.getInventory().getItem(pouch.getDecayedPouch());
			}
			if (item != null && pouch.hasDecay(player, item)) {
				items.add(item);
			}
		}
		return items;
	}

	@Override
	public int[] getIds() {
		return new int[] { 2262 };
	}
}