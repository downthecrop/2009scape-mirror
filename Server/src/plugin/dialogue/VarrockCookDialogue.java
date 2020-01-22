package plugin.dialogue;

import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.content.dialogue.FacialExpression;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.node.item.Item;

/**
 * Handles the VarrockCookDialogue dialogue.
 * @author 'Vexia
 */
@InitializablePlugin
public class VarrockCookDialogue extends DialoguePlugin {

	public VarrockCookDialogue() {

	}

	public VarrockCookDialogue(Player player) {
		super(player);
	}

	@Override
	public int[] getIds() {
		return new int[] { 5910 };
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {

		switch (stage) {
		case 0:
			interpreter.sendOptions("What would you like to say?", "Can you sell me any food?", "Can you give me any free food?", "I don't want anything from this horrible kitchen.");
			stage = 1;
			break;
		case 1:

			switch (buttonId) {
			case 1:
				interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Can you sell me any food?");
				stage = 10;
				break;
			case 2:
				interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Can you give me any free food?");
				stage = 20;
				break;
			case 3:
				interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "I don't want anything from this horrible kitchen.");
				stage = 30;
				break;
			}

			break;

		case 10:
			interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "I suppose I could sell you some cabbage, if you're willing to", "pay for it. Cabbage is good for you.");
			stage = 11;
			break;
		case 11:
			interpreter.sendOptions("What would you like to say?", "Alright, I'll buy a cabbage.", "No thanks, I don't like cabbage.");
			stage = 12;
			break;
		case 12:

			switch (buttonId) {
			case 1:
				interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Alright, I'll buy a cabbage.");
				stage = 150;
				break;
			case 2:
				interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "No thanks, I don't like cabbage.");
				stage = 160;
				break;
			}

			break;
		case 20:
			interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Can you give me any free money?");
			stage = 21;
			break;
		case 21:
			interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Why should I give you free money?");
			stage = 22;
			break;
		case 22:
			interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Why should I give you free food?");
			stage = 23;
			break;
		case 23:
			interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Oh, forget it.");
			stage = 24;
			break;
		case 24:
			end();
			break;
		case 30:
			interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "How dare you? I put a lot of effort into cleaning this", "kitchen. My daily sweat and elbow-grease keep this kitchen", "clean!");
			stage = 31;
			break;
		case 31:
			interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Ewww!");
			stage = 32;
			break;
		case 32:
			interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Oh, just leave me alone.");
			stage = 33;
			break;
		case 33:
			end();
			break;
		case 150:
			if (player.getInventory().contains(995, 1)) {
				player.getInventory().remove(new Item(995, 1));
				player.getInventory().add(new Item(1965, 1));
				interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "It's a deal. Now, make sure you eat it all up. Cabbage is", "good for you.");
				stage = 151;
			} else {
				end();
				player.getPacketDispatch().sendMessage("You need one coin to buy a cabbage.");
			}
			break;
		case 151:
			end();
			break;
		case 160:
			interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Bah! People these days only appreciate junk food.");
			stage = 161;
			break;
		case 161:
			end();
			break;
		}

		return true;
	}

	@Override
	public DialoguePlugin newInstance(Player player) {

		return new VarrockCookDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "What do you want? I'm busy!");
		stage = 0;
		return true;
	}
}
