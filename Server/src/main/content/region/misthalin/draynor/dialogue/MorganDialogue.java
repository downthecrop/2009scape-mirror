package content.region.misthalin.draynor.dialogue;

import core.game.dialogue.DialoguePlugin;
import core.game.dialogue.FacialExpression;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.game.node.entity.player.link.quest.Quest;
import content.data.Quests;

/**
 * Represents the dialogue plugin used for the morgan npc.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class MorganDialogue extends DialoguePlugin {

	/**
	 * Represents the quest instance.
	 */
	private Quest quest;

	/**
	 * Constructs a new {@code MorganDialogue} {@code Object}.
	 */
	public MorganDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code MorganDialogue} {@code Object}.
	 * @param player the player.
	 */
	public MorganDialogue(Player player) {
		super(player);
	}

	@Override
	public boolean open(Object... args) {
		quest = player.getQuestRepository().getQuest(Quests.VAMPIRE_SLAYER);
		npc = (NPC) args[0];
		quest = player.getQuestRepository().getQuest(Quests.VAMPIRE_SLAYER);
		if (quest.getStage(player) == 0) {
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Please please help us, bold adventurer!");
			stage = 0;
		}
		switch (quest.getStage(player)) {
		case 10:
		case 20:
		case 30:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "How are you doing with the quest?");
			stage = 10;
			break;
		case 100:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "I have slain the foul creature!");
			stage = 101;
			break;
		}
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "What's the problem?");
			stage = 1;
			break;
		case 1:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Our little village has been dreadfully ravaged by an evil", "vampire! He lives in the basement of the manor to the", "north, we need someone to get rid of him once and for", "all!");
			stage = 2;
			break;
		case 2:
			interpreter.sendOptions("Select an Option", "No, vampires are scary!", "Ok, I'm up for an adventure.", "Have you got any tips on killing the vampire?");
			stage = 7;
			break;
		case 7:
			switch (buttonId) {
			case 1:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "No, vampires are scary!");
				stage = 8;
				break;
			case 2:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Ok, I'm up for an adventure.");
				stage = 3;
				break;
			case 3:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Have you got any tips on killing the vampire?");
				stage = 3;
				break;
			}
			break;
		case 3:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "I think first you should seek help. I have a friend who", "is a retired vampire hunter, his name is Dr. Harlow. He", "may be able to give you some tips. He can normally be", "found in the Blue Moon Inn in Varrock, he's a bit of");
			stage = 4;
			break;
		case 4:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "an old soak these days. Mention his old friend Morgan,", "I'm sure he wouldn't want me killed by a vampire.");
			stage = 5;
			break;
		case 5:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "I'll look him up then.");
			stage = 6;
			break;
		case 6:
			quest.start(player);
			player.getQuestRepository().syncronizeTab(player);
			end();
			break;
		case 8:
			end();
			break;
		case 10:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "I'm still working on it.");
			stage = 11;
			break;
		case 11:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Please hurry! Every day we live in fear that we", "the vampire's next victim!");
			stage = 12;
			break;
		case 12:
			end();
			break;
		case 101:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Thank you, thank you! You will always be a hero in", "our village!");
			stage = 102;
			break;
		case 102:
			end();
			break;
		}
		return true;
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new MorganDialogue(player);
	}

	@Override
	public int[] getIds() {
		return new int[] { 755 };
	}
}
