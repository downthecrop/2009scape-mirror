package core.game.content.dialogue;

import core.game.content.global.Skillcape;
import core.game.node.item.Item;
import org.rs09.consts.Items;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Handles the BrotherJeredDialogue dialogue.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class BrotherJeredDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code BrotherJeredDialogue} {@code Object}.
	 */
	public BrotherJeredDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code BrotherJeredDialogue} {@code Object}.
	 * @param player the player.
	 */
	public BrotherJeredDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new BrotherJeredDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		if(player.getInventory().contains(Items.UNBLESSED_SYMBOL_1716,1)){
			player("Can you bless this symbol for me?");
			stage = 10;
			return true;
		}
		if(player.getInventory().contains(Items.HOLY_ELIXIR_13754,1) && player.getInventory().contains(Items.SPIRIT_SHIELD_13734,1)){
			player("Can you bless this spirit shield for me?");
			stage = 100;
			return true;
		}
		if (Skillcape.isMaster(player, Skills.PRAYER)) {
			player("Can I buy a Skillcape of Prayer?");
			stage = 2;
		} else {
			player("Praise to Saradomin!");
			stage = 0;
		}
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
			case 0:
				npc("Yes! Praise he who brings life to this world.");
				stage = 1;
				break;
			case 1:
			case 12:
				end();
				break;
			case 2:
				npc("Certainly! Right when you give me 99000 coins.");
				stage = 3;
				break;
			case 3:
				options("Okay, here you go.", "No");
				stage = 4;
				break;
			case 4:
				switch (buttonId) {
					case 1:
						player("Okay, here you go.");
						stage = 5;
						break;
					case 2:
						end();
						break;
				}
				break;
			case 5:
				if (Skillcape.purchase(player, Skills.PRAYER)) {
					npc("There you go! Enjoy.");
				}
				stage = 6;
				break;
			case 6:
				end();
				break;
			case 10:
				npc("Sure thing! Just give me a moment, here...");
				stage++;
				break;
			case 11:
				npc("There we go! I have laid the blessing of", "Saradomin upon it.");
				player.getInventory().remove(new Item(Items.UNBLESSED_SYMBOL_1716));
				player.getInventory().add(new Item(Items.HOLY_SYMBOL_1718));
				stage++;
				break;
			case 100:
				npc("Yes I certainly can, but for", "1,000,000 coins.");
				stage++;
				break;
			case 101:
				options("Okay, sounds good.","Oh, no thanks..");
				stage++;
				break;
			case 102:
				switch(buttonId){
					case 1:
						if(player.getInventory().contains(995,1000000)){
							player("Okay, sounds good!");
							stage = 110;
						} else {
							player("I'd like to but I don't have","the coin.");
							stage = 120;
						}
						break;
					case 2:
						player("Oh. No, thank you, then.");
						stage = 1000;
						break;
				}
				break;
			case 110:
				npc("Here you are.");
				if(player.getInventory().remove(new Item(Items.HOLY_ELIXIR_13754), new Item(Items.SPIRIT_SHIELD_13734),new Item(995,1000000))){
					player.getInventory().add(new Item(Items.BLESSED_SPIRIT_SHIELD_13736));
				}
				stage++;
				break;
			case 111:
				player("Thank you!");
				stage = 1000;
				break;
			case 120:
				npc("That's too bad then.");
				stage = 1000;
				break;
			case 1000:
				end();
				break;

		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 802 };
	}
}
