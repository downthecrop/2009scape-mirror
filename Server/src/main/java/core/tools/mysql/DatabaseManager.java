package core.tools.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import rs09.ServerConstants;
import rs09.game.system.SystemLogger;

public class DatabaseManager {

	private Map<String, Connection> connections = new HashMap<>();
	private Map<String, Database> databases;

	private Database db;
	private boolean connected;

	public DatabaseManager(Database db) {
		this.db = db;
	}

	public DatabaseManager connect() {
		try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection connection = DriverManager.getConnection("jdbc:mysql://" + db.host() + "/" + db.name() + "?useTimezone=true&serverTimezone=UTC", db.username(), db.password());
				connections.put(db.name(), connection);

				SystemLogger.logInfo("Successfully connected with '" + db.name() + "'.");

				this.connected = true;


		} catch (SQLException e) {
			SystemLogger.logErr("Couldn't connect to the database.");
			e.printStackTrace();
			ServerConstants.MYSQL = false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return this;
	}

	public ResultSet query(String name, String query) {

		try {

			Connection connection = connections().get(name);

			if (connection == null)
				return null;

			Statement statement = connection.createStatement();

			return statement.executeQuery(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public int update(String name, String query) {

		try {

			Connection connection = connections().get(name);

			if (connection == null)
				return -1;

			Statement statement = connection.createStatement();

			return statement.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return -1;
	}

	public Map<String, Connection> connections() {
		return connections;
	}

	public Map<String, Database> databases() {
		return databases;
	}

	public boolean isConnected() {
		return connected;
	}

}
