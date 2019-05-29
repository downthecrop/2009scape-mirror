package plugin.interaction.object;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.content.global.action.DoorActionHandler;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.diary.DiaryType;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin used to handle the interaction with the champions guild
 * door.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class ChampionsGuildDoor extends OptionHandler {

	@Override
	public boolean handle(Player player, Node node, String option) {
		if (player.getLocation().getY() > 3362 && player.getQuestRepository().getPoints() < 32) {
			player.getDialogueInterpreter().open(70099, "You have not proved yourself worthy to enter here yet.");
			player.getPacketDispatch().sendMessage("The door won't open - you need at least 32 Quest Points.");
		} else {
			if (player.getLocation().getX() == 3191 && player.getLocation().getY() == 3363) {
				player.getDialogueInterpreter().sendDialogues(198, null, "Greetings bold adventurer. Welcome to the guild of", "Champions.");
			}
			if (!player.getAchievementDiaryManager().getDiary(DiaryType.VARROCK).isComplete(1, 1)) {
				player.getAchievementDiaryManager().getDiary(DiaryType.VARROCK).updateTask(player, 1, 1, true);
			}
			DoorActionHandler.handleAutowalkDoor(player, (GameObject) node);
			return true;
		}
		return true;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(1805).getConfigurations().put("option:open", this);
		return this;
	}

	@Override
	public Location getDestination(Node node, Node n) {
		return DoorActionHandler.getDestination(((Player) node), ((GameObject) n));
	}
}
