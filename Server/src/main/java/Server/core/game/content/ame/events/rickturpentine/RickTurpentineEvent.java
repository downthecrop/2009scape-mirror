package core.game.content.ame.events.rickturpentine;

import java.nio.ByteBuffer;

import core.game.content.ame.AntiMacroDialogue;
import core.game.content.ame.AntiMacroEvent;
import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.npc.drop.DropFrequency;
import core.game.node.entity.player.Player;
import core.game.node.item.ChanceItem;
import core.game.node.item.Item;
import core.game.world.map.Location;
import core.plugin.PluginManager;
import core.plugin.Initializable;
import core.tools.RandomFunction;
import core.tools.Items;

/**
 * Handles the rick turpentine anti macro event.
 * @author Vexia
 */
@Initializable
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
		final RickTurpentineNPC npc = new RickTurpentineNPC(2476, player.getLocation(), this, player);
		npc.init();
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
		private static final ChanceItem[] ITEMS = new ChanceItem[] {
				new ChanceItem(Items.COINS_995, 1, 640, DropFrequency.COMMON),
				new ChanceItem(Items.SPINACH_ROLL_1969, 1, 1, DropFrequency.COMMON),
				new ChanceItem(Items.KEBAB_1971, 1, 1, DropFrequency.COMMON),
				new ChanceItem(Items.UNCUT_SAPPHIRE_1623, 1, 1, DropFrequency.UNCOMMON),
				new ChanceItem(Items.UNCUT_EMERALD_1621, 1, 1, DropFrequency.UNCOMMON),
				new ChanceItem(Items.UNCUT_RUBY_1619, 1, 1, DropFrequency.UNCOMMON),
				new ChanceItem(Items.UNCUT_DIAMOND_1617, 1, 1, DropFrequency.UNCOMMON),
				new ChanceItem(Items.COSMIC_TALISMAN_1454, 1, 1, DropFrequency.UNCOMMON),
				new ChanceItem(Items.TOOTH_HALF_OF_A_KEY_985, 1, 1, DropFrequency.RARE)};

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
			Item item = RandomFunction.getChanceItem(ITEMS).getRandomItem();
			if (item.getId() == Items.TOOTH_HALF_OF_A_KEY_985 && RandomFunction.randomSign(1) <= 0) {
				item = new Item(Items.LOOP_HALF_OF_A_KEY_987);
			}

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
