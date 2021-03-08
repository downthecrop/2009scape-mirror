package core.game.content.ame.events.eviltwin;

import core.game.content.ame.AntiMacroEvent;
import core.game.content.dialogue.DialogueInterpreter;
import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.TeleportManager.TeleportType;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;
import core.game.system.script.ScriptContext;
import core.game.system.script.ScriptManager;
import core.game.world.map.Location;
import core.tools.RandomFunction;

/**
 * Handles Molly's dialogue.
 * @author Emperor
 */
public final class MollyDialogue extends DialoguePlugin {

	/**
	 * The possible rewards.
	 */
	private static final Item[] REWARDS = { new Item(1618, 2), new Item(1620, 3), new Item(1622, 3), new Item(1624, 4), };

	/**
	 * If the player successfully caught the evil twin.
	 */
	private int type;

	/**
	 * Constructs a new {@code MollyDialogue} {@code Object}.
	 */
	public MollyDialogue() {
		super();
	}

	/**
	 * Constructs a new {@code MollyDialogue} {@code Object}.
	 * @param player The player.
	 */
	public MollyDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new MollyDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		type = (Integer) args[1];
		if (type == 0) {
			player.getPacketDispatch().sendMessage("Congratulations! You caught the evil twin!");
			npc("Well done! You managed to catch my sister!");
		} else if (type == 1) {
			npc("Such incompetence! I should never have asked a", "baboon like you to do a complex task like this!", "Get out of my sight!");
		} else if (type == 2) {
			player("Well, I've managed to get her into the cage.");
		} else if (type == 3) {
			npc("Wait! Do you know what you're doing here?");
		}
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage++) {
		case 0:
			switch (type) {
			case 0:
				npc("Come next door and talk to me.");
				return true;
			case 1:
				player("Err, sorry. I seem to have messed it up a little.");
				return true;
			case 2:
				npc("Fantastic! For so many years I've had to put up with", "her and now she's locked up for good.");
				return true;
			case 3:
				options("Yes, I know.", "Erm, no I don't actually.");
				return true;
			}
			break;
		case 1:
			switch (type) {
			case 0:
				end();
				return true;
			case 1:
				player.getTeleporter().send(AntiMacroEvent.getRandomLocation(), TeleportType.NORMAL, -1);
				end();
				return true;
			case 2:
				npc("Thank you for all your help. Take this as a reward.");
				return true;
			case 3:
				switch (buttonId) {
				case 1:
					player.setAttribute("/save:ame:evil_twin_info", true);
					player("Yes, I know, I've been here before.");
					stage = 10;
					return true;
				case 2:
					stage = 20;
					player("Erm, no I don't actually.");
					return true;
				}
				break;
			}
			break;
		case 2:
			switch (type) {
			case 2:
				end();
				Location destination = player.getAttribute("ame:location");
				if (destination != null) {
					Item item = RandomFunction.getRandomElement(REWARDS);
					if (player.getInventory().hasSpaceFor(item)) {
						player.getInventory().add(item);
					} else {
						GroundItemManager.create(item, destination, player);
					}
					player.getDialogueInterpreter().sendItemMessage(item, "Molly's given you " + item.getAmount() + " " + item.getName().toLowerCase() + "s.");
					player.getProperties().setTeleportLocation(destination);
					player.removeAttribute("ame:location");
				}
				return true;
			}
			break;
		case 10:
			npc("Good luck!");
			break;
		case 11:
			end();
			break;
		case 20:
			end();
			ScriptContext script = DialogueInterpreter.getScript(npc.getId());
			if (script != null && (script = ScriptManager.getMethod(script, "event_info")) != null) {
				player.getDialogueInterpreter().startScript(npc.getId(), script, player, npc);
			}
			break;
		}
		return false;
	}

	@Override
	public int[] getIds() {
		return new int[] { DialogueInterpreter.getDialogueKey("ame:molly") };
	}

}