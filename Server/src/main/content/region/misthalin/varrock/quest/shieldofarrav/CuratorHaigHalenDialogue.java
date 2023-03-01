package content.region.misthalin.varrock.quest.shieldofarrav;

import core.game.dialogue.DialoguePlugin;
import core.game.dialogue.FacialExpression;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.dialogue.IfTopic;
import content.region.desert.quest.thegolem.CuratorHaigHalenGolemDialogue;

/**
 * Represents the curator haig halen dialogue.
 */
public final class CuratorHaigHalenDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code CuratorHaigHalenDialogue} {@code Object}.
	 */
	public CuratorHaigHalenDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code CuratorHaigHalenDialogue} {@code Object}.
	 * @param player the player.
	 */
	public CuratorHaigHalenDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new CuratorHaigHalenDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(npc, FacialExpression.HAPPY, "Welcome to the museum of Varrock.");
		if (player.getQuestRepository().getPoints() >= 50 && !player.getAchievementDiaryManager().hasCompletedTask(DiaryType.VARROCK, 0 ,12)) {
			player.getAchievementDiaryManager().finishTask(player, DiaryType.VARROCK, 0, 12);
		}
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch(stage) {
			case 0:
				boolean fallthrough = showTopics(
						new IfTopic("I have the Shield of Arrav", new CuratorHaigHalenSOADialogue(),
								player.getQuestRepository().getQuest("Shield of Arrav").getStage(player) == 70
								, false),
						new IfTopic("I'm looking for a statuette recovered from the city of Uzer.", new CuratorHaigHalenGolemDialogue(),
								player.getQuestRepository().getQuest("The Golem").getStage(player) == 3
								, false)
				);
				if(fallthrough) { stage = 1; handle(interfaceId, buttonId); }
				break;
			case 1:
				end();
				break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 646 };
	}
}
