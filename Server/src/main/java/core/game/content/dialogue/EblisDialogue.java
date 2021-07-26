package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;
import org.rs09.consts.Items;

/**
 * Handles Eblis's dialogue.
 * @author ceik
 * @version 1.0
 */
@Initializable
public class EblisDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code EblisDialogue} {@code Object}.
	 */
	public EblisDialogue() {
		/**
		 * Empty
		 */
	}

	/**
	 * Constructs a new {@code EblisDialogue} {@code Object}.
	 * @param player The player to construct the class for.
	 */
	public EblisDialogue(final Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new EblisDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		//TODO: Add proper dialogue once DT is implemented
		npc = (NPC) args[0];
		if(!player.getAttribute("DT:staff-bought",false)) {
			player("Hey.");
			stage = 0;
		} else {
			player("Say, what's up with your dialogue?");
			stage = 20;
		}
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		//TODO: Add proper dialogue once DT is implemented
		switch(stage){
			case 0:
				npc("Hey, you wanna buy this funny stick thing?");
				stage = 1;
				break;
			case 1:
				options("Yes please.","No thanks.");
				stage = 2;
				break;
			case 2:
				switch(buttonId){
					case 1:
						npc("Alright, that'll be 80,000 golden shekels.");
						stage = 10;
						break;
					case 2:
						player("Nah, I'm good, homie.");
						stage = 30;
						break;
				}
				break;
			case 10:
				player("Hmm.... ok.");
				stage = 11;
				break;
			case 11:
				if(!player.getInventory().contains(Items.COINS_995,80000)){
					npc("Oi vey... you dont have enough..");
				} else {
					player.getInventory().remove(new Item(Items.COINS_995,80000));
					player.getInventory().add(new Item(Items.ANCIENT_STAFF_4675));
				}
				stage = 30;
				break;
			case 20:
				npc("Well I don't have me bloody quest yet, mate","so I needed SOMETHING to say,","oi vey.");
				stage = 30;
				break;
			case 30:
				end();
				break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 1923 };
	}

}
