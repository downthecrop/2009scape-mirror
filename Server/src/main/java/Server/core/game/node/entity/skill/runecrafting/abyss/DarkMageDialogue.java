package core.game.node.entity.skill.runecrafting.abyss;

import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;

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
			options("Why not?", "What are you doing here?", "Can you repair my pouches?","Ok, Sorry");
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
				player("Can you repair my pouches, please?");
				stage = 50;
				break;
			case 4:
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
		case 50:
			npc("Fine, fine! Give them here.");
			stage++;
			break;
		case 51:
			repair();
			npc("There, I've repaired them all.","Now get out of my sight!");
			stage = 30;
			break;
		}
		return true;
	}

	/**
	 * Repairs pouches.
	 */
	private boolean repair() {
		player.pouchManager.getPouches().forEach((id, pouch) -> {
			pouch.setCurrentCap(pouch.getCapacity());
			pouch.setCharges(10);
			Item essence = null;
			if(!pouch.getContainer().isEmpty()){
				int ess = pouch.getContainer().get(0).getId();
				int amount = pouch.getContainer().getAmount(ess);
				essence = new Item(ess,amount);
			}
			pouch.remakeContainer();
			if(essence != null){
				pouch.getContainer().add(essence);
			}
			if(id != 5509) {
				if (player.getInventory().contains(id + 1, 1)) {
					player.getInventory().remove(new Item(id + 1, 1));
					player.getInventory().add(new Item(id, 1));
				}
				if (player.getBank().contains(id + 1, 1)) {
					player.getBank().remove(new Item(id + 1, 1));
					player.getBank().add(new Item(id, 1));
				}
			}
		});
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 2262 };
	}
}