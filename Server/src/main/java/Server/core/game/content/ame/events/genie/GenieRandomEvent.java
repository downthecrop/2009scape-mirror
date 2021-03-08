package core.game.content.ame.events.genie;

import java.nio.ByteBuffer;

import core.game.content.ame.AntiMacroDialogue;
import core.game.content.ame.AntiMacroEvent;
import core.plugin.Initializable;
import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.map.Location;
import core.plugin.PluginManager;

/**
 * Handles the genie random event.
 * @author Vexia
 */
@Initializable
public final class GenieRandomEvent extends AntiMacroEvent {

	/**
	 * The lamp item.
	 */
	private static final Item LAMP = new Item(2528);

	/**
	 * Constructs a new {@code GenieRandomEvent} {@code Object}.
	 */
	public GenieRandomEvent() {
		super("Genie", true, false);
	}

	@Override
	public void save(ByteBuffer buffer) {
	}

	@Override
	public void parse(ByteBuffer buffer) {
	}

	@Override
	public boolean start(Player player, boolean login, Object... args) {
		if (player.hasItem(new Item(2528))) {
			return false;
		}
		super.init(player);
		final GenieNPC npc = new GenieNPC(409, player.getLocation(), this, player);
		npc.init();
		return true;
	}

	@Override
	public AntiMacroEvent create(Player player) {
		final GenieRandomEvent event = new GenieRandomEvent();
		event.player = player;
		return event;
	}

	@Override
	public Location getSpawnLocation() {
		return null;
	}

	@Override
	public void configure() {
	}

	@Override
	public void register() {
		PluginManager.definePlugin(new GenieDialogue());
	}

	@Override
	public String getGenderPrefix(boolean male) {
		return male ? "Master" : "Mistress";
	}

	/**
	 * Handles the dialogue used for the genie npc.
	 * @author Vexia
	 */
	public final class GenieDialogue extends AntiMacroDialogue {

		/**
		 * Constructs a new {@code GenieDialogue} {@code Object}.
		 */
		public GenieDialogue() {
			/**
			 * empty.
			 */
		}

		/**
		 * Constructs a new {@code GenieDialogue} {@code Object}.
		 * @param player the player.
		 */
		public GenieDialogue(Player player) {
			super(player);
		}

		@Override
		public DialoguePlugin newInstance(Player player) {
			return new GenieDialogue(player);
		}

		@Override
		public boolean open(Object... args) {
			if (super.open(args)) {
				npc("Ah, so you are there " + player.getAntiMacroHandler().getEvent().getGenderPrefix().toLowerCase() + ". I'm so glad you", "summoned me. Please take this lamp and make your", "wish!");
			}
			return true;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			switch (stage) {
			case 0:
				npc("I hope you're happy with your wish. I must be leaving", "now, though.");
				stage++;
				break;
			case 1:
				wave();
				player.getInventory().add(LAMP, player);
				break;
			}
			return true;
		}

		@Override
		public int[] getIds() {
			return new int[] { 409 };
		}

	}
}
