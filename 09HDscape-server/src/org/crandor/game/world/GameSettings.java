package org.crandor.game.world;

import org.crandor.net.Constants;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Represents the game settings used for this game instance.
 * @author Vexia
 */
public final class GameSettings {

	/**
	 * The name of the namme.
	 */
	private final String name;

	/**
	 * If the game is in beta mode.
	 */
	private final boolean beta;

	/**
	 * If the game is in developer mode.
	 */
	private final boolean devMode;

	/**
	 * If the gui is enabled.
	 */
	private final boolean gui;
	
	/**
	 * The world id of the server.
	 */
	private final int worldId;

	/**
	 * The country index.
	 */
	private final int countryIndex;

	/**
	 * The activity.
	 */
	private final String activity;

	/**
	 * If the world is members only.
	 */
	private final boolean members;

	/**
	 * If the world is a pvp world.
	 */
	private final boolean pvp;

	/**
	 * If only quick chat can be used on the world.
	 */
	private final boolean quickChat;

	/**
	 * If lootshare option is enabled on this world.
	 */
	private final boolean lootshare;

	/**
	 * The address of the Management server.
	 */
	private final String msAddress;

	/**
	 * Constructs a new {@code GameSettings} {@code Object}.
	 * @param name the name.
	 * @param beta the beta.
	 * @param type the game type.
	 * @param gui if gui is enabled.
	 * @param worldId the world id.
	 * @param countryIndex The country index.
	 * @param members If the world is members only.
	 * @param msAddress The address of the Management server.
	 */
	GameSettings(String name, boolean beta, boolean devMode, boolean gui, int worldId, int countryIndex, String activity, boolean members, boolean pvp, boolean quickChat, boolean lootshare, String msAddress) {
		this.name = name;
		this.beta = beta;
		this.devMode = devMode;
		this.gui = gui;
		this.worldId = worldId;
		this.countryIndex = countryIndex;
		this.activity = activity;
		this.members = members;
		this.pvp = pvp;
		this.quickChat = quickChat;
		this.lootshare = lootshare;
		this.msAddress = msAddress;
	}

	/**
	 * Parses the game settings from the program arguments.
	 * @param args The program arguments.
	 * @return The game settings.
	 */
	public static GameSettings parse(String[] args) {
		return parse(args[0]);
	}

	/**
	 * Parses a game settings file.
	 * @param path the path.
	 * @return the settings.
	 */
	public static GameSettings parse(final String path) {
		final Properties properties = getProperties(path);
		String address = Constants.DEFAULT_MS_IP;
		if (properties.containsKey("msip")) {
			address = properties.getProperty("msip");
		}
		return new GameSettings(properties.getProperty("name"), 
				Boolean.parseBoolean(properties.getProperty("beta")), 
				Boolean.parseBoolean(properties.getProperty("devMode")), 
				Boolean.parseBoolean(properties.getProperty("gui")),
				Integer.parseInt(properties.getProperty("worldId")), 
				Integer.parseInt(properties.getProperty("country")), 
				properties.getProperty("activity"),
				Boolean.parseBoolean(properties.getProperty("members")), 
				Boolean.parseBoolean(properties.getProperty("pvp")),
				Boolean.parseBoolean(properties.getProperty("quickChat")),
				Boolean.parseBoolean(properties.getProperty("lootshare")),
				address);
	}

	/**
	 * Gets the properties.
	 * @param path the path.
	 * @return the properties.
	 */
	private static Properties getProperties(String path) {
		FileInputStream file;
		Properties properties = new Properties();
		try {
			file = new FileInputStream(path);
			properties.load(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	/**
	 * Gets the devMode.
	 * @return The devMode.
	 */
	public boolean isDevMode() {
		return devMode;
	}

	/**
	 * Gets the name.
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the beta.
	 * @return The beta.
	 */
	public boolean isBeta() {
		return beta;
	}

	/**
	 * Checks if the server is hosted publicly.
	 * @return {@code True} if so.
	 */
	public boolean isHosted() {
		return !devMode;
	}

	/**
	 * Gets the gui.
	 * @return The gui.
	 */
	public boolean isGui() {
		return gui;
	}

	/**
	 * Gets the worldId.
	 * @return The worldId.
	 */
	public int getWorldId() {
		return worldId;
	}

	/**
	 * Gets the countryIndex.
	 * @return the countryIndex
	 */
	public int getCountryIndex() {
		return countryIndex;
	}

	/**
	 * Gets the members.
	 * @return the members
	 */
	public boolean isMembers() {
		return members;
	}

	/**
	 * Gets the msAddress.
	 * @return the msAddress
	 */
	public String getMsAddress() {
		return msAddress;
	}

	/**
	 * Gets the activity value.
	 * @return The activity.
	 */
	public String getActivity() {
		return activity;
	}

	/**
	 * Gets the pvp value.
	 * @return The pvp.
	 */
	public boolean isPvp() {
		return pvp;
	}

	/**
	 * Gets the quickChat value.
	 * @return The quickChat.
	 */
	public boolean isQuickChat() {
		return quickChat;
	}

	/**
	 * Gets the lootshare value.
	 * @return The lootshare.
	 */
	public boolean isLootshare() {
		return lootshare;
	}
	
	@Override
	public String toString() {
		return "GameSettings [name=" + name + ", beta=" + beta + ", devMode=" + devMode + ", gui=" + gui + ", worldId=" + worldId + "]";
	}
}
