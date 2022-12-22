package core.game.content.quest.free.shieldofarrav;

import core.game.content.dialogue.DialoguePlugin;
import core.game.content.dialogue.FacialExpression;
import core.game.content.quest.free.shieldofarrav.CuratorHaigHalenSOADialogue;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.node.item.Item;
import rs09.game.content.dialogue.IfTopic;
import rs09.game.content.quest.members.thegolem.CuratorHaigHalenGolemDialogue;

/**
 * Represents the curator haig halen dialogue.
 * @author 'Vexia
 * @version 1.0
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
		interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Welcome to the museum of Varrock.");

		if (player.getQuestRepository().getPoints() >= 50) {
			player.getAchievementDiaryManager().finishTask(player, DiaryType.VARROCK, 0, 12);
		}

		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
        switch(stage) {
            case 0:
            showTopics(
                new IfTopic("I have the Shield of Arrav", new CuratorHaigHalenSOADialogue(),
                    player.getQuestRepository().getQuest("Shield of Arrav").getStage(player) == 70
                , false),
                new IfTopic("I'm looking for a statuette recovered from the city of Uzer.", new CuratorHaigHalenGolemDialogue(),
                    player.getQuestRepository().getQuest("The Golem").getStage(player) >= 3
                , false)
            );
            break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 646 };
	}
}
