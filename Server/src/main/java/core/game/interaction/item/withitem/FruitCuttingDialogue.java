package core.game.interaction.item.withitem;

import static api.ContentAPIKt.*;
import core.game.component.Component;
import core.plugin.Initializable;
import kotlin.Unit;
import org.rs09.consts.Items;
import core.game.content.dialogue.DialoguePlugin;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.RunScript;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;
import rs09.game.interaction.item.withitem.FruitSlicing;

/**
 * Represents the fruit cutting plugin.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class FruitCuttingDialogue extends DialoguePlugin {

	/**
	 * Represents the component interface.
	 */
	private static final Component COMPONENT = new Component(140);

	/**
	 * Represents the fruit.
	 */
	private FruitSlicing.Fruit fruit;

	/**
	 * Represents the fruit to cut.
	 */
	private Item item;

	/**
	 * Represents if its a sliced cut.
	 */
	private boolean slice;

	/**
	 * Constructs a new {@code FruitCuttingDialogue} {@code Object}.
	 */
	public FruitCuttingDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code FruitCuttingDialogue} {@code Object}.
	 * @param player the player.
	 */
	public FruitCuttingDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new FruitCuttingDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		fruit = ((FruitSlicing.Fruit) args[0]);
		player.getPacketDispatch().sendString("Would you like to...", 140, 4);
		player.getPacketDispatch().sendItemOnInterface(fruit.getSliced().getId(), 1, 140, 5);
		player.getPacketDispatch().sendItemOnInterface(fruit.getDiced().getId(), 1, 140, 6);
		player.getPacketDispatch().sendString("Slice the " + fruit.getBase().getName().toLowerCase() + ".", 140, 2);
		player.getPacketDispatch().sendString("Dice the " + fruit.getBase().getName().toLowerCase() + ".", 140, 3);
		player.getInterfaceManager().openChatbox(COMPONENT);
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
			case 0:
				player.getInterfaceManager().openChatbox(309);
				player.getPacketDispatch().sendItemZoomOnInterface(1, 160, 309, 2);
				slice = buttonId == 2 ? false : true;
				item = buttonId == 2 ? fruit.getDiced() : fruit.getSliced();
				player.getPacketDispatch().sendString("<br><br><br><br>" + item.getName(), 309, 6);
				player.getPacketDispatch().sendItemZoomOnInterface(item.getId(), 175, 309, 2);
				stage = 1;
				break;
			case 1:
				int amount = 0;
				switch (buttonId) {
					case 6:
						amount = 1;
						break;
					case 5:
						amount = 5;
						break;
					case 4:
						sendInputDialogue(player, false, "Enter the amount:", (value) -> {
							String s = value.toString();
							s = s.replace("k","000");
							s = s.replace("K","000");
							int val = Integer.parseInt(s);
							cut(player, val, slice);
							return Unit.INSTANCE;
						});
						return true;
					case 3:
						amount = player.getInventory().getAmount(fruit.getBase());
						break;
				}
				cut(player, amount, slice);
				break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 31237434 };
	}

	/**
	 * Method used to cut a fruit.
	 * @param player the player.
	 * @param amount the amount.
	 * @param slice if its sliced.
	 */
	private final void cut(final Player player, int amount, boolean slice) {
		if (amount > player.getInventory().getAmount(fruit.getBase())) {
			amount = player.getInventory().getAmount(fruit.getBase());
		}
		end();
		final Item remove = new Item(fruit.getBase().getId(), amount);
		if (!player.getInventory().containsItem(remove)) {
			return;
		}
		if (player.getInventory().remove(remove)) {
			player.getPacketDispatch().sendMessage("You cut the " + fruit.name().toLowerCase() + " into " + (slice ? fruit == FruitSlicing.Fruit.PINEAPPLE ? "rings" : "slices" : "chunks") + ".");
			for (int i = 0; i < amount; i++) {
				player.getInventory().add(slice ? fruit.getSliced() : fruit.getDiced());
			}
		}
	}

}