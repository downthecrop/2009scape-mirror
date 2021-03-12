package core.game.content.activity.magearena;

import core.game.content.dialogue.DialoguePlugin;
import core.game.content.global.GodType;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;

/**
 * Handles the chamber guardian dialogue.
 * @author Vexia
 */
public final class ChamberGuardianDialogue extends DialoguePlugin {

	/**
	 * The god type.
	 */
	private GodType godType;

	/**
	 * Constructs a new {@code ChamberGuardianDialogue} {@code Object}.
	 */
	public ChamberGuardianDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code ChamberGuardianDialogue} {@code Object}.
	 * @param player the player.
	 */
	public ChamberGuardianDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new ChamberGuardianDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		if (player.getSavedData().getActivityData().hasRecievedKolodionReward()) {
			player("Hello again.");
			return true;
		} else if (player.getSavedData().getActivityData().hasKilledKolodion()) {
			npc("Hello adventurer, have you made your choice?");
			return true;
		}
		npc("YOU SHOULD NOT BE IN HERE!");
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		if (player.getSavedData().getActivityData().hasRecievedKolodionReward()) {
			switch (stage) {
			case 0:
				npc("Hello adventurer, are you looking for another staff?");
				stage++;
				break;
			case 1:
				options("What do you have to offer?", "No thanks.");
				stage++;
				break;
			case 2:
				switch (buttonId) {
				case 1:
					end();
					npc.openShop(player);
					break;
				case 2:
					player("No thanks.");
					stage++;
					break;
				}
				break;
			case 3:
				npc("Well let me know if you need one.");
				stage++;
				break;
			case 4:
				end();
				break;
			}
			return true;
		} else if (player.getSavedData().getActivityData().hasKilledKolodion()) {
			switch (stage) {
			case 0:
				godType = GodType.getCape(player);
				if (godType == null) {
					player("Sorry, I'm still looking.");
					stage++;
				} else {
					player("I have.");
					stage += 2;
				}
				break;
			case 1:
				end();
				break;
			case 2:
				npc("Good, good, I hope you have chosen well. I will now", "present you with a magic staff. This, along with the", "cape awarded to you by your chosen god, are all the", "weapons and armour you will need here.");
				stage++;
				break;
			case 3:
				if (!player.getInventory().hasSpaceFor(godType.getStaff())) {
					player("Sorry, I don't have enough inventory space.");
					stage = 1;
					return true;
				}
				if (player.getInventory().containsItem(godType.getCape()) || player.getEquipment().containsItem(godType.getCape())) {
					stage++;
					player.getInventory().add(godType.getStaff());
					player.getSavedData().getActivityData().setKolodionStage(3);
					interpreter.sendItemMessage(godType.getStaff(), "The guardian hands you an ornate magic staff.");
				} else {
					end();
				}
				break;
			case 4:
				end();
				break;
			}
			return true;
		} else {
			end();
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 904 };
	}

}
