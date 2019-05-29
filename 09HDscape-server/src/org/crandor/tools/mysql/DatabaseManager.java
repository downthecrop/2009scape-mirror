package org.crandor.tools.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.crandor.ServerConstants;
import org.crandor.game.system.SystemLogger;

public class DatabaseManager {

	private Map<String, Connection> connections;
	private Map<String, Database> databases;

	private boolean connected;

	public DatabaseManager(Database[] databases) {

		this.connections = new HashMap<String, Connection>(databases.length);
		this.databases = new HashMap<String, Database>(databases.length);

		for (Database database : databases) {

			if (databases().get(database.name()) != null)
				continue;

			databases().put(database.name(), database);

		}

	}

	public DatabaseManager connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			for (Database database : databases().values()) {

				Connection connection = DriverManager.getConnection("jdbc:mysql://" + database.host() + "/" + database.name(), database.username(), database.password());

				connections().put(database.name(), connection);

				SystemLogger.log("Successfully connected with '" + database.name() + "'.");

				this.connected = true;

			}

		} catch (SQLException e) {
			System.out.println("Couldn't connect to the database.");
			e.printStackTrace();
			ServerConstants.MYSQL = false;
		} catch (ClassNotFoundException e) {
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
			System.out.println(e);
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
			System.out.println(e);
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

	public enum Databases {
		BLUEVISION, FROSTBLADES_317, FROSTBLADES_718;
	}

}
