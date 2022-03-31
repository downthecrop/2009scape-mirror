package core.game.interaction.city;

import core.cache.def.impl.SceneryDefinition;
import core.plugin.Initializable;
import core.game.content.dialogue.DialogueInterpreter;
import core.game.content.dialogue.DialoguePlugin;
import core.game.content.global.action.DoorActionHandler;
import core.game.node.entity.skill.crafting.gem.Gems;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.TeleportManager.TeleportType;
import core.game.node.scenery.Scenery;
import core.game.world.map.Location;
import core.plugin.Plugin;
import rs09.plugin.ClassScanner;

/**
 * Handles the zanaris city plugin.
 * @author Vexia
 */
@Initializable
public final class ZanarisPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ClassScanner.definePlugin(new MagicDoorDialogue());
		SceneryDefinition.forId(12094).getHandlers().put("option:use", this);
		SceneryDefinition.forId(12045).getHandlers().put("option:open", this);
		SceneryDefinition.forId(12047).getHandlers().put("option:open", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		switch (node.getId()) {
		case 12094:
			player.getWalkingQueue().reset();
			player.getWalkingQueue().addPath(2452, 4473);
			player.getTeleporter().send(Location.create(3201, 3169, 0), TeleportType.FAIRY_RING);
			break;
		case 12045:
		case 12047:
			if ((node.getId() == 12045 && node.getLocation().equals(new Location(2469, 4438, 0)) && player.getLocation().getX() >= 2470) || player.getLocation().getY() < 4434 && (node.getId() == 12045 || node.getId() == 12047 && node.getLocation().equals(new Location(2465, 4434, 0))) || node.getId() == 12047 && player.getLocation().getX() >= 2470) {
				DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
				return true;
			}
			player.getDialogueInterpreter().open(MagicDoorDialogue.NAME, node);
			break;
		}
		return true;
	}

	/**
	 * Handles the magic door dialogue.
	 * @author Vexia
	 */
	public final class MagicDoorDialogue extends DialoguePlugin {

		/**
		 * The dialogue name.
		 */
		public static final String NAME = "tax door";

		/**
		 * The object.
		 */
		private Scenery door;

		/**
		 * Constructs a new {@code MagicDoorDialogue} {@code Object}.
		 */
		public MagicDoorDialogue() {
			/**
			 * empty.
			 */
		}

		/**
		 * Constructs a new {@code MagicDoorDialogue} {@code Object}.
		 * @param player the player.
		 */
		public MagicDoorDialogue(Player player) {
			super(player);
		}

		@Override
		public DialoguePlugin newInstance(Player player) {
			return new MagicDoorDialogue(player);
		}

		@Override
		public boolean open(Object... args) {
			door = (Scenery) args[0];
			npc("You may not pass through this door without paying the", "trading tax.");
			return true;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			switch (stage) {
			case 0:
				player("So how much is the tax?");
				stage++;
				break;
			case 1:
				npc("The cost is one diamond.");
				stage++;
				break;
			case 2:
				options("Okay...", "A diamond? Are you crazy?", "I haven't brought my diamonds with me.", "What do you do with all the diamonds you get?");
				stage++;
				break;
			case 3:
				switch (buttonId) {
				case 1:
					player("Okay...");
					stage = 10;
					break;
				case 2:
					player("A diamond? Are you crazy?");
					stage = 20;
					break;
				case 3:
					player("I haven't brought my diamonds with me.");
					stage = 30;
					break;
				case 4:
					player("What do you do with all the diamonds you get?");
					stage = 40;
					break;
				}
				break;
			case 10:
				if (!player.getInventory().containsItem(Gems.DIAMOND.getGem())) {
					player("...but...");
					stage = 11;
				} else {
					end();
					if (player.getInventory().remove(Gems.DIAMOND.getGem())) {
						DoorActionHandler.handleAutowalkDoor(player, door);
						player.getPacketDispatch().sendMessage("You give the doorman a diamond.");
					}
				}
				break;
			case 11:
				player("I haven't brought my diamonds with me.");
				stage = 30;
				break;
			case 20:
				npc("Not at all. Those are the rules.");
				stage++;
				break;
			case 21:
				end();
				break;
			case 30:
				npc("No tax, no entry.");
				stage++;
				break;
			case 31:
				end();
				break;
			case 40:
				npc("Ever heard of fairylights? Well how do you think we", "make 'em? First we collect a pile of gems and then we", "get a spider to spin 'em into a long web, we light the", "jewels by imbuing each one with a little bit of magic.");
				stage++;
				break;
			case 41:
				player("So you're telling me fairylights are made out of gems?");
				stage++;
				break;
			case 42:
				npc("That's right, how else could we make 'em twinkle so", "beautifully?");
				stage++;
				break;
			case 43:
				end();
				break;
			}
			return true;
		}

		@Override
		public int[] getIds() {
			return new int[] { 3321, DialogueInterpreter.getDialogueKey(NAME) };
		}

	}
}
