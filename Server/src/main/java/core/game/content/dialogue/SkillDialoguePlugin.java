package core.game.content.dialogue;

import static api.ContentAPIKt.*;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.RunScript;
import core.plugin.Initializable;
import kotlin.Unit;
import rs09.game.content.dialogue.SkillDialogueHandler;

/**
 * Represents the dialogue plugin used to automatically handle skill dialogues  with creation amounts.
 * @author Vexia
 * 
 */
@Initializable
public class SkillDialoguePlugin extends DialoguePlugin {

	/**
	 * Represents the skill dialogue handler.
	 */
	private SkillDialogueHandler handler;

	/**
	 * Constructs a new {@code SkillDialogue} {@code Object}.
	 */
	public SkillDialoguePlugin() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code SkillDialogue} {@code Object}.
	 * @param player the player.
	 */
	public SkillDialoguePlugin(final Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new SkillDialoguePlugin(player);
	}

	@Override
	public boolean open(Object... args) {
		handler = (SkillDialogueHandler) args[0];
		handler.display();
		player.getInterfaceManager().openChatbox(handler.getType().getInterfaceId());
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		final int amount = handler.getType().getAmount(handler, buttonId);
		final int index = handler.getType().getIndex(handler, buttonId);
		end();
		if (amount != -1) {
			handler.create(amount, index);
		} else {
			sendInputDialogue(player, true, "Enter the amount:", (value) -> {
				handler.create((int) value, index);
				return Unit.INSTANCE;
			});
			return true;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { SkillDialogueHandler.SKILL_DIALOGUE };
	}

}
