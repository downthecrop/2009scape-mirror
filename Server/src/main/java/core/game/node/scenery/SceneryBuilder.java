package core.game.node.scenery;

import core.game.node.item.GroundItem;
import core.game.node.item.GroundItemManager;
import rs09.game.system.SystemLogger;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.map.build.LandscapeParser;
import core.game.world.update.flag.chunk.ObjectUpdateFlag;

/**
 * An aiding class for object constructing/removing.
 *
 * @author Emperor
 */
public final class SceneryBuilder {

	/**
	 * Replaces a scenery.
	 *
	 * @param remove    The object to remove.
	 * @param construct The object to add.
	 * @return {@code True} if successful.
	 */
	public static boolean replace(Scenery remove, Scenery construct) {
		return replace(remove, construct, true, false);
	}

	/**
	 * Replaces a scenery.
	 *
	 * @param remove    The object to remove.
	 * @param construct The object to add.
	 * @param clip      If clipping should be adjusted.
	 * @return {@code True} if successful.
	 */
	public static boolean replace(Scenery remove, Scenery construct, boolean clip, boolean permanent) {
		if (!clip) {
			return replaceClientSide(remove, construct, -1);
		}
		remove = remove.getWrapper();
		Scenery current = LandscapeParser.removeScenery(remove);
		if (current == null) {
			if (GameWorld.getSettings().isDevMode()) {
				SystemLogger.logErr("Object could not be replaced - object to remove is invalid.");
			}
			return false;
		}
		if (current.getRestorePulse() != null) {
			current.getRestorePulse().stop();
			current.setRestorePulse(null);
		}
		if (current instanceof Constructed) {
			Scenery previous = ((Constructed) current).getReplaced();
			if (previous != null && previous.equals(construct)) {
				LandscapeParser.addScenery(previous);
				update(current, previous);
				return true;
			}
		}
		Constructed constructed = construct.asConstructed();
		if (!permanent) {
			constructed.setReplaced(current);
		}
		LandscapeParser.addScenery(constructed);
		update(current, constructed);
		return true;
	}

	/**
	 * Replaces the object client sided alone.
	 *
	 * @param remove       The object to remove.
	 * @param construct    The object to replace with.
	 * @param restoreTicks The restoration ticks.
	 * @return {@code True} if successful.
	 */
	private static boolean replaceClientSide(final Scenery remove, final Scenery construct, int restoreTicks) {
		RegionManager.getRegionChunk(remove.getLocation()).flag(new ObjectUpdateFlag(remove, true));
		RegionManager.getRegionChunk(construct.getLocation()).flag(new ObjectUpdateFlag(construct, false));
		if (restoreTicks > 0) {
			GameWorld.getPulser().submit(new Pulse(restoreTicks) {
				@Override
				public boolean pulse() {
					return replaceClientSide(construct, remove, -1);
				}
			});
		}
		return true;
	}

	/**
	 * Replaces a scenery temporarily.
	 *
	 * @param remove       The object to remove.
	 * @param construct    The object to add.
	 * @param restoreTicks The amount of ticks before the object gets restored.
	 * @return {@code True} if successful.
	 */
	public static boolean replace(Scenery remove, Scenery construct, int restoreTicks) {
		return replace(remove, construct, restoreTicks, true);
	}

