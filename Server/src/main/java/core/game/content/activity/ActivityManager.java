package core.game.content.activity;

import core.game.node.entity.player.Player;
import rs09.game.system.SystemLogger;
import rs09.game.world.GameWorld;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages the activities.
 * @author Emperor
 */
public final class ActivityManager {

	/**
	 * The mapping of instanced activities.
	 */
	private static final Map<String, ActivityPlugin> ACTIVITIES = new HashMap<>();

	/**
	 * Constructs a new {@code ActivityManager} {@code Object}.
	 */
	private ActivityManager() {
		/*
		 * empty.
		 */
	}

	/**
	 * Registers an activity plugin.
	 * @param plugin The plugin to register.
	 */
	public static void register(ActivityPlugin plugin) {
		plugin.register();
		ACTIVITIES.put(plugin.getName(), plugin);
		if (!plugin.isInstanced()) {
			plugin.configure();
		}
	}

	/**
	 * Starts an instanced activity.
	 * @param player The player.
	 * @param name The name.
	 * @param login If we are logging in.
	 * @param args The arguments.
	 */
	public static boolean start(Player player, String name, boolean login, Object... args) {
		ActivityPlugin plugin = ACTIVITIES.get(name);
		if (plugin == null) {
			if (GameWorld.getSettings().isDevMode()) {
				SystemLogger.logErr("Unhandled activity - " + name + "!");
			}
			return false;
		}
		try {
			if (plugin.isInstanced()) {
				(plugin = plugin.newInstance(player)).configure();
			}
			return plugin.start(player, login, args);
		} catch (Throwable e) {
			e.printStackTrace();
			if (GameWorld.getSettings().isDevMode()) {
				player.getPacketDispatch().sendMessage("Error starting activity " + (plugin == null ? null : plugin.getName()) + "!");
			}
		}
		return false;
	}

	/**
	 * Gets the activity by the name.
	 * @param name the name.
	 * @return the activity.
	 */
	public static ActivityPlugin getActivity(String name) {
		return ACTIVITIES.get(name);
	}
}