package core.game.node.entity.npc.city.tzhaar;

import core.game.content.dialogue.DialogueInterpreter;
import core.game.content.dialogue.DialoguePlugin;
import core.game.content.dialogue.FacialExpression;
import core.game.node.entity.npc.NPC;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;
import rs09.game.world.GameWorld;

/**
 * Handles the TzHaarMejJal dialogue.
 * @author 'Vexia
 * @author Empathy
 * @author Logg
 */
@Initializable
public class TzHaarMejJah extends DialoguePlugin {
	private static final Item APPEARANCE_FEE = new Item(6529, 8000); // 8000 tokkul, about the same as you get from failing jad

	public TzHaarMejJah() {

	}

	public TzHaarMejJah(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {

		return new TzHaarMejJah(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "You want help JalYt-Ket-" + player.getUsername() + "?");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			if (GameWorld.getSettings().getJad_practice_enabled()) {
				if (player.getAttribute("fc_practice_jad", false)) {
					interpreter.sendOptions("Select an Option", player.getInventory().containItems(6570) ? "I have a fire cape here." : "What is this place?", "What did you call me?", "About my challenge...", "No I'm fine thanks.");
				} else {
					interpreter.sendOptions("Select an Option", player.getInventory().containItems(6570) ? "I have a fire cape here." : "What is this place?", "What did you call me?", "I want to challenge Jad directly.", "No I'm fine thanks.");
				}
			} else {
				interpreter.sendOptions("Select an Option", player.getInventory().containItems(6570) ? "I have a fire cape here." : "What is this place?", "What did you call me?", "No I'm fine thanks.");
			}
			stage = 1;
			break;
		case 1:
			switch (buttonId) {
			case 1:
				if (player.getInventory().containItems(6570)) {
					interpreter.open(DialogueInterpreter.getDialogueKey("firecape-exchange"), npc);
					break;
				}
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "What is this place?");
				stage = 10;
				break;
			case 2:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "What did you call me?");
				stage = 20;
				break;
			case 3:
				if (GameWorld.getSettings().getJad_practice_enabled()) {
					if (player.getAttribute("fc_practice_jad", false)) {
						interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "About my challenge...");
						stage = 64;
					} else {
						interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "The challenge is too long.", "I want to challenge Jad directly.");
						stage = 50;
					}
				} else {
					interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "No I'm fine thanks.");
					stage = 30;
				}
				break;
			case 4:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "No I'm fine thanks.");
				stage = 30;
				break;
			}
			break;
		case 10:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "This is the fight caves, TzHaar-Xil made it for practice,", "but many JalYt come here to fight too.", "Just enter the cave and make sure you're prepared.");
			stage = 11;
			break;
		case 11:
			interpreter.sendOptions("Select an Option", "Are there any rules?", "Ok thanks.");
			stage = 12;
			break;
		case 12:
			switch (buttonId) {
			case 1:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Are there any rules?");
				stage = 14;
				break;
			case 2:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Ok thanks.");
				stage = 13;
				break;
			}
			break;
		case 13:
			end();
			break;
		case 14:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Rules? Survival is the only rule in there.");
			stage = 15;
			break;
		case 15:
			interpreter.sendOptions("Select an Option", "Do I win anything?", "Sounds good.");
			stage = 16;
			break;
		case 16:
			switch (buttonId) {
			case 1:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Do I win anything?");
				stage = 17;
				break;
			case 2:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Sounds good.");
				stage = 13;
				break;

			}
			break;
		case 17:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "You ask a lot of questions.", "Might give you TokKul if you last long enough.");
			stage = 18;
			break;
		case 18:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "...");
			stage = 19;
			break;
		case 19:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Before you ask, TokKul is like your Coins.");
			stage = 500;
			break;
		case 500:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Gold is like you JalYt, soft and easily broken, we use", "hard rock forged in fire like TzHaar!");
			stage = 501;
			break;
		case 501:
			end();
			break;
		case 20:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Are you not JalYt-Ket?");
			stage = 21;
			break;
		case 21:
			interpreter.sendOptions("Select an Option", "What's a 'JalYt-Ket'?", "I guess so...", "No I'm not!");
			stage = 22;
			break;
		case 22:
			switch (buttonId) {
			case 1:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "What's a 'JalYt-Ket'?");
				stage = 100;
				break;
			case 2:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "I guess so...");
				stage = 200;
				break;
			case 3:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "No I'm not!");
				stage = 300;
				break;
			}
			break;
		case 100:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "That what you are... you tough and strong no?");
			stage = 101;
			break;
		case 101:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Well yes I suppose I am...");
			stage = 102;
			break;
		case 102:
			end();
			break;
		case 200:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "I guess so....");
			stage = 201;
			break;
		case 201:
			end();
			break;
		case 300:
			end();
			break;
		case 30:
			end();
			break;

		case 50:
			interpreter.sendDialogues(npc, FacialExpression.DISGUSTED_HEAD_SHAKE, "I thought you strong and tough", "but you want skip endurance training?");
			stage = 57;
			break;
		case 57:
			interpreter.sendDialogues(npc, FacialExpression.NEUTRAL, "Maybe you not JalYt-Ket afterall.");
			stage = 58;
			break;
		case 58:
			interpreter.sendOptions("Select an Option", "I don't have time for it, man.", "No, I'm JalYt-Ket!");
			stage = 51;
			break;
		case 51:
			switch (buttonId) {
			case 1:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "I don't have time for it, man.");
				stage = 52;
				break;
			case 2:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "No, I'm JalYt-Ket! I swear!", "I'll do the training properly.");
				stage = 30;
				break;
			}
			break;
		case 52:
			interpreter.sendDialogues(npc, FacialExpression.DISGUSTED_HEAD_SHAKE, "JalYt, you know you not get reward","if you not do training properly, ok?");
			stage = 56;
			break;
		case 56:
			interpreter.sendOptions("Select an Option", "That's okay, I don't need a reward.", "Oh, nevermind then.");
			stage = 53;
			break;
		case 53:
			switch (buttonId) {
			case 1:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "That's okay, I don't need a reward.");
				stage = 54;
				break;
			case 2:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Oh, nevermind then.");
				stage = 30;
				break;
			}
			break;
		case 54:
			interpreter.sendDialogues(player, FacialExpression.NEUTRAL, "I just wanna fight the big guy.");
			stage = 55;
			break;
		case 55:
			interpreter.sendDialogues(npc, FacialExpression.NEUTRAL, "Okay JalYt.","TzTok-Jad not show up for just anyone.");
			stage = 59;
			break;
		case 59:
			interpreter.sendDialogues(npc, FacialExpression.NEUTRAL, "You give 8000 TokKul, TzTok-Jad know you serious.", "You get it back if you victorious.");
			stage = 60;
			break;
		case 60:
			interpreter.sendOptions("Select an Option", "That's fair, here's 8000 TokKul.", "I don't have that much on me, but I'll go get it.", "TzTok-Jad must be old and tired to not just accept my challenge.");
			stage = 61;
		case 61:
			switch (buttonId) {
			case 1:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "That's fair, here's 8000 TokKul.");
				if (!player.getInventory().containsItem(APPEARANCE_FEE)) {
					stage = 62;
					break;
				}
				if (player.getInventory().remove(APPEARANCE_FEE)) {
					stage = 69;
				} else {
					stage = 62;
				}
				break;
			case 2:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "I don't have that much on me, but I'll go get it.");
				stage = 30;
				break;
			case 3:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "TzTok-Jad must be old and tired", "to not just accept my challenge.");
				stage = 63;
				break;
			}
			break;
		case 62:
			interpreter.sendDialogues(npc, FacialExpression.NEUTRAL, "JalYt, you not have the TokKul.", "You come back when you are serious.");
			stage = 30;
			break;
		case 63:
			interpreter.sendDialogues(npc, FacialExpression.ANGRY, "JalYt-Mor, you the old and tired one.", "You the one not want to do proper training.");
			stage = 30;
			break;
		case 64:
			interpreter.sendDialogues(npc, FacialExpression.NEUTRAL, "TzTok-Jad is waiting for you.", "Do not make TzTok-Jad wait long.");
			stage = 30;
			break;
		case 69:
			interpreter.sendDialogues(npc, FacialExpression.NEUTRAL, "Okay JalYt. Enter cave when you are prepared.", "You find TzTok-Jad waiting for JalYt challenger.");
			player.setAttribute("fc_practice_jad", true);
			stage = 30;
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { DialogueInterpreter.getDialogueKey("tzhaar-mej"), 2617 };
	}
}
