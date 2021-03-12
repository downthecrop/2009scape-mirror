package core.game.system.mysql;

import rs09.ServerConstants;
import rs09.game.system.SystemLogger;
import core.game.system.SystemManager;

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
	 * If the sql manager is locally hosted. Generally you never should change this. Ignore this.
	 */
	public static final boolean LOCAL = false;

    /**
     * The database URL.
     */
    public static String DATABASE_URL;

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
		initialized = true;
		SystemManager.getSystemConfig().parse();
	}
	
	/**
	 * Pre-plugin parsing.
	 */
	public static void prePlugin() {
			//new NPCConfigSQLHandler().parse();

	}

	/**
	 * Parses data from the database for the server post plugin loading.
	 */
	public static void postPlugin() {

	}

	/**
	 * Gets a connection.
	 * @return The connection.
	 */
	public static Connection getConnection() {
		DATABASE_URL  = ServerConstants.DATABASE_ADDRESS + ":" + ServerConstants.DATABASE_PORT + "/" + ServerConstants.DATABASE_NAME + "?useTimezone=true&serverTimezone=UTC";
		try {
			return DriverManager.getConnection("jdbc:mysql://" +   DATABASE_URL, ServerConstants.DATABASE_USER, ServerConstants.DATABASE_PASS);
		} catch (SQLException e) {
			SystemLogger.logErr("Mysql error message=" + e.getMessage() + ".");
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
