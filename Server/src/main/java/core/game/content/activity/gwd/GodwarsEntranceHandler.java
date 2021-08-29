package core.game.content.activity.gwd;

import core.cache.def.impl.SceneryDefinition;
import core.plugin.Initializable;
import core.game.content.dialogue.FacialExpression;
import core.game.node.entity.skill.Skills;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.impl.ForceMovement;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;

/**
 * Handles the entrance hole to the godwars dungeon.
 * @author Emperor
 */
@Initializable
public final class GodwarsEntranceHandler extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(26340).getHandlers().put("option:tie-rope", this);
		SceneryDefinition.forId(26341).getHandlers().put("option:climb-down", this);
		SceneryDefinition.forId(26338).getHandlers().put("option:move", this);
		SceneryDefinition.forId(26305).getHandlers().put("option:crawl-through", this);
		return this;
	}

	@Override
	public boolean handle(final Player player, Node node, String option) {
		Scenery object = (Scenery) node;
		switch (object.getId()) {
		case 26340:
			if (!player.getInventory().remove(new Item(954))) {
				player.getPacketDispatch().sendMessage("You don't have a rope to tie around the pillar.");
				return true;
			}
			player.varpManager.get(1048).setVarbit(0,1).send(player);
			player.varpManager.flagSave(1048);
			return true;
		case 26341:
			if (player.getSkills().getStaticLevel(Skills.AGILITY) < 15) {
				player.getPacketDispatch().sendMessage("You need an Agility level of 15 to enter this.");
				return true;
			}
			if (player.varpManager.get(1048).getVarbit(4) == null) {
				player.getDialogueInterpreter().sendDialogues(6201, FacialExpression.HALF_GUILTY, "Cough... Hey, over here.");
				return true;
			}
			player.lock(2);
			player.getPacketDispatch().sendMessage("You climb down the rope.");
			player.animate(Animation.create(828));
			GameWorld.getPulser().submit(new Pulse(1, player) {
				@Override
				public boolean pulse() {
					player.getProperties().setTeleportLocation(Location.create(2882, 5311, 2));
					return true;
				}
			});
			return true;
		case 26338:
			if (player.getSkills().getStaticLevel(Skills.STRENGTH) < 60) {
				player.getPacketDispatch().sendMessage("You need a Strength level of 60 to move this boulder.");
				return true;
			}
			player.getPacketDispatch().sendSceneryAnimation(object, Animation.create(6980));
			if (player.getLocation().getY() < 3716) {
				ForceMovement.run(player, Location.create(2898, 3715, 0), Location.create(2898, 3719, 0), new Animation(6978), 3);
			} else {
				ForceMovement.run(player, Location.create(2898, 3719, 0), Location.create(2898, 3715, 0), new Animation(6979), 3);
			}
			GameWorld.getPulser().submit(new Pulse(12, player) {
				@Override
				public boolean pulse() {
					player.getPacketDispatch().sendSceneryAnimation(RegionManager.getObject(0, 2898, 3716), Animation.create(6981));
					return true;
				}
			});
			return true;
		case 26305:
			if (player.getSkills().getStaticLevel(Skills.AGILITY) < 60) {
				player.getPacketDispatch().sendMessage("You need an Agility level of 60 to squeeze through the crack.");
				return true;
			}
			if (object.getLocation().equals(Location.create(2900, 3713, 0))) {
				player.getProperties().setTeleportLocation(Location.create(2904, 3720, 0));
			} else {
				player.getProperties().setTeleportLocation(Location.create(2899, 3713, 0));
			}
			return true;
		}
		return false;
	}

}
