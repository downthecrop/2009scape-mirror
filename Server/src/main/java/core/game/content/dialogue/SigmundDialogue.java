package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

import static rs09.tools.DialogueConstKt.END_DIALOGUE;


/**
 * Handles the SigmundDialogue dialogue.
 * @author 'Vexia
 */
@Initializable
public class SigmundDialogue extends DialoguePlugin {

	int[] TLTNPCS = {278,0,519,2244,3777};

	public SigmundDialogue() {

	}

	public SigmundDialogue(Player player) {
		super(player);
	}

	@Override
	public int[] getIds() {
		return new int[] { 2082, 2083, 2090, 3713, 3716, 3717, 3718, 3719, 3720, 4328, 4331, 4332, 4333, 4334, 4335 };
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Can I help you?");
		if(player.getQuestRepository().getQuest("Lost Tribe").getStage(player) > 0 && player.getQuestRepository().getQuest("Lost Tribe").getStage(player) < 100){
			npc("Have you found out what it was?");
			stage = 34;
			return true;
		}
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			interpreter.sendOptions("Select an Option", "Do you have any quests for me?", "Who are you?");
			stage = 1;
			break;
		case 1:
			switch (buttonId) {
			case 1:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Do you have any quests for me?");
				stage = 10;
				break;
			case 2:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Who are you?");
				stage = 20;
				break;

			}
			break;
		case 20:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "I'm the Duke's advisor.");
			stage = 21;
			break;
		case 21:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Can you give me any advice then?");
			stage = 22;
			break;
		case 22:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "I only advise the Duke. But if you want to make", "yourself useful, there are evil goblins to slay on the", "other side of the river.");
			stage = 23;
			break;
		case 23:
			end();
			break;
		case 10:
			if(player.getQuestRepository().hasStarted("Lost Tribe") && !player.getQuestRepository().isComplete("Lost Tribe")){
				npc("No, not right now.");
				stage = 12;
				break;
			}
			if(player.getQuestRepository().isComplete("Goblin Diplomacy") && player.getQuestRepository().isComplete("Rune Mysteries") && !player.getQuestRepository().hasStarted("Lost Tribe")){
				npc("There was recently some damage to the castle cellar.","Part of the wall has collapsed.");
				stage = 30;
				break;
			}
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "I hear the Duke has a task for an adventurer.", "Otherwise, if you want to make yourself useful, there", "are always evil monsters to slay.");
			stage = 11;
			break;
		case 11:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Okay, I might just do that.");
			stage = 12;
			break;
		case 12:
			end();
			break;
		case 30:
			npc("The Duke insists that it was an earthquake, but I think","some kind of monsters are to blame.");
			stage++;
			break;
		case 31:
			npc("You should ask other people around the town if they","saw anything.");
			stage = END_DIALOGUE;
			player.getQuestRepository().getQuest("Lost Tribe").start(player);
			player.setAttribute("/save:tlt-witness", TLTNPCS[0]);
			break;
		case 34:
			player("No...");
			stage = END_DIALOGUE;
			break;
		}
		return true;
	}

	@Override
	public DialoguePlugin newInstance(Player player) {

		return new SigmundDialogue(player);
	}
}
