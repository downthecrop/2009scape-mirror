package core.game.interaction.object;

import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.TeleportManager.TeleportType;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.chunk.GraphicUpdateFlag;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;

/**
 * Represents the wilderness obelisk plugin.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class WildernessObeliskPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(14829).getHandlers().put("option:activate", this);
		SceneryDefinition.forId(14826).getHandlers().put("option:activate", this);
		SceneryDefinition.forId(14827).getHandlers().put("option:activate", this);
		SceneryDefinition.forId(14828).getHandlers().put("option:activate", this);
		SceneryDefinition.forId(14830).getHandlers().put("option:activate", this);
		SceneryDefinition.forId(14831).getHandlers().put("option:activate", this);
		return this;
	}

	@Override
	public boolean handle(final Player player, final Node node, String option) {
		final Scenery nodeObject = (Scenery) node;
		final Obelisk stationObelisk = Obelisk.forLocation(player.getLocation());
		if (stationObelisk == null) {
			return false;
		}
		for (int i = 0; i < 4; i++) {
			int x = stationObelisk.getLocation().getX();
			int y = stationObelisk.getLocation().getY();
			int z = stationObelisk.getLocation().getZ();
			switch (i) {
			case 0:
				x = x + 2;
				y = y + 2;
				SceneryBuilder.replace(new Scenery(nodeObject.getId(), Location.create(x, y, z)), new Scenery(14825, Location.create(x, y, 0)), 6);
				break;
			case 1:
				x = x - 2;
				y = y + 2;
				SceneryBuilder.replace(new Scenery(nodeObject.getId(), Location.create(x, y, z)), new Scenery(14825, Location.create(x, y, 0)), 6);
				break;
			case 2:
				x = x - 2;
				y = y - 2;
				SceneryBuilder.replace(new Scenery(nodeObject.getId(), Location.create(x, y, z)), new Scenery(14825, Location.create(x, y, 0)), 6);
				break;
			case 3:
				x = x + 2;
				y = y - 2;
				SceneryBuilder.replace(new Scenery(nodeObject.getId(), Location.create(x, y, z)), new Scenery(14825, Location.create(x, y, 0)), 6);
				break;
			}
		}
		player.getAudioManager().send(204);
		GameWorld.getPulser().submit(new Pulse(6, player) {
			@Override
			public boolean pulse() {
				final Location center = stationObelisk.getLocation();
				if (getDelay() == 1) {
					for (int x = center.getX() - 1; x <= center.getX() + 1; x++) {
						for (int y = center.getY() - 1; y <= center.getY() + 1; y++) {
							Location l = Location.create(x, y, 0);
							RegionManager.getRegionChunk(l).flag(new GraphicUpdateFlag(Graphics.create(342), l));
						}
					}
					return true;
				}
				int index = RandomFunction.random(Obelisk.values().length);
				Obelisk newObelisk = Obelisk.values()[index];
				if (newObelisk == stationObelisk) {
					newObelisk = Obelisk.values()[index++ % Obelisk.values().length];
				}
				for (Player player : RegionManager.getLocalPlayers(center, 1)) {
					player.getPacketDispatch().sendMessage("Ancient magic teleports you somewhere in the wilderness.");
					int xDif = stationObelisk.getLocation().getX() - player.getLocation().getX();
					int yDif = stationObelisk.getLocation().getY() - player.getLocation().getY();
					if (xDif > 0 || yDif > 0) {
						player.getTeleporter().send(Location.create(newObelisk.getLocation().getX() - xDif, newObelisk.getLocation().getY() - yDif, 0), TeleportType.OBELISK, 2);
					} else {
						player.getTeleporter().send(Location.create(newObelisk.getLocation().getX() + xDif, newObelisk.getLocation().getY() + yDif, 0), TeleportType.OBELISK, 2);
					}
				}
				super.setDelay(1);
				return false;
			}

		});
		return true;
	}

	/**
	 * Represents an obelisk type.
	 * @author 'Vexia
	 * @version 1.0
	 */
	public enum Obelisk {
		LEVEL_13(new Location(3156, 3620, 0)), LEVEL_19(new Location(3219, 3656, 0)), LEVEL_27(new Location(3035, 3732, 0)), LEVEL_35(new Location(3106, 3794, 0)), LEVEL_44(new Location(2980, 3866, 0)), LEVEL_50(new Location(3307, 3916, 0));

		/**
		 * Represents the location to teleport to.
		 */
		private Location location;

		/**
		 * Constructs a new {@code Obelisk} {@code Object}.
		 * @param location the location.
		 */
		Obelisk(Location location) {
			this.location = location;
		}

		/**
		 * Gets the location.
		 * @return the location
		 */
		public Location getLocation() {
			return location;
		}

		/**
		 * Gets the obelisk by location.
		 * @param location the location.
		 * @return the obelisk.
		 */
		public static Obelisk forLocation(Location location) {
			for (Obelisk obelisk : Obelisk.values())
				if (obelisk.getLocation().getDistance(location) <= 20)
					return obelisk;
			return null;
		}
	}
}
