package plugin.random.lostpirate;

import java.nio.ByteBuffer;

import org.crandor.game.content.ame.AntiMacroDialogue;
import org.crandor.game.content.ame.AntiMacroEvent;
import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.node.entity.npc.drop.DropFrequency;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.ChanceItem;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.PluginManager;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Handles the capn hand anti macro event.
 * @author Vexia
 */
@InitializablePlugin
public final class CapnHandEvent extends AntiMacroEvent {

	/**
	 * The name of the event.
	 */
	public static final String NAME = "capn hand";

	/**
	 * Constructs a new {@code CapnHandEvent} {@code Object}.
	 */
	public CapnHandEvent() {
		super(NAME, true, false);
	}

	@Override
	public void save(ByteBuffer buffer) {

	}

	@Override
	public void parse(ByteBuffer buffer) {

	}

	@Override
	public boolean start(Player player, boolean login, Object... args) {
		final CapnHandNPC capn = new CapnHandNPC(2539, player.getLocation(), this, player);
		capn.init();
		super.init(player);
		return true;
	}

	@Override
	public AntiMacroEvent create(Player player) {
		CapnHandEvent event = new CapnHandEvent();
		event.player = player;
		return event;
	}

	@Override
	public void register() {
		PluginManager.definePlugin(new CapnHandDialogue());
	}

	@Override
	public Location getSpawnLocation() {
		return null;
	}

	@Override
	public void configure() {

	}

	@Override
	public String getGenderPrefix(boolean male) {
		return male ? "mister" : "miss";
	}

	/**
	 * Handles the rick turpnetine dialogue.
	 * @author Vexia
	 */
	public static final class CapnHandDialogue extends AntiMacroDialogue {

		/**
		 * The random items recieved.
		 */
		private static final ChanceItem[] ITEMS = new ChanceItem[] { new ChanceItem(995, 1, 640, DropFrequency.COMMON), new ChanceItem(1969, 1, 1, DropFrequency.COMMON), new ChanceItem(1971, 1, 1, DropFrequency.COMMON), new ChanceItem(1623, 1, 1, DropFrequency.UNCOMMON), new ChanceItem(1621, 1, 1, DropFrequency.UNCOMMON), new ChanceItem(1619, 1, 1, DropFrequency.UNCOMMON), new ChanceItem(1617, 1, 1, DropFrequency.UNCOMMON), new ChanceItem(1454, 1, 1, DropFrequency.UNCOMMON), new ChanceItem(985, 1, 1, DropFrequency.RARE) };

		/**
		 * Constructs a new {@code CapnHandDialogue} {@code Object}.
		 */
		public CapnHandDialogue() {
			/**
			 * empty.
			 */
		}

		/**
		 * Constructs a new {@code CapnHandDialogue} {@code Object}.
		 * @param player the player.
		 */
		public CapnHandDialogue(Player player) {
			super(player);
		}

		@Override
		public DialoguePlugin newInstance(Player player) {
			return new CapnHandDialogue(player);
		}

		@Override
		public boolean open(Object... args) {
			if (super.open(args)) {
				npc("My mistake " + event.getGenderPrefix() + ", I was thinking yer be some other", "fella!", "Take this swag, and we'll be saying no more about it!", "Yarrr!");
			} else {
				return true;
			}
			return true;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			final Item item = RandomFunction.getChanceItem(ITEMS).getRandomItem();
			player.getInventory().add(item, player);
			wave(null);
			return true;
		}

		@Override
		public int[] getIds() {
			return new int[] { 2539 };
		}

	}
}