	/**
	 * Replaces a scenery temporarily.
	 *
	 * @param remove       The object to remove.
	 * @param construct    The object to add.
	 * @param restoreTicks The amount of ticks before the object gets restored.
	 * @return {@code True} if successful.
	 */
	public static boolean replace(Scenery remove, Scenery construct, int restoreTicks, final boolean clip) {
		if (!clip) {
			return replaceClientSide(remove, construct, restoreTicks);
		}
		remove = remove.getWrapper();
		Scenery current = LandscapeParser.removeScenery(remove);
		if (current == null) {
			if (GameWorld.getSettings().isDevMode()) {
				SystemLogger.logErr("Object could not be replaced - object to remove is invalid.");
			}
			return false;
		}
		if (current.getRestorePulse() != null) {
			current.getRestorePulse().stop();
			current.setRestorePulse(null);
		}
		if (current instanceof Constructed) {
			Scenery previous = ((Constructed) current).getReplaced();
			if (previous != null && previous.equals(construct)) {
				// Shouldn't happen.
				throw new IllegalStateException("Can't temporarily replace an already temporary object!");
			}
		}
		final Constructed constructed = construct.asConstructed();
		constructed.setReplaced(current);
		LandscapeParser.addScenery(constructed);
		update(current, constructed);
		if (restoreTicks < 0) {
			return true;
		}
		constructed.setRestorePulse(new Pulse(restoreTicks) {
			@Override
			public boolean pulse() {
				replace(constructed, constructed.getReplaced());
				return true;
			}
		});
		GameWorld.getPulser().submit(constructed.getRestorePulse());
		return true;
	}

	/**
	 * Adds a scenery.
	 *
	 * @param object The object to add.
	 * @return {@code True} if successful.
	 */
	public static Constructed add(Scenery object) {
		return add(object, -1);
	}

	/**
	 * Adds a scenery.
	 *
	 * @param object The object to add.
	 * @param ticks  The amount of ticks this object should last for (-1 for
	 *               permanent).
	 * @return {@code True} if successful.
	 */
	public static Constructed add(Scenery object, int ticks, final GroundItem... items) {
		object = object.getWrapper();
		final Constructed constructed = object.asConstructed();
		LandscapeParser.addScenery(constructed);
		update(constructed);
		if (ticks > -1) {
			GameWorld.getPulser().submit(new Pulse(ticks, object) {
				@Override
				public boolean pulse() {
					remove(constructed);
					if (items != null) {
						for (int i = 0; i < items.length; i++) {
							GroundItemManager.create(items[i]);
						}
					}
					return true;
				}
			});
		}
		return constructed;
	}

	/**
	 * Removes all objects within a box
	 * @param objectId - the object id to remove
	 * @param southWest
	 * @param northEast
	 * @return
	 */
	public static boolean removeAll(int objectId, Location southWest, Location northEast) {
		if (southWest.getX() > northEast.getX() || southWest.getY() > northEast.getY())
			return false;

		int differenceX = northEast.getX() - southWest.getX();
		int differenceY = northEast.getY() - southWest.getY();

		for (int x = 0; x <= differenceX; x++) {
			for (int y = 0; y <= differenceY; y++){
				Scenery object = new Scenery(objectId, Location.create(southWest.getX() + x, southWest.getY() + y, southWest.getZ()));
				remove(object);
			}
		}
		return true;
	}

	/**
	 * Removes a scenery.
	 *
	 * @param object The object to remove.
	 * @return {@code True} if successful.
	 */
	public static boolean remove(Scenery object) {
		if (object == null) {
			return false;
		}
		object = object.getWrapper();
		Scenery current = LandscapeParser.removeScenery(object);
		if (current == null) {
			return false;
		}
		update(current);
		return true;
	}

	/**
	 * Removes a scenery.
	 *
	 * @param object       the object.
	 * @param respawnTicks the respawn ticks.
	 * @return {@code True}if removed.
	 */
	public static boolean remove(final Scenery object, int respawnTicks) {
		if (remove(object)) {
			GameWorld.getPulser().submit(new Pulse(respawnTicks) {

				@Override
				public boolean pulse() {
					add(object);
					return true;
				}

			});
			return true;
		}
		return false;
	}

	/**
	 * Updates the scenery on all the player's screen.
	 *
	 * @param objects The scenerys.
	 */
	public static void update(Scenery... objects) {
		for (Scenery o : objects) {
			if (o == null) {
				continue;
			}
			RegionManager.getRegionChunk(o.getLocation()).flag(new ObjectUpdateFlag(o, !o.isActive()));
		}
	}
}
