package core.game.interaction.object;

import core.cache.def.impl.SceneryDefinition;
import core.game.content.global.action.DoorActionHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.node.scenery.Scenery;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used to handle the interaction with the champions guild
 * door.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
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
			player.getAchievementDiaryManager().finishTask(player, DiaryType.VARROCK, 1, 1);
			DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
			return true;
		}
		return true;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(1805).getHandlers().put("option:open", this);
		return this;
	}

	@Override
	public Location getDestination(Node node, Node n) {
		return DoorActionHandler.getDestination(((Player) node), ((Scenery) n));
	}
}
