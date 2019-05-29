package plugin.interaction.object;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.quest.Quest;
import org.crandor.game.node.item.Item;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin to handle vampire slayer node handling.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class VampireSlayerPlugin extends OptionHandler {

	/**
	 * Represents the basement location.
	 */
	private static final Location BASEMENT = Location.create(3077, 9770, 0);

	/**
	 * Represents the ground floor location.
	 */
	private static final Location GROUND_FLOOR = Location.create(3115, 3356, 0);

	/**
	 * Represents the stake item
	 */
	private static final Item STAKE = new Item(1549);

	/**
	 * Represents the hammer item.
	 */
	private static final Item HAMMER = new Item(2347);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(33502).getConfigurations().put("option:open", this);
		ObjectDefinition.forId(2614).getConfigurations().put("option:open", this);
		ObjectDefinition.forId(32835).getConfigurations().put("option:walk-down", this);
		ObjectDefinition.forId(32836).getConfigurations().put("option:walk-up", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		final Quest quest = player.getQuestRepository().getQuest("Vampire Slayer");
		switch (option) {
		case "open":
			int id = ((GameObject) node).getId();
			switch (id) {
			case 2614:
				if (quest.getStage(player) == 100) {
					player.getDialogueInterpreter().sendDialogue("There's only a pillow in here..");
				} else if (quest.getStage(player) == 30) {
					if (!player.getInventory().containsItem(STAKE) && !player.getInventory().containsItem(HAMMER)) {
						player.getPacketDispatch().sendMessage("You must have a stake and hammer with you to kill the vampire.");
						return true;
					}
					final NPC o = player.getAttribute("count", null);
					if (o == null || !o.isActive()) {
						final NPC vampire = NPC.create(757, player.getLocation());
						vampire.setRespawn(false);
						vampire.setAttribute("player", player);
						vampire.init();
						vampire.getProperties().getCombatPulse().attack(player);
						player.setAttribute("count", vampire);
					}
				}
				break;
			case 33502:
				player.getPacketDispatch().sendMessage("The cupboard contains garlic. You take a clove.");
				if (!player.getInventory().add(new Item(1550))) {
					player.getPacketDispatch().sendMessage("Not enough inventory space.");
				}
				break;
			}
			break;
		case "walk-down":
			player.getProperties().setTeleportLocation(BASEMENT);
			player.getPacketDispatch().sendMessage("You walk down the stairs...");
			break;
		case "walk-up":
			player.getProperties().setTeleportLocation(GROUND_FLOOR);
			break;
		}
		return true;
	}
}
