package content.region.asgarnia.portsarim.dialogue;

import core.cache.def.impl.ItemDefinition;
import content.global.travel.ship.Ships;
import core.game.dialogue.DialoguePlugin;
import core.game.dialogue.FacialExpression;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.link.diary.DiaryType;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

import static core.api.ContentAPIKt.*;

/**
 * Represents the dialogue plugin used for the monk of entrana dialogue.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class MonkOfEntranaDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code MonkOfEntranaDialogue} {@code Object}.
	 */
	public MonkOfEntranaDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code MonkOfEntranaDialogue} {@code Object}.
	 * @param player the player.
	 */
	public MonkOfEntranaDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new MonkOfEntranaDialogue(player);
	}

	public void sail(Player player, Ships ship) {
		ship.sail(player);
		playJingle(player, 172);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		if (npc.getId() == 2730 || npc.getId() == 658 || npc.getId() == 2731) {
			interpreter.sendDialogues(npc, null, "Do you wish to leave holy Entrana?");
			stage = 500;
			return true;
		}
		interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Do you seek passage to holy Entrana? If so, you must", "leave your weaponry and armour behind. This is", "Saradomin's will.");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			interpreter.sendOptions("Select an Option", "No, not right now.", "Yes, okay, I'm ready to go.");
			stage = 1;
			break;
		case 1:
			switch (buttonId) {
			case 1:
				interpreter.sendDialogues(player, null, "No, not right now.");
				stage = 10;
				break;
			case 2:
				interpreter.sendDialogues(player, null, "Yes, okay, I'm ready to go.");
				stage = 20;
				break;
			}
			break;
		case 10:
			interpreter.sendDialogues(npc, null, "Very well.");
			stage = 11;
			break;
		case 11:
			end();
			break;
		case 20:
			interpreter.sendDialogues(npc, null, "Very well. One moment please.");
			stage = 21;
			break;
		case 21:
			interpreter.sendDialogue("The monk quickly searches you.");
			stage = 22;
			break;
		case 22:
			if (!ItemDefinition.canEnterEntrana(player)) {
				interpreter.sendDialogues(npc, null, "NO WEAPONS OR ARMOUR are permitted on holy", "Entrana AT ALL. We will not allow you to travel there", "in breach of mighty Saradomin's edict.");
				stage = 23;
				return true;
			}
			interpreter.sendDialogues(npc, null, "All is satisfactory. You may board the boat now.");
			stage = 25;
			break;
		case 23:
			interpreter.sendDialogues(npc, null, "Do not try to deceive us again. Come back when you", "have laid down your Zamorakian instruments of death.");
			stage = 24;
			break;
		case 24:
			end();
			break;
		case 25:
			end();
			sail(player, Ships.PORT_SARIM_TO_ENTRANA);
			if (!player.getAchievementDiaryManager().getDiary(DiaryType.FALADOR).isComplete(0, 14)) {
				player.getAchievementDiaryManager().getDiary(DiaryType.FALADOR).updateTask(player, 0, 14, true);
			}
			break;
		case 500:
			interpreter.sendOptions("Select an Option", "Yes, I'm ready to go.", "Not just yet.");
			stage = 501;
			break;
		case 501:
			switch (buttonId) {
			case 1:
				interpreter.sendDialogues(player, null, "Yes, I'm ready to go.");
				stage = 510;
				break;
			case 2:
				interpreter.sendDialogues(player, null, "Not just yet.");
				stage = 520;
				break;
			}
			break;
		case 510:
			interpreter.sendDialogues(npc, null, "Okay, let's board...");
			stage = 511;
			break;
		case 511:
			sail(player, Ships.ENTRANA_TO_PORT_SARIM);
			break;
		case 520:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 2728, 657, 2729, 2730, 2731, 658 };
	}
}
