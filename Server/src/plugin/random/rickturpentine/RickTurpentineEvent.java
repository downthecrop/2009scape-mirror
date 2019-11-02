package plugin.random.rickturpentine;

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
 * Handles the rick turpentine anti macro event.
 * @author Vexia
 */
@InitializablePlugin
public final class RickTurpentineEvent extends AntiMacroEvent {

	/**
	 * The name of the event.
	 */
	public static final String NAME = "rick turpentine";

	/**
	 * Constructs a new {@code RickTurpentineEvent} {@code Object}.
	 */
	public RickTurpentineEvent() {
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
		final RickTurpentineNPC dwarf = new RickTurpentineNPC(2476, player.getLocation(), this, player);
		dwarf.init();
		super.init(player);
		return true;
	}

	@Override
	public AntiMacroEvent create(Player player) {
		RickTurpentineEvent event = new RickTurpentineEvent();
		event.player = player;
		return event;
	}

	@Override
	public void register() {
		PluginManager.definePlugin(new RickTurpentineDialogue());
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
		return male ? "milord" : "milady";
	}

	/**
	 * Handles the rick turpnetine dialogue.
	 * @author Vexia
	 */
	public static final class RickTurpentineDialogue extends AntiMacroDialogue {

		/**
		 * The random items recieved from rick turpnetine.
		 */
		private static final ChanceItem[] ITEMS = new ChanceItem[] { new ChanceItem(995, 1, 640, DropFrequency.COMMON), new ChanceItem(1969, 1, 1, DropFrequency.COMMON), new ChanceItem(1971, 1, 1, DropFrequency.COMMON), new ChanceItem(1623, 1, 1, DropFrequency.UNCOMMON), new ChanceItem(1621, 1, 1, DropFrequency.UNCOMMON), new ChanceItem(1619, 1, 1, DropFrequency.UNCOMMON), new ChanceItem(1617, 1, 1, DropFrequency.UNCOMMON), new ChanceItem(1454, 1, 1, DropFrequency.UNCOMMON), new ChanceItem(985, 1, 1, DropFrequency.RARE) };

		/**
		 * Constructs a new {@code RickTurpentineDialogue} {@code Object}.
		 */
		public RickTurpentineDialogue() {
			/**
			 * empty.
			 */
		}

		/**
		 * Constructs a new {@code RickTurpentineDialogue} {@code Object}.
		 * @param player the player.
		 */
		public RickTurpentineDialogue(Player player) {
			super(player);
		}

		@Override
		public DialoguePlugin newInstance(Player player) {
			return new RickTurpentineDialogue(player);
		}

		@Override
		public boolean open(Object... args) {
			if (super.open(args)) {
				npc("Today is your lucky day, sirrah!", "I am donating to the victims of crime to atone for my", "past actions!");
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
			return new int[] { 2476 };
		}

	}
}
