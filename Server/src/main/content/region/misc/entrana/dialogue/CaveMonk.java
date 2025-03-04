package content.region.misc.entrana.dialogue;

import core.game.dialogue.DialoguePlugin;
import core.game.dialogue.FacialExpression;
import core.plugin.Initializable;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.quest.Quest;
import core.game.world.map.Location;
import content.data.Quests;

/**
 * Represents the dialogue plugin used for a cave monk.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class CaveMonk extends DialoguePlugin {

	/**
	 * Represents the dungeon location.
	 */
	private static final Location DUNGEON = Location.create(2822, 9774, 0);

	/**
	 * The quest.
	 */
	private Quest quest;

	/**
	 * Constructs a new {@code CaveMonk} {@code Object}.
	 */
	public CaveMonk() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code CaveMonk} {@code Object}.
	 * @param player the player.
	 */
	public CaveMonk(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new CaveMonk(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		quest = player.getQuestRepository().getQuest(Quests.LOST_CITY);
		switch (quest.getStage(player)) {
		case 0:
		case 10:
			player("Hello, what are you doing here?");
			stage = 100;
			break;
		default:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Be careful going in there! You are unarmed, and there", "is much evilness lurking down there! The evilness seems", "to block off our contact with our gods,");
			stage = 0;
			break;
		}
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 100:
			npc("None of your business.");
			stage = 101;
			break;
		case 101:
			end();
			break;
		case 0:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "so our prayers seem to have less effect down there. Oh,", "also, you won't be able to come back this way - This", "ladder only goes one way!");
			stage = 1;
			break;
		case 1:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "The only exit from the caves below is a portal which", "leads only to the deepest wilderness!");
			stage = 2;
			break;
		case 2:
			interpreter.sendOptions("Select an Option", "I don't think I'm strong enough to enter then.", "Well that is a risk I will have to take.");
			stage = 3;
			break;
		case 3:
			switch (buttonId) {
			case 1:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "I don't think I'm strong enough to enter then.");
				stage = 10;
				break;
			case 2:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Well that is a risk I will have to take.");
				stage = 20;
				break;
			}
			break;
		case 10:
			end();
			break;
		case 20:
			if (player.getSkills().getLevel(Skills.PRAYER) > 2 && player.getSkills().getPrayerPoints() > 2) {
				player.getSkills().decrementPrayerPoints(player.getSkills().getLevel(Skills.PRAYER) - 2);
			}
			player.getProperties().setTeleportLocation(DUNGEON);
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 656 };
	}
}
