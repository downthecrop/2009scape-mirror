package core.game.interaction.item;

import core.cache.def.impl.ItemDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.object.Scenery;
import core.game.node.object.SceneryBuilder;
import core.game.world.map.Location;
import core.game.world.map.Region;
import core.game.world.map.RegionManager;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;
import rs09.game.content.quest.members.naturespirit.NSUtils;

/**
 * Handles the Silver Sickle (b) to collect Mort Myre Fungus.
 * @author Splinter
 */
@Initializable
public final class SilverSicklePlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.forId(2963).getHandlers().put("option:operate", this);
		ItemDefinition.forId(2963).getHandlers().put("option:cast bloom", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		Region region = RegionManager.forId(player.getLocation().getRegionId());
		switch (option) {
		case "operate":
		case "cast bloom":
			NSUtils.castBloom(player);
			return true;
		}
		return false;
	}

	/**
	 * Handles the draining of prayer points and physical graphics and
	 * animation.
	 */
	public void handleVisuals(Player player, Node node) {
		player.getSkills().decrementPrayerPoints(RandomFunction.random(1, 3));
		player.getPacketDispatch().sendAnimation(9021);
		final Location[] AROUND_YOU = new Location[] { Location.create(player.getLocation().getX() - 1, player.getLocation().getY(), 0), Location.create(player.getLocation().getX() + 1, player.getLocation().getY(), 0), Location.create(player.getLocation().getX(), player.getLocation().getY() - 1, 0), Location.create(player.getLocation().getX(), player.getLocation().getY() + 1, 0), Location.create(player.getLocation().getX() + 1, player.getLocation().getY() + 1, 0), Location.create(player.getLocation().getX() - 1, player.getLocation().getY() + 1, 0), Location.create(player.getLocation().getX() + 1, player.getLocation().getY() - 1, 0), Location.create(player.getLocation().getX() - 1, player.getLocation().getY() - 1, 0), Location.create(player.getLocation().getX() + 1, player.getLocation().getY() + 1, 0), };
		for (Location location : AROUND_YOU) {
			// The graphic is meant to play on a 3x3 radius around you, but not
			// including the tile you are on.
			player.getPacketDispatch().sendGlobalPositionGraphic(263, location);
		}

	}

}
