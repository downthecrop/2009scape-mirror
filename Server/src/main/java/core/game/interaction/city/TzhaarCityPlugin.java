package core.game.interaction.city;

import core.cache.def.impl.SceneryDefinition;
import core.game.content.activity.ActivityManager;
import core.game.content.dialogue.DialoguePlugin;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used for tzhaar city.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class TzhaarCityPlugin extends OptionHandler {

	/**
	 * Represents the locations to use.
	 */
	private static final Location[] LOCATIONS = new Location[] { Location.create(2480, 5175, 0), Location.create(2866, 9571, 0) };

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(31284).getHandlers().put("option:enter", this);// karamja
		// cave.
		SceneryDefinition.forId(9359).getHandlers().put("option:enter", this);// tzhaar
		// exit
		SceneryDefinition.forId(9356).getHandlers().put("option:enter", this);
		SceneryDefinition.forId(9369).getHandlers().put("option:pass", this);
		new TzhaarDialogue().init();
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		int id = ((Scenery) node).getId();
		switch (option) {
		case "enter":
			switch (id) {
			case 31284:
				player.getProperties().setTeleportLocation(LOCATIONS[0]);
				break;
			case 9359:
				player.getProperties().setTeleportLocation(LOCATIONS[1]);
				break;
			case 9356:
				if (player.getFamiliarManager().hasFamiliar()) {
					player.getPacketDispatch().sendMessage("You can't enter this with a follower.");
					break;
				}
				ActivityManager.start(player, "fight caves", false);
				break;
			}
			break;
		case "pass":
			switch (id) {
			case 9369:
				ActivityManager.start(player, "fight pits", false);
				break;
			}
			break;
		}
		return true;
	}

	/**
	 * Represents the dialogue plugin used for the tzhaar npcs.
	 * @author 'Vexia
	 * @version 1.0
	 */
	public static final class TzhaarDialogue extends DialoguePlugin {

		/**
		 * Constructs a new {@code TzhaarDialogue} {@code Object}.
		 */
		public TzhaarDialogue() {
			/**
			 * empty.
			 */
		}

		/**
		 * Constructs a new {@code TzhaarDialogue} {@code Object}.
		 * @param player the player.
		 */
		public TzhaarDialogue(Player player) {
			super(player);
		}

		@Override
		public DialoguePlugin newInstance(Player player) {
			return new TzhaarDialogue(player);
		}

		@Override
		public boolean open(Object... args) {
			npc = (NPC) args[0];
			npc("Can I help you JalYt-Ket-" + player.getUsername() + "?");
			return true;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			switch (stage) {
			case 0:
				options("What do you have to trade?", "What did you call me?", "No I'm fine thanks.");
				stage = 1;
				break;
			case 1:
				switch (buttonId) {
				case 1:
					end();
					npc.openShop(player);
					break;
				case 2:
					player("What did you call me?");
					stage = 20;
					break;
				case 3:
					player("No I'm fine thanks.");
					stage = 30;
					break;
				}
				break;
			case 10:
				break;
			case 20:
				npc("Are you not JalYt-Ket?");
				stage = 21;
				break;
			case 21:
				options("What's a 'JalYt-Ket'?", "I guess so...", "No I'm not!");
				stage = 22;
				break;
			case 22:
				switch (buttonId) {
				case 1:
					player("What's a 'JalYt-Ket'?");
					stage = 100;
					break;
				case 2:
					player("I guess so...");
					stage = 120;
					break;
				case 3:
					player("No I'm not!");
					stage = 130;
					break;
				}
				break;
			case 100:
				npc("That what you are... you tough and strong no?");
				stage = 101;
				break;
			case 101:
				player("Well yes I suppose I am...");
				stage = 102;
				break;
			case 102:
				npc("Then you JalYt-Ket!");
				stage = 103;
				break;
			case 103:
				end();
				break;
			case 120:
				npc("Well then, no problems.");
				stage = 121;
				break;
			case 121:
				end();
				break;
			case 130:
				end();
				break;
			case 23:
				end();
				break;
			case 30:
				end();
				break;
			}
			return true;
		}

		@Override
		public int[] getIds() {
			return new int[] { 2620, 2622, 2623 };
		}

	}
}
