package plugin.interaction.object;

import org.crandor.cache.def.impl.NPCDefinition;
import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.component.Component;
import org.crandor.game.component.ComponentDefinition;
import org.crandor.game.component.ComponentPlugin;
import org.crandor.game.content.global.action.DoorActionHandler;
import org.crandor.game.content.skill.member.agility.AgilityHandler;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin to handle the shantay pass.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public class ShantayPassPlugin extends OptionHandler {

	/**
	 * Represents a shantay pass item.
	 */
	private static final Item PASS = new Item(1854);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (int i = 35542; i < 35545; i++) {
			ObjectDefinition.forId(i).getConfigurations().put("option:look-at", this);
			ObjectDefinition.forId(i).getConfigurations().put("option:go-through", this);
			ObjectDefinition.forId(i).getConfigurations().put("option:quick-pass", this);
		}
		NPCDefinition.forId(838).getConfigurations().put("option:bribe", this);
		ObjectDefinition.forId(35401).getConfigurations().put("option:open", this);
		ObjectDefinition.forId(2693).getConfigurations().put("option:open", this);
		new ShantayComponentPlugin().newInstance(arg);
		return this;
	}

	@Override
	public boolean handle(final Player player, Node node, String option) {
		final int id = node instanceof GameObject ? ((GameObject) node).getId() : ((NPC) node).getId();
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
				DoorActionHandler.handleDoor(player, (GameObject) node);
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
		if (n instanceof GameObject) {
			final GameObject object = (GameObject) n;
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
