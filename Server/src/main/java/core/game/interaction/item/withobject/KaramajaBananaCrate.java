package core.game.interaction.item.withobject;

import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the karamaja banna crate.
 * @author 'Vexia
 * @date 29/11/2013
 */
@Initializable
public final class KaramajaBananaCrate extends UseWithHandler {

	/**
	 * Represents the banana item.
	 */
	private static final Item BANANA = new Item(1963);

	/**
	 * Represents the filling animation.
	 */
	private static final Animation ANIMATION = new Animation(832);

	/**
	 * Represents the cap of bannas to add.
	 */
	private static final int CAP = 10;

	/**
	 * Constructs a new {@code KaramajaBananaCrate} {@code Object}.
	 */
	public KaramajaBananaCrate() {
		super(1963, 431);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(2072, OBJECT_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		if (player.getSavedData().getGlobalData().isLuthasTask() && event.getUsedItem().getId() == 431) {
			if (player.getAttribute("stashed-rum", false)) {
				player.getPacketDispatch().sendMessage("There's already some rum in here...");
				return true;
			}
			if (player.getInventory().remove(event.getUsedItem())) {
				player.animate(ANIMATION);
				player.getDialogueInterpreter().sendDialogue("You stash the rum in the crate.");
				player.setAttribute("/save:stashed-rum", true);
			}
			return true;
		}
		final int current = player.getSavedData().getGlobalData().getKaramjaBananas();
		if (current == CAP) {
			player.getPacketDispatch().sendMessage("The crate is already full.");
			return true;
		}
		if (player.getInventory().remove(BANANA)) {
			player.getDialogueInterpreter().sendDialogue("You pack a banana into the crate.");
			player.getSavedData().getGlobalData().setKaramjaBannanas(current + 1);
			player.animate(ANIMATION);
			player.lock(2);
		}
		return true;
	}

}
