package core.game.content.activity.guild;

import core.cache.def.impl.ObjectDefinition;
import core.game.content.global.action.ClimbActionHandler;
import core.game.content.global.action.DoorActionHandler;
import core.game.node.entity.skill.Skills;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.object.GameObject;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used for the mining guild.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class MiningGuildPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(2113).getHandlers().put("option:climb-down", this);
		ObjectDefinition.forId(30941).getHandlers().put("option:climb-up", this);
		ObjectDefinition.forId(2112).getHandlers().put("option:open", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		if (option.equals("climb-down")) {
			if (player.getLocation().withinDistance(Location.create(3019, 3339, 0), 4)) {
				if (player.getSkills().getStaticLevel(Skills.MINING) < 60) {
					player.getDialogueInterpreter().open(382, NPC.create(382, Location.create(0, 0, 0)), 1);
					return true;
				}
				ClimbActionHandler.climb(player, new Animation(828), Location.create(3021, 9739, 0));
				return true;
			}
			ClimbActionHandler.climbLadder(player, (GameObject) node, option);
			return true;
		}
		if (option.equals("open")) {
			if (player.getSkills().getStaticLevel(Skills.MINING) < 60) {
				player.getDialogueInterpreter().open(382, NPC.create(382, Location.create(0, 0, 0)), 1);
				return true;
			}
			DoorActionHandler.handleAutowalkDoor(player, (GameObject) node);
		}
		if (option.equals("climb-up")) {
			if (player.getLocation().withinDistance(new Location(3019, 9739, 0))) {
				ClimbActionHandler.climb(player, new Animation(828), new Location(3017, 3339, 0));
			} else {
				ClimbActionHandler.climbLadder(player, (GameObject) node, "climb-up");
			}
		}
		return true;
	}

}
