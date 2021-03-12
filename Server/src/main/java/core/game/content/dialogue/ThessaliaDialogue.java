package core.game.content.dialogue;

import core.game.component.Component;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.diary.DiaryType;
import core.plugin.Initializable;

/**
 * Represents the thessalia dialogue plugin.
 */

@Initializable
public final class ThessaliaDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code ThessaliaDialogue} {@code Object}.
	 */
	public ThessaliaDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code ThessaliaDialogue} {@code Object}.
	 * @param player the player.
	 */
	public ThessaliaDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new ThessaliaDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];

		//The trade argument is handled elsewhere
		if (args.length == 3) { //Right-Click 'Change-Clothes' Option
			if (player.getEquipment().isEmpty()) {
				player.getAchievementDiaryManager().finishTask(player, DiaryType.VARROCK, 0, 0);
				if (player.isMale()) {
					end();
					player.getInterfaceManager().open(new Component(591));
				} else {
					end();
					player.getInterfaceManager().open(new Component(594));
				}
			}
			else{ //Has some armour equipped
				interpreter.sendDialogues(548, FacialExpression.WORRIED, "You can't try them on while wearing armour. Take","it off and speak to me again.");
				stage = 52;
			}
			return true;
		}

		//Default Talk

		interpreter.sendDialogues(npc, FacialExpression.ASKING, "Would you like to buy any fine clothes?");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
			case 0:
				interpreter.sendOptions("Choose an option:", "What do you have?", "No, thank you.");
				stage++;
				break;
			case 1:
				switch (buttonId) {
					case 1:
						interpreter.sendDialogues(player, FacialExpression.ASKING, "What do you have?");
						stage = 10;
						break;
					case 2:
						interpreter.sendDialogues(player, FacialExpression.NEUTRAL, "No, thank you.");
						stage = 51;
						break;
				}
				break;

			case 10:
				interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "I have a number of fine pieces of clothing on sale or,", "if you prefer, I can offer you an exclusive", "total clothing makeover?");
				stage++;
				break;
			case 11:
				interpreter.sendOptions("Select an Option", "Tell me more about this makeover.", "I'd just like to buy some clothes.");
				stage++;
				break;

			case 12:
				switch (buttonId) {
					case 1:
						interpreter.sendDialogues(player, FacialExpression.THINKING, "Tell me more about this makeover.");
						stage = 20;
						break;
					case 2:
						interpreter.sendDialogues(player, FacialExpression.NEUTRAL, "I'd just like to buy some clothes.");
						stage = 50;
						break;
				}
				break;

			//More about the makeover
			case 20:
				interpreter.sendDialogues(npc, FacialExpression.HAPPY, "Certainly!");
				stage++;
				break;
			case 21:
				interpreter.sendDialogues(npc, FacialExpression.HAPPY, "Here at Thessalia's fine clothing boutique, we offer a", "unique service where we will totally revamp your outfit", "to your choosing, for... wait for it...");
				stage++;
				break;
			case 22:
				interpreter.sendDialogues(npc, FacialExpression.FRIENDLY, "A fee of only 500 gold coins! Tired of always wearing", "the same old outfit, day in, day out? This is the service", "for you!");
				stage++;
				break;
			case 23:
				interpreter.sendDialogues(npc, FacialExpression.ASKING, "So what do you say? Interested? We can change either", "your top, or your legwear for only 500 gold a item!");
				stage++;
				break;

			//Buying Clothes or changing outfit
			case 24:
				interpreter.sendOptions("Select an Option", "I'd like to change my outfit, please.",  "I'd just like to buy some clothes.");
				stage++;
				break;
			case 25:
				switch (buttonId) {
					case 1:
						interpreter.sendDialogues(player, FacialExpression.HAPPY, "I'd like to change my outfit, please.");
						stage = 30;
						break;
					case 2:
						interpreter.sendDialogues(player, FacialExpression.NEUTRAL, "I'd just like to buy some clothes.");
						stage = 50;
						break;
				}
				break;

			//Changing outfit code
			case 30:
				if(player.getEquipment().isEmpty()){
					interpreter.sendDialogues(npc, FacialExpression.HAPPY, "Just select what style and colour you would like from", "this catalogue, and then give me the 1000 gold when", "you've picked.");
					stage++;
					break;
				} else { //Has some armour equipped
					interpreter.sendDialogues(npc, FacialExpression.WORRIED, "You can't try them on while wearing armour. Take", "it off and speak to me again.");
					stage = 52;
					break;
				}
			case 31://Player has money and is not wearing armour/weapons
				if (player.getEquipment().isEmpty()) {
					player.getAchievementDiaryManager().finishTask(player, DiaryType.VARROCK, 0, 0);
					if (player.isMale()) {
						end();
						player.getInterfaceManager().open(new Component(591));
					} else {
						end();
						player.getInterfaceManager().open(new Component(594));
					}
				}
				break;

			//Closing Remarks
			case 49: //Not enough money
				interpreter.sendDialogues(npc, FacialExpression.FRIENDLY, "That's ok! Just come back when you do have it!");
				stage = 52;
				break;

			case 50://Just buying some clothes
				end();
				npc.openShop(player);
				break;

			case 51://No Thanks
				npc("Well, please return if you change your mind.");
				stage++;
				break;
			case 52:
				end();
				break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 548 };
	}
}
