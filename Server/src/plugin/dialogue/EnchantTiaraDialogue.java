package plugin.dialogue;

import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.content.skill.free.runecrafting.Altar;
import org.crandor.game.content.skill.free.runecrafting.EnchantTiaraPulse;
import org.crandor.game.content.skill.free.runecrafting.Talisman;
import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.RunScript;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.node.object.GameObject;

/**
 * Represents the enchant tiara dialogue.
 * @author Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class EnchantTiaraDialogue extends DialoguePlugin {

	/**
	 * Represents the node usage event.
	 */
	private NodeUsageEvent event;

	/**
	 * The altar.
	 */
	private Altar altar;

	/**
	 * Constructs a new {@code EnchantTiaraDialogue} {@code Object}.
	 */
	public EnchantTiaraDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code EnchantTiaraDialogue} {@code Object}.
	 * @param player the player.
	 */
	public EnchantTiaraDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new EnchantTiaraDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		event = ((NodeUsageEvent) args[0]);
		altar = Altar.forObject(((GameObject) event.getUsedWith()));
		if (!player.getInventory().containsItem(altar.getTalisman().getTalisman())) {
			player.getPacketDispatch().sendMessage("You don't have the required talisman.");
			return true;
		}
		player.getInterfaceManager().openChatbox(309);
		player.getPacketDispatch().sendString("<br><br><br><br>" + altar.getTiara().getTiara().getName(), 309, 6);
		player.getPacketDispatch().sendItemZoomOnInterface(altar.getTiara().getTiara().getId(), 175, 309, 2);
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		end();
		int amt = 0;
		switch (buttonId) {
		case 6:
			amt = 1;
			break;
		case 5:
			amt = 5;
			break;
		case 4:
			player.setAttribute("runscript", new RunScript() {
				@Override
				public boolean handle() {
					int ammount = (int) value;
					player.getPulseManager().run(new EnchantTiaraPulse(player, event.getUsedItem(), (Talisman.forItem(event.getUsedItem()).getTiara()), ammount));
					return false;
				}
			});
			player.getDialogueInterpreter().sendInput(false, "Enter the amount");
			return true;
		case 3:
			amt = player.getInventory().getAmount(event.getUsedItem());
			break;
		}
		player.getPulseManager().run(new EnchantTiaraPulse(player, event.getUsedItem(), altar.getTiara(), amt));
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 8432482 };
	}
}
