package core.game.interaction.item.withobject;

import core.cache.def.impl.ItemDefinition;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.OptionHandler;
import core.game.interaction.UseWithHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used to collect sap.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class SapCollectPlugin extends UseWithHandler {

	/**
	 * Represents the bucket of sap item.
	 */
	private static final Item BUCKET_OF_SAP = new Item(4687);

	/**
	 * Represents the bucket item.
	 */
	private static final Item BUCKET = new Item(1925);

	/**
	 * Represents the animation to use.
	 */
	private static final Animation ANIMATION = new Animation(2009);

	/**
	 * Represents the tree ids.
	 */
	private static final int[] IDS = new int[] { 1276, 1277, 1278, 1279, 1280, 1315, 1316, 1318, 1319, 1330, 1331, 1332, 3033, 3034, 3035, 3036, 3879, 3881, 3882, 3883, 10041, 14308, 14309, 30132, 30133, 37477, 37478, 37652 };

	/**
	 * Constructs a new {@code SapCollectPlugin} {@code Object}.
	 */
	public SapCollectPlugin() {
		super(946);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (int i : IDS) {
			addHandler(i, OBJECT_TYPE, this);
		}
		new SapEmptyPlugin().newInstance(arg);
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		if (!player.getInventory().remove(BUCKET)) {
			player.getPacketDispatch().sendMessage("I need an empty bucket to collect the sap.");
			return true;
		}
		player.animate(ANIMATION);
		player.getInventory().remove(BUCKET);
		player.getInventory().add(BUCKET_OF_SAP);
		player.getPacketDispatch().sendMessage("You cut the tree and allow its sap to drip down into your bucket.");
		return true;
	}

	/**
	 * Represents the plugin used to empty a bucket of sap.
	 * @author 'Vexia
	 * @version 1.0
	 */
	public static final class SapEmptyPlugin extends OptionHandler {

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			ItemDefinition.forId(4687).getHandlers().put("option:empty", this);
			return this;
		}

		@Override
		public boolean handle(Player player, Node node, String option) {
			Item item = (Item) node;
			if (item.getSlot() < 0) {
				return false;
			}
			player.getInventory().replace(BUCKET, item.getSlot());
			player.getPacketDispatch().sendMessage("You empty the contents of the bucket on the floor.");
			return true;
		}

		@Override
		public boolean isWalk() {
			return false;
		}

	}
}
