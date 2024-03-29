package content.region.misthalin.lumbridge.dialogue;

import core.game.dialogue.DialoguePlugin;
import core.game.dialogue.FacialExpression;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.game.node.item.Item;

/**
 * Represents the dialogue plugin used for the candle seller.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class CandleSellerDialogue extends DialoguePlugin {

	/**
	 * Represents the coins item.
	 */
	private static final Item COINS = new Item(995, 1000);

	/**
	 * Represents the candle item.
	 */
	private static final Item CANDLE = new Item(33, 1);

	/**
	 * Constructs a new {@code CandleSellerPlugin} {@code Object}.
	 */
	public CandleSellerDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code CandleSellerPlugin} {@code Object}.
	 * @param player the player.
	 */
	public CandleSellerDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new CandleSellerDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(npc, FacialExpression.HAPPY, "Do you want a lit candle for 1000 gold?");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			interpreter.sendOptions("Select an Option", "Yes please.", "One thousand gold?!", "No thanks, I'd rather curse the darkness.");
			stage = 1;
			break;
		case 800:
			if (player.getInventory().freeSlots() == 0) {
				end();
				player.getPacketDispatch().sendMessage("You don't have enough inventory space to buy a candle.");
				break;
			}
			if (!player.getInventory().contains(995, 1000)) {
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Sorry, I don't seem to have enough coins.");
				stage = 30;
				break;
			}
			if (player.getInventory().contains(995, 1000)) {
				player.getInventory().remove(COINS);
				player.getInventory().add(new Item(33, 1));
				interpreter.sendDialogues(npc, FacialExpression.HAPPY, "Here you go then.");
				stage = 400;
			}
			break;
		case 1:
			switch (buttonId) {
			case 1:
				interpreter.sendDialogues(player, FacialExpression.HAPPY, "Yes please.");
				stage = 800;
				break;
			case 2:
				interpreter.sendDialogues(player, FacialExpression.EXTREMELY_SHOCKED, "One thousand gold?!");
				stage = 20;
				break;
			case 3:
				interpreter.sendDialogues(player, FacialExpression.EXTREMELY_SHOCKED, "No thanks, I'd rather curse the darkness.");
				stage = 30;
				break;
			}
			break;
		case 20:
			interpreter.sendDialogues(npc, FacialExpression.NEUTRAL, "Look, you're not going to be able to survive down that", "hole without a light source.");
			stage = 21;
			break;
		case 21:
			interpreter.sendDialogues(npc, FacialExpression.NEUTRAL, "So you could go off to the candle shop to buy one", "more cheaply. You could even make your own lantern,", "which is a lot better.");
			stage = 22;
			break;
		case 22:
			interpreter.sendDialogues(npc, FacialExpression.HAPPY, "But I bet you want to find out what's down there right", "now, don't you? And you can pay me 1000 gold for", "the privilege!");
			stage = 23;
			break;
		case 23:
			interpreter.sendOptions("Select an Option", "All right, you win, I'll buy a candle.", "No way.", "How do you make lanterns?");
			stage = 240;
			break;
		case 30:
			end();
			break;
		case 240:
			switch (buttonId) {
			case 1:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "All right, you win, I'll buy a candle.");
				stage = 350;
				break;
			case 2:
				interpreter.sendDialogues(player, FacialExpression.ANNOYED, "No way.");
				stage = 30;
				break;
			case 3:
				interpreter.sendDialogues(player, FacialExpression.HALF_ASKING, "How do you make lanterns?");
				stage = 230;
				break;
			}
			break;
		case 230:
			interpreter.sendDialogues(npc, FacialExpression.FRIENDLY, "Out of glass. The more advanced lanterns have a", "metal component as well.");
			stage = 231;
			break;
		case 231:
			interpreter.sendDialogues(npc, FacialExpression.FRIENDLY, "Firstly you can make a simple candle lantern out of", "glass. It's just like a candle, but the flame isn't exposed,", "so it's safer.");
			stage = 232;
			break;
		case 232:
			interpreter.sendDialogues(npc, FacialExpression.FRIENDLY, "Then you can make an oil lamp, which is brighter but", "has an exposed flame. But if you make an iron frame", "for it you can turn it into an oil lantern.");
			stage = 233;
			break;
		case 233:
			interpreter.sendDialogues(npc, FacialExpression.FRIENDLY, "Finally there's a Bullseye lantern. You'll need to", "make a frame out of steel and add a glass lens.");
			stage = 234;
			break;
		case 234:
			interpreter.sendDialogues(npc, FacialExpression.FRIENDLY, "Oce you've made your lamp or lantern, you'll need to", "make lamp oil for it. The chemist near Reimmington has", "a machine for that.");
			stage = 235;
			break;
		case 235:
			interpreter.sendDialogues(npc, FacialExpression.FRIENDLY, "For any light source, you'll need a tinderbox to light it.", "Keep your tinderbox handy in case it goes out!");
			stage = 236;
			break;
		case 236:
			interpreter.sendDialogues(npc, FacialExpression.HAPPY, "But if all that's to complicated, you can buy a candle", "right here for 1000 gold!");
			stage = 237;
			break;
		case 237:
			interpreter.sendOptions("Select an Option", "All right, you win, I'll buy a candle.", "No thanks, I'd rather curse the darkness.");
			stage = 290;
			break;
		case 350:
			if (player.getInventory().freeSlots() == 0) {
				end();
				player.getPacketDispatch().sendMessage("You don't have enough inventory space to buy a candle.");
				break;
			}
			if (!player.getInventory().contains(995, 1000)) {
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Sorry, I don't seem to have enough coins.");
				stage = 30;
				break;
			}

			if (player.getInventory().remove(COINS)) {
				player.getInventory().add(CANDLE);
				interpreter.sendDialogues(npc, FacialExpression.HAPPY, "Here you go then.");
				stage = 400;
			}
			break;
		case 400:
			interpreter.sendDialogues(npc, FacialExpression.NEUTRAL, "I should warn you, though, it can be dangerous to take", "a naked flame down there. You'd better off making", "a lantern.");
			stage = 401;
			break;
		case 401:
			interpreter.sendDialogues(player, FacialExpression.FRIENDLY, "Okay, thanks.");
			stage = 402;
			break;
		case 402:
			end();
			break;
		case 290:
			switch (buttonId) {
			case 1:
				if (player.getInventory().freeSlots() == 0) {
					end();
					player.getPacketDispatch().sendMessage("You don't have enough inventory space to buy a candle.");
					break;
				}
				if (!player.getInventory().contains(995, 1000)) {
					interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Sorry, I don't seem to have enough coins.");
					stage = 30;
					break;
				}
				if (player.getInventory().remove(COINS)) {
					player.getInventory().add(CANDLE);
					interpreter.sendDialogues(npc, FacialExpression.FRIENDLY, "Here you go then.");
					stage = 400;
				}
				break;
			case 2:
				interpreter.sendDialogues(player, FacialExpression.EXTREMELY_SHOCKED, "No thanks, I'd rather curse the darkness.");
				stage = 291;
				break;
			}
			break;
		case 291:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 1834 };
	}
}
