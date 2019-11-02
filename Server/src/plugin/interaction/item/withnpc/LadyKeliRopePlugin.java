package plugin.interaction.item.withnpc;

import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.quest.Quest;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.GameWorld;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin used to tie up lady keli.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class LadyKeliRopePlugin extends UseWithHandler {

	/**
	 * Represents the rope item.
	 */
	private static final Item ROPE = new Item(954);

	/**
	 * Constructs a new {@code LadyKeliRopePlugin} {@code Object}.
	 */
	public LadyKeliRopePlugin() {
		super(954);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(919, NPC_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		final Quest quest = player.getQuestRepository().getQuest("Prince Ali Rescue");
		if (quest.getStage(player) >= 40 && player.getAttribute("guard-drunk", false) && quest.getStage(player) != 100) {
			if (player.getInventory().remove(ROPE)) {
				player.getDialogueInterpreter().sendDialogue("You overpower Keli, tie her up, and put her in a cupboard.");
				quest.setStage(player, 50);
				player.setAttribute("keli-gone", GameWorld.getTicks() + 350);
			}
		} else {
			if (quest.getStage(player) == 40) {
				player.getPacketDispatch().sendMessage("You need to do something about the guard first.");
			}
			return true;
		}
		return true;
	}

}
