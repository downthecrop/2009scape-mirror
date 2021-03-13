package core.game.interaction.item.withitem;

import org.rs09.consts.Items;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.system.task.Pulse;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;

import java.util.Arrays;

/**
 * Represents the poison fish food making plugin.
 * @author afaroutdude, fixed by Ceikry
 */
@Initializable
public final class FishfoodPlugin extends UseWithHandler {

	/**
	 * Represents the poisoned fish food item.
	 */
	private static final int FISH_FOOD = Items.FISH_FOOD_272;
	private static final int POISON = Items.POISON_273;
	private static final int POISONED_FISH_FOOD = Items.POISONED_FISH_FOOD_274;

	protected enum FishFoodUses{
		POISONED(POISON, FISH_FOOD, POISONED_FISH_FOOD, "You poison the fish food."),
		GUAMBOX(Items.GROUND_GUAM_6681, Items.AN_EMPTY_BOX_6675, Items.GUAM_IN_A_BOX_6677, "You put the ground Guam into the box."),
		SEAWEEDBOX(Items.GROUND_SEAWEED_6683, Items.AN_EMPTY_BOX_6675, Items.SEAWEED_IN_A_BOX_6679, "You put the ground Seaweed into the box."),
		FOOD1(Items.GROUND_SEAWEED_6683, Items.GUAM_IN_A_BOX_6677, FISH_FOOD, "You put the ground Seaweed into the box and make Fish Food."),
		FOOD2(Items.GROUND_GUAM_6681, Items.SEAWEED_IN_A_BOX_6679, FISH_FOOD, "You put the ground Guam into the box and make Fish Food."),
		FISHBOWL(Items.FISHBOWL_6668, Items.SEAWEED_401, Items.FISHBOWL_6669, "You place the seaweed in the bowl.");


		private final int used;
		private final int with;
		private final int product;
		private final String msg;

		static protected int[] usables = Arrays.stream(FishFoodUses.values())
				.mapToInt(v -> v.used)
				.toArray();

		FishFoodUses(int used, int with, int product, String msg) {
			this.used = used;
			this.with = with;
			this.product = product;
			this.msg = msg;
		}

		protected static Item productFor(int used, int with) {
			for (FishFoodUses value : values()) {
				if (value.used == used && value.with == with) {
					return new Item(value.product);
				}
			}
			return null;
		}

		protected static String msgFor(int used, int with) {
			for (FishFoodUses value : values()) {
				if (value.used == used && value.with == with) {
					return value.msg;
				}
			}
			return null;
		}
	}


	/**
	 * Constructs a new {@code FishfoodPlugin} {@code Object}.
	 */
	public FishfoodPlugin() {
		super(FishFoodUses.usables);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (FishFoodUses value : FishFoodUses.values()) {
			addHandler(value.with, ITEM_TYPE, this);
		}
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		int used = event.getUsedItem().getId();
		int with = event.getBaseItem().getId();
		Item product = FishFoodUses.productFor(used, with);

		player.getPulseManager().run(new Pulse(1, player) {
			@Override
			public boolean pulse() {
				if (player.getInventory().remove(new Item(with),new Item(used))) {
					player.animate(new Animation(1309));
					player.getPacketDispatch().sendMessage(FishFoodUses.msgFor(used, with));
					player.getInventory().add(product);
					player.lock(2);
				}
				return true;
			}

			@Override
			public void stop() {
				super.stop();
			}
		});

		return true;
	}

}
