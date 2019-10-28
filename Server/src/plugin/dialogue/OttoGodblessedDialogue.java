package plugin.dialogue;

import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.node.item.Item;

/**
 * Represents the dialogue plugin used for Otto
 * @author Splinter
 * @version 1.0
 */
@InitializablePlugin
public final class OttoGodblessedDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code OttoGodblessedDialogue} {@code Object}.
	 */
	public OttoGodblessedDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code OttoGodblessedDialogue} {@code Object}.
	 * @param player the player.
	 */
	public OttoGodblessedDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new OttoGodblessedDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		npc("Good day, you seem a hearty warrior. Maybe even", "some barbarian blood in that body of yours?");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			player("Can you help me with Zamorakian weapons?");
			stage = 1;
			break;
		case 1:
			npc("Yes, I can convert a Zamorakian spear into a hasta.", "The spirits require me to request 300,000 coins from","you for this service.");
			stage = 2;
			break;
		case 2:
			interpreter.sendOptions("Select an Option", "Spear into hasta", "Hasta back to spear");
			stage = 3;
			break;
		case 3:
			switch(buttonId){
			case 1:
				if(player.getInventory().contains(11716, 1) && player.getInventory().contains(995, 300000)){
					interpreter.sendOptions("Convert your spear?", "Yes", "No");
					stage = 4;
				} else {
					player.sendMessage("You need a Zamorakian Spear and 300,000 coins to proceed.");
					end();
				}
				break;
			case 2:
				if(player.getInventory().contains(14662, 1)){
					interpreter.sendOptions("Revert back to spear?", "Yes", "No");
					stage = 5;
				} else {
					player.sendMessage("You need a Zamorakian Hasta to proceed.");
					end();
				}
				break;
			}
			break;
		case 4:
			switch(buttonId){
			case 1:
				if(player.getInventory().remove(new Item(11716, 1), new Item(995, 300000))){
					player.getInventory().add(new Item(14662));
					interpreter.sendItemMessage(14662, "Otto converts your spear into a hasta.");
					stage = 6;
				} else {
					end();
				}
				break;
			case 2:
				end();
				break;
			}
			break;
		case 5:
			switch(buttonId){
			case 1:
				if(player.getInventory().remove(new Item(14662, 1))){
					player.getInventory().add(new Item(11716));
					interpreter.sendItemMessage(11716, "Otto converts your hasta back into a spear.");
					stage = 6;
				} else {
					end();
				}
				break;
			case 2:
				end();
				break;
			}
			break;
		case 6:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 2725 };
	}
}
