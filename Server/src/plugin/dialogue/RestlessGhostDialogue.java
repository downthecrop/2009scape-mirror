package plugin.dialogue;

import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.content.dialogue.FacialExpression;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.node.entity.player.Player;

/**
 * Handles the RestlessGhostDialogue dialogue.
 * @author 'Vexia
 */
@InitializablePlugin
public class RestlessGhostDialogue extends DialoguePlugin {

	public RestlessGhostDialogue() {

	}

	public RestlessGhostDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {

		return new RestlessGhostDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(player, FacialExpression.NORMAL, "Hello ghost, how are you?");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			if (!player.getEquipment().contains(552, 1)) {
				interpreter.sendDialogues(npc, FacialExpression.NORMAL, "Wooo wooo wooooo!");
				stage = 1;
			} else {
				if (player.getQuestRepository().getQuest("The Restless Ghost").getStage(player) == 20) {
					interpreter.sendDialogues(npc, FacialExpression.NORMAL, "Not very good actually.");
					stage = 500;
					break;
				}
				if (player.getQuestRepository().getQuest("The Restless Ghost").getStage(player) == 30) {
					interpreter.sendDialogues(npc, FacialExpression.NORMAL, "How are you doing finding my skull?");
					stage = 520;
					break;
				}
				if (player.getQuestRepository().getQuest("The Restless Ghost").getStage(player) == 40) {
					interpreter.sendDialogues(npc, FacialExpression.NORMAL, "How are you doing finding my skull?");
					stage = 550;
					break;
				}
				interpreter.sendDialogues(npc, null, "Fine, thanks.");
				stage = 990;
			}
			break;
		case 990:
			end();
			break;
		case 500:
			interpreter.sendDialogues(player, FacialExpression.NORMAL, "What's the problem then?");
			stage = 501;
			break;
		case 501:
			interpreter.sendDialogues(npc, FacialExpression.NORMAL, "Did you just understand what I said???");
			stage = 502;
			break;
		case 502:
			interpreter.sendDialogues(player, FacialExpression.NORMAL, "Yep, now tell me what the problem is.");
			stage = 503;
			break;
		case 503:
			interpreter.sendDialogues(npc, FacialExpression.NORMAL, "WOW! This is INCREDIBLE! I didn't expect anyone", "to ever understand me again!");
			stage = 504;
			break;
		case 504:
			interpreter.sendDialogues(player, FacialExpression.NORMAL, "Ok, Ok, I can understand you!");
			stage = 505;
			break;
		case 505:
			interpreter.sendDialogues(player, FacialExpression.NORMAL, "But have you any idea WHY you're doomed to be a", "ghost?");
			stage = 506;
			break;
		case 506:
			interpreter.sendDialogues(npc, FacialExpression.NORMAL, "Well, to be honest... I'm not sure.");
			stage = 507;
			break;
		case 507:
			interpreter.sendDialogues(player, FacialExpression.NORMAL, "I've been told a certain task may need to be completed", "so you can rest in peace.");
			stage = 508;
			break;
		case 508:
			interpreter.sendDialogues(npc, FacialExpression.NORMAL, "I should think it is probably because a warlock has come", "along and stolen my skull. If you look inside my coffin", "there, you'll find my corpse without a head on it.");
			stage = 509;
			break;
		case 509:
			interpreter.sendDialogues(player, FacialExpression.NORMAL, "Do you know where this warlock might be now?");
			stage = 510;
			break;
		case 510:
			interpreter.sendDialogues(npc, FacialExpression.NORMAL, "I think it was one of the warlocks who lives in the big", "tower by the sea south-west from here.");
			stage = 511;
			break;
		case 511:
			interpreter.sendDialogues(player, FacialExpression.NORMAL, "Ok. I will try and get the skull back for you, then you", "can rest in peace.");
			player.getQuestRepository().getQuest("The Restless Ghost").setStage(player, 30);
			stage = 512;
			break;
		case 512:
			interpreter.sendDialogues(npc, FacialExpression.NORMAL, "Ooh, thank you. That would be such a great relief!");
			stage = 513;
			break;
		case 513:
			interpreter.sendDialogues(npc, FacialExpression.NORMAL, "It is so dull being a ghost...");
			stage = 514;
			break;
		case 514:
			end();
			break;
		case 520:
			if (player.getInventory().contains(964, 1)) {

				break;
			} else {
				interpreter.sendDialogues(player, FacialExpression.NORMAL, "Sorry, I can't find it at the moment.");
				stage = 521;
			}
			break;
		case 521:
			interpreter.sendDialogues(npc, FacialExpression.NORMAL, "Ah well. Keep on looking.");
			stage = 522;
			break;
		case 522:
			interpreter.sendDialogues(npc, FacialExpression.NORMAL, "I'm pretty sure it's somewhere in the tower south-west", "from here. There's a lot of levels to the tower, though. I", "suppose it might take a little while to find.");
			stage = 523;
			break;
		case 523:
			end();
			break;
		case 550:
			interpreter.sendDialogues(player, FacialExpression.NORMAL, "I have found it!");
			stage = 551;
			break;
		case 551:
			interpreter.sendDialogues(npc, FacialExpression.NORMAL, "Hurah! Now I can stop being a ghost! You just need", "to put it on my coffin there, and I will be free!");
			stage = 523;
			break;
		case 1:
			interpreter.sendOptions("Select an Option", "Sorry, I don't speak ghost.", "Ooh... THAT'S interesting.", "Any hints where I can find some treasure?");
			stage = 2;
			break;
		case 2:
			switch (buttonId) {
			case 1:
				interpreter.sendDialogues(player, FacialExpression.NORMAL, "Sorry, I don't speak ghost.");
				stage = 10;
				break;
			case 2:
				interpreter.sendDialogues(player, FacialExpression.NORMAL, "Ooh... THAT'S interesting.");
				stage = 20;
				break;
			case 3:
				interpreter.sendDialogues(player, FacialExpression.NORMAL, "Any hints wher I can find some treasure?");
				stage = 30;
				break;

			}
			break;
		case 10:
			interpreter.sendDialogues(npc, FacialExpression.NORMAL, "Woo woo?");
			stage = 11;
			break;
		case 11:
			interpreter.sendDialogues(player, FacialExpression.NORMAL, "Nope, still don't understand you.");
			stage = 12;
			break;
		case 12:
			interpreter.sendDialogues(npc, FacialExpression.NORMAL, "WOOOOOOOOO!");
			stage = 13;
			break;
		case 13:
			interpreter.sendDialogues(player, FacialExpression.NORMAL, "Never mind.");
			stage = 14;
			break;
		case 14:
			end();
			break;
		case 30:
			interpreter.sendDialogues(npc, FacialExpression.NORMAL, "Wooooooo woo! Wooooo woo wooooo woowoowoo wooo", "Woo woooo. Wooooo woo woo? Wooooooooooooooooooo!");
			stage = 31;
			break;
		case 31:
			interpreter.sendDialogues(player, FacialExpression.NORMAL, "Sorry, I don't speak ghost.");
			stage = 32;
			break;
		case 32:
			interpreter.sendDialogues(npc, FacialExpression.NORMAL, "Woo woo?");
			stage = 11;
			break;
		case 20:
			interpreter.sendDialogues(npc, FacialExpression.NORMAL, "Woo woooo. Woooooooooooooooooo!");
			stage = 21;
			break;
		case 21:
			interpreter.sendDialogues(player, FacialExpression.NORMAL, "Did he really?");
			stage = 22;
			break;
		case 22:
			interpreter.sendDialogues(npc, FacialExpression.NORMAL, "Woo.");
			stage = 23;
			break;
		case 23:
			interpreter.sendDialogues(player, FacialExpression.NORMAL, "My brother had EXACTLY the same problem.");
			stage = 24;
			break;
		case 24:
			interpreter.sendDialogues(npc, FacialExpression.NORMAL, "Woo Wooooo!");
			stage = 25;
			break;
		case 25:
			interpreter.sendDialogues(npc, FacialExpression.NORMAL, "Wooooo Woo woo woo!");
			stage = 26;
			break;
		case 26:
			interpreter.sendDialogues(player, FacialExpression.NORMAL, "Goodbye. Thanks for the chat.");
			stage = 27;
			break;
		case 27:
			interpreter.sendDialogues(npc, FacialExpression.NORMAL, "Wooo wooo?");
			stage = 28;
			break;
		case 28:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 457 };
	}
}
