package core.game.interaction.item.withplayer;

import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;

/**
 * Handles the breaking of a christmas cracker on a player.
 * @author Vexia
 */
@Initializable
public final class ChristmasCrackerUsage extends UseWithHandler {

	/**
	 * The party hat items.
	 */
	private static final Item[] HATS = new Item[] { new Item(1038), new Item(1040), new Item(1042), new Item(1044), new Item(1046), new Item(1048) };

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(962, PLAYER_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		Player target = (Player) event.getUsedWith();
		if (target == null || !target.isActive() || target.getLocks().isInteractionLocked() || target.getInterfaceManager().getOpened() != null) {
			event.getPlayer().getPacketDispatch().sendMessage("The other player is currently busy.");
			return true;
		}
		if (target.getInventory().freeSlots() == 0) {
			event.getPlayer().sendMessage("The other player doesn't have enough inventory space.");
			return true;
		}
		Player player = event.getPlayer();
		if (player.getInventory().remove(event.getUsedItem())) {
			player.faceTemporary(target, 2);
			player.lock(3);
			player.animate(Animation.create(451));
			player.graphics(Graphics.create(176));
			player.sendMessage("You pull a Christmas cracker...");
			boolean winner = RandomFunction.random(2) == 1;
			player.sendMessage(winner ? "You get the prize from the cracker." : "The person you pull the cracker with gets the prize.");
			if (winner) {
				player.sendChat("Hey! I got the cracker!");
			} else {
				target.sendChat("Hey! I got the cracker!");
			}
			Item hat = RandomFunction.getRandomElement(HATS);
			if (winner) {
				player.getInventory().add(hat, player);
			} else {
				target.getInventory().add(hat, target);
			}
		}
		return true;
	}

}
