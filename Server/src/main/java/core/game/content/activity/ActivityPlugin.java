package core.game.content.activity;

import rs09.ServerConstants;
import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.game.world.map.Region;
import core.game.world.map.build.DynamicRegion;
import core.game.world.map.zone.*;
import core.game.world.map.zone.impl.MultiwayCombatZone;
import core.plugin.Plugin;
import core.plugin.PluginManifest;
import core.plugin.PluginType;

/**
 * A plugin implementation used for activity plugins.
 * @author Emperor
 */
@PluginManifest(type = PluginType.ACTIVITY)
public abstract class ActivityPlugin extends MapZone implements Plugin<Player> {

	/**
	 * If the activity is instanced.
	 */
	private boolean instanced;

	/**
	 * If the activity is multicombat.
	 */
	private boolean multicombat;

	/**
	 * If the activity is safe.
	 */
	private boolean safe;

	/**
	 * The region of the activity.
	 */
	protected DynamicRegion region;

	/**
	 * The base location.
	 */
	protected Location base;

	/**
	 * A location which the player is teleported to in the case of death, if this activity is safe.
	 * Defaults to the home location of the server.
	 */
	protected Location safeRespawn = ServerConstants.HOME_LOCATION;

	/**
	 * The player.
	 */
	protected Player player;

	/**
	 * Constructs a new {@code ActivityPlugin} {@code Object}.
	 * @param name The name.
	 * @param instanced If the activity is instanced.
	 * @param multicombat If the activity is multicombat.
	 * @param safe If the activity is safe (the player does not lose his/her
	 * items).
	 */
	public ActivityPlugin(String name, boolean instanced, boolean multicombat, boolean safe, ZoneRestriction... restrictions) {
		super(name, true, ZoneRestriction.RANDOM_EVENTS);
		for (ZoneRestriction restriction : restrictions) {
			addRestriction(restriction.getFlag());
		}
		this.instanced = instanced;
		this.multicombat = multicombat;
		this.safe = safe;
		if (safe) {
			setZoneType(ZoneType.SAFE.getId());
		}
	}

	@Override
	public void register(ZoneBorders borders) {
		if (multicombat) {
			MultiwayCombatZone.getInstance().register(borders);
		}
		super.register(borders);
	}

	/**
	 * Sets the region base location.
	 */
	protected void setRegionBase() {
		if (region != null) {
			if (multicombat) {
				region.toggleMulticombat();
			}
			setBase(Location.create(region.getBorders().getSouthWestX(), region.getBorders().getSouthWestY(), 0));
		}
	}

	/**
	 * Sets the region base for multiple regions.
	 * @param regions The regions.
	 */
	protected void setRegionBase(DynamicRegion[] regions) {
		region = regions[0];
		Location l = region.getBaseLocation();
		for (DynamicRegion r : regions) {
			if (r.getX() > l.getX() || r.getY() > l.getY()) {
				l = r.getBaseLocation();
			}
		}
		ZoneBorders borders = new ZoneBorders(region.getX() << 6, region.getY() << 6, l.getX() + Region.SIZE, l.getY() + Region.SIZE);
		RegionZone multiZone = multicombat ? new RegionZone(MultiwayCombatZone.getInstance(), borders) : null;
		RegionZone zone = new RegionZone(this, borders);
		for (DynamicRegion r : regions) {
			if (multicombat) {
				r.setMulticombat(true);
				r.getRegionZones().add(multiZone);
			}
			r.getRegionZones().add(zone);
		}
		setBase(Location.create(borders.getSouthWestX(), borders.getSouthWestY(), 0));
	}

	/**
	 * Starts the activity for the player.
	 * @param player The player.
	 * @param login If the player is logging in.
	 * @param args The arguments.
	 * @return {@code True} if successfully started the activity.
	 */
	public boolean start(Player player, boolean login, Object... args) {
		this.player = player;
		return true;
	}

	@Override
	public boolean enter(Entity e) {
		Location l;
		if (e instanceof Player && (l = getSpawnLocation()) != null) {
			e.getProperties().setSpawnLocation(l);
		}
		e.getProperties().setSafeZone(safe);
		e.getProperties().safeRespawn = this.safeRespawn;
		e.setAttribute("activity", this);
		return super.enter(e);
	}

	@Override
	public boolean leave(Entity e, boolean logout) {
		if (e instanceof Player) {
			e.getProperties().setSpawnLocation(ServerConstants.HOME_LOCATION);
		}
		Location l;
		if (instanced && logout && (l = getSpawnLocation()) != null) {
			e.setLocation(l);
		}
		e.getProperties().setSafeZone(false);
		e.getProperties().safeRespawn = ServerConstants.HOME_LOCATION;
		e.removeAttribute("activity");
		return super.leave(e, logout);
	}

	/**
	 * Method used to do anything on registration.
	 */
	public void register() {
	}

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	@Override
	public abstract ActivityPlugin newInstance(Player p) throws Throwable;

	/**
	 * Gets the spawn location for this activity.
	 */
	public abstract Location getSpawnLocation();

	/**
	 * Gets the instanced.
	 * @return The instanced.
	 */
	public boolean isInstanced() {
		return instanced;
	}

	/**
	 * Sets the instanced.
	 * @param instanced The instanced to set.
	 */
	public void setInstanced(boolean instanced) {
		this.instanced = instanced;
	}

	/**
	 * Gets the multicombat.
	 * @return The multicombat.
	 */
	public boolean isMulticombat() {
		return multicombat;
	}

	/**
	 * Sets the multicombat.
	 * @param multicombat The multicombat to set.
	 */
	public void setMulticombat(boolean multicombat) {
		this.multicombat = multicombat;
	}

	/**
	 * Gets the safe.
	 * @return The safe.
	 */
	public boolean isSafe() {
		return safe;
	}

	/**
	 * Sets the safe.
	 * @param safe The safe to set.
	 */
	public void setSafe(boolean safe) {
		this.safe = safe;
	}

	/**
	 * Gets the player.
	 * @return The player.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the base.
	 * @return The base.
	 */
	public Location getBase() {
		return base;
	}

	/**
	 * Sets the base.
	 * @param base The base to set.
	 */
	public void setBase(Location base) {
		this.base = base;
	}

}