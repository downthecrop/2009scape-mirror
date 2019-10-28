package plugin.random.drunkdwarf;

import java.nio.ByteBuffer;

import org.crandor.game.content.ame.AntiMacroDialogue;
import org.crandor.game.content.ame.AntiMacroEvent;
import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.content.dialogue.FacialExpression;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.PluginManager;

/**
 * Handles the drunken dward anti macro event.
 * @author Vexia
 */
@InitializablePlugin
public final class DrunkDwarfEvent extends AntiMacroEvent {

	/**
	 * The name of the drunken dwarf event.
	 */
	public static final String NAME = "drunk dwarf";

	/**
	 * Constructs a new {@code DrunkDwarfEvent} {@code Object}.
	 */
	public DrunkDwarfEvent() {
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
		final DrunkenDwarfNPC dwarf = new DrunkenDwarfNPC(956, player.getLocation(), this, player);
		dwarf.init();
		super.init(player);
		return true;
	}

	@Override
	public AntiMacroEvent create(Player player) {
		DrunkDwarfEvent event = new DrunkDwarfEvent();
		event.player = player;
		return event;
	}

	@Override
	public void register() {
		PluginManager.definePlugin(new DrunkDwarfDialogue());
	}

	@Override
	public Location getSpawnLocation() {
		return null;
	}

	@Override
	public void configure() {

	}

	/**
	 * Handles the drunk dwarf dialogue.
	 * @author Vexia
	 */
	public final class DrunkDwarfDialogue extends AntiMacroDialogue {

		/**
		 * Constructs a new {@code DrunkDwarfDialogue} {@code Object}.
		 */
		public DrunkDwarfDialogue() {
			/**
			 * empty.
			 */
		}

		/**
		 * Constructs a new {@code DrunkDwarfDialogue} {@code Object}.
		 * @param player the player.
		 */
		public DrunkDwarfDialogue(Player player) {
			super(player);
		}

		@Override
		public DialoguePlugin newInstance(Player player) {
			return new DrunkDwarfDialogue(player);
		}

		@Override
		public boolean open(Object... args) {
			if (super.open(args)) {
				player.getDialogueInterpreter().sendDialogues(npc, FacialExpression.OSRS_NORMAL, "I 'new it were you matey! 'Ere, have some ob the good", "stuff!");
			} else {
				return true;
			}
			return true;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			wave(Animation.create(105));
			player.getInventory().add(new Item(1917), player);
			player.getInventory().add(new Item(1971), player);
			return true;
		}

		@Override
		public int[] getIds() {
			return new int[] { 956 };
		}

	}
}
