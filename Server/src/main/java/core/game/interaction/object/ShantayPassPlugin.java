package core.game.interaction.object;

import core.cache.def.impl.NPCDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.content.global.action.DoorActionHandler;
import core.game.node.entity.skill.agility.AgilityHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin to handle the shantay pass.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public class ShantayPassPlugin extends OptionHandler {

	/**
	 * Represents a shantay pass item.
	 */
	private static final Item PASS = new Item(1854);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (int i = 35542; i < 35545; i++) {
			SceneryDefinition.forId(i).getHandlers().put("option:look-at", this);
			SceneryDefinition.forId(i).getHandlers().put("option:go-through", this);
			SceneryDefinition.forId(i).getHandlers().put("option:quick-pass", this);
		}
		SceneryDefinition.forId(35400).getHandlers().put("option:look-at", this);
		SceneryDefinition.forId(35400).getHandlers().put("option:go-through", this);
		SceneryDefinition.forId(35400).getHandlers().put("option:quick-pass", this);
		NPCDefinition.forId(838).getHandlers().put("option:bribe", this);
		SceneryDefinition.forId(35401).getHandlers().put("option:open", this);
		SceneryDefinition.forId(2693).getHandlers().put("option:open", this);
		new ShantayComponentPlugin().newInstance(arg);
		return this;
	}

	@Override
	public boolean handle(final Player player, Node node, String option) {
		final int id = node instanceof Scenery ? ((Scenery) node).getId() : ((NPC) node).getId();
		switch (option) {
		case "open":
			if (id == 2693) {
				player.getBank().open();
				return true;
			}
			if (player.getAttribute("shantay-jail", false) && player.getLocation().getX() > 3299) {
				player.removeAttribute("shantay-jail");// if we tele
				// out(witch is
				// allowed) then we
				// need to remove.
			}
			if (!player.getAttribute("shantay-jail", false)) {
				DoorActionHandler.handleDoor(player, (Scenery) node);
				return true;
			} else {
				player.getDialogueInterpreter().open(836, null, true);
			}
			break;
		case "bribe":
			player.getDialogueInterpreter().open(838, ((NPC) node));
			break;
		case "look-at":
			player.getDialogueInterpreter().sendDialogue("<col=8A0808>The Desert is a VERY Dangerous place. Do not enter if you are", "<col=8A0808>afraid of dying. Beware of high temperatures, and storms, robbers,", "<col=8A0808>and slavers. No responsibility is taken by Shantay if anything bad", "<col=8A0808>should happen to you in any circumstances whatsoever.");
			break;
		case "go-through":
			if (player.getLocation().getY() < 3117) {
				player.getPacketDispatch().sendMessage("You go through the gate.");
				AgilityHandler.walk(player, 0, player.getLocation(), player.getLocation().transform(0, player.getLocation().getY() > 3116 ? -2 : 2, 0), null, 0, null);
				return true;
			}
			player.getInterfaceManager().open(new Component(565));
			break;
		case "quick-pass":
			if (player.getLocation().getY() < 3117) {
				AgilityHandler.walk(player, 0, player.getLocation(), player.getLocation().transform(0, player.getLocation().getY() > 3116 ? -2 : 2, 0), null, 0, null);
				return true;
			}
			player.getDialogueInterpreter().open(838, 838, true);
			break;
		}
		return true;
	}

	@Override
	public Location getDestination(Node node, Node n) {
		if (n instanceof Scenery) {
			final Scenery object = (Scenery) n;
			if (object.getId() == 35543) {
				return object.getLocation().transform(-1, node.getLocation().getY() > n.getLocation().getY() ? 1 : -1, 0);
			} else if (object.getId() == 35544) {
				return object.getLocation().transform(-1, node.getLocation().getY() > n.getLocation().getY() ? 1 : -1, 0);
			} else if (object.getId() == 35542) {
				return object.getLocation().transform(1, node.getLocation().getY() > n.getLocation().getY() ? 1 : -1, 0);
			}
		}
		return null;
	}

	/**
	 * Represents the shantay component plugin.
	 * @author 'Vexia
	 * @version 1.0
	 */
	public final class ShantayComponentPlugin extends ComponentPlugin {

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			ComponentDefinition.forId(565).setPlugin(this);
			return this;
		}

		@Override
		public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
			switch (button) {
			case 17:// proceed.
				player.getInterfaceManager().close();
				if (!player.getInventory().containsItem(PASS)) {
					player.getDialogueInterpreter().sendDialogues(838, null, "You need a Shantay pass to get through this gate. See", "Shantay, he will sell you one for a very reasonable", "price.");
					return true;
				} else {
					player.getDialogueInterpreter().open(838, null, true);
				}
				break;
			case 18:// stay out.
				player.getInterfaceManager().close();
				player.getDialogueInterpreter().sendDialogue("You decide that your visit to the desert can be postponed.", "Perhaps indefinitely.");
				break;
			}
			return true;
		}

	}
}
