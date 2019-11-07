package org.crandor.game.system.mysql;

import org.crandor.ServerConstants;
import org.crandor.game.system.SystemLogger;
import org.crandor.game.system.SystemManager;
import org.crandor.game.system.mysql.impl.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages the SQL connections.
 * @author Vexia
 * 
 */
public final class SQLManager {
	
	/**
	 * If the sql manager is locally hosted.
	 */
	public static final boolean LOCAL = true;

	public static final String SQL_SERVER_ADDRESS = LOCAL ? "127.0.0.1" : "mywebsite.com";

	/**
	 * The username of the user.
	 */
	private static final String USERNAME = LOCAL ? "root" : "username";

	/**
	 * The password of the user.
	 */
	private static final String PASSWORD = LOCAL ? "" : "password";

    /**
     * The database URL.
     */
    public static final String DATABASE_URL = SQL_SERVER_ADDRESS + ":3306/" + ServerConstants.DATABASE_NAMES[1];
    public static final String DATABASE_URL_SERVER = SQL_SERVER_ADDRESS + ":3306/" + ServerConstants.DATABASE_NAMES[0];

	/**
	 * IF the sql manager is initialized.
	 */
	private static boolean initialized;

	/**
	 * Constructs a new {@code SQLManager} {@code Object}
	 */
	public SQLManager() {
		/**
		 * empty.
		 */
	}

	/**
	 * Initializes the sql manager.
	 */
	public static void init() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			initialized = false;
			return;
		}
		initialized = true;
		SystemManager.getSystemConfig().parse();
	}
	
	/**
	 * Pre-plugin parsing.
	 */
	public static void prePlugin() {
		try {
			new NPCConfigSQLHandler().parse();
			new ItemConfigSQLHandler().parse();
			new RegionSQLHandler().parse();
			new ComponentSQLHandler().parse();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Parses data from the database for the server post plugin loading.
	 */
	public static void postPlugin() {
		try {
			new ShopSQLHandler().parse();
			new NPCDropSQLHandler().parse();
			new NPCSpawnSQLHandler().parse();
			new DoorConfigSQLHandler().parse();
			new GroundSpawnSQLHandler().parse();
			new MusicConfigSQLHandler().parse();
			new RangeConfigSQLHandler().parse();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets a connection.
	 * @return The connection.
	 */
	public static Connection getConnection() {
		try {
			return DriverManager.getConnection("jdbc:mysql://" +   DATABASE_URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			SystemLogger.error(SQLManager.class, "Error: Mysql error message=" + e.getMessage() + ".");
		}
		return null;
	}
	public static Connection getConnectionServer() {
		try {
			return DriverManager.getConnection("jdbc:mysql://" + DATABASE_URL_SERVER, USERNAME, PASSWORD);
		} catch (SQLException e) {
			SystemLogger.error(SQLManager.class, "Error: Mysql error message=" + e.getMessage() + ".");
		}
		return null;
	}

	/**
	 * Releases the connection so it's available for usage.
	 * @param connection The connection.
	 */
	public static void close(Connection connection) {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the initialized.
	 * @return the initialized
	 */
	public static boolean isInitialized() {
		return initialized;
	}

	/**
	 * Sets the bainitialized.
	 * @param initialized the initialized to set.
	 */
	public static void setInitialized(boolean initialized) {
		SQLManager.initialized = initialized;
	}

}
