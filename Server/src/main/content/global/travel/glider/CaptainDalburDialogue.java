package content.global.travel.glider;

import content.data.Quests;
import core.game.component.Component;
import core.game.dialogue.DialoguePlugin;
import core.game.dialogue.FacialExpression;
import core.game.node.entity.npc.NPC;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

import static core.api.ContentAPIKt.isQuestComplete;
import static core.tools.DialogueConstKt.END_DIALOGUE;

/**
 * Represents the dialogue plugin used for the captain dalbur npc.
 * @author 'Vexia
 * @note complete with gnome city stuff.
 * @version 1.0
 */
@Initializable
public final class CaptainDalburDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code CaptainDalburDialogue} {@code Object}.
	 */
	public CaptainDalburDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code CaptainDalburDialogue.java} {@code Object}.
	 * @param player the player.
	 */
	public CaptainDalburDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new CaptainDalburDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "What do you want human?");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "May you fly me somewhere on your glider?");
			stage = 1;
			break;
		case 1:
			if(!isQuestComplete(player, Quests.THE_GRAND_TREE)){
				interpreter.sendDialogues(npc, FacialExpression.ANNOYED, "I only fly friends of the gnomes!");
				stage = END_DIALOGUE;
			}
			else {
				npc("If you wish.");
				stage++;
			}
			break;
		case 2:
			end();
			player.getInterfaceManager().open(new Component(138));
			Gliders.sendConfig(npc, player);
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 3809, 3810, 3811, 3812, 3813 };
	}
}
