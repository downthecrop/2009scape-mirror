package core.game.content.zone.phasmatys;

import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.plugin.Plugin;
import rs09.plugin.ClassScanner;
import core.tools.RandomFunction;

/**
 * Handles the gravingas npc.
 * @author Vexia
 */
public final class GravingasNPC extends AbstractNPC {

	/**
	 * The force chats.
	 */
	private static final String[] CHATS = new String[] { "Down with Necrovaus!!", "Rise up my fellow ghosts, and we shall be victorious!", "Power to the Ghosts!!", "Rise together, Ghosts without a cause!!", "United we conquer - divided we fall!!", "We shall overcome!!", "Let Necrovarus know we want out!!", "Don't stay silent - victory in numbers!!" };

	/**
	 * Constructs a new {@code GravingasNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 */
	public GravingasNPC(int id, Location location) {
		super(id, location);
	}

	/**
	 * Constructs a new {@code GravingasNPC} {@code Object}.
	 */
	public GravingasNPC() {
		super(0, null);
	}

	@Override
	public void handleTickActions() {
		super.handleTickActions();
		if (RandomFunction.random(45) == 5) {
			sendChat(CHATS[RandomFunction.random(CHATS.length)]);
		}
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new GravingasNPC(id, location);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ClassScanner.definePlugin(new GravingasDialogue());
		return super.newInstance(arg);
	}

	@Override
	public int[] getIds() {
		return new int[] { 1685 };
	}

	/**
	 * Handles the gravingas dialogue.
	 * @author Vexia
	 */
	public final class GravingasDialogue extends DialoguePlugin {

		/**
		 * Constructs a new {@code GravingasDialogue} {@code Object}.
		 * @param player the player.
		 */
		public GravingasDialogue(final Player player) {
			super(player);
		}

		/**
		 * Constructs a new {@code GravingasDialogue} {@code Object}.
		 */
		public GravingasDialogue() {
			/**
			 * empty.
			 */
		}

		@Override
		public DialoguePlugin newInstance(Player player) {
			return new GravingasDialogue(player);
		}

		@Override
		public boolean open(Object... args) {
			if (PhasmatysZone.hasAmulet(player)) {
				npc("Will you join with me and protect against the evil ban", "of Nercrovarus and his disciples?");
				stage = 0;
			} else {
				npc("Woooo wooo wooooo woooo");
				stage = 10;
			}
			return true;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			switch (stage) {
			case 0:
				player("I'm sorry, I don't really think I should get involved.");
				stage++;
				break;
			case 1:
				npc("Ah, the youth of today - so apathetic to politics.");
				stage++;
				break;
			case 2:
				end();
				break;
			case 10:
				interpreter.sendDialogue("You cannot understand the ghost.");
				stage = 11;
				break;
			case 11:
				end();
				break;
			}
			return true;
		}

		@Override
		public int[] getIds() {
			return GravingasNPC.this.getIds();
		}

	}

}
