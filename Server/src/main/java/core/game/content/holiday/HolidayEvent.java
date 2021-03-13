package core.game.content.holiday;

import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import rs09.game.system.SystemLogger;
import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.ZoneBuilder;
import core.game.world.map.zone.ZoneRestriction;
import core.game.world.map.zone.ZoneType;
import core.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles a holiday event.
 * @author Vexia
 */
public abstract class HolidayEvent extends MapZone implements Plugin<Object> {

	/**
	 * The list of holiday events.
	 */
	private static final List<HolidayEvent> EVENTS = new ArrayList<>(5);

	/**
	 * The current holiday event.
	 */
	private static HolidayEvent current;

	/**
	 * The holiday type.
	 */
	private final HolidayType type;

	/**
	 * The config id used to handle the stages.
	 */
	private final int configId;

	/**
	 * The hash to use for this event.
	 */
	private final int hash;

	/**
	 * The max stage.
	 */
	private final int maxStage;

	/**
	 * Constructs a new {@code HolidayEvent} {@code Object}
	 * @param type the holiday type.
	 * @param name the name of the event.
	 * @param configId the config id.
	 * @param hash the hash.
	 * @param maxStage the max stage.
	 */
	public HolidayEvent(String name, HolidayType type, int configId, int hash, int maxStage) {
		super(name, true, ZoneRestriction.CANNON, ZoneRestriction.FIRES, ZoneRestriction.FOLLOWERS, ZoneRestriction.RANDOM_EVENTS);
		this.type = type;
		this.hash = hash;
		this.configId = configId;
		this.maxStage = maxStage;
		this.setZoneType(ZoneType.SAFE.getId());
	}

	/**
	 * Used to load all the necessary nodes for this event.
	 */
	public abstract void load();

	/**
	 * Indicates whether or not this holiday event is active.
	 * @return {@code True} if so.
	 */
	public abstract boolean isActive();

	/**
	 * Initializes the holiday events.
	 */
	public static void init() {
		new ItemLimitation().parse();
		for (HolidayEvent event : EVENTS) {
			if (event.isActive()) {
				event.load();
			}
		}
	}

	@Override
	public boolean enter(Entity entity) {
		if (entity instanceof Player && isComplete(entity.asPlayer())) {
			return false;
		}
		return super.enter(entity);
	}

	@Override
	public boolean leave(Entity e, boolean logout) {
		return super.leave(e, logout);
	}

	@Override
	public boolean parseCommand(Player player, String name, String[] arguments) {
		if (!player.isAdmin()) {
			return false;
		}
		switch (name) {
		case "setstage":
			setStage(player, Integer.parseInt(arguments[1]));
			return true;
		}
		return false;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		if (isActive()) {
			setCurrent(this);
			ZoneBuilder.configure(this);
			SystemLogger.logInfo("Holiday event active [type=" + type + "]!");
		}
		EVENTS.add(this);
		return this;
	}

	/**
	 * Adds experience.
	 * @param player The player.
	 * @param skill The skill.
	 * @param experience The experience.
	 */
	public void addExperience(Player player, int skill, double experience) {

	}

	/**
	 * Commences the death event.
	 * @param killer The killing entity.
	 * @param victim The entity getting killed.
	 */
	public void commenceDeath(Entity killer, Entity victim) {

	}

	/**
	 * Finalizes the death event.
	 * @param killer The killing entity.
	 * @param victim The entity getting killed.
	 */
	public void finalizeDeath(Entity killer, Entity victim) {

	}

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	/**
	 * Checks if it is a certain holiday.
	 * @param type the type.
	 * @return {@code True} if its a holiday.
	 */
	public static boolean isHoliday(HolidayType type) {
		return current != null && current.getType() == type;
	}

	/**
	 * Checks if the event is complete.
	 * @return {@code True} if so.
	 */
	public boolean isComplete(Player player) {
		return getStage(player) >= maxStage;
	}

	/**
	 * Sets the stage of the event.
	 * @param player the player.
	 * @param stage the stage.
	 */
	public void setStage(Player player, int stage) {
		setStage(player, stage, hash);
	}

	/**
	 * Sets the stage of the event.
	 * @param player the player.
	 * @param stage the stage.
	 * @param hash the hash.
	 */
	public void setStage(Player player, int stage, int hash) {
		setStageValue(player, getConfig(player) + stage << hash);
	}

	/**
	 * Sets the stage value of the event.
	 * @param player the player.
	 * @param value the value.
	 */
	public void setStageValue(Player player, int value) {
		player.getConfigManager().set(configId, value, true);
	}

	/**
	 * Gets the stage of the event.
	 * @param player the player.
	 * @return the stage.
	 */
	public int getStage(Player player) {
		return getStage(player, hash);
	}

	/**
	 * Gets the stage of the event.
	 * @param player the player.
	 * @return the stage.
	 */
	public int getStage(Player player, int hash) {
		return getConfig(player) >> hash;
	}

	/**
	 * Gets the config value.
	 * @param player the player.
	 * @return the value.
	 */
	public int getConfig(Player player) {
		return player.getConfigManager().get(configId);
	}

	/**
	 * Gets the max stage.
	 * @return the stage.
	 */
	public int getMaxStage() {
		return maxStage;
	}

	/**
	 * Gets the configId.
	 * @return the configId
	 */
	public int getConfigId() {
		return configId;
	}

	/**
	 * Gets the hash.
	 * @return the hash
	 */
	public int getHash() {
		return hash;
	}

	/**
	 * Gets the type.
	 * @return the type
	 */
	public HolidayType getType() {
		return type;
	}

	/**
	 * Gets the current.
	 * @return the current
	 */
	public static HolidayEvent getCurrent() {
		return current;
	}

	/**
	 * Sets the current.
	 * @param current the current to set.
	 */
	public static void setCurrent(HolidayEvent current) {
		HolidayEvent.current = current;
	}

}
