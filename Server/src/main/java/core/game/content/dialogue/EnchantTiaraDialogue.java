package core.game.content.dialogue;

import static api.ContentAPIKt.*;
import core.plugin.Initializable;
import core.game.node.entity.skill.runecrafting.Altar;
import core.game.node.entity.skill.runecrafting.EnchantTiaraPulse;
import core.game.node.entity.skill.runecrafting.Talisman;
import core.game.interaction.NodeUsageEvent;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import kotlin.Unit;

/**
 * Represents the enchant tiara dialogue.
 * @author Vexia
 * @version 1.0
 */
@Initializable
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
		altar = Altar.forObject(((Scenery) event.getUsedWith()));
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
			sendInputDialogue(player, true, "Enter the amount:", (value) -> {
				player.getPulseManager().run(new EnchantTiaraPulse(player, event.getUsedItem(), Talisman.forItem(event.getUsedItem()).getTiara(), (int) value));
				return Unit.INSTANCE;
			});
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
