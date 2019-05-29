package plugin.interaction.city;

import org.crandor.cache.def.impl.NPCDefinition;
import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.system.task.Pulse;
import org.crandor.game.world.GameWorld;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Represents the port sarim plugin.
 * @author 'Vexia
 * @date 30/11/2013
 */
@InitializablePlugin
public final class PortSarimPlugin extends OptionHandler {

	/**
	 * Represents the taking animation.
	 */
	private static final Animation TAKE_ANIM = new Animation(832);

	/**
	 * Represents the karamjan rum item.
	 */
	private static final Item KARAMJAN_RUM = new Item(431);

	/**
	 * Represents the messages the sleeping guard can say.
	 */
	private static final String MESSAGES[] = new String[] { "Hmph... heh heh heh...", "Mmmm... big pint of beer... kebab...", "Mmmmmm... donuts...", "Guh.. mwww... zzzzzz..." };

	/**
	 * Represents the entrana monks.
	 */
	private static final int[] MONKS = new int[] { 2728, 657, 2729, 2730, 2731, 658 };

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		NPCDefinition.forId(2704).getConfigurations().put("option:talk-to", this);
		ObjectDefinition.forId(9565).getConfigurations().put("option:open", this);
		ObjectDefinition.forId(9565).getConfigurations().put("option:pick-lock", this);
		ObjectDefinition.forId(9563).getConfigurations().put("option:open", this);
		for (int i : MONKS) {
			NPCDefinition.forId(i).getConfigurations().put("option:take-boat", this);
		}
		ObjectDefinition.forId(2071).getConfigurations().put("option:search", this);
		NPCDefinition.forId(745).getConfigurations().put("option:attack", this);
		ObjectDefinition.forId(33173).getConfigurations().put("option:exit", this);
		ObjectDefinition.forId(33174).getConfigurations().put("option:enter", this);
		return this;
	}

	@Override
	public boolean handle(final Player player, Node node, String option) {
		switch (option) {
		case "enter":
			player.getProperties().setTeleportLocation(Location.create(3056, 9562, 0));
			player.getPacketDispatch().sendMessage("You leave the icy cavern.");
			break;
		case "exit":
			player.getDialogueInterpreter().open(238284);
			break;
		case "attack":
			if (player.getQuestRepository().getQuest("Dragon Slayer").getStage(player) != 20) {
				player.getPacketDispatch().sendMessage("The goblin is already in prison. You have no reason to attack him.");
			} else {
				player.getPulseManager().clear("interaction:attack:" + node.hashCode());
				player.getProperties().getCombatPulse().attack(node);
			}
			break;
		case "take-boat":
			player.getDialogueInterpreter().open(((NPC) node).getId(), ((NPC) node));
			break;
		case "open":
			switch (((GameObject) node).getId()) {
			case 9565:
			case 9563:
				player.getPacketDispatch().sendMessage("The door is securely locked.");
				break;
			}
			break;
		case "pick-lock":
			switch (((GameObject) node).getId()) {
			case 9565:
				if (player.getLocation().getY() <= 3187) {
					player.getPacketDispatch().sendMessage("You simply cannot find a way to pick the lock from this side.");
					return true;
				}
				break;
			}
			break;
		case "talk-to":
			player.lock(2);
			((NPC) node).sendChat(MESSAGES[RandomFunction.random(MESSAGES.length)]);
			GameWorld.submit(new Pulse(1) {
				@Override
				public boolean pulse() {
					player.getDialogueInterpreter().sendDialogues(player, null, "Maybe I should let him sleep.");
					return true;
				}
			});
			break;
		case "search":// banan box
			if (player.getInventory().freeSlots() == 0) {
				player.getPacketDispatch().sendMessage("Not enough inventory space.");
				return true;
			}
			player.getPacketDispatch().sendMessage("There are lots of bananas in the crate.");
			player.lock(2);
			GameWorld.submit(new Pulse(1) {
				@Override
				public boolean pulse() {
					if (player.getAttribute("wydin-rum", false)) {
						if (player.getInventory().add(KARAMJAN_RUM)) {
							player.removeAttribute("wydin-rum");
							player.removeAttribute("stashed-rum");
							player.animate(TAKE_ANIM);
							player.getPacketDispatch().sendMessage("You find your bottle of rum in amongst the bananas.");
						}
						return true;
					}
					player.getDialogueInterpreter().open(9682749);
					return true;
				}

			});
			break;
		}
		return true;
	}

	@Override
	public boolean isWalk(final Player player, final Node node) {
		return !(node instanceof Item);
	}
}
