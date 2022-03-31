package core.game.interaction.npc;

import core.game.content.dialogue.DialoguePlugin;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;
import rs09.plugin.ClassScanner;
import org.rs09.consts.Items;

@Initializable
public final class SecurityGuardPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ClassScanner.definePlugin(new SecurityGuardDialogue());
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		if (option.equals("talk-to")) {
			player.getDialogueInterpreter().open(4375);
		}
		return true;
	}

	public final class SecurityGuardDialogue extends DialoguePlugin {

		public SecurityGuardDialogue() {}

		public SecurityGuardDialogue(Player player) {
			super(player);
		}

		@Override
		public DialoguePlugin newInstance(Player player) {
			return new SecurityGuardDialogue(player);
		}

		@Override
		public boolean open(Object... args) {
			npc = (NPC) args[0];
			if (player.getInventory().hasSpaceFor(new Item(Items.SECURITY_BOOK_9003))) {
				npc("Here's a Security book for you.", "Have a nice day.");
				player.getInventory().add(new Item(Items.SECURITY_BOOK_9003));
			} else {
				npc("Have a nice day.");
			}
			return false;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			return false;
		}

		@Override
		public int[] getIds() {
			return new int[] { 4375 };
		}
	}
}