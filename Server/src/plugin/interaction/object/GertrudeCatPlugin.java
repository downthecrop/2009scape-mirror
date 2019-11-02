package plugin.interaction.object;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.quest.Quest;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Represents the plugin to handle interactions with gertrudes cat.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class GertrudeCatPlugin extends OptionHandler {

	/**
	 * Represents the kitten item.
	 */
	private static final Item KITTEN = new Item(1555);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(2620).getConfigurations().put("option:search", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		final Quest quest = player.getQuestRepository().getQuest("Gertrude's Cat");
		switch (option) {
		case "search":
			if (quest.getStage(player) == 50 && !player.getInventory().containsItem(KITTEN) && !player.getBank().containsItem(KITTEN)) {
				quest.setStage(player, 40);
			}
			if (quest.getStage(player) != 40) {
				player.getPacketDispatch().sendMessage("You search the crate.");
				player.getPacketDispatch().sendMessage("You find nothing.");
				if (RandomFunction.random(0, 3) == 1) {
					player.getPacketDispatch().sendMessage("You can hear kittens mewing close by...");
					player.setAttribute("findkitten", true);
				}
			} else {
				if (player.getAttribute("findkitten", false) && player.getInventory().freeSlots() > 0) {
					quest.setStage(player, 50);
					player.getDialogueInterpreter().sendDialogue("You find a kitten! You carefully place it in your backpack.");
					player.getInventory().add(KITTEN);
					return true;
				}
			}
			break;
		}
		return true;
	}

}
