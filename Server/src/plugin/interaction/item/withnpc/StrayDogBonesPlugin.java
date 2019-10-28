package plugin.interaction.item.withnpc;

import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.diary.DiaryType;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Handles the bones on stray dogs plugin.
 * @author Vexia
 */
@InitializablePlugin
public class StrayDogBonesPlugin extends UseWithHandler {

	/**
	 * The dog ids.
	 */
	private static final int[] DOGS = new int[] { 4766, 4767, 5917, 5918 };

	/**
	 * Constructs a new {@code StrayDogBonesPlugin} {@code Object}
	 */
	public StrayDogBonesPlugin() {
		super(526, 528, 530, 532, 534, 536, 2530, 2859, 3123, 3125, 3127, 3128, 3129, 3130, 3131, 3132, 3133, 3179, 3180, 3181, 3182, 3183, 3185, 3186, 3187, 4812, 4830, 4832, 4834, 6729, 6812, 11806);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (int id : DOGS) {
			addHandler(id, NPC_TYPE, this);
		}
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Item item = event.getUsedItem();
		final Player player = event.getPlayer();
		final NPC npc = event.getUsedWith().asNpc();
		if (player.getInventory().remove(item)) {
			player.sendMessage("You feed your dog bones.");
			npc.sendChat("Woof");
			if (!player.getAchievementDiaryManager().getDiary(DiaryType.VARROCK).isComplete(0, 8)) {
				player.getAchievementDiaryManager().getDiary(DiaryType.VARROCK).updateTask(player, 0, 8, true);
			}
		}
		return true;
	}

}
