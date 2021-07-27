package core.game.system.mysql.impl;

import rs09.ServerConstants;
import core.game.system.monitor.MessageLog;
import core.game.system.monitor.PlayerMonitor;
import core.game.system.mysql.SQLEntryHandler;
import core.game.system.mysql.SQLManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL Entry handler to log player's messages, duplications, & addresses.
 * @author Vexia
 *
 */
public class PlayerLogSQLHandler extends SQLEntryHandler<PlayerMonitor> {

	/**
	 * The column names.
	 */
	private static final String[] MESSAGE_COLUMNS = new String[] { "public_chat", "private_chat", "clan_chat", "address_log", "command_log", "trade_log", "ge_log", "duel_log" };

	/**
	 * Constructs a new {@Code PlayerLogSQLHandler} {@Code Object}
	 * @param entry the player monitor entry.
	 */
	public PlayerLogSQLHandler(PlayerMonitor entry, String playerName) {
		super(entry, (SQLManager.LOCAL ? "global" : ServerConstants.DATABASE_NAME) + ".player_logs", "username", playerName);
	}

	@Override
	public void parse() throws SQLException {
	}

	@Override
	public void create() throws SQLException {
	}

	@Override
	public void save() throws SQLException {
		Connection connection = getConnection();
		if (connection.prepareStatement("SELECT * FROM " + table + " WHERE username='" + value + "' LIMIT 1").executeQuery().next()) {
			String b = "SET ";
			int size = 0;
			List<Integer> columns = new ArrayList<>(20);
			for (int i = 0; i < MESSAGE_COLUMNS.length; i++) {
				if (!entry.getLogs()[i].getMessages().isEmpty()) {
					b += MESSAGE_COLUMNS[i] + " = CONCAT(" + MESSAGE_COLUMNS[i] + ", ?)" + (i == MESSAGE_COLUMNS.length - 1 ? "" : ",");
					size++;
					columns.add(i);
				}
			}
			if (!entry.getDuplicationLog().getMessages().isEmpty()) {
				b += (b.charAt(b.length() - 1) != ',' ? "," : "") + "duplication_log = CONCAT(duplication_log, ?)";
			}
			if (b.charAt(b.length() - 1) == ',') {
				b = b.substring(0, b.length() - 1);
			}
			PreparedStatement statement = connection.prepareStatement("UPDATE " + table + " " + b + " WHERE username='" + value + "'");
			for (int i = 0; i < size; i++) {
				writeLog(statement, 1 + i, columns.get(i));
			}
			if (!entry.getDuplicationLog().getMessages().isEmpty()) {
				String log = "";
				MessageLog messageLog = entry.getDuplicationLog();
				for (int i = 0; i < messageLog.getMessages().size(); i++) {
					if (log.contains(messageLog.getMessages().get(i))) {
						continue;
					}
					log += messageLog.getMessages().get(i) + "\n";
				}
				statement.setString(size + 1, log);
			}
			//statement.executeUpdate();
		} else {
			PreparedStatement statement = connection.prepareStatement("INSERT INTO " + table + " (username,public_chat,private_chat,clan_chat,address_log,command_log,trade_log,ge_log,duel_log,duplication_log) VALUES(?,?,?,?,?,?,?,?,?,?)");
			statement.setString(1, value);
			for (int i = 0; i < MESSAGE_COLUMNS.length; i++) {
				//writeLog(statement, 2 + i, i);
			}
			String log = "";
			MessageLog messageLog = entry.getDuplicationLog();
			for (int i = 0; i < messageLog.getMessages().size(); i++) {
				if (log.contains(messageLog.getMessages().get(i))) {
					continue;
				}
				log += messageLog.getMessages().get(i) + "\n";
			}
			statement.setString(10, log);
			//statement.executeUpdate();
		}
		entry.clear();
		SQLManager.close(connection);
	}

	/**
	 * Writes a message log to the prepared statement.
	 * @param statement the statement.
	 * @param columnIndex the column index.
	 * @param logIndex the log index.
	 * @throws SQLException the exception if thrown.
	 */
	private void writeLog(PreparedStatement statement, int columnIndex, int logIndex) throws SQLException {
		String log = "";
		MessageLog messageLog = entry.getLogs()[logIndex];
		for (int i = 0; i < messageLog.getMessages().size(); i++) {
			log += messageLog.getMessages().get(i) + "\n";
		}
		statement.setString(columnIndex, log);
	}

	@Override
	public Connection getConnection() {
		return SQLManager.getConnection();
	}

}
